package com.oop.edconnect;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private LinkedHashMap<String, Object> map;

    public interface DataStatus{
        void DataIsLoaded(LinkedHashMap<String, Object> map);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(String type) {
        this.mDatabase = FirebaseDatabase.getInstance();
        this.ref = mDatabase.getReference(type);
        this.map = new LinkedHashMap<>();
    }

    public void readDataClassroom(final DataStatus dataStatus){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                map.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    map.put(keyNode.getKey(), keyNode.getValue(Classroom.class));
                }
                dataStatus.DataIsLoaded(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void readDataStudent(final DataStatus dataStatus){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                map.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    map.put(keyNode.getKey(), keyNode.getValue(Student.class));
                }
                dataStatus.DataIsLoaded(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readDataTeacher(final DataStatus dataStatus){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                map.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    map.put(keyNode.getKey(), keyNode.getValue(Teacher.class));
                }
                dataStatus.DataIsLoaded(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addProfile(Profile profile, String userID){
        this.ref.child(userID).setValue(profile);
    }

    public void addDoubtroom(Doubtroom doubtroom, String UID){
        this.ref.child(UID).setValue(doubtroom);
    }

}
