package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_DIFF_LEVEL;
import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_NIGHT_MODE;
import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_SONG;
import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_SOUND;

public class SettingsActivity extends AppCompatActivity {

    private boolean music;
    private boolean nightMode;
    private int track;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Toolbar setup stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Switch nightModeSwitch = (Switch) findViewById(R.id.nightModeSwitch);
        Switch playMusicSwitch = (Switch) findViewById(R.id.playMusicSwitch);
        Spinner songSpinner = (Spinner) findViewById(R.id.songSpinner);

        Intent fromAdventure = getIntent();
        music = fromAdventure.getBooleanExtra(EXTRA_MAIN_SOUND, true);
        track = fromAdventure.getIntExtra(EXTRA_MAIN_SONG, 0); //Default song is 0 in the array
        nightMode = fromAdventure.getBooleanExtra(EXTRA_MAIN_NIGHT_MODE, false);

        //Need to add a difficulty spinner or dropdown in the view
        difficulty = fromAdventure.getIntExtra(EXTRA_MAIN_DIFF_LEVEL, 1); //Default difficulty is 1, or easy

        playMusicSwitch.setChecked(music);
        nightModeSwitch.setChecked(nightMode);

    }

}
