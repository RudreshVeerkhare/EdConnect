package com.oop.edconnect;


import java.util.LinkedList;

public class EnrolledClasses {

    private String userName;
    private LinkedList<String> classUids;



    public EnrolledClasses(String userName) {
        this.userName = userName;
        this.classUids = new LinkedList<>();
    }

    public EnrolledClasses() {
    }

    public String getUserName() {
        return userName;
    }

    public LinkedList<String> getClassUids() {
        return classUids;
    }

    public void addClassroom(String classId){
        this.classUids.add(classId);
    }
}
