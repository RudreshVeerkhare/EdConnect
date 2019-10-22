package com.oop.edconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class JoinClassroom extends AppCompatActivity {

    private TextInputEditText classCode;
    private TextView className;
    private TextView classTeacher;
    private Button verify;
    private Button join;

    private LinkedHashMap<String, Classroom> allClasses;
    private LinkedHashMap<String, User> allUsers;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);

        classCode = findViewById(R.id.join_code);
        className = findViewById(R.id.class_name_join);
        classTeacher = findViewById(R.id.class_creater_join);
        verify = findViewById(R.id.join_verify);
        join = findViewById(R.id.join_join);
        allClasses = new LinkedHashMap<>();
        allUsers = new LinkedHashMap<>();

        DatabaseReference classroom = FirebaseDatabase.getInstance().getReference("Classroom");
        DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");

        classroom.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allClasses.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    allClasses.put(keyNode.getKey(), keyNode.getValue(Classroom.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUsers.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    allUsers.put(keyNode.getKey(), keyNode.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinClass();
            }
        });


    }

    public void joinClass(){

        final String classCodeText = classCode.getText().toString().trim();

        if(classCodeText.equals("")){
            classCode.setError("Enter code");
            return;
        }

        Classroom classroom = allClasses.get(classCodeText);

        if (classroom == null){
            classCode.setError("Invalid Code");
            return;
        }

        final DatabaseReference enrolledClasses = FirebaseDatabase.getInstance().getReference("EnrolledClasses");
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        enrolledClasses.child(userId).child("classUids").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinkedList<String> list = new LinkedList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    list.add(Integer.parseInt(keyNode.getKey()), keyNode.getValue().toString());
                }

                boolean check = check_if_exist(list, classCodeText);

                if(check == false){
                    classCode.setError("Already Enrolled to Class");
                    return;
                }

                if(check == true) {
                    list.add(classCodeText);
                    enrolledClasses.child(userId).child("classUids").setValue(list);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private boolean check_if_exist(LinkedList<String> list, String classCodeText) {

        HashSet<String> users_set = new HashSet<>();

        int size = list.size();

        for(int i = 0 ; i < size ; i++){
            users_set.add(list.get(i));
        }

        if(users_set.contains(classCodeText)){
            return false;
        }

        return true;

    }

    public void verifyCode(){
        final String classCodeText = classCode.getText().toString().trim();

        if(classCodeText.equals("")){
            classCode.setError("Enter code");
            return;
        }

        Classroom classroom = allClasses.get(classCodeText);

        if (classroom == null){
            classCode.setError("Invalid Code");
            return;
        }

        className.setText(classroom.getTitle());
        classTeacher.setText(allUsers.get(classroom.getCreater()).getUserName());
        findViewById(R.id.join_class_info).setVisibility(View.VISIBLE);



    }
}
