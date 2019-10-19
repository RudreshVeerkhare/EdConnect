package com.oop.edconnect;

import java.util.LinkedHashMap;

public class Doubtroom {
    private LinkedHashMap<String, Doubts> doubts;
    private String className;
    private String classId;

    public Doubtroom() {
    }

    public Doubtroom(LinkedHashMap<String, Doubts> doubts, String className, String classId) {
        this.doubts = doubts;
        this.className = className;
        this.classId = classId;
    }

    public LinkedHashMap<String, Doubts> getDoubts() {
        return doubts;
    }

    public String getClassName() {
        return className;
    }

    public String getClassId() {
        return classId;
    }
}
