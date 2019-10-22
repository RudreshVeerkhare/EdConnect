package com.oop.edconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class RecyclerViewConfig {
    private Context mContext;
    private ListItemAdapter itemsAdapter;
    private LinkedHashMap<String, User> users;

    public RecyclerViewConfig(RecyclerView recyclerView, Context context, LinkedList<Classroom> classrooms) {
        this.users = new LinkedHashMap<>();
        this.mContext = context;
        this.itemsAdapter = new ListItemAdapter(classrooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this.itemsAdapter);
    }

    class ListItemView extends RecyclerView.ViewHolder {
        private TextView textview1;
        private TextView textview2;


        public ListItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.list_data, parent, false));
            textview1 = itemView.findViewById(R.id.textView1);
            textview2 = itemView.findViewById(R.id.textView2);
        }


        public void bindClassroom(final Classroom obj){
            DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users");
            user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.clear();
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        users.put(keyNode.getKey(), keyNode.getValue(User.class));
                    }


                    textview1.setText(obj.title);
                    textview2.setText(users.get(obj.getCreater()).getUserName());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    class ListItemAdapter extends RecyclerView.Adapter<ListItemView> {
        private LinkedList<Classroom> classrooms;

        public ListItemAdapter(LinkedList<Classroom> classrooms) {
            this.classrooms = classrooms;
        }

        @NonNull
        @Override
        public ListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemView holder, int position) {
            holder.bindClassroom(classrooms.get(position));

        }

        @Override
        public int getItemCount() {
            return classrooms.size();
        }
    }
}
