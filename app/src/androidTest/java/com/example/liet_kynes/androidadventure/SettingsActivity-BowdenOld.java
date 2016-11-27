package com.example.liet_kynes.homework2;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    //These are ways to swap between spinner index and track reference without having to know
    //what physical int is being stored for each track. If you know a better way please tell me!
    public int getTrackIndex(int rawValue){
        if(rawValue == R.raw.e1m1)
            return 0;
        else if(rawValue == R.raw.e1m3)
            return 1;
        else if(rawValue == R.raw.e1m5)
            return 2;
        else if(rawValue == R.raw.e1m8)
            return 3;
        else
            return -1;
    }

    public int getMP3ResourceValue(int trackIndex){
        if(trackIndex == 0)
            return R.raw.e1m1;
        else if(trackIndex == 1)
            return R.raw.e1m3;
        else if(trackIndex == 2)
            return R.raw.e1m5;
        else if(trackIndex == 3)
            return R.raw.e1m8;
        else
            return -1;
    }

    private boolean music;
    private boolean doom;
    private int track;
    private int doomCount = 0;
    private final String TAG = "settings";
    private boolean afterDeclared = false;

    private Animation doomButtonAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Toolbar setup stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Switch sMusicSwitch = (Switch) findViewById(R.id.musicSwitch);
        final Spinner sTrackSpinner = (Spinner) findViewById(R.id.trackSpinner);
        final ImageButton sDoomImageButton = (ImageButton) findViewById(R.id.doomImageButton);
        final TextView sDoomModeTV = (TextView) findViewById(R.id.doomModeTextView);
        final TextView sDCTV1 = (TextView) findViewById(R.id.doomTextView1);
        final TextView sDCTV2 = (TextView) findViewById(R.id.doomTextView2);
        final TextView sDCTV3 = (TextView) findViewById(R.id.doomTextView3);
        sDoomImageButton.setZ(100);

        Intent fromContacts = getIntent();
        //Pull values from intent
        music = fromContacts.getBooleanExtra(ContactsActivity.EXTRA_IS_PLAYING, true);
        track = fromContacts.getIntExtra(ContactsActivity.EXTRA_CURRENT_TRACK, R.raw.e1m1);
        doom = fromContacts.getBooleanExtra(ContactsActivity.EXTRA_IS_DOOM, false);
        Log.d(TAG, "Track received from contacts: " + track);

        //Set values from pushed intents
        sMusicSwitch.setChecked(music);
        sTrackSpinner.setSelection(getTrackIndex(track));
        afterDeclared = true;

        //Set up animation
        doomButtonAnimation = AnimationUtils.loadAnimation(this, R.anim.doom_button_scale_up);
        final ValueAnimator doomTextStyleAnim = ValueAnimator.ofArgb(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        doomTextStyleAnim.setDuration(500);
        doomTextStyleAnim.setInterpolator(new LinearInterpolator());
        doomTextStyleAnim.setRepeatMode(ValueAnimator.REVERSE);
        doomTextStyleAnim.setRepeatCount(ValueAnimator.INFINITE);
        doomTextStyleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sDoomModeTV.setTextColor((int) animation.getAnimatedValue());
            }
        });

        if (doom) {
            doomCount = 6;
            sDCTV1.setText(String.valueOf(doomCount));
            sDCTV2.setText(String.valueOf(doomCount));
            sDCTV3.setText(String.valueOf(doomCount));
            sDCTV1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sDCTV2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sDCTV3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            sDoomModeTV.setVisibility(View.VISIBLE);
            doomTextStyleAnim.start();
            track = R.raw.e1m8;
        }

        sMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                music = isChecked; //should set music to the value of the switch
                if(afterDeclared)
                    Toast.makeText(SettingsActivity.this, R.string.toast_music, Toast.LENGTH_SHORT).show();
            }
        });

        sDoomImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doomCount++;
                if(doomCount <= 6) {
                    sDCTV1.setText(String.valueOf(doomCount));
                    sDCTV2.setText(String.valueOf(doomCount));
                    sDCTV3.setText(String.valueOf(doomCount));
                }
                if(doomCount == 6) {
                    //do the fun stuff
                    sDCTV1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    sDCTV2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    sDCTV3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    sDoomImageButton.startAnimation(doomButtonAnimation);
                    sDoomModeTV.setVisibility(View.VISIBLE);
                    doomTextStyleAnim.start();
                    doom = true;
                    track = R.raw.e1m8;
                    sTrackSpinner.setSelection(getTrackIndex(R.raw.e1m8));
                }
                if(doomCount > 6) {
                    doomCount = 0;
                    doomButtonAnimation.reset();
                    doomTextStyleAnim.cancel();
                    sDCTV1.setText(String.valueOf(doomCount));
                    sDCTV2.setText(String.valueOf(doomCount));
                    sDCTV3.setText(String.valueOf(doomCount));
                    sDCTV1.setTextColor(Color.WHITE);
                    sDCTV2.setTextColor(Color.WHITE);
                    sDCTV3.setTextColor(Color.WHITE);
                    sDoomModeTV.setVisibility(View.INVISIBLE);
                    doom = false;
                    track = R.raw.e1m1;
                    sTrackSpinner.setSelection(getTrackIndex(R.raw.e1m1));
                }
            }
        });

        sTrackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                track = getMP3ResourceValue(position); //should set new resource value
                Log.d(TAG, "Position and track received: " + position + ", " + track);
                if (afterDeclared)
                    Toast.makeText(SettingsActivity.this, R.string.toast_track, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.activity_contacts) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        //here's where putting the return values will be
        Intent response = new Intent(SettingsActivity.this, ContactsActivity.class);

        response.putExtra(ContactsActivity.EXTRA_IS_PLAYING, music);
        response.putExtra(ContactsActivity.EXTRA_CURRENT_TRACK, track);
        response.putExtra(ContactsActivity.EXTRA_IS_DOOM, doom);
        Log.d(TAG, "Track sent back to contacts: " + track);
        Log.d(TAG, "Doom sent back to contacts: " + doom);

        setResult(RESULT_OK, response);
        super.finish();
    }
}
