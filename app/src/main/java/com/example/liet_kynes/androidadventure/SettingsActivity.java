package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

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

        //Initializing view elements
        Switch nightModeSwitch = (Switch) findViewById(R.id.nightModeSwitch);
        Switch playMusicSwitch = (Switch) findViewById(R.id.playMusicSwitch);
        Spinner songSpinner = (Spinner) findViewById(R.id.songSpinner);
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);

        //Get data from main intent
        Intent fromAdventure = getIntent();
        music = fromAdventure.getBooleanExtra(EXTRA_MAIN_SOUND, true);
        track = fromAdventure.getIntExtra(EXTRA_MAIN_SONG, 0); //Default song is 0 in the array
        nightMode = fromAdventure.getBooleanExtra(EXTRA_MAIN_NIGHT_MODE, false);
        difficulty = fromAdventure.getIntExtra(EXTRA_MAIN_DIFF_LEVEL, 0); //Default difficulty is text input only

        //Take that data and change the settings view to match the configuration
        playMusicSwitch.setChecked(music);
        nightModeSwitch.setChecked(nightMode);
        difficultySpinner.setSelection(difficulty);
        songSpinner.setSelection(track);

        //Play music switch functionality
        playMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                music = isChecked; //should set music to the value of the switch
            }
        });

        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                nightMode = isChecked; //should set music to the value of the switch
            }
        });

        //Song spinner functionality
        songSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                track = position; //should set new resource value
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.content_main) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        //here's where putting the return values will be
        Intent response = new Intent(SettingsActivity.this, MainActivity.class);

        response.putExtra(MainActivity.EXTRA_MAIN_SOUND, music);
        response.putExtra(MainActivity.EXTRA_MAIN_SONG, track);
        response.putExtra(MainActivity.EXTRA_MAIN_NIGHT_MODE, nightMode);
        response.putExtra(MainActivity.EXTRA_MAIN_DIFF_LEVEL, difficulty);

        setResult(RESULT_OK, response);
        super.finish();
    }

}
