package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/15/2016.
 */

import android.content.Context;

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
        private Ending end;

        public boolean isEnding() {return end == Ending.NOEND ? false : true;};
    }

    //Class declaration of Tree structure
    //Want a Pre-order traversal to build
    private class Tree {
        public TreeNode treeNode;
        public Tree leftChild;
        public Tree rightChild;

//        Tree() {};
        Tree(Context context){};
//        Tree(TreeNode nodeToAdd) {
//            node = nodeToAdd;
//        }

        public Tree buildTree(Context context) throws ParserConfigurationException, SAXException, IOException {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            Tree tree = new Tree(context);
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(context.getResources().openRawResource(R.raw.sample_tree));
            //At this point we have a DOM built, now we need to construct it into the tree

            Node root = doc.getFirstChild();


            return tree;
        }
    }
}
