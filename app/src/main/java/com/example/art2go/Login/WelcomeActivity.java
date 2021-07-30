package com.example.art2go.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.example.art2go.Adapter.LoginRegisterPagerAdapter;
import com.example.art2go.App.MainActivity;
import com.example.art2go.Common.Common;
import com.example.art2go.Custom.NonSwipeViewPager;
import com.example.art2go.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class WelcomeActivity extends AppCompatActivity {

    NonSwipeViewPager viewPager;
    ImageView logo;
    LinearLayout layout_button;

    Button btnLogin, btnRegister;

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    AlertDialog loading;

    private String passwordTmp = new String(), userEmail = new String() ;

    LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if (step == 0) {
                userEmail = intent.getStringExtra(Common.KEY_EMAIL);
                passwordTmp = intent.getStringExtra(Common.KEY_PASSWORD);
            } else if (step == 1) {
                Common.currentUser = intent.getParcelableExtra(Common.KEY_USER);
                passwordTmp = intent.getStringExtra(Common.KEY_PASSWORD);
                btnRegister.setEnabled(true);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        Paper.init(this);
        String userId = Paper.book().read(Common.KEY_LOGGED);
        if(TextUtils.isEmpty(userId)){
            setContentView(R.layout.activity_welcome);
            setUiView();
            loadInScreen();
            initView();
        }else{
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra(Common.KEY_USER_ID, userId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_REGISTER));

        viewPager.setAdapter(new LoginRegisterPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                } else {
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.step == 0) {
                    validate(userEmail, passwordTmp);
                } else {
                    Common.step--;
                    btnRegister.setEnabled(true);
                }
                viewPager.setCurrentItem(Common.step);
            }

        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.step == 0) {
                    Common.step++;
                    btnRegister.setEnabled(false);
                } else {
                    startEmailVerification();
                }
                viewPager.setCurrentItem(Common.step);
            }

        });
    }

    private void setUiView() {
        logo = findViewById(R.id.logo);
        viewPager = findViewById(R.id.view_pager);
        layout_button = findViewById(R.id.layout_button);

        loading = new SpotsDialog.Builder().setCancelable(false).setContext(WelcomeActivity.this).build();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void loadInScreen() {

        Animation fadeOut = new AlphaAnimation(1, (float) 0.5);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setTransitionAlpha(0.5f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.setAnimation(fadeOut);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation fadeInAfter1Sec = new AlphaAnimation(0, 1);
        fadeInAfter1Sec.setInterpolator(new DecelerateInterpolator());
        fadeInAfter1Sec.setStartOffset(1000); //add this
        fadeInAfter1Sec.setDuration(2000);

        logo.setAnimation(fadeIn);

        viewPager.setAnimation(fadeInAfter1Sec);
        layout_button.setAnimation(fadeInAfter1Sec);
    }

    private void startEmailVerification() {
        loading.show();
        mAuth.createUserWithEmailAndPassword(Common.currentUser.getUserEmail(), passwordTmp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Registration failed " + task.getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(WelcomeActivity.this, "Verification mail has been send", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WelcomeActivity.this, "Verification mail has not been send", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void sendUserData() {
        Common.currentUser.setUserId(mAuth.getUid());
        CollectionReference userRef;
        userRef = FirebaseFirestore.getInstance().collection("Users");
        userRef.document(mAuth.getUid()).set(Common.currentUser);
        if (loading.isShowing())
            loading.dismiss();
        viewPager.setCurrentItem(Common.step = 0);
    }

    private void validate(String userEmail,String userPassword) {
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkEmailVerification();
                } else {
                    Toast.makeText(WelcomeActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkEmailVerification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag = firebaseUser.isEmailVerified();
        if (emailFlag) {
            Paper.init(this);
            Paper.book().write(Common.KEY_LOGGED, firebaseUser.getUid());
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra(Common.KEY_USER_ID, firebaseUser.getUid());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Verify your e-mail", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}