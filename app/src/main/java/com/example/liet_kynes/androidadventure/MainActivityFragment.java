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

    public MainActivityFragment() {
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        //Items for main fragment
        TextView storyTextView = (TextView) view.findViewById(R.id.storyTextView);
        Button choiceOneButton = (Button) view.findViewById(R.id.choiceOneButton);
        Button choiceTwoButton = (Button) view.findViewById(R.id.choiceTwoButton);


    return view;
    }
}
