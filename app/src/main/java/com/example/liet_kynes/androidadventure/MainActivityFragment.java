package com.example.liet_kynes.androidadventure;

import android.media.MediaPlayer;
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

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false); //It breaks if we don't have View view = in here

        //Items for main fragment
        final TextView storyTextView = (TextView) view.findViewById(R.id.storyTextView);
        final Button choiceOneButton = (Button) view.findViewById(R.id.choiceOneButton);
        final Button choiceTwoButton = (Button) view.findViewById(R.id.choiceTwoButton);

        final Adventure adventure = new Adventure();
        try {
            adventure.buildAdventure(getActivity().getApplicationContext(), storyTextView, choiceOneButton, choiceTwoButton);
        }
        catch (Exception e) {
            Log.d("debug", e.getMessage());
        }

        choiceOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adventure.setNewLocation("choice1", storyTextView, choiceOneButton, choiceTwoButton);
            }
        });

        choiceTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adventure.setNewLocation("choice2", storyTextView, choiceOneButton, choiceTwoButton);
            }
        });


    //Added this for skeleton code
    return view;
    }
}
