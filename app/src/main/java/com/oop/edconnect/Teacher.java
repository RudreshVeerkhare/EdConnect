package com.oop.edconnect;

public class Teacher {
    String name;
    String username;

    public Teacher(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public Teacher() {
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
