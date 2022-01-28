package com.example.myapplication.fav;

import com.google.firebase.firestore.DocumentReference;

public class FavoriteArray {
    String bookId;

    public FavoriteArray() {
    }

    public FavoriteArray(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
