package com.example.liet_kynes.homework2;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

public class ContactsActivity extends AppCompatActivity {

    //logging and extra variables
    private final String TAG = "contacts";
    private final String FILENAME = "doom_data";
    private static final int REQUEST_CODE_SETTINGS = 0;

    //need a key to save the current index in bundle
    private static final String KEY_CONTACT_INDEX = "contact_index";

    //Mediaplayer initiation
    //Static makes it so only one is declared for app instance (allegedly)
    private static MediaPlayer doomSound;
    public static final String EXTRA_CURRENT_TRACK = "CURRENT_TRACK";
    public static final int TRACK_E1M1 = R.raw.e1m1;
    public static final int TRACK_E1M3 = R.raw.e1m3;
    public static final int TRACK_E1M5 = R.raw.e1m5;
    public static final int TRACK_E1M8 = R.raw.e1m8;
    private int CURRENT_TRACK = TRACK_E1M1; //default is first track
    public static final String EXTRA_IS_PLAYING = "PLAYING_STATUS";
    private boolean PLAYING_STATUS = true;
    public static final String EXTRA_IS_DOOM = "DOOM_STATUS";
    private boolean DOOM_STATUS = false;

    protected void playMusic() {
        if(!doomSound.isPlaying() && PLAYING_STATUS)
            doomSound.start();
    }

    protected void pauseMusic() {
        doomSound.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //pauses the music if pause is called
        pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //plays the music only if it isn't already playing (i.e. orientation change)
        playMusic();

        //dumb animator stuff
        backgroundAnim.setDuration(2000);
        backgroundAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        backgroundAnim.setRepeatCount(ValueAnimator.INFINITE);
        backgroundAnim.setRepeatMode(ValueAnimator.REVERSE);
        backgroundAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                cWholeLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });

        if (DOOM_STATUS)
            backgroundAnim.start();
        else {
            //I wanted to make sure the animation was cancelling because I troubleshoot the damn thing
            //for about half an hour. I'm not gonna remove any of these because it works now lol.
            cWholeLayout.animate().cancel();
            cWholeLayout.clearAnimation();
            backgroundAnim.cancel();
            backgroundAnim.end();
            cWholeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    //Controllers to edit view
    private ImageView cMonsterImageView;
    private TextView cNameTextView;
    private TextView cDiffTextView;
    private Button cPreviousButton;
    private Button cNextButton;
    private RelativeLayout cWholeLayout;
    private ValueAnimator backgroundAnim;

    //Editable portions
    private EditText cWeakEditText;
    private Spinner cMapSpinner;


    //Model: array of Doom Monsters
    final Contacts[] cContacts = new Contacts[] {
            new Contacts(R.string.zombieman_name, R.drawable.zombieman, R.string.diff_rating_0),
            new Contacts(R.string.shotgunguy_name, R.drawable.shotgunguy, R.string.diff_rating_0),
            new Contacts(R.string.imp_name, R.drawable.imp, R.string.diff_rating_1),
            new Contacts(R.string.demon_name, R.drawable.demon, R.string.diff_rating_1),
            new Contacts(R.string.skele_name, R.drawable.skele, R.string.diff_rating_2),
            new Contacts(R.string.eye_name, R.drawable.eye, R.string.diff_rating_2),
            new Contacts(R.string.spider_name, R.drawable.spider, R.string.diff_rating_3),
            new Contacts(R.string.minotaur_name, R.drawable.minotaur, R.string.diff_rating_3)
    };

    //Default is to start with the zombieguy
    private int cCurrentIndex = 0;

    //Get array length for file I/O use
    private int cArrayLength = cContacts.length;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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
            Intent message = new Intent(ContactsActivity.this, SettingsActivity.class);
            message.putExtra(EXTRA_IS_PLAYING, PLAYING_STATUS); //add is playing flag
            message.putExtra(EXTRA_IS_DOOM, DOOM_STATUS); //add is doom flag
            message.putExtra(EXTRA_CURRENT_TRACK, CURRENT_TRACK); //add current track number
            Log.d(TAG, "Current track passed to settings: " + CURRENT_TRACK);

            startActivityForResult(message, REQUEST_CODE_SETTINGS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //SharedPreferences
        SharedPreferences sharedPref = ContactsActivity.this.getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < cArrayLength; i++) {
            cContacts[i].setcWeakness(sharedPref.getString(getResources().getString(R.string.weakness_label) + i, ""));
            Log.d(TAG, "Retrieving weakness: " + getResources().getString(R.string.weakness_label) + i + " - " + cContacts[i].getcWeakness());
            cContacts[i].setcMapID(sharedPref.getInt(getResources().getString(R.string.map_label) + i, 0));
            Log.d(TAG, "Retrieving mapID: " + getResources().getString(R.string.map_label) + i + " - " + cContacts[i].getcMapID());
        }
        CURRENT_TRACK = sharedPref.getInt(EXTRA_CURRENT_TRACK, R.raw.e1m1);
        PLAYING_STATUS = sharedPref.getBoolean(EXTRA_IS_PLAYING, true);
        DOOM_STATUS = sharedPref.getBoolean(EXTRA_IS_DOOM, false);

        //This is designed to return to the original index when rotated to landscape
        if(savedInstanceState != null)
            cCurrentIndex = savedInstanceState.getInt(KEY_CONTACT_INDEX, 0);
        else {
            //If the app has just started, begin the music player
            doomSound = MediaPlayer.create(this, CURRENT_TRACK);
            doomSound.setLooping(true);
            doomSound.setVolume(50, 50);
            doomSound.start();
        }

        //Boring copying of view elements so we can reference them easier
        //I realize my naming could have been better here, my apologies
        cMonsterImageView = (ImageView) findViewById(R.id.contactImageView);
        cNameTextView = (TextView) findViewById(R.id.nameTextView);
        cDiffTextView = (TextView) findViewById(R.id.diffTextView);
        cPreviousButton = (Button) findViewById(R.id.prevButton);
        cNextButton = (Button) findViewById(R.id.nextButton);
        cWeakEditText = (EditText) findViewById(R.id.weakEditText);
        cMapSpinner = (Spinner) findViewById(R.id.mapSpinner);
        cWholeLayout = (RelativeLayout) findViewById(R.id.activity_contacts);

        //I'll have to figure out why this step is necessary
        cWeakEditText.setText(cContacts[cCurrentIndex].getcWeakness());
        cMapSpinner.setSelection(cContacts[cCurrentIndex].getcMapID());

        //I have to add a text watcher for weakness so that the field gets updated
        cWeakEditText.addTextChangedListener(weakWatcher);

        //I may also have to add a selection changed watcher for the spinner
        cMapSpinner.setOnItemSelectedListener(mapWatcher);

        backgroundAnim = ValueAnimator.ofArgb(getResources().getColor(R.color.colorPrimaryDark), Color.TRANSPARENT);


        //Update fields and image on current state
        update();

        //Here I define how the next button works.
        cNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Next clicked");

                //need to be able to come back to the starting index of 0
                cCurrentIndex = (cCurrentIndex + 1) % cArrayLength;


                //update the text fields again as necessary
                update();
                cWeakEditText.setText(cContacts[cCurrentIndex].getcWeakness());
                cMapSpinner.setSelection(cContacts[cCurrentIndex].getcMapID());
            }
        });

        //gotta do the same thing for the previous button
        cPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cCurrentIndex = (cCurrentIndex - 1);

                //need to wrap around but to the furthest index
                if(cCurrentIndex == -1)
                    cCurrentIndex = cArrayLength - 1;

                //update again and get saved values
                update();
                cWeakEditText.setText(cContacts[cCurrentIndex].getcWeakness());
                cMapSpinner.setSelection(cContacts[cCurrentIndex].getcMapID());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_CODE_SETTINGS) {
            if(data == null)
                return;

            PLAYING_STATUS = data.getBooleanExtra(EXTRA_IS_PLAYING, true);
            DOOM_STATUS = data.getBooleanExtra(EXTRA_IS_DOOM, false);
            int track = data.getIntExtra(EXTRA_CURRENT_TRACK, CURRENT_TRACK);
            Log.d(TAG, "Track received back from settings: " + track);
            if (track != CURRENT_TRACK)
                updateTrack(track);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy()");

        //Save to shared preferences
        SharedPreferences sharedPreferences = ContactsActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < cArrayLength; i++) {
            editor.putString(getResources().getString(R.string.weakness_label) + i, cContacts[i].getcWeakness());
            Log.d(TAG, "Writing weakness: " + getResources().getString(R.string.weakness_label) + i + " - " + cContacts[i].getcWeakness());
            editor.putInt(getResources().getString(R.string.map_label) + i, cContacts[i].getcMapID());
            Log.d(TAG, "Writing mapID: " + getResources().getString(R.string.map_label) + i + " - " + cContacts[i].getcMapID());
        }
        editor.putInt(EXTRA_CURRENT_TRACK, CURRENT_TRACK);
        editor.putBoolean(EXTRA_IS_PLAYING, PLAYING_STATUS);
        editor.putBoolean(EXTRA_IS_DOOM, DOOM_STATUS);
        editor.commit();

    }

    //Defining weakWatcher
    private final TextWatcher weakWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cContacts[cCurrentIndex].setcWeakness(s.toString());
            //Log.d(TAG, "About to set weakness");
            update();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    //Defining mapWatcher
    private final AdapterView.OnItemSelectedListener mapWatcher = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            cContacts[cCurrentIndex].setcMapID(position);
            //Log.d(TAG, "About to change selection");
            update();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //need to save the current location by overwriting onSavedInstance
    @Override
    public void onSaveInstanceState( Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(KEY_CONTACT_INDEX, cCurrentIndex);
    }

    private void updateTrack(int track) {
        doomSound.pause();
        doomSound.reset();
        AssetFileDescriptor afd = getResources().openRawResourceFd(track);
        try {
            doomSound.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            doomSound.prepare();
        }
        catch (IOException ioe) {
            Log.d(TAG, "IOException when trying to locate new track" + ioe);
        }
        CURRENT_TRACK = track;
        playMusic();
    }

    //method to update all text fields and editable fields when a new contact is chosen
    private void update() {
        //Log.d(TAG, "updating");
        cMonsterImageView.setImageResource(cContacts[cCurrentIndex].getcImageID());
        cNameTextView.setText(cContacts[cCurrentIndex].getcNameID());
        cMapSpinner.setSelection(cContacts[cCurrentIndex].getcMapID());
        cDiffTextView.setText(cContacts[cCurrentIndex].getcDiffID());
    }

}
