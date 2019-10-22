package com.oop.edconnect;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class FirebaseDatabaseClassroomHandler {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference allClassesRef;
    private LinkedList<Classroom> classrooms;
    private LinkedHashMap<String, Classroom> allClassrooms;

    public FirebaseDatabaseClassroomHandler(String userId){
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mRef = this.mDatabase.getReference("EnrolledClasses/" + userId + "/classUids");
        Log.i("classIds", "EnrolledClasses/" + userId + "/classUids");
        this.classrooms = new LinkedList<>();
        this.allClassrooms = new LinkedHashMap<>();

        this.allClassesRef = mDatabase.getReference("Classroom");


    }

    public interface DataStatus{
        void DataIsLoaded(LinkedList<Classroom> classrooms);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public void readClassroomData(final DataStatus dataStatus){

        allClassesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allClassrooms.clear();
                for(DataSnapshot keynode : dataSnapshot.getChildren()){
                    allClassrooms.put(keynode.getKey(), keynode.getValue(Classroom.class));
                }

                proccedReading(dataStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void proccedReading(final DataStatus dataStatus) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classrooms.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    String key = (String) keyNode.getValue();
                    classrooms.add(allClassrooms.get(key));
                }
                dataStatus.DataIsLoaded(classrooms);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
