package com.example;

import javafx.beans.property.SimpleStringProperty;

public class AlarmEntry {
    private final SimpleStringProperty time;
    private final SimpleStringProperty comment;

    public AlarmEntry(String time, String comment) {
        this.time = new SimpleStringProperty(time);
        this.comment = new SimpleStringProperty(comment);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }
}
