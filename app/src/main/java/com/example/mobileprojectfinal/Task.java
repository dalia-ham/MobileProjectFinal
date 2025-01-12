package com.example.mobileprojectfinal;

public class Task {
    private String taskId, title, project, status, priority, startDate, dueDate;
    private int completionPercent;

    public Task(String taskId, String title, String project, String status, String priority, String startDate, String dueDate, int completionPercent) {
        this.taskId = taskId;
        this.title = title;
        this.project = project;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.completionPercent = completionPercent;
    }

    public String getTaskId() { return taskId; }
    public String getTitle() { return title; }
    public String getProject() { return project; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getStartDate() { return startDate; }
    public String getDueDate() { return dueDate; }
    public int getCompletionPercent() { return completionPercent; }
}
