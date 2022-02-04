package com.example.myapplication.exoplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.MyEventBus;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Notification.BADGE_ICON_LARGE;

public class MyService extends Service {
    IBinder binder = new MyBinder();
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager playerNotificationManager;
    private int notificationId = 1234;
    public static SimpleExoPlayer simpleExoPlayer;

    private PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter =
            new PlayerNotificationManager.MediaDescriptionAdapter() {


                @Override
                public String getCurrentSubText(Player player) {
                    return "";
                }

                @Override
                public String getCurrentContentTitle(Player player) {
                    return books.getName_book();
                }

                @Override
                public PendingIntent createCurrentContentIntent(Player player) {
                    Intent intent = new Intent(MyService.this, ExoPlayerActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_ID, bookId);
                    intent.putExtra(Constants.INTENT_KEY_DURATION, player.getCurrentPosition());
                    Log.e(" service", player.getCurrentPosition() + "");
                    PendingIntent contentPendingIntent = PendingIntent.getActivity
                            (MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    return contentPendingIntent;
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

    Books books;
    String bookId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private long Duration;

    public MyService() {
//        this.player = player;
        // this.books = books;
        //   this.bookId = bookId;
//        this.context = context;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("MyService", "onBind");
        return binder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.e("MyService", "unbindService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        simpleExoPlayer = ExoPlayerActivity.simpleExoPlayer;

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "My_channel_id", R.string.channel_name, notificationId, mediaDescriptionAdapter, new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            }

            @Override
            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            }
        });
        playerNotificationManager.setPlayer(simpleExoPlayer);
        playerNotificationManager.setColor(ResourcesCompat.getColor(this.getResources(), R.color.colorAccent, null));

//        playerNotificationManager.setColor(getResources().getColor(R.color.colorPrimary));
        playerNotificationManager.setColorized(true);
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_HIGH);
//        playerNotificationManager.useFastForwardAction(true);
        playerNotificationManager.setSmallIcon(R.drawable.music_logo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            playerNotificationManager.setBadgeIconType(BADGE_ICON_LARGE);
        }

        Log.e("MyService", "onCreate");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bookId = intent.getStringExtra("bookId");
        books = intent.getParcelableExtra("Book");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return START_STICKY;
    }

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
                Toast.makeText(this, "لتتمكن من تنزيل الكتاب ع جهازك يجب تسجيل دخول ", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "4444", Toast.LENGTH_SHORT).show();
                saveToLibrary();

            }
//        }
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


    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

    }


}