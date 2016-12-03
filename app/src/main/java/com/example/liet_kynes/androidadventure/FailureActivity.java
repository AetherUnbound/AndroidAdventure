package com.example.liet_kynes.androidadventure;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FailureActivity extends AppCompatActivity {
    //Activity variables
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);

        //Layout variables
        TextView failureTextView = (TextView) findViewById(R.id.lossTextView);

        //Play some meditative music (or stressful)
        mediaPlayer = MediaPlayer.create(this, R.raw.nightmare); //If we're going to play the same music per song, this can be hard coded.
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
}
