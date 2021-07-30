package com.example.art2go.App;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.art2go.Adapter.StoryPagerAdapter;
import com.example.art2go.Custom.NonSwipeViewPager;
import com.example.art2go.R;

public class StoryFragment extends Fragment {
    public StoryFragment() {
        // Required empty public constructor
    }

    NonSwipeViewPager viewPager;
    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_story, container, false);
        viewPager = itemView.findViewById(R.id.view_pager);

        viewPager.setAdapter(new StoryPagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);

        return itemView;
    }
}