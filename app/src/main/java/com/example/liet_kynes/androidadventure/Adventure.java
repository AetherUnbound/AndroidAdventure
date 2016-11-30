package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/15/2016.
 */

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

//Class declaration of "Adventure"
public class Adventure {
    private static final String TAG = "ADVENTURE_DEBUG";

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
        public int nodeNumber = 0;
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
            Tree tree;
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(context.getResources().openRawResource(R.raw.sample_tree));
            //At this point we have a DOM built, now we need to construct it into the tree

            Element rootElement = getFirstChildElement(getFirstChildElement(doc));
            tree = buildBranch(rootElement);
            Log.d(TAG, "Tree built");



            return tree;
        }

        private Tree buildBranch(Element rootNode) throws RuntimeException {
            Tree tree = new Tree();
            tree.nodeNumber = nodeNumber++;
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
            else if(child1.getNodeName().equals("riddle")) {
                tree.data.setEnd(Ending.RIDDLE);
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
    private Context parentContext; //Context of parent fragment
    private TextView storyTextView; //Textview of parent
    private Button choice1Button; //Button for choice 1 on parent
    private Button choice2Button; //Button for choice 2 on parent

    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;

    Adventure(Context context, TextView storyTV, Button c1Button, Button c2Button, FragmentActivity activity) {
        this.parentContext = context;
        this.storyTextView = storyTV;
        this.choice1Button = c1Button;
        this.choice2Button = c2Button;
        this.fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_animation);
        this.fadeOutAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_out_animation);
    }


    public void buildAdventure()
            throws ParserConfigurationException, SAXException, IOException, RuntimeException{
        adventureTree = new Tree().buildTree(parentContext);
        playerTree = adventureTree;
        this.setNewLocation("root");
    }

    public void buildAdventure(int nodeToResume)
            throws ParserConfigurationException, SAXException, IOException, RuntimeException{
        adventureTree = new Tree().buildTree(parentContext);
        playerTree = searchAdventureForResume(adventureTree, nodeToResume);
        this.setNewLocation("root");
    }

    public void restartAdventure() {
        playerTree = adventureTree;
        this.setNewLocation("root");
    }

    private Tree searchAdventureForResume(Tree root, int nodeNumber){
        if(root != null) {
            if(root.nodeNumber == nodeNumber) {
                return root;
            }
            else {
                Tree foundNode = searchAdventureForResume(root.leftChild, nodeNumber);
                if (foundNode == null) {
                    foundNode = searchAdventureForResume(root.rightChild, nodeNumber);
                }
                return foundNode;
            }
        }
        else
            return null;
    }

    public int getPlayerPosition() {
        return this.playerTree.nodeNumber;
    }


    public void setNewLocation(String choice){
        if (choice.equals("choice1")) {
            playerTree = playerTree.leftChild;
        }
        else if (choice.equals("choice2")) {
            playerTree = playerTree.rightChild;
        }
        //else the node is the root
        storyTextView.setText(playerTree.data.getText());
        choice1Button.setText(playerTree.data.getChoice1());
        choice2Button.setText(playerTree.data.getChoice2());
    }

    public boolean isEnding() {return (playerTree.data.getEnd() == Ending.VICTORY || playerTree.data.getEnd() == Ending.FAILURE);}
    public boolean isRiddle() {return (playerTree.data.getEnd() == Ending.RIDDLE);}
}
