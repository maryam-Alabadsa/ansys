package com.example.myapplication.fav;

import java.util.ArrayList;

public class Favorite {
    String userId;
    ArrayList<FavoriteArray> list;

    public Favorite(String userId, ArrayList<FavoriteArray> list) {
        this.userId = userId;
        this.list = list;
    }

    public Favorite() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<FavoriteArray> getList() {
        return list;
    }

    public void setList(ArrayList<FavoriteArray> list) {
        this.list = list;
    }
}
