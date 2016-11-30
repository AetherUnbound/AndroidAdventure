package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/29/2016.
 */

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Random;

public class Riddle {
    private static final String TAG = "RIDDLE_DEBUG";

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getChoice(int index) { return choices[index]; }
    public int getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }
    public void setChoices(String choice, int index) { this.choices[index] = choice; }

    private String question;
    private String[] choices = new String[4];
    private int correctAnswer;

    public int createRiddle(Context context, TextView riddleTV, Button c1Button, Button c2Button, Button c3Button, Button c4Button, int difficulty, String riddleContext) throws ParserConfigurationException, SAXException, IOException, RuntimeException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(context.getResources().openRawResource(R.raw.sample_riddles));

        Node diffNode = getFirstChildElement(getFirstChildElement(doc));
        //Find nodes for correct difficulty setting
        while(diffNode != null && !diffNode.getNodeName().equals("difficulty" + difficulty)) {
            diffNode = diffNode.getNextSibling();
        }
        Element elemDiffNode = (Element)diffNode;
        NodeList rList = elemDiffNode.getElementsByTagName("riddle");
        //List of riddles acquired, now to randomize

        Button buttonArray[] = new Button[4];
        buttonArray[0] = c1Button;
        buttonArray[1] = c2Button;
        buttonArray[2] = c3Button;
        buttonArray[3] = c4Button;

        Random r = new Random();
        int randomRiddle = (r.nextInt(rList.getLength()));
        Element riddleElem = (Element) rList.item(randomRiddle);
        NodeList text = riddleElem.getElementsByTagName("text");
        riddleTV.setText(riddleContext + "\n\n" + text.item(0).getFirstChild().getNodeValue());
        NodeList choices = riddleElem.getElementsByTagName("choice");
        int answer = -1;
        for (int i = 0; i < choices.getLength(); i++) {
            if (choices.item(i).hasAttributes()) {
                //i.e. if it is the answer
                answer = i;
            }
            buttonArray[i].setText(choices.item(i).getFirstChild().getNodeValue());
        }


        return answer;
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

    //I made these
    private Element getNextSiblingElement(Node node) {
        node = node.getNextSibling();
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
            node = node.getNextSibling();
        return (Element)node;
    }



}
