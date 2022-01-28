package com.example.myapplication.models;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ConstantsList {
    String bookId;
    Books books;


    public ConstantsList(String bookId, Books books) {
        this.bookId = bookId;
        this.books = books;
    }

    public String getId() {
        return bookId;
    }

    public void setId(String bookId) {
        this.bookId = bookId;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }
}
