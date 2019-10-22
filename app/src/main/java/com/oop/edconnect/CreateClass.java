package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class CreateClass extends AppCompatActivity {

    private TextInputEditText title;
    private TextInputEditText description;
    private Button create;
    private LinkedList<String> enrolledClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);

        enrolledClassList = new LinkedList<>();
        title = findViewById(R.id.title_edittext);
        description = findViewById(R.id.description_edittext);
        create = findViewById(R.id.create_class_button);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClass();
            }
        });
    }

    private void createClass() {

        DatabaseReference classroomRef = FirebaseDatabase.getInstance().getReference("Classroom");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String classTitle = title.getText().toString().trim();
        String classDescription = description.getText().toString().trim();

        if (classTitle.equals("")){
            title.setError("Enter valid title");
            return;
        }



        final String classId = classroomRef.push().getKey();
        Classroom classroom = new Classroom(classTitle, userId, classId);

        classroomRef.child(classId).setValue(classroom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                enrollToClass(classId);
//                DatabaseReference enrolledClassesRef = FirebaseDatabase.getInstance().getReference("EnrolledClasses");
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//
//                EnrolledClasses enrolledClasses = new EnrolledClasses(userName);
//                enrolledClasses.addClassroom(classId);
//
//                enrolledClassesRef.child(uid).setValue(enrolledClasses);
            }
        });

        finish();



    }

    private void enrollToClass(final String classId) {

        final DatabaseReference enrolledClassesRef = FirebaseDatabase.getInstance().getReference("EnrolledClasses");
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        enrolledClassesRef.child(uid).child("classUids").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                enrolledClassList.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    enrolledClassList.add(Integer.parseInt(keyNode.getKey()), keyNode.getValue().toString());
                }

                enrolledClassList.add(classId);

                enrolledClassesRef.child(uid).child("classUids").setValue(enrolledClassList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
