package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/15/2016.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

//Class declaration of "Adventure"
public class Adventure {
    //Enumeration for endings
    private enum Ending {
        NOEND, VICTORY, FAILURE, RIDDLE
    }

    //Struct declaration of TreeNode
    protected class TreeNode {
        private String text;
        private String choice1;
        private String choice2;
        private Ending end = Ending.NOEND;

        public boolean isEnding() {return (end == Ending.VICTORY || end == Ending.FAILURE);}
        public boolean isRiddle() {return (end == Ending.RIDDLE);}

        public String getChoice1() {
            return choice1;
        }
        public void setChoice1(String choice1) {
            this.choice1 = choice1;
        }
        public String getChoice2() {
            return choice2;
        }
        public void setChoice2(String choice2) {
            this.choice2 = choice2;
        }
        public Ending getEnd() {
            return end;
        }
        public void setEnd(Ending end) {
            this.end = end;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }

    //Class declaration of Tree structure
    //Want a Pre-order traversal to build
    private class Tree {
        public TreeNode data;
        public Tree leftChild;
        public Tree rightChild;

//        Tree() {};
        Tree(){
            this.data = new TreeNode();
        };
//        Tree(TreeNode nodeToAdd) {
//            node = nodeToAdd;
//        }

        public Tree buildTree(Context context) throws ParserConfigurationException, SAXException, IOException, RuntimeException {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            Tree tree = new Tree();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(context.getResources().openRawResource(R.raw.sample_tree));
            //At this point we have a DOM built, now we need to construct it into the tree

            Element rootElement = getFirstChildElement(getFirstChildElement(doc));
            tree = buildBranch(rootElement);
            Log.d("debug", "Tree built");



            return tree;
        }

        private Tree buildBranch(Element rootNode) throws RuntimeException {
            Tree tree = new Tree();
            tree.data.setText(rootNode.getAttribute("text"));
            Element child1 = getFirstChildElement(rootNode);
            if(child1.getNodeName().equals("choice1")) {
                //choice 1 of two choices, extract text
                tree.data.setChoice1(child1.getAttribute("text"));
                //then build branch from child node of choice 1
                tree.leftChild = buildBranch(getFirstChildElement(child1));
                Element child2 = getLastChildElement(rootNode);
                if(!child2.getNodeName().equals("choice2")) {
                    throw new RuntimeException("Choice 2 not found when expected"); //sanity check
                }
                tree.data.setChoice2(child2.getAttribute("text"));
                tree.rightChild = buildBranch(getFirstChildElement(child2));
            }
            else if(child1.getNodeName().equals("ending")) {
                String outcome = child1.getAttribute("outcome");
                if(outcome.equals("failure")) {
                    tree.data.setEnd(Ending.FAILURE);
                }
                else if (outcome.equals("victory")) {
                    tree.data.setEnd(Ending.VICTORY);
                }
                else {
                    throw new RuntimeException("Unexpected outcome of ending");
                }
            }
            return tree;
        }


        //The following methods are retrieved from www.java2s.com as helpful functions for use
        // here in DOM parsing
        private Element getFirstChildElement(Node node) {
            node = node.getFirstChild();
            while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
            {
                node = node.getNextSibling();
            }
            return (Element)node;
        }

        private Element getLastChildElement(Node parent) {
            // search for node
            Node child = parent.getLastChild();
            while (child != null) {
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    return (Element)child;
                }
                child = child.getPreviousSibling();
            }

            // not found
            return null;
        }
    }

    //Class properties
    private Tree adventureTree; //Tree of whole adventure
    private Tree playerTree;    //Subtree of player's location


    public void buildAdventure(Context context, TextView storyTV, Button c1Button, Button c2Button)
            throws ParserConfigurationException, SAXException, IOException, RuntimeException{
        adventureTree = new Tree().buildTree(context);
        playerTree = adventureTree;
        this.setNewLocation("root", storyTV, c1Button, c2Button);
    }

    public boolean setNewLocation(String choice, TextView storyTV, Button c1Button, Button c2Button) {
        if (choice.equals("choice1")) {
            playerTree = playerTree.leftChild;
        }
        else if (choice.equals("choice2")) {
            playerTree = playerTree.rightChild;
        }
        //else the node is the root
        storyTV.setText(playerTree.data.getText());
        c1Button.setText(playerTree.data.getChoice1());
        c2Button.setText(playerTree.data.getChoice2());
        return playerTree.data.isEnding();
    }
}
