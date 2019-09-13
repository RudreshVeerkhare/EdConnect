package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {
    private EditText email, fullname, password1, password2;
    private TextView emailError, nameError, passwordError;
    private FirebaseAuth mAuth;
    private ProgressBar progressB;
    private Button login, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });


    }

    private void signUp() {
        String email = this.email.getText().toString();
        String password1 = this.password1.getText().toString();
        String password2 = this.password2.getText().toString();

        if(email.isEmpty()){
            emailError.setVisibility(View.VISIBLE);
        }

        if(password1.isEmpty() || password2.isEmpty()){
            passwordError.setVisibility(View.VISIBLE);
        }


        if(password1.equals(password2)){

            this.progressB.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        finish();
                        startActivity(new Intent(getApplicationContext(), ListData.class));
                    }
                    else{
                        progressB.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Sign Up failed!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        password1 = findViewById(R.id.fpassword);
        password2 = findViewById(R.id.cnfpassword);

        emailError = findViewById(R.id.emailError);
        nameError = findViewById(R.id.nameError);
        passwordError = findViewById(R.id.passwordError);

        progressB = findViewById(R.id.progressBar);

        login = findViewById(R.id.login);
        signupButton = findViewById(R.id.signupButton);

    }
}
