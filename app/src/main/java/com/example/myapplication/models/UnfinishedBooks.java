package com.example.myapplication.models;

public class UnfinishedBooks {
    String bookId;
    long stopDuration;

    public UnfinishedBooks() {
    }

    public UnfinishedBooks(String bookId, long stopDuration) {
        this.bookId = bookId;
        this.stopDuration = stopDuration;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getStopDuration() {
        return stopDuration;
    }

    public void setStopDuration(long stopDuration) {
        this.stopDuration = stopDuration;
    }
}
