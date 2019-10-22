package com.oop.edconnect;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Classroom {
    String classId;
    String title;
    String creater;
    LinkedList<String> students;

    public Classroom() {
    }

    public Classroom(String title, String creater, String classId) {
        this.title = title;
        this.classId = classId;
        this.creater = creater;
        this.students = new LinkedList<>();
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

    public String getClassId() {
        return classId;
    }
}
