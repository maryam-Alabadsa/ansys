package com.example.myapplication.exoplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activites.BaseActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityExoPlayerBinding;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.MyEventBus;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.BADGE_ICON_LARGE;

public class ExoPlayerActivity extends BaseActivity {

    private static final int FROM_STORAGE = 1;
    private static final int FROM_FIREBASE = 2;
    ActivityExoPlayerBinding binding;
    private Books books;
    long duration;
   public static SimpleExoPlayer simpleExoPlayer;
    private String bookId;
    private long Duration;

    MyService myService;


    @RequiresApi(api = Build.VERSION_CODES.O)
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
        binding.tvNameWriter.setText(books.getName_writer());
        binding.tvNameBook.setText(books.getName_book());
        Glide.with(this).load(books.getImg_uri()).into(binding.imgBook);

        //-------------------------Make activity  full screen ------------------------------------
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //----------------------------video url ---------------------------------------------------
//        Uri uri = Uri.parse("https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4");

//        Uri uri= Uri.parse("https://firebasestorage.googleapis.com/v0/b/ansys-c19a7.appspot.com/o/%D8%A3%D9%86%D9%85%D8%A7%D8%B7%20%D8%A7%D9%84%D8%B4%D8%AE%D8%B5%D9%8A%D8%A9%20%D8%A7%D9%844--------%20_%20%D9%83%D8%AA%D8%A7%D8%A8%20%D9%85%D8%B9%D8%B1%D9%81%D8%A9%20%D8%A7%D9%84%D8%A5%D9%86%D8%B3%D8%A7%D9%86%20%D9%85%D9%86%20%D9%86%D8%B8%D8%B1%D8%A9%20_%D8%A3%D8%AE%D8%B6%D8%B1(MP3_128K).mp3?alt=media&token=fc7220b5-4feb-4d20-a34c-744725a6da5a");
//        getUrlAsync();
//        Uri uri = getUri();
        getUri();

        // myService=new MyService(simpleExoPlayer,books,bookId,this);
        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
        Util.startForegroundService(this, intent);
    }

    Uri uri2;

    private void getUri() {
        firebaseFirestore.collection("library").document(currentUser.getUid())
                .collection("myloadedBooks").document(bookId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ansys/" + bookId;
                            uri2 = Uri.parse(path);
                            playAudio(FROM_STORAGE);
//                            Log.e("from ExternalStorage", path);
                            Toast.makeText(ExoPlayerActivity.this, "from Download ", Toast.LENGTH_SHORT).show();
                        } else {
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference dateRef = storageRef.child("/Media" + "/" + bookId + ".mp3");
//                            Log.e("dateRef", dateRef + "");
                            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    uri2 = downloadUrl;
                                    Toast.makeText(ExoPlayerActivity.this, uri2 + "", Toast.LENGTH_SHORT).show();

                                    playAudio(FROM_FIREBASE);
                                }
                            });
//                            Log.e("from fire store2", uri2 + "");
                        }
                    }
                });
//        Log.e("get uri", uri2 + "");
//        return uri2;
    }

    public void playAudio(int fromFlag) {
        //-------------------------Initialize load control-----------------------------------------
        LoadControl loadControl = new DefaultLoadControl();
        //-------------------------Initialize band width meter -----------------------------------------
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        //-------------------------Initialize track selector -----------------------------------------
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        //-------------------------Initialize exo Player -----------------------------------------
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(ExoPlayerActivity.this, trackSelector, loadControl);
        //-------------------------Initialize data  source factory -----------------------------------------
        DataSource.Factory dataSourceFactory = new FileDataSourceFactory(); //FROM_STORAGE
        if (fromFlag == FROM_FIREBASE)
            dataSourceFactory = new DefaultHttpDataSourceFactory("exoPlayer_video");//FROM_FIREBASE
        //-------------------------Initialize Extractors Factory -----------------------------------------
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        //-------------------------Initialize Extractors Factory -----------------------------------------
        MediaSource mediaSource = new ExtractorMediaSource(uri2, dataSourceFactory, extractorsFactory, null, null);
        Log.e("getUri2", uri2 + "");

        //-----------------------------set player---------------------------------------------------------
        binding.playerView.setPlayer(simpleExoPlayer);
        //keep screen on
        binding.playerView.setKeepScreenOn(true);
        //prepare media
        simpleExoPlayer.prepare(mediaSource);

        //play video player
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(duration);
        simpleExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
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

        // start playerNotificationManager
    }

    @Override
    public void onPause() {
        super.onPause();
        //play video when ready
        simpleExoPlayer.setPlayWhenReady(false);
        //Get playback state
        simpleExoPlayer.getPlaybackState();
    }


//    private PlayerNotificationManager playerNotificationManager;
//    private int notificationId = 1234;
//    private PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter =
//            new PlayerNotificationManager.MediaDescriptionAdapter() {
//
//
//                @Override
//                public String getCurrentSubText(Player player) {
//                    return "";
//                }
//
//                @Override
//                public String getCurrentContentTitle(Player player) {
//                    return books.getName_book();
//                }
//
//                @Override
//                public PendingIntent createCurrentContentIntent(Player player) {
//                    Intent intent = new Intent(ExoPlayerActivity.this, ExoPlayerActivity.class);
//                    intent.putExtra(Constants.INTENT_KEY_ID, bookId);
//                    intent.putExtra(Constants.INTENT_KEY_DURATION, player.getCurrentPosition());
//                    PendingIntent contentPendingIntent = PendingIntent.getActivity
//                            (ExoPlayerActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, bookId, player.getCurrentPosition()));
//
////                    if (simpleExoPlayer != null) {
////                        simpleExoPlayer.release();
////                        simpleExoPlayer = null;
////                    }
////                    playerNotificationManager.setPlayer(null);
//
//                    return contentPendingIntent;
//                }
//
//                @Override
//                public String getCurrentContentText(Player player) {
//                    return books.getName_writer();
//                }
//
//                @Override
//                public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
//                    return null;
//                }
//            };

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (playerNotificationManager != null) {
        String realDurationMillis = TimeUnit.MILLISECONDS.toMinutes(simpleExoPlayer.getCurrentPosition())
                + ":"
                + (TimeUnit.MILLISECONDS.toSeconds(simpleExoPlayer.getCurrentPosition())
                - (TimeUnit.MILLISECONDS.toMinutes(simpleExoPlayer.getCurrentPosition()) * 60)
        );
        Duration = simpleExoPlayer.getDuration();
        currentPosition = simpleExoPlayer.getCurrentPosition();
//            stopMedia();
        if (currentUser == null || (currentUser != null && currentUser.getEmail() == null)) {
            saveToLibrary();
        } else
            Toast.makeText(this, "لتتمكن من تنزيل الكتاب ع جهازك يجب تسجيل دخول ", Toast.LENGTH_SHORT).show();
//            playerNotificationManager.setPlayer(null);
//        }
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