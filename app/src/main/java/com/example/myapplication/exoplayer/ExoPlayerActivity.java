package com.example.myapplication.exoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activites.BaseActivity;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityExoPlayerBinding;
import com.example.myapplication.databinding.FragmentExoPlayerBinding;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.CompletedBooks;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.UnfinishedBooks;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExoPlayerActivity extends BaseActivity {

    ActivityExoPlayerBinding binding;
    private Books books;
    long duration;
    SimpleExoPlayer simpleExoPlayer;
    private String bookId;
    private long Duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

        bookId = getIntent().getStringExtra(Constants.INTENT_KEY_ID);
        duration = getIntent().getLongExtra(Constants.INTENT_KEY_DURATION, 0);

        if (Constants.LIST != null) {
            for (int i = 0; i < Constants.LIST.size(); i++) {
                if (Constants.LIST.get(i).getId().equals(bookId)) {
                    books = Constants.LIST.get(i).getBooks();
                }
            }
        }

        binding = ActivityExoPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvBookDes.setText(books.getDes());
        binding.tvNameWriter.setText(books.getName_writer());
        binding.tvNameBookInfo.setText(books.getName_book());
        Glide.with(this).load(books.getImg_uri()).into(binding.imgBook);

        //-------------------------Make activity  full screen ------------------------------------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //----------------------------video url ---------------------------------------------------
//        Uri uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4");
        Uri uri = Uri.parse(books.getAudioUrl());
        //-------------------------Initialize load control-----------------------------------------
        LoadControl loadControl = new DefaultLoadControl();
        //-------------------------Initialize band width meter -----------------------------------------
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //-------------------------Initialize track selector -----------------------------------------
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        //-------------------------Initialize exo Player -----------------------------------------
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        //-------------------------Initialize data  source factory -----------------------------------------
        DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory("exoPlayer_video");
        //-------------------------Initialize Extractors Factory -----------------------------------------
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //-------------------------Initialize Extractors Factory -----------------------------------------
        MediaSource mediaSource = new ExtractorMediaSource(uri, factory, extractorsFactory, null, null);
        //-----------------------------set player---------------------------------------------------------
        binding.playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        binding.playerView.setKeepScreenOn(true);
        //prepare media
        simpleExoPlayer.prepare(mediaSource);
        //play video player
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(duration);

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "My_channel_id", R.string.channel_name, notificationId, mediaDescriptionAdapter, new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            }

            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            }
        });
        playerNotificationManager.setPlayer(simpleExoPlayer);

        playerNotificationManager.setColor(getResources().getColor(R.color.colorPrimary));
        playerNotificationManager.setSmallIcon(R.drawable.audio_book);



        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                Log.e("timeline", "" + reason);
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.e("onTracksChanged", "" + trackSelections);

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                //check condition
                if (playbackState == Player.STATE_BUFFERING) {
                    // when buffering
                    //show progress bar
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else if (playbackState == Player.STATE_READY) {
                    //when ready
                    //hide progress bar
                    long realDurationMillis = simpleExoPlayer.getDuration();

                    binding.progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        //play video when ready
        simpleExoPlayer.setPlayWhenReady(false);
        //Get playback state
        simpleExoPlayer.getPlaybackState();
    }


    private PlayerNotificationManager playerNotificationManager;
    private int notificationId = 1234;
    private PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {


        @Override
        public String getCurrentSubText(Player player) {
            return  "";
        }

        @Override
        public String getCurrentContentTitle(Player player) {
            return books.getName_book();
        }

        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            return null;
        }

        @Override
        public String getCurrentContentText(Player player) {
            return books.getName_writer();
        }

        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            return null;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playerNotificationManager != null) {
            String realDurationMillis = TimeUnit.MILLISECONDS.toMinutes(simpleExoPlayer.getCurrentPosition())
                    + ":"
                    + (TimeUnit.MILLISECONDS.toSeconds(simpleExoPlayer.getCurrentPosition())
                    - (TimeUnit.MILLISECONDS.toMinutes(simpleExoPlayer.getCurrentPosition()) * 60)
            );
            Duration = simpleExoPlayer.getDuration();
            currentPosition = simpleExoPlayer.getCurrentPosition();
//            stopMedia();
            if (currentUser != null) {
                saveToLibrary();
            } else
                Toast.makeText(this, "لتتمكن من تنزيل الكتاب ع جهازك يجب تسجيل دخول ", Toast.LENGTH_SHORT).show();

            Log.e("playerNotifi onDestroy", realDurationMillis + "");
            playerNotificationManager.setPlayer(null);
        }
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }


    long currentPosition;
    String formattedDate;


    private void saveToLibrary() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());

        formattedDate = df.format(c);
        if (currentPosition >= Duration) {
            Library library = new Library(bookId, formattedDate, true, currentPosition);
            firebaseFirestore.collection("library").document(currentUser.getUid())
                    .collection("myObj").document(bookId).set(library)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            } else {
                                task.getException().printStackTrace();
                            }
                        }
                    })
            ;

        } else {
            Library library = new Library(bookId, formattedDate, false, currentPosition);
            firebaseFirestore.collection("library").document(currentUser.getUid())
                    .collection("myObj").document(bookId).set(library)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                            } else {
                                task.getException().printStackTrace();
                            }
                        }
                    })
            ;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSlideDown(this); //fire the slide left animation
    }
}