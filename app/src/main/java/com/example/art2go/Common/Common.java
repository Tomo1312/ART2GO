package com.example.art2go.Common;

import com.example.art2go.Model.Art;
import com.example.art2go.Model.User;

import java.util.ArrayList;

public class Common {
    public static final String KEY_USER = "USER";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_LOGGED = "LOGGED";
    public static final String KEY_USER_ID = "USER_ID";
    public static final String KEY_ENABLE_BUTTON_REGISTER = "ENABLE_BUTTON_REGISTER";

    public static int step = 0;
    public static User currentUser;
    public static ArrayList<Art> sculptures;
    public static ArrayList<Art> monuments;
    public static ArrayList<Art> architecture;
    public static final int REQUEST_LOCATION = 99;
    public static ArrayList<Art> allArt;
}
