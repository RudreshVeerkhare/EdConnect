package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class ClassLessonActivity extends AppCompatActivity {

    private TextInputEditText title;
    private TextInputEditText content;
    private Button addFile;
    private TextView filename;
    private Button postLesson;
    private final int PICK_FILE_REQUEST = 1;
    private ProgressBar progressBar;

    private Uri fileUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private String classID;
    private String className;
    private String fileurl;

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_lesson);

        title = findViewById(R.id.lesson_title);
        content = findViewById(R.id.lesson_content);
        addFile = findViewById(R.id.lesson_add_file);
        postLesson = findViewById(R.id.post_lesson);
        filename = findViewById(R.id.file_name);
        progressBar = findViewById(R.id.create_lesson_progress_bar);



        // getting data from intent
        Intent intent = getIntent();
        classID = intent.getStringExtra("classId");
        className =  intent.getStringExtra("className");
        mStorageRef = FirebaseStorage.getInstance().getReference("Lessons/" + classID);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Lessons");



        // assigning storage and database ref



        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        postLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();
            }
        });
    }

    private void post() {
        final String contentText = content.getText().toString().trim();
        final String titleText = title.getText().toString().trim();

        if (contentText.isEmpty()){
            content.setError("content cannot be empty");
            return;
        }

        if (titleText.isEmpty()){
            title.setError("Title cannot be empty");
            return;
        }

        mStorageRef = mStorageRef.child(getFileName(fileUri));

        progressBar.setVisibility(View.VISIBLE);
        uploadTask = mStorageRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_LONG);
                                fileurl = uri.toString();
                                Lesson lesson = new Lesson(contentText, fileurl, titleText);
                                mDatabaseRef.child(classID).push().setValue(lesson);
                                finish();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_FILE_REQUEST &&  resultCode == RESULT_OK && data != null && data.getData() != null){
            fileUri = data.getData();
            filename.setText(getFileName(fileUri));
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try{
                if(cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally {
                cursor.close();
            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }

        return result;
    }
}
