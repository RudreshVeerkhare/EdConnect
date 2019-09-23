package com.oop.edconnect;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

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

        final int delayMs = 4500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        }, delayMs);



    }

    private void updateUI(){
        if(user == null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        else {
            startActivity(new Intent(getApplicationContext(), NavigationDrawerHome.class));
        }

        finish();
    }


}