package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/15/2016.
 */

import android.content.Context;
import android.util.Log;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

//Class declaration of "Adventure"
public class Adventure {
    //Enumeration for endings
    private enum Ending {
        NOEND, VICTORY, FAILURE
    }

    //Struct declaration of TreeNode
    private class TreeNode {
        private String text;
        private String choice1;
        private String choice2;
        private Ending end = Ending.NOEND;

        public boolean isEnding() {return end == Ending.NOEND ? false : true;};
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
            tree.data.text = rootNode.getAttribute("text");
            Element child1 = getFirstChildElement(rootNode);
            if(child1.getNodeName().equals("choice1")) {
                //choice 1 of two choices, extract text
                tree.data.choice1 = child1.getAttribute("text");
                //then build branch from child node of choice 1
                tree.leftChild = buildBranch(getFirstChildElement(child1));
                Element child2 = getLastChildElement(rootNode);
                if(!child2.getNodeName().equals("choice2")) {
                    throw new RuntimeException("Choice 2 not found when expected"); //sanity check
                }
                tree.data.choice2 = child2.getAttribute("text");
                tree.rightChild = buildBranch(getFirstChildElement(child2));
            }
            else if(child1.getNodeName().equals("ending")) {
                String outcome = child1.getAttribute("outcome");
                if(outcome.equals("failure")) {
                    tree.data.end = Ending.FAILURE;
                }
                else if (outcome.equals("victory")) {
                    tree.data.end = Ending.VICTORY;
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


    public void buildAdventure(Context context) throws ParserConfigurationException, SAXException, IOException, RuntimeException{
        Tree adventure = new Tree();
        adventure.buildTree(context);
    }
}
