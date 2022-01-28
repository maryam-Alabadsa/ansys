package com.example.myapplication.models;

import java.util.ArrayList;

public class Library {
    String bookId, dataOfFinished;
    Boolean isFinished;
    long duration;

    public Library() {
    }

    public Library(String bookId, String dataOfFinished, Boolean isFinished, long duration) {
        this.bookId = bookId;
        this.dataOfFinished = dataOfFinished;
        this.isFinished = isFinished;
        this.duration = duration;
    }


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getDataOfFinished() {
        return dataOfFinished;
    }

    public void setDataOfFinished(String dataOfFinished) {
        this.dataOfFinished = dataOfFinished;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
