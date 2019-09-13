package com.oop.edconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

public class RecyclerViewConfig {
    private Context mContext;
    private ListItemAdapter itemsAdapter;

    public RecyclerViewConfig(RecyclerView recyclerView, Context context, LinkedHashMap<String, Object> map) {
        this.mContext = context;
        this.itemsAdapter = new ListItemAdapter(map);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this.itemsAdapter);
    }

    class ListItemView extends RecyclerView.ViewHolder {
        private TextView textview1;
        private TextView textview2;

        private String key;

        public ListItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.list_data, parent, false));
            textview1 = itemView.findViewById(R.id.textView1);
            textview2 = itemView.findViewById(R.id.textView2);
        }

        public void bindStudent(Student obj, String key){
            textview1.setText(obj.name);
            textview2.setText(obj.username);
            this.key = key;
        }

        public void bindTeacher(Teacher obj, String key){
            textview1.setText(obj.name);
            textview2.setText(obj.username);
            this.key = key;
        }
    }

    class ListItemAdapter extends RecyclerView.Adapter<ListItemView> {
        private LinkedHashMap<String, Object> map;

        public ListItemAdapter(LinkedHashMap<String, Object> map) {
            this.map = map;
        }

        @NonNull
        @Override
        public ListItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ListItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemView holder, int position) {
            String key = (String) map.keySet().toArray()[position];
            if(map.get(key) instanceof Student)
                holder.bindStudent((Student) map.get(key), key);
            else if(map.get(key) instanceof Teacher)
                holder.bindTeacher((Teacher) map.get(key), key);

        }

        @Override
        public int getItemCount() {
            return map.size();
        }
    }
}
