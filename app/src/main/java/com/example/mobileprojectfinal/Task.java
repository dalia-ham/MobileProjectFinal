package com.example.mobileprojectfinal;

public class Task {
    private String title;
    private String description;
    private String time;
    private int progress;

    // Constructor to initialize Task
    public Task(String title, String description, String time, int progress) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.progress = progress;
    }

    // Getter methods for each attribute
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public int getProgress() {
        return progress;
    }
}
