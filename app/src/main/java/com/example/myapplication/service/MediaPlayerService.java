package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;

public class MediaPlayerService extends Service {
    IBinder binder = new MediaPlayerService.MyBinder();
    MediaPlayer mediaPlayer;
    int[] mediaArray = {R.raw.quran1, R.raw.quran2, R.raw.quran3};
    int position = 0;

    public MediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        playFirstMedia();
        return binder;
    }

    public class MyBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }

    }

    public String testMethod() {
        return "Test";
    }

    public void playFirstMedia() {
        position = 0;
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                stopMediaPlayer();
            mediaPlayer = MediaPlayer.create(this, mediaArray[0]);
            mediaPlayer.start();
        } else {
            mediaPlayer = MediaPlayer.create(this, mediaArray[0]);
            mediaPlayer.start();
        }

    }

    public void nextMedia() {
        if (mediaPlayer != null) {
            if (position != (mediaArray.length - 1)) {
                if (mediaPlayer.isPlaying())
                    stopMediaPlayer();
                position++;
                mediaPlayer = MediaPlayer.create(this, mediaArray[position]);
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "The Last One", Toast.LENGTH_SHORT).show();
            }
        } else {
            mediaPlayer = MediaPlayer.create(this, mediaArray[position]);
            mediaPlayer.start();
        }

    }

    public void preMedia() {
        if (mediaPlayer != null) {
            if (position != 0) {
                if (mediaPlayer.isPlaying())
                    stopMediaPlayer();
                position--;
                mediaPlayer = MediaPlayer.create(this, mediaArray[position]);
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "The First One", Toast.LENGTH_SHORT).show();
            }
        } else {
            mediaPlayer = MediaPlayer.create(this, mediaArray[position]);
            mediaPlayer.start();
        }

    }

    public void stopMediaPlayer() {
        mediaPlayer.stop();

    }

    public void pauseMediaPlayer() {
        mediaPlayer.pause();
    }

    public void resumeMediaPlayer() {
        mediaPlayer.start();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MediaPlayerService", "onDestroy");
        stopMediaPlayer();
    }



}
