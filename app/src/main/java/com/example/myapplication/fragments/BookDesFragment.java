package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;
import com.example.myapplication.adapter.Adapter_Rv_Books;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.FragmentBookDesBinding;
import com.example.myapplication.databinding.FragmentExploreBinding;
import com.example.myapplication.exoplayer.ExoPlayerActivity;
import com.example.myapplication.fav.Favorite;
import com.example.myapplication.fav.FavoriteArray;
import com.example.myapplication.models.Books;
import com.example.myapplication.models.CompletedBooks;
import com.example.myapplication.models.ConstantsList;
import com.example.myapplication.models.Library;
import com.example.myapplication.models.LoadedBooks;
import com.example.myapplication.models.MyEventBus;
import com.example.myapplication.models.UnfinishedBooks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class BookDesFragment extends BaseFragment implements OnLikeListener,
        OnAnimationEndListener {

    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "cat.";
    FragmentBookDesBinding binding;
    private String id;
    private Boolean cat;
    private Books book;
    // ArrayList<ConstantsList> list;

    public BookDesFragment() {
    }

    public static BookDesFragment newInstance(String id, Boolean cat) {
        BookDesFragment fragment = new BookDesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        args.putBoolean(ARG_PARAM2, cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cat = getArguments().getBoolean(ARG_PARAM2);
            if (Constants.LIST != null) {
                id = getArguments().getString(ARG_PARAM1);
                for (int i = 0; i < Constants.LIST.size(); i++) {
                    if (Constants.LIST.get(i).getId().equals(id)) {
                        book = Constants.LIST.get(i).getBooks();
                        break;
                    }

                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookDesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.heartButton.setOnLikeListener(this);
        binding.heartButton.setOnAnimationEndListener(this);
        if (currentUser == null || (currentUser != null && currentUser.getEmail() == null)) {
            binding.btn.setVisibility(View.INVISIBLE);
        }
        setData(book);

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cat != null) {
                    Next();
                    setData(book);
//                    changeFavColor(id);

                }
            }
        });
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        binding.btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cat != null) {
                    Pre();
                    setData(book);
//                    changeFavColor();
                }
            }
        });
        binding.btnRunMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), list.get(id).getId()+"", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, id, 0));
            }
        });

        return view;
    }


    @Override
    public void liked(LikeButton likeButton) {
        addToFavs();
        Toast.makeText(getActivity(), "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        deleteFromFavs();
        Toast.makeText(getActivity(), "Disliked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationEnd(LikeButton likeButton) {
        Log.d("tag", "Animation End for %s" + likeButton);
    }


    private void Next() {

        if (Constants.LIST != null) {
            for (int i = 0; i < Constants.LIST.size(); i++) {
                if (Constants.LIST.get(i).getId().equals(id)) {
                    for (int j = i + 1; j < Constants.LIST.size(); j++) {
                        if (Constants.LIST.get(j).getBooks().getMost_listened() == cat) {
                            book = Constants.LIST.get(j).getBooks();
                            id = Constants.LIST.get(j).getId();
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void Pre() {
        if (Constants.LIST != null) {
            for (int i = 0; i < Constants.LIST.size(); i++) {
                if (Constants.LIST.get(i).getId().equals(id)) {

                    for (int j = i - 1; j >= 0; j--) {
                        if (Constants.LIST.get(j).getBooks().getMost_listened() == cat) {
                            book = Constants.LIST.get(j).getBooks();
                            id = Constants.LIST.get(j).getId();
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void setData(Books book) {
        if (book != null) {
            binding.tvNameWriter.setText(book.getName_writer());
            binding.tvNameBookInfo.setText(book.getName_book());
            binding.tvBookDes.setText(book.getDes());
            Glide.with(getActivity()).load(book.getImg_uri()).into(binding.imgBook);
            changeFavColor(id);
        }
    }


//----------------------------------------------------------------------------------------


    private void addToFavs() {

        FavoriteArray favoriteArray = new FavoriteArray(id);
        firebaseFirestore.collection("Fav").document(currentUser.getUid())
                .collection("myBooks").document(id).set(favoriteArray)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    private void deleteFromFavs() {

        firebaseFirestore.collection("Fav").document(currentUser.getUid())
                .collection("myBooks").document(id).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                            task.getException().printStackTrace();
                        }
                    }
                });

    }


    private void changeFavColor(String id) {

        firebaseFirestore.collection("Fav").document(currentUser.getUid()).collection("myBooks")
                .whereEqualTo("bookId", id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //shown when the button is liked!
                        binding.heartButton.setLiked(true);
                        binding.heartButton.setUnlikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_heart).colorRes(android.R.color.holo_red_light).sizeDp(25).toBitmap()));
                    }
                });
        binding.heartButton.setLiked(false);

        //shown when the button is in its default state or when unLiked.
        binding.heartButton.setUnlikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_heart).colorRes(android.R.color.white).sizeDp(25).toBitmap()));


    }

    @SuppressLint("RestrictedApi")
    public void showPopupMenu(View v) {
        androidx.appcompat.widget.PopupMenu menu = new androidx.appcompat.widget.PopupMenu(getActivity(), v);
        menu.inflate(R.menu.popup_menu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.Download) {
                    if (currentUser != null) {
                        downloadAudio();
                    } else
                        Toast.makeText(activity, "لتتمكن من تنزيل الكتاب ع جهازك يجب تسجيل دخول ", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.Share) {
                    Glide.with(getActivity()).asBitmap()
                            .load(book.getImg_uri())
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

                            .into(new SimpleTarget<Bitmap>(250, 250) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, "كتاب : " + book.getName_book() + " للكاتب :" + book.getName_writer());
                                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), resource, "", null);
                                    Log.i("quoteswahttodo", "is onresoursereddy" + path);

                                    Uri screenshotUri = Uri.parse(path);

                                    Log.i("quoteswahttodo", "is onresoursereddy" + screenshotUri);

                                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                    intent.setType("image/*");

                                    startActivity(Intent.createChooser(intent, "Share image via..."));

                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                                    super.onLoadFailed(errorDrawable);
                                }

                                @Override
                                public void onLoadStarted(Drawable placeholder) {
                                    Toast.makeText(getActivity(), "Starting", Toast.LENGTH_SHORT).show();
                                    super.onLoadStarted(placeholder);
                                }
                            });
                }
                return true;
            }
        });
        MenuPopupHelper menuHelper = new MenuPopupHelper(getActivity(), (MenuBuilder) menu.getMenu(), v);

        menu.show();
    }


    public void downloadAudio() {
        DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri downloadUri = Uri.parse(book.getAudioUrl());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/Media" + "/" + id + ".mp3");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                DownloadManager.Request request = new DownloadManager.Request(downloadUrl);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDescription("Downloading " + book.getName_book());
                downloadmanager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Downloading " + book.getName_book())
                        .setDescription("Audio File Download")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/ansys/" + id));
                getContext().registerReceiver(onComplete, new
                        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });

    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            // your code
            if (intent != null && ctxt != null) {
                downloadMedia();
                Toast.makeText(ctxt, book.getName_book() + " downloaded ", Toast.LENGTH_LONG).show();
            }
        }
    };


    String formattedDate;

    private void downloadMedia() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        formattedDate = df.format(c);
        LoadedBooks loadedBooks = new LoadedBooks(id, formattedDate);
        firebaseFirestore.collection("library").document(currentUser.getUid())
                .collection("myloadedBooks").document(id).set(loadedBooks)
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
//        loadedBooks = new ArrayList<>();
//
//        CollectionReference questionRef = firebaseFirestore.collection("Library");
//        questionRef
//                .whereEqualTo("uId", currentUser.getUid())
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                        library = snapshot.toObject(Library.class);
//                    }
//                    loadedBooks = library.getLoadedBooks();
//                    for (int i = 0; i < library.getLoadedBooks().size(); i++) {
//                        if (!loadedBooks.get(i).getBookId().equals(id)) {
//                            loadedBooks.add(new LoadedBooks(id, formattedDate));
//                            Library library1 = new Library(currentUser.getUid(), loadedBooks);
//                            firebaseFirestore.collection("Library").document(currentUser.getUid()).set(library1);
//                            new saveMedia().execute();
//                        }
//                    }
//                } else {
//                    loadedBooks.add(new LoadedBooks(id, formattedDate));
//                    Library library1 = new Library(currentUser.getUid(), loadedBooks);
//                    firebaseFirestore.collection("Library").document(currentUser.getUid()).set(library1);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }


}