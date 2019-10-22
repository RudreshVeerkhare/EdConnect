package com.oop.edconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewConfigDoubtroom {

    private Context mContext;
    private MessageListAdapter itemsAdapter;
    private String currentUserID;



    public RecyclerViewConfigDoubtroom(RecyclerView recyclerView, Context context, LinkedHashMap<String, Doubts> doubts) {
        this.currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mContext = context;
        this.itemsAdapter = new MessageListAdapter(doubts);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this.itemsAdapter);


    }

    class MessageListAdapter extends RecyclerView.Adapter{
        private static final int VIEW_TYPE_SENT_MESSAGE = 1;
        private static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;

        private LinkedHashMap<String, Doubts> doubts;

        public MessageListAdapter(LinkedHashMap<String, Doubts> doubts){
            this.doubts = doubts;
        }

        @Override
        public int getItemCount() {
            return doubts.size();
        }

        //Determines appropriate ViewType according sender of the message

        @Override
        public int getItemViewType(int position) {
            String key = (String) doubts.keySet().toArray()[position];
            Doubts doubt = doubts.get(key);

            if(doubt.getUserId().equals(currentUserID)){
                return VIEW_TYPE_SENT_MESSAGE;
            }

            else{
                return VIEW_TYPE_RECEIVED_MESSAGE;
            }
        }

        //Infaltes appropriate layout according to the ViewType

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view;

            if(viewType == VIEW_TYPE_SENT_MESSAGE){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message, parent, false);
                return new SentMessageHolder(view);

            }
            else if (viewType == VIEW_TYPE_RECEIVED_MESSAGE){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message_layout, parent, false);
                return new ReceivedMessageHolder(view);

            }

            return null;
        }

        // populate the View
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Doubts doubt = doubts.get(doubts.keySet().toArray()[position]);

            switch (holder.getItemViewType()){
                case VIEW_TYPE_SENT_MESSAGE:
                    ((SentMessageHolder) holder).bind(doubt);
                    break;

                case VIEW_TYPE_RECEIVED_MESSAGE:
                    ((ReceivedMessageHolder) holder).bind(doubt);
                    break;
            }
        }

        private class SentMessageHolder extends RecyclerView.ViewHolder {
            private TextView message;
            private TextView time;

            SentMessageHolder(View view){
                super(view);

                message = view.findViewById(R.id.sent_message_text);
                time = view.findViewById(R.id.sent_message_time);
            }

            void bind(Doubts doubt){
                message.setText(doubt.getMessage());
                time.setText(doubt.getTime());
            }
        }

        private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            private TextView message;
            private TextView time;
            private TextView user;
            private CircleImageView image;
            LinkedHashMap<String, User> allUsers;

            ReceivedMessageHolder(View view){
                super(view);

                message = view.findViewById(R.id.recevied_message_text);
                time = view.findViewById(R.id.received_message_time);
                user = view.findViewById(R.id.sender_userid);
                image = view.findViewById(R.id.avatar_received_message);
            }


            void bind(final Doubts doubt){

                allUsers = new LinkedHashMap<>();

                DatabaseReference allUserRef = FirebaseDatabase.getInstance().getReference("Users");
                allUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allUsers.clear();
                        for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                            allUsers.put(keyNode.getKey(), keyNode.getValue(User.class));
                        }
                        User usr = allUsers.get(doubt.getUserId());
                        user.setText(usr.getUserName());

                        //user.setText(doubt.getUserName());
                        Picasso.get().load(usr.getImageUrl()).into(image);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                message.setText(doubt.getMessage());
                time.setText(doubt.getTime());

            }
        }
    }


}
