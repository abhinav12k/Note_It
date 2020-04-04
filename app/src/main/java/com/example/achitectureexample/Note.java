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
    private String dateTime;

    public Note(String title, String description, String priority,int priorityNumber,String dateTime) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.priorityNumber = priorityNumber;
        this.dateTime = dateTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }

    public String getDateTime(){
        return dateTime;
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
