package com.oop.edconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ListData extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton add;
    LinkedHashMap<String, User> users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar2);
        add = view.findViewById(R.id.addClassFab);
        users = new LinkedHashMap<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addClassroom();
            }
        });

        new FirebaseDatabaseClassroomHandler(FirebaseAuth.getInstance().getCurrentUser().getUid()).readClassroomData(new FirebaseDatabaseClassroomHandler.DataStatus() {
            @Override
            public void DataIsLoaded(final LinkedList<Classroom> classrooms) {

                progressBar.setVisibility(View.INVISIBLE);
                new RecyclerViewConfig(recyclerView, getActivity(), classrooms);

                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), ClassroomView.class);
                        Classroom classroom = classrooms.get(position);
                        intent.putExtra("classID", classroom.getClassId());
                        intent.putExtra("className", classroom.getTitle());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });


        return view;
    }

    private void addClassroom() {
        final String currUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    users.put(keyNode.getKey(), keyNode.getValue(User.class));
                }
                createClass(currUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createClass(String currUserId) {
        User user = users.get(currUserId);
        if ( user.getProfileType().equals("Teacher") ){
            startActivity(new Intent(getContext(), CreateClass.class));
        }

        if (user.getProfileType().equals("Student")){
            startActivity(new Intent(getContext(), JoinClassroom.class));
        }
    }


}
