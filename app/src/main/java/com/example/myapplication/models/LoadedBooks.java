package com.example.myapplication.models;

public class LoadedBooks {
    String bookId,LoadedData;


    public LoadedBooks() {
    }

    public LoadedBooks(String bookId, String loadedData) {
        this.bookId = bookId;
        LoadedData = loadedData;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getLoadedData() {
        return LoadedData;
    }

    public void setLoadedData(String loadedData) {
        LoadedData = loadedData;
    }
}
