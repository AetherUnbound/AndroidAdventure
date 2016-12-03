package com.example.liet_kynes.androidadventure;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.liet_kynes.androidadventure.MainActivity.REQUEST_CODE_RIDDLE;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdventureFragment extends Fragment {

    private static final String TAG = "MAIN_FRAGMENT_DEBUG";
    public static final String PLAYER_POSITION = "PLAYER_POSITION";
    public static final String DIFFICULTY_LEVEL = "DIFFICULTY_LEVEL";
    public static final String RIDDLE_CONTEXT = "RIDDLE_CONTEXT";
    public static final String RIDDLE_RESULT = "RIDDLE_RESULT";
    public static final int GET_RIDDLE_RESULT = 0;
    private String choice = "";
    private Adventure ADVENTURE;
    Animation fadeInAnimation;
    Animation fadeOutAnimation;
    Animation buttonFadeAnimation;


    public AdventureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false); //It breaks if we don't have View view = in here

        //Items for main fragment
        final TextView storyTextView = (TextView) view.findViewById(R.id.storyTextView);
        final Button choiceOneButton = (Button) view.findViewById(R.id.choiceOneButton);
        final Button choiceTwoButton = (Button) view.findViewById(R.id.choiceTwoButton);


        ADVENTURE = new Adventure(storyTextView, choiceOneButton, choiceTwoButton, getActivity());


        if(savedInstanceState == null) {
            try {
                ADVENTURE.buildAdventure();
            }
            catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
        else {
            int playerLocation = savedInstanceState.getInt(PLAYER_POSITION, 0);
            try {
                ADVENTURE.buildAdventure(playerLocation);
            }
            catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
        storyTextView.setVisibility(View.VISIBLE);
        choiceOneButton.setVisibility(View.VISIBLE);
        choiceTwoButton.setVisibility(View.VISIBLE);


        choiceOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADVENTURE.setNewLocation("choice1");
                afterLocationChange();
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADVENTURE.setNewLocation("choice2");
                afterLocationChange();
            }
        });


    //Added this for skeleton code
    return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYER_POSITION, ADVENTURE.getPlayerPosition());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static void restartAdventure(AdventureFragment fragment) {
        fragment.choice = "root";
        fragment.ADVENTURE.restartAdventure();
    }

    private void launchRiddle(int difficulty, String riddleContext) {
        //pass difficulty into intent as extra
        Intent toRiddle = new Intent(getActivity(), RiddleActivity.class);
        toRiddle.putExtra(DIFFICULTY_LEVEL, difficulty);
        toRiddle.putExtra(RIDDLE_CONTEXT, riddleContext);
        getActivity().startActivityForResult(toRiddle, REQUEST_CODE_RIDDLE);
    }

    private void afterLocationChange() {
        if (ADVENTURE.isRiddle()) {
//                        launch riddle activity
            launchRiddle(((MainActivity)getActivity()).getDIFFICULTY_LEVEL(), ADVENTURE.getRiddleString());
        }
        else if(ADVENTURE.isVictory()){
            //Nate, these will also be given strings but if you get down the basic
            // code we can add that in later
            launchEnding(true);
        }
        else if(ADVENTURE.isFailure()){
            launchEnding(false);
        }
    }

    private void launchEnding(boolean victory) {

    }

    protected void onRiddleResult(Intent data) {
        boolean passed = data.getBooleanExtra(RIDDLE_RESULT, false); //so if the code fails they fail MUAHAHAHAHA
        if(passed) {
            //how do we access the button here so we can advance the thing?
            //maybe a direct access will work
            ADVENTURE.setNewLocation("choice1");
            Toast.makeText(getActivity().getApplicationContext(), "Riddle Passed", Toast.LENGTH_LONG).show();
        }
        else {
            ADVENTURE.setNewLocation("choice2");
            Toast.makeText(getActivity().getApplicationContext(), "Riddle Failed", Toast.LENGTH_LONG).show();
        }
    }

}
