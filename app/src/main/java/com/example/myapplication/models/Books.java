package com.example.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Books
        implements Parcelable
{
    private String img_uri, name_book, name_writer, Des,AudioUrl;
    Boolean most_listened;

    public Books() {
    }


    public Books(String img_uri, String name_book, String name_writer, String des, String audioUrl, Boolean most_listened) {
        this.img_uri = img_uri;
        this.name_book = name_book;
        this.name_writer = name_writer;
        Des = des;
        AudioUrl = audioUrl;
        this.most_listened = most_listened;
    }

    public static final Creator<Books> CREATOR = new Creator<Books>() {
        @Override
        public Books createFromParcel(Parcel in) {
            return new Books(in);
        }

        @Override
        public Books[] newArray(int size) {
            return new Books[size];
        }
    };

    public String getAudioUrl() {
        return AudioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        AudioUrl = audioUrl;
    }

    protected Books(Parcel in) {
        img_uri = in.readString();
        name_book = in.readString();
        name_writer = in.readString();
        Des = in.readString();
        byte tmpMost_listened = in.readByte();
        most_listened = tmpMost_listened == 0 ? null : tmpMost_listened == 1;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }

    public String getName_book() {
        return name_book;
    }

    public void setName_book(String name_book) {
        this.name_book = name_book;
    }

    public String getName_writer() {
        return name_writer;
    }

    public void setName_writer(String name_writer) {
        this.name_writer = name_writer;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public Boolean getMost_listened() {
        return most_listened;
    }

    public void setMost_listened(Boolean most_listened) {
        this.most_listened = most_listened;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img_uri);
        parcel.writeString(name_book);
        parcel.writeString(name_writer);
        parcel.writeString(Des);
        parcel.writeString(AudioUrl);
        parcel.writeByte((byte) (most_listened == null ? 0 : most_listened ? 1 : 2));
    }
}