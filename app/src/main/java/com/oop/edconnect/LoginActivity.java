package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView passwordError, usernameError;
    private EditText username, password;
    private Button login, signup;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        passwordError = findViewById(R.id.passError);
        usernameError = findViewById(R.id.usernameError);
        username = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressB);

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            mAuth.signOut();
        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

    }


    private void switchToSignup(){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    private void SignIn() {


        String name = username.getText().toString().trim();
        String pass = password.getText().toString();

        if (name.isEmpty()){
            changeColor(username);
        }
        if (pass.isEmpty()){
            changeColor(password);
        }

        if (!name.isEmpty() && !pass.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(name, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent next = new Intent(getApplicationContext(), NavigationDrawerHome.class);
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();
                        startActivity(next);
                    }
                    else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Login Failed!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void changeColor(EditText et){

        if(et == username ) usernameError.setVisibility(View.VISIBLE);
        if(et == password) passwordError.setVisibility(View.VISIBLE);
        Drawable drawable = et.getBackground(); // get current EditText drawable
        drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP); // change the drawable color

        if(Build.VERSION.SDK_INT > 16) {
            et.setBackground(drawable); // set the new drawable to EditText
        }else{
            et.setBackgroundDrawable(drawable); // use setBackgroundDrawable because setBackground required API 16
        }

    }

}
