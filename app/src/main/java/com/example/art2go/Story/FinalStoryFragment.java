package com.example.art2go.Story;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.art2go.R;


public class FinalStoryFragment extends Fragment {

    static FinalStoryFragment instance;
    public static FinalStoryFragment getInstance() {
        if (instance == null)
            instance = new FinalStoryFragment();
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_final_story, container, false);
    }
}