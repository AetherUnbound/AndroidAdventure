package com.example.liet_kynes.androidadventure;

/**
 * Created by Liet-Kynes on 11/29/2016.
 */

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;

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

    Riddle(int difficulty) throws ParserConfigurationException, SAXException, IOException, RuntimeException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();


    }


}
