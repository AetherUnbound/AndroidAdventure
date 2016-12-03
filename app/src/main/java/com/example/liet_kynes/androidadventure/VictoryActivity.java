package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.example.liet_kynes.androidadventure.AdventureFragment.ENDING_TEXT;

public class VictoryActivity extends AppCompatActivity {

    //Setting activity variables
    private static MediaPlayer mediaPlayer;
    private String endingText;

    protected void pauseMusic() { mediaPlayer.pause(); }
    protected void resumeMusic() { if (!mediaPlayer.isPlaying()) mediaPlayer.start(); }

    @Override
    protected void onPause() {
        super.onPause();
        pauseMusic();
    }

    @Override
    protected void onResume(){
        super.onResume();
        resumeMusic();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        TextView victoryTextView = (TextView) findViewById(R.id.victoryTextView); //Setting up layout variable interaction

        //Victory music
        mediaPlayer = MediaPlayer.create(this, R.raw.victory_sound); //If we're going to play the same music per song, this can be hard coded.
        mediaPlayer.start();

        Intent fromAdventure = getIntent();
        endingText = fromAdventure.getStringExtra(ENDING_TEXT);

        victoryTextView.setText(endingText);
    }


}
