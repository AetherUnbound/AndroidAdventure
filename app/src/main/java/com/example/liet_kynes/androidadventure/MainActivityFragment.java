package com.example.liet_kynes.androidadventure;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MAIN_FRAGMENT_DEBUG";
    public static final String PLAYER_POSITION = "PLAYER_POSITION";
    private Adventure ADVENTURE;

    public MainActivityFragment() {

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
        ADVENTURE = new Adventure(getActivity().getApplicationContext(), storyTextView, choiceOneButton, choiceTwoButton);

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
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADVENTURE.setNewLocation("choice2");
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

    public static void restartAdventure(MainActivityFragment fragment) {
        fragment.ADVENTURE.restartAdventure();
    }
}
