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

        //Animation
        fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_animation);
        buttonFadeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha);
        fadeOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out_animation);

        //Items for main fragment
        final TextView storyTextView = (TextView) view.findViewById(R.id.storyTextView);
        final Button choiceOneButton = (Button) view.findViewById(R.id.choiceOneButton);
        final Button choiceTwoButton = (Button) view.findViewById(R.id.choiceTwoButton);


        ADVENTURE = new Adventure(getActivity().getApplicationContext(), storyTextView, choiceOneButton, choiceTwoButton, getActivity());

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                storyTextView.setVisibility(View.INVISIBLE); //Matt, this is the code to use to transition easier.
                if(choice.equals("choice1") || choice.equals("choice2"))
                    ADVENTURE.setNewLocation(choice);
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
//                    ADVENTURE.setNewLocation(choice);
                storyTextView.startAnimation(fadeInAnimation);
                choiceTwoButton.startAnimation(fadeInAnimation);
                choiceOneButton.startAnimation(fadeInAnimation);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                storyTextView.setVisibility(View.VISIBLE);
                choiceOneButton.setVisibility(View.VISIBLE);
                choiceTwoButton.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

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
                //Temporarily testing out fragment launching

                choiceTwoButton.startAnimation(fadeOutAnimation); //This made things weird. Not a good way weird.
                choiceOneButton.startAnimation(fadeOutAnimation);
                choice = "choice1";
                storyTextView.startAnimation(fadeOutAnimation);

//                if(fadeOutAnimation.hasEnded()) {
//                    ADVENTURE.setNewLocation("choice1");
//                    if (ADVENTURE.isRiddle()) {
//                        launch riddle activity
//                        launchRiddle();
//                    }
//                    storyTextView.setVisibility(View.VISIBLE);
                }
//            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choiceTwoButton.startAnimation(fadeOutAnimation); This made things weird. Not a good way weird.
                //choiceOneButton.startAnimation(fadeOutAnimation);
                choice = "choice2";
                storyTextView.startAnimation(fadeOutAnimation);

//                ADVENTURE.setNewLocation("choice2");
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

//        int diff = activity.getDIFFICULTY_LEVEL();
//        RiddleFragment riddleFragment = new RiddleFragment();
//        activity.replaceAdventureFragmentWithRiddle(riddleFragment);
//        this.getChildFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, riddleFragment, TAG_RIDDLE_FRAGMENT)
//                .addToBackStack(null)
//                .commit();
    }

    private void launchEnding(boolean victory) {

    }

    protected void onRiddleResult(Intent data) {
        boolean passed = data.getBooleanExtra(RIDDLE_RESULT, false); //so if the code fails they fail MUAHAHAHAHA
        if(passed) {
            //how do we access the button here so we can advance the thing?
            //maybe a direct access will work
            ADVENTURE.setNewLocation("choice1");
        }
        else {
            ADVENTURE.setNewLocation("choice2");
        }
    }

}
