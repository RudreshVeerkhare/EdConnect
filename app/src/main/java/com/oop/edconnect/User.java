package com.oop.edconnect;

public class User {

    private String userName;
    private String imageUrl;

    public User() {
    }

    public User(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
