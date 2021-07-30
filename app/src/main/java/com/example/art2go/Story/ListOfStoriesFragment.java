package com.example.art2go.Story;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.art2go.R;

public class ListOfStoriesFragment extends Fragment {


    static ListOfStoriesFragment instance;
    public static ListOfStoriesFragment getInstance() {
        if (instance == null)
            instance = new ListOfStoriesFragment();
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
        return inflater.inflate(R.layout.fragment_list_of_stories, container, false);
    }
}