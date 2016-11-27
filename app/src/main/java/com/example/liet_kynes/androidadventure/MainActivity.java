package com.example.liet_kynes.androidadventure;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_MAIN_SOUND = "com.example.lietkynes.androidadventure.main_sound";
    public static String EXTRA_MAIN_SONG = "com.example.lietkynes.androidadventure.main_song";
    public static String EXTRA_MAIN_NIGHT_MODE = "com.example.lietkynes.androidadventure.main_night_mode";
    public static String EXTRA_MAIN_DIFF_LEVEL = "com.example.lietkynes.androidadventure.main_diff_level";
    private static final int REQUEST_SONG_CHANGE = 0;
    //Variables and their default values
    private int difficulty_level = 0;
    private int track = 0;
    private boolean musicPlaying = true;
    private boolean nightMode = false;
    MediaPlayer mediaPlayer;

    protected void pauseMusic() {
        mediaPlayer.pause();
    }

    protected void resumeMusic() {
        if (!mediaPlayer.isPlaying() && musicPlaying)
            mediaPlayer.start();
    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Start off with some sound
        mediaPlayer = MediaPlayer.create(this, R.raw.ambient_cave);
        mediaPlayer.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Setting and sending data to the settings activity
            Intent i = new Intent(this, SettingsActivity.class);
            i.putExtra(EXTRA_MAIN_SOUND, musicPlaying); //Sound is on by default
            i.putExtra(EXTRA_MAIN_SONG, track); //Default song is element 0 in the array
            i.putExtra(EXTRA_MAIN_DIFF_LEVEL, difficulty_level); //Default difficulty is 4, for text input only
            i.putExtra(EXTRA_MAIN_NIGHT_MODE, nightMode); //This may be harder to do than I originally thought, but I'm giving it a shot

            startActivityForResult(i, REQUEST_SONG_CHANGE); //I'm not 100% sure this is proper form, but it works

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Skeleton to return the intent and change settings in the main fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){ //Check for failure
            return;
        }

        if (requestCode == REQUEST_SONG_CHANGE){
            if (data == null)
                return; //Default case; nothing changed

            //Change track setting change
            track = data.getIntExtra(EXTRA_MAIN_SONG, 0);

            //Difficulty level
            if (data.getIntExtra(EXTRA_MAIN_DIFF_LEVEL, 4) != 4){
                //Code to change the difficulty level
            }

            //Nightmode code
            nightMode = data.getBooleanExtra(EXTRA_MAIN_NIGHT_MODE, false);

            //Turn music/sound off/on
            if (!data.getBooleanExtra(EXTRA_MAIN_SOUND, true)){
                musicPlaying = false;
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
