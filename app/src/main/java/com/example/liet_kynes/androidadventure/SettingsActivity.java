package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.os.Bundle;
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
import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_SONG;
import static com.example.liet_kynes.androidadventure.MainActivity.EXTRA_MAIN_SOUND;

public class SettingsActivity extends AppCompatActivity {

    //These functions allow us to pass strictly the audio resource tag rather than setting global
    // variables for these values
    public int getTrackIndex(int rawValue){
        if(rawValue == R.raw.ambient_cave)
            return 0;
        else if(rawValue == R.raw.forest_night)
            return 1;
        else if(rawValue == R.raw.winter_woods)
            return 2;
        else if(rawValue == R.raw.nightmare)
            return 3;
        else
            return -1;
    }

    public int getMP3ResourceValue(int trackIndex){
        if(trackIndex == 0)
            return R.raw.ambient_cave;
        else if(trackIndex == 1)
            return R.raw.forest_night;
        else if(trackIndex == 2)
            return R.raw.winter_woods;
        else if(trackIndex == 3)
            return R.raw.nightmare;
        else
            return -1;
    }

    private boolean music;
    private int track;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toolbar setup stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        //Initializing view elements
        Switch playMusicSwitch = (Switch) findViewById(R.id.playMusicSwitch);
        Spinner songSpinner = (Spinner) findViewById(R.id.songSpinner);
        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficultySpinner);

        //Get data from main intent
        Intent fromAdventure = getIntent();
        music = fromAdventure.getBooleanExtra(EXTRA_MAIN_SOUND, true);
        track = fromAdventure.getIntExtra(EXTRA_MAIN_SONG, R.raw.ambient_cave); //Default song is 0 in the array
        difficulty = fromAdventure.getIntExtra(EXTRA_MAIN_DIFF_LEVEL, 0); //Default difficulty is text input only

        //Take that data and change the settings view to match the configuration
        playMusicSwitch.setChecked(music);
        difficultySpinner.setSelection(difficulty);
        songSpinner.setSelection(getTrackIndex(track));

        //Play music switch functionality
        playMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                music = isChecked; //should set music to the value of the switch
            }
        });


        //Song spinner functionality
        songSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                track = getMP3ResourceValue(position);
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
        response.putExtra(MainActivity.EXTRA_MAIN_DIFF_LEVEL, difficulty);

        setResult(RESULT_OK, response);
        super.finish();
    }

}
