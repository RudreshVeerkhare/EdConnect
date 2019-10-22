package com.oop.edconnect;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        animationView = findViewById(R.id.animation);
        animationView.playAnimation();

        aninmate();



    }

    private void updateUI(){
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        else {
            startActivity(new Intent(getApplicationContext(), NavigationDrawerHome.class));
        }


    }

    private void aninmate(){
        final int delayMs = 4500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
                finish();
            }
        }, delayMs);
    }


}