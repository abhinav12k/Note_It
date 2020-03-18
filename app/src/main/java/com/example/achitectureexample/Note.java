package com.example.achitectureexample;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String priority;
    private int priorityNumber;

    public Note(String title, String description, String priority,int priorityNumber) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.priorityNumber = priorityNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }
}
