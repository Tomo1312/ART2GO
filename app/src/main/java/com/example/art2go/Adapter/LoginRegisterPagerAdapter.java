package com.example.art2go.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.art2go.Login.LoginFragment;
import com.example.art2go.Login.RegisterFragment;

public class LoginRegisterPagerAdapter extends FragmentPagerAdapter {


    public LoginRegisterPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public LoginRegisterPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return RegisterFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
