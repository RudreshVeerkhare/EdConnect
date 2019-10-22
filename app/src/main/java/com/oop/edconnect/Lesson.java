package com.oop.edconnect;

public class Lesson {
    private String content;
    private String fileurl;
    private String title;

    public Lesson() {
    }

    public Lesson(String content, String fileurl, String title) {
        this.content = content;
        this.fileurl = fileurl;
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public String getFileurl() {
        return fileurl;
    }

    public String getTitle() {
        return title;
    }
}
