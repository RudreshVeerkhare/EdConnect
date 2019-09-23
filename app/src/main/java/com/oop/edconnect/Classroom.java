package com.oop.edconnect;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Classroom {
    String title;
    String creater;
    LinkedList<String> students;

    public Classroom() {
    }

    public Classroom(String title, String creater, LinkedList<String> students) {
        this.title = title;
        this.creater = creater;
        this.students = students;
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
}
