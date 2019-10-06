package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private EditText email, fullname, password1, password2;
    private TextView emailError, nameError, passwordError;
    private FirebaseAuth mAuth;
    private ProgressBar progressB;
    private Button login, signupButton;
    private CircleImageView signupImage;
    private FloatingActionButton imageFab;

    private Uri imageUri;
    private final int PICK_IMAGE_REQUEST = 1;

    private StorageReference profileImgRef;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();

        imageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
                //uploadFile();// function will upload image to storage and then sign up user
            }
        });


    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.i("Fun", "image selected is " + data.getData().toString());

            Picasso.get().load(imageUri).into(signupImage);
        }

    }

    private void signUp() {
        final String email = this.email.getText().toString().trim();
        String password1 = this.password1.getText().toString();
        String password2 = this.password2.getText().toString();
        String fullname = this.fullname.getText().toString();
        RadioGroup radioGender = findViewById(R.id.signup_gender);
        RadioGroup radioType = findViewById(R.id.signup_profile);
        String type, gender;



        if(email.isEmpty()){
            emailError.setVisibility(View.VISIBLE);
            return;
        }

        if(password1.isEmpty() || password2.isEmpty()){
            passwordError.setVisibility(View.VISIBLE);
            return;
        }

        if(radioGender.getCheckedRadioButtonId() == -1 || radioType.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), "Select proper choices", Toast.LENGTH_SHORT).show();
            return;
        }



        if(password1.equals(password2)){

            if(radioType.getCheckedRadioButtonId() == R.id.student){
                type = "Student";
            } else {
                type = "Teacher";
            }

            if(radioGender.getCheckedRadioButtonId() == R.id.male){
                gender = "Male";
            } else {
                gender = "Female";
            }



            final Profile profile = new Profile(fullname, gender, email);
            final FirebaseDatabaseHelper mHelper = new FirebaseDatabaseHelper(type);

            this.progressB.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        mHelper.addProfile(profile, FirebaseAuth.getInstance().getCurrentUser().getUid());

                        uploadFile();


                    }
                    else{
                        progressB.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Sign Up failed!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void setProfileImage(Uri uri){
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullname.getText().toString().trim())
                .setPhotoUri(uri).build();

        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"profile image added",Toast.LENGTH_LONG);
                        finish();
                        startActivity(new Intent(getApplicationContext(), NavigationDrawerHome.class));
                    }
                });
    }

    private void uploadFile() {
        if (imageUri != null){


            final StorageReference fileRef = profileImgRef.child(mAuth.getUid() + "." + getFileExtension(imageUri));
            uploadTask = fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG);
                                    setProfileImage(uri);

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressB.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT);
                        }
                    });

        }
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(imageUri));
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

        signupImage = findViewById(R.id.signup_image);
        imageFab = findViewById(R.id.image_fab);

        profileImgRef = FirebaseStorage.getInstance().getReference("profileImages");
    }
}
