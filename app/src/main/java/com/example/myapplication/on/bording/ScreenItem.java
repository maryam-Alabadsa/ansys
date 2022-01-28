package com.example.myapplication.on.bording;

public class ScreenItem {
    String Title;
    int ScreenImg;

    public ScreenItem(String title, int screenImg) {
        Title = title;
        ScreenImg = screenImg;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public int getScreenImg() {
        return ScreenImg;
    }

    public void setScreenImg(int screenImg) {
        ScreenImg = screenImg;
    }
}