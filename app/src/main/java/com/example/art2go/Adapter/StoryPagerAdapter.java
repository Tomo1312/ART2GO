package com.example.art2go.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.art2go.Login.LoginFragment;
import com.example.art2go.Login.RegisterFragment;
import com.example.art2go.Story.FinalStoryFragment;
import com.example.art2go.Story.ListOfStoriesFragment;
import com.example.art2go.Story.TypeOfStoryFragment;

public class StoryPagerAdapter extends FragmentPagerAdapter {


    public StoryPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public StoryPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TypeOfStoryFragment.getInstance();
            case 1:
                return ListOfStoriesFragment.getInstance();
            case 2:
                return FinalStoryFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
