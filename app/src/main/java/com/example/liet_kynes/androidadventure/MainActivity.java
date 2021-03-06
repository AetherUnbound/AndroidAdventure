package com.example.liet_kynes.androidadventure;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static String EXTRA_MAIN_SOUND = "com.example.lietkynes.androidadventure.main_sound";
    public static String EXTRA_MAIN_SONG = "com.example.lietkynes.androidadventure.main_song";
    public static String EXTRA_MAIN_DIFF_LEVEL = "com.example.lietkynes.androidadventure.main_diff_level";
    public static final int REQUEST_CODE_SETTINGS = 0;
    public static final int REQUEST_CODE_RIDDLE = 1;
    public static final int REQUEST_CODE_VICTORY = 2;
    public static final int REQUEST_CODE_FAILURE = 3;
    public static String TAG = "DEBUG";
    public int getDIFFICULTY_LEVEL() { return DIFFICULTY_LEVEL; }
    //Variables and their default values
    private int DIFFICULTY_LEVEL = 0;
    private int TRACK = R.raw.ambient_cave;
    private boolean IS_PLAYING = true;
    private static MediaPlayer mediaPlayer;

    protected void pauseMusic() {
        mediaPlayer.pause();
    }

    protected void resumeMusic() {
        if (!mediaPlayer.isPlaying() && IS_PLAYING)
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
                Snackbar snackbar = Snackbar.make(view, "Restarting Adventure...", Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(getColor(R.color.colorAccent));
                snackbar.setAction("Action", null).show();

                //This code ensures that pressing the FAB from a fragment that's not the main screen
                // doesn't crash the app
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if(frag instanceof AdventureFragment) {
                    AdventureFragment advFrag = (AdventureFragment)frag;
//                    AdventureFragment.restartAdventure(advFrag);
                    advFrag.restartAdventure();
                }
            }
        });

        if(savedInstanceState != null) {
            //anything to be saved through orientation change
        }
        else {
            //If app has just started, begin music player
            //Start off with some sound
            mediaPlayer = MediaPlayer.create(this, TRACK);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        AdventureFragment adventureFragment = new AdventureFragment();
//        RiddleFragment riddleFragment = new RiddleFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment_container, adventureFragment).commit();


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
            i.putExtra(EXTRA_MAIN_SOUND, IS_PLAYING); //Sound is on by default
            i.putExtra(EXTRA_MAIN_SONG, TRACK); //Default song is element 0 in the array
            i.putExtra(EXTRA_MAIN_DIFF_LEVEL, DIFFICULTY_LEVEL); //Default difficulty is 4, for text input only

            startActivityForResult(i, REQUEST_CODE_SETTINGS); //I'm not 100% sure this is proper form, but it works

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

        if (requestCode == REQUEST_CODE_SETTINGS){
            if (data == null)
                return; //Default case; nothing changed

            //Change TRACK setting change
            int track = data.getIntExtra(EXTRA_MAIN_SONG, 0);
            if (track != TRACK) {
                updateTrack(track);
                Toast.makeText(MainActivity.this, "Music Track Changed", Toast.LENGTH_SHORT).show();
            }

            //Difficulty level
            int diff = data.getIntExtra(EXTRA_MAIN_DIFF_LEVEL, 4);
            if (diff != DIFFICULTY_LEVEL){
                DIFFICULTY_LEVEL = diff;
                //Code to change the difficulty level
                Toast.makeText(MainActivity.this, "Riddle Difficulty Changed", Toast.LENGTH_SHORT).show();
            }


            //Turn music/sound off/on
            if (!data.getBooleanExtra(EXTRA_MAIN_SOUND, true)){
                IS_PLAYING = false;
            }
        }

        else if (requestCode == REQUEST_CODE_RIDDLE) {
            AdventureFragment fragment = (AdventureFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.onRiddleResult(data);
        }

        else if (requestCode == REQUEST_CODE_FAILURE || requestCode == REQUEST_CODE_VICTORY) {
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String toastString;
            if(frag instanceof AdventureFragment) {
                AdventureFragment advFrag = (AdventureFragment)frag;
//                AdventureFragment.restartAdventure(advFrag);
                advFrag.restartAdventure();
            }

            if(requestCode == REQUEST_CODE_FAILURE)
                toastString = "Try to do better this time...";
            else
                toastString = "Try to win again!";
            Toast.makeText(MainActivity.this, toastString, Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Breakpoint testing");
    }

    private void updateTrack(int track) {
        mediaPlayer.pause();
        mediaPlayer.reset();
        AssetFileDescriptor afd = getResources().openRawResourceFd(track);
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        }
        catch (IOException ioe) {
            Log.d(TAG, "IOException when trying to locate new TRACK" + ioe);
        }
        TRACK = track;
        resumeMusic();
    }

}
