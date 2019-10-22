package com.oop.edconnect;

public class User {

    private String userName;
    private String imageUrl;
    private String profileType;
    private String gender;
    private String email;

    public User() {
    }

    public User(String userName, String imageUrl, String profileType, String gender, String email) {
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.profileType = profileType;
        this.gender = gender;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getProfileType() {
        return profileType;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }
}
