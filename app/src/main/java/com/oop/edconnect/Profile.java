package com.oop.edconnect;

public class Profile {
    String fullname;
    String gender;
    String email;

    public Profile() {
    }

    public Profile(String fullname, String gender, String email) {
        this.fullname = fullname;
        this.gender = gender;
        this.email = email;
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
}
