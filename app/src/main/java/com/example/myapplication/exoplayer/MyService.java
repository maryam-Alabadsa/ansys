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

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.example.myapplication.activites.MainActivity;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.MyEventBus;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import org.greenrobot.eventbus.EventBus;

import static android.app.Notification.BADGE_ICON_LARGE;

public class MyService extends Service {
    IBinder binder = new MyBinder();
    private MediaSessionCompat mediaSession;
    private PlayerNotificationManager playerNotificationManager;
    private int notificationId = 1234;

    private PlayerNotificationManager.MediaDescriptionAdapter mediaDescriptionAdapter =
            new PlayerNotificationManager.MediaDescriptionAdapter() {


                @Override
                public String getCurrentSubText(Player player) {
                    return "";
                }

                @Override
                public String getCurrentContentTitle(Player player) {
                    return "sasa";
                }

                @Override
                public PendingIntent createCurrentContentIntent(Player player) {
//                    Intent intent = new Intent(context, ExoPlayerActivity.class);
//                    intent.putExtra(Constants.INTENT_KEY_ID, bookId);
//                    intent.putExtra(Constants.INTENT_KEY_DURATION, player.getCurrentPosition());
//                    PendingIntent contentPendingIntent = PendingIntent.getActivity
//                            (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, bookId, player.getCurrentPosition()));
//
////                    if (simpleExoPlayer != null) {
////                        simpleExoPlayer.release();
////                        simpleExoPlayer = null;
////                    }
////                    playerNotificationManager.setPlayer(null);

                    return null;
                }

                @Override
                public String getCurrentContentText(Player player) {
                    return "dsds";
                }

                @Override
                public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                    return null;
                }
            };
//    Player player;
//    Books books;
    //   String bookId;
//    Context context;

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

    //old on crate
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "My_channel_id", R.string.channel_name, notificationId, mediaDescriptionAdapter, new PlayerNotificationManager.NotificationListener() {
//
//
//            @Override
//            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
//            }
//
//            @Override
//            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
//            }
//        });
//        playerNotificationManager.setPlayer(ExoPlayerActivity.simpleExoPlayer);
//        playerNotificationManager.setColor(ResourcesCompat.getColor(this.getResources(), R.color.colorAccent, null));
//
////        playerNotificationManager.setColor(getResources().getColor(R.color.colorPrimary));
//        playerNotificationManager.setColorized(true);
//        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_HIGH);
////        playerNotificationManager.useFastForwardAction(true);
//        playerNotificationManager.setSmallIcon(R.drawable.music);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            playerNotificationManager.setBadgeIconType(BADGE_ICON_LARGE);
//        }
//
//        Log.e("MyService", "onCreate");
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                "My_channel_id", R.string.channel_name, notificationId,
                new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        String title;
//                        if (MainActivity.lessonsPlayedFrom == 0) {
//                            title = LessonsAdapter.mediaList.get(player.getCurrentWindowIndex()).getTitle();
//                        } else if (MainActivity.lessonsPlayedFrom == 1) {
//                            title = DownloadsAdapter.downloadedLessonsList.get(player.getCurrentWindowIndex()).getTitle();
//                        } else {
//                            title = FavoritesAdapter.favoritesLessonsList.get(player.getCurrentWindowIndex()).getTitle();
//                        }
                        return "title";
                    }

                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        return null;
                    }

                    @Override
                    public String getCurrentContentText(Player player) {
                        String ContentText;
//                        if (MainActivity.lessonsPlayedFrom == 0) {
//                            ContentText = LessonsAdapter.mediaList.get(player.getCurrentWindowIndex()).getPresenteName();
//                        } else if (MainActivity.lessonsPlayedFrom == 1) {
//                            ContentText = DownloadsAdapter.downloadedLessonsList.get(player.getCurrentWindowIndex()).getPresenteName();
//                        } else {
//                            ContentText = FavoritesAdapter.favoritesLessonsList.get(player.getCurrentWindowIndex()).getPresenteName();
//                        }
                        return "ContentText";
                    }

                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {

//                        if (MainActivity.lessonsPlayedFrom == 0) {
//                            GlideApp.with(context)
//                                    .asBitmap()
//                                    .load(P)
//                                    .into(new SimpleTarget<Bitmap>() {
//                                        @Override
//                                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                                            //you can use loaded bitmap here
//                                        }
//                                    });
//                        } else if (MainActivity.lessonsPlayedFrom == 1) {
//                            ContextWrapper cw = new ContextWrapper(context);
//                            File directory = cw.getExternalFilesDir("downloads/photo");
//                            File mypath = new File(directory, DownloadsAdapter.downloadedLessonsList.get(player.getCurrentWindowIndex()).getPersonalImageName() + ".jpg");
//                            icon = ((BitmapDrawable) Drawable.createFromPath(mypath.toString())).getBitmap();
//                        } else {
//                            ContentText = FavoritesAdapter.favoritesLessonsList.get(player.getCurrentWindowIndex()).getPresenteName();
//                        }
                        return null;
                    }
                }
        );

        playerNotificationManager.setSmallIcon(R.drawable.music);


        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });
        playerNotificationManager.setPlayer(ExoPlayerActivity.simpleExoPlayer);

        mediaSession = new MediaSessionCompat(context, "MEDIA_SESSION_TAG");
        mediaSession.setActive(true);
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

//        mediaSessionConnector = new MediaSessionConnector(mediaSession);
//        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
//            @Override
//            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
//                return Samples.getMediaDescription(context, SAMPLES[windowIndex]);
//            }
//        });
//        mediaSessionConnector.setPlayer(player, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.e("MyService", "onStartCommand  startId = " + startId);
//        String url = intent.getStringExtra("url");
//        if (url != null)
//            Log.e("MyService", "url = " + url);
////        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(this, "My_channel_id", R.string.channel_name, notificationId, mediaDescriptionAdapter, new PlayerNotificationManager.NotificationListener() {
////
////
////            @Override
////            public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
////            }
////
////            @Override
////            public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
////            }
////        });
////        playerNotificationManager.setPlayer(ExoPlayerActivity.simpleExoPlayer);
////        playerNotificationManager.setColor(ResourcesCompat.getColor(this.getResources(), R.color.colorAccent, null));
////
//////        playerNotificationManager.setColor(getResources().getColor(R.color.colorPrimary));
////        playerNotificationManager.setColorized(true);
////        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_HIGH);
//////        playerNotificationManager.useFastForwardAction(true);
////        playerNotificationManager.setSmallIcon(R.drawable.music);
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////            playerNotificationManager.setBadgeIconType(BADGE_ICON_LARGE);
////        }
//
////        stopSelf();
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyService", "onDestroy");
    }



    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }

    }

}