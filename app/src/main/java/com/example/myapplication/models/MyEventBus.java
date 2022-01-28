package com.example.myapplication.models;

public class MyEventBus {
    private int type;
    private String title = "";

    private Books books;
    Boolean Cat;
    int id;
    int id_from_any_adapter;

    public MyEventBus(int type, String title, Boolean cat) {
        this.type = type;
        this.title = title;
        Cat = cat;
    }

    public Boolean getCat() {
        return Cat;
    }

    public void setCat(Boolean cat) {
        Cat = cat;
    }

    public MyEventBus(int type, String title, int id, int id_from_any_adapter) {
        this.type = type;
        this.title = title;
        this.id = id;
        this.id_from_any_adapter = id_from_any_adapter;
    }


    public MyEventBus(int type, String title, Books books) {
        this.type = type;
        this.title = title;
        this.books = books;
    }

    public MyEventBus(int type, String title) {
        this.type = type;
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_from_any_adapter() {
        return id_from_any_adapter;
    }

    public void setId_from_any_adapter(int id_from_any_adapter) {
        this.id_from_any_adapter = id_from_any_adapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
        this.books = books;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
