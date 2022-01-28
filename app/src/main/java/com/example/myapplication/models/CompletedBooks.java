package com.example.myapplication.models;

public class CompletedBooks {
    String bookId,dataOfFinished;

    public CompletedBooks() {
    }

    public CompletedBooks(String bookId, String dataOfFinished) {
        this.bookId = bookId;
        this.dataOfFinished = dataOfFinished;
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
}
