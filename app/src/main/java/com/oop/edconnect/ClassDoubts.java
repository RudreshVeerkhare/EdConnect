package com.oop.edconnect;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.LinkedHashMap;

public class ClassDoubts extends Fragment {

    private String classID, className;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText message;
    private FloatingActionButton send;

    private LinkedHashMap<String, Doubts> doubts;
    private DatabaseReference myRef;


    public ClassDoubts(String classID, String className){
        this.classID = classID;
        this.className = className;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_message, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_message);
        progressBar = view.findViewById(R.id.progressBarMessage);
        message = view.findViewById(R.id.classview_message);
        send = view.findViewById(R.id.classview_send);

        progressBar.setVisibility(View.INVISIBLE);


        myRef = FirebaseDatabase.getInstance().getReference("Doubtroom").child(classID).child("doubts");
        doubts = new LinkedHashMap<>();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doubts.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    doubts.put(keyNode.getKey(), keyNode.getValue(Doubts.class));
                }
                new RecyclerViewConfigDoubtroom(recyclerView, getContext(), doubts);
                recyclerView.scrollToPosition(doubts.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
            }
        });




        return view;


    }

    private void sendMessage() {

        String message = this.message.getText().toString().trim();


        if(message.equals(""))
            return;

        this.message.setText("");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = user.getUid();
        Date date = new Date();

        Doubts doubt = new Doubts(message, userId,  date);

        // to generate unique key
        String key = myRef.push().getKey();
        myRef.child(key).removeValue();


        doubts.put(key, doubt);

        myRef.setValue(doubts);






    }
}
