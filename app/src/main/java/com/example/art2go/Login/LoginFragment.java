package com.example.art2go.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.art2go.Common.Common;
import com.example.art2go.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    TextView txt_eMail, txt_password;
    LocalBroadcastManager localBroadcastManager;
    FirebaseAuth firebaseAuth;

    static LoginFragment instance;
    public static LoginFragment getInstance() {
        if (instance == null)
            instance = new LoginFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        txt_eMail= view.findViewById(R.id.tvEmail);
        txt_password = view.findViewById(R.id.tvPassword);

        txt_eMail.addTextChangedListener(registerTextWatcher);
        txt_password.addTextChangedListener(registerTextWatcher);
        return view;
    }

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (checkForEmpty()) {
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_REGISTER);
                intent.putExtra(Common.KEY_EMAIL, txt_eMail.getText().toString());
                intent.putExtra(Common.KEY_PASSWORD, txt_password.getText().toString());
                intent.putExtra(Common.KEY_STEP, 0);
                localBroadcastManager.sendBroadcast(intent);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private boolean checkForEmpty() {
        if (TextUtils.isEmpty(txt_eMail.getText())) {
            return false;
        } else if (TextUtils.isEmpty(txt_password.getText())) {
            return false;
        }
        return true;
    }
}