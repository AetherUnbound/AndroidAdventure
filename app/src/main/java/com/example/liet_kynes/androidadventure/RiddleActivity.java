package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.liet_kynes.androidadventure.AdventureFragment.DIFFICULTY_LEVEL;
import static com.example.liet_kynes.androidadventure.AdventureFragment.RIDDLE_CONTEXT;
import static com.example.liet_kynes.androidadventure.AdventureFragment.RIDDLE_RESULT;

public class RiddleActivity extends AppCompatActivity {
    //Variables
    private int difficulty;
    private String riddleContext;
    private boolean correct = false; //Default to incorrect
    private int correctAnswer;
    private Riddle RIDDLE;
    private final String TAG = "RIDDLE_DEBUG";
    private static MediaPlayer mediaPlayer;




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

        //Play some meditative music (or stressful)
        mediaPlayer = MediaPlayer.create(this, R.raw.nightmare); //If we're going to play the same music per song, this can be hard coded.
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Parsing data from the XML and adding text to layout
        try {
            correctAnswer = RIDDLE.createRiddle(this.getApplicationContext(), riddleTextView, choiceOneButton, choiceTwoButton, choiceThreeButton, choiceFourButton, difficulty, riddleContext);
            if(correctAnswer < 0) {
                throw new RuntimeException("No answer found");
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }

        choiceOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctAnswer == 1) {
                    correct = true;
                }
                finish();
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctAnswer == 2) {
                    correct = true;
                }
                finish();
            }
        });

        choiceThreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctAnswer == 3) {
                    correct = true;
                }
                finish();
            }
        });

        choiceFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctAnswer == 4) {
                    correct = true;
                }
                finish();
            }
        });

        //Code to populate the layout with the riddle and return to AdventureFragment;
    }


    @Override
    public void finish() {

        Intent response = new Intent(RiddleActivity.this, MainActivity.class);

        response.putExtra(RIDDLE_RESULT, correct);
        setResult(RESULT_OK, response);

        super.finish();
    }
}
