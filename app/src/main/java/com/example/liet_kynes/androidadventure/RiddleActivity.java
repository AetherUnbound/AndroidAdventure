package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import static com.example.liet_kynes.androidadventure.AdventureFragment.DIFFICULTY_LEVEL;
import static com.example.liet_kynes.androidadventure.AdventureFragment.RIDDLE_CONTEXT;

public class RiddleActivity extends AppCompatActivity {
    //Variables
    private int difficulty;
    private String riddleContext;
    private boolean correct = false; //Default to incorrect
    private int correctIndex;
    private Riddle RIDDLE;
    private final String TAG = "RIDDLE_DEBUG";

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
        riddleContext = fromAdventure.getStringExtra(RIDDLE_CONTEXT);
        RIDDLE = new Riddle();

        try {
            correctIndex = RIDDLE.createRiddle(this.getApplicationContext(), riddleTextView, choiceOneButton, choiceTwoButton, choiceThreeButton, choiceFourButton, difficulty, riddleContext);
            if(correctIndex < 0) {
                throw new RuntimeException("No answer found");
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }



        //Code to populate the layout with the riddle and return to AdventureFragment;
    }
}
