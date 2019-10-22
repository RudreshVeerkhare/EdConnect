package com.oop.edconnect;

public class Profile {
    String fullname;
    String gender;
    String email;
    String type;

    public Profile() {
    }

    public Profile(String fullname, String gender, String email, String type) {
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
        this.type = type;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail(){
        return email;
    }

    public String getType() {
        return type;
    }
}
