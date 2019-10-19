package com.oop.edconnect;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Classroom {
    String title;
    String creater;
    LinkedList<String> students;
    String doubtroom;

    public Classroom() {
    }

    public Classroom(String title, String creater, LinkedList<String> students, String doubtroom) {
        this.title = title;
        this.creater = creater;
        this.students = students;
        this.doubtroom = doubtroom;
    }

    public String getTitle() {
        return title;
    }

    public String getCreater() {
        return creater;
    }

    public LinkedList<String> getStudents() {
        return students;
    }

    public String getDoubtroom(){
        return this.doubtroom;
    }
}
