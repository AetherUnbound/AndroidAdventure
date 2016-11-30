package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.liet_kynes.androidadventure.AdventureFragment.DIFFICULTY_LEVEL;

public class RiddleActivity extends AppCompatActivity {
    //Variables
    private int difficulty;
    public boolean correct = false; //Default to incorrect

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);

        //Making the layout work
        Button choiceOneButton = (Button) findViewById(R.id.choiceOneButton);
        Button choiceTwoButton = (Button) findViewById(R.id.choiceTwoButton);
        Button choiceThreeButton = (Button) findViewById(R.id.choiceThreeButton);
        Button choiceFourButton = (Button) findViewById(R.id.choiceFourButton);
        TextView riddleTextView = (TextView) findViewById(R.id.riddleTextView);

        //Get the data from the intent
        Intent fromAdventure = getIntent();
        difficulty = fromAdventure.getIntExtra(DIFFICULTY_LEVEL, 0); //Is default 0?

        //Code to populate the layout with the riddle and return to AdventureFragment;
    }
}
