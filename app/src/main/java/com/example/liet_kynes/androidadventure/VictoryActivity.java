package com.example.liet_kynes.androidadventure;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VictoryActivity extends AppCompatActivity {

    //Setting activity variables
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);

        TextView victoryTextView = (TextView) findViewById(R.id.victoryTextView); //Setting up layout variable interaction

        //Victory music
        mediaPlayer = MediaPlayer.create(this, R.raw.victory_sound); //If we're going to play the same music per song, this can be hard coded.
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


}
