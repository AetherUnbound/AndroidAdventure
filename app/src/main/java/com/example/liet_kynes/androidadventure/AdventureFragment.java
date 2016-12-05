package com.example.liet_kynes.androidadventure;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.liet_kynes.androidadventure.MainActivity.REQUEST_CODE_FAILURE;
import static com.example.liet_kynes.androidadventure.MainActivity.REQUEST_CODE_RIDDLE;
import static com.example.liet_kynes.androidadventure.MainActivity.REQUEST_CODE_VICTORY;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdventureFragment extends Fragment {

    private static final String TAG = "MAIN_FRAGMENT_DEBUG";
    public static final String PLAYER_POSITION = "PLAYER_POSITION";
    public static final String DIFFICULTY_LEVEL = "DIFFICULTY_LEVEL";
    public static final String RIDDLE_CONTEXT = "RIDDLE_CONTEXT";
    public static final String RIDDLE_RESULT = "RIDDLE_RESULT";
    public static final String ENDING_TEXT = "ENDING_TEXT";
    public static final int GET_RIDDLE_RESULT = 0;
    private Adventure ADVENTURE;


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

    public void restartAdventure() {
        ADVENTURE.restartAdventure();
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
            launchRiddle(((MainActivity)getActivity()).getDIFFICULTY_LEVEL(), ADVENTURE.getCurrentTextString());
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
        Intent toEnding;
        int requestCode;
        if(victory) {
            toEnding = new Intent(getActivity(), VictoryActivity.class);
            requestCode = REQUEST_CODE_VICTORY;
        }
        else {
            toEnding = new Intent(getActivity(), FailureActivity.class);
            requestCode = REQUEST_CODE_FAILURE;
        }
        toEnding.putExtra(ENDING_TEXT, ADVENTURE.getCurrentTextString());
        getActivity().startActivityForResult(toEnding, requestCode);
    }

    protected void onRiddleResult(Intent data) {
        boolean passed = data.getBooleanExtra(RIDDLE_RESULT, false); //so if the code fails they fail MUAHAHAHAHA
        if(passed) {
            //how do we access the button here so we can advance the thing?
            //maybe a direct access will work
            ADVENTURE.setNewLocation("choice1");
            afterLocationChange();
            Toast.makeText(getActivity().getApplicationContext(), "Riddle Passed", Toast.LENGTH_LONG).show();
        }
        else {
            ADVENTURE.setNewLocation("choice2");
            afterLocationChange();
            Toast.makeText(getActivity().getApplicationContext(), "Riddle Failed", Toast.LENGTH_LONG).show();
        }
    }

}
