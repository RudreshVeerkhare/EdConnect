package com.oop.edconnect;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Doubts {

    String message;
    String userId;
    String date;
    String time;

    public Doubts() {
    }

    public Doubts(String message,String userId, Date dateTime) {
        this.message = message;
        this.userId = userId;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        this.date = dateFormat.format(dateTime);
        this.time = timeFormat.format(dateTime);
    }

    public String getMessage() {
        return message;
    }


    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
