package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/15/2016.
 */

//Class declaration of "Adventure"
public class AdventureTree {
    //Enumeration for endings
    private enum Ending {
        NOEND, VICTORY, FAILURE
    }

    //Struct declaration of Node
    private class Node{
        private String text;
        private String choice1;
        private String choice2;
        private Ending end;

        public boolean isEnding() {return end == Ending.NOEND ? false : true;};
    }

    //Class declaration of Tree structure
    //Want a Pre-order traversal to build
    private class Tree {
        public Node node;
        public Tree leftChild;
        public Tree rightChild;

        Tree(Node nodeToAdd) {
            node = nodeToAdd;
        }
    }
}
