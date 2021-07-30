package com.example.art2go.Story;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.art2go.Login.LoginFragment;
import com.example.art2go.R;


public class TypeOfStoryFragment extends Fragment {

    static TypeOfStoryFragment instance;
    public static TypeOfStoryFragment getInstance() {
        if (instance == null)
            instance = new TypeOfStoryFragment();
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
        return inflater.inflate(R.layout.fragment_type_of_story, container, false);
    }
}