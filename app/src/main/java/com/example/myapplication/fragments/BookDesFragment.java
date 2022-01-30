package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
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
        Log.e("first id", id + "");
        if (currentUser==null||(currentUser!=null&&currentUser.getEmail()==null)) {
            binding.btn.setVisibility(View.INVISIBLE);
        }
        setData(book);
//        changeFavColor(id);

//        Toast.makeText(getActivity(), "Constants.LIST.size() = " + list.size(), Toast.LENGTH_SHORT).show();
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
                EventBus.getDefault().post(new MyEventBus(Constants.FROM_BOOK_DES_TO_EXO_PLAYER, id,0));
            }
        });

        favoriteArray = new ArrayList<>();

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
                Log.e("Next Loop1", i + "");
                if (Constants.LIST.get(i).getId().equals(id)) {
                    for (int j = i + 1; j < Constants.LIST.size(); j++) {
                        Log.e("next old book name ", book.getName_book());
                        if (Constants.LIST.get(j).getBooks().getMost_listened() == cat) {
                            book = Constants.LIST.get(j).getBooks();
                            id = Constants.LIST.get(j).getId();
                            Log.e("next new book name", book.getName_book());
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
                    Log.e(" pre pre old book name ", book.getName_book());

                    for (int j = i - 1; j >= 0; j--) {
                        Log.e(" pre old book name ", book.getName_book());
                        if (Constants.LIST.get(j).getBooks().getMost_listened() == cat) {
                            book = Constants.LIST.get(j).getBooks();
                            id = Constants.LIST.get(j).getId();
                            Log.e(" pre new book name", book.getName_book());
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void setData(Books book) {
        binding.tvNameWriter.setText(book.getName_writer());
        binding.tvNameBookInfo.setText(book.getName_book());
        binding.tvBookDes.setText(book.getDes());
        Glide.with(getActivity()).load(book.getImg_uri()).into(binding.imgBook);
        changeFavColor(id);
    }


//----------------------------------------------------------------------------------------

    Favorite favorite;

    ArrayList<FavoriteArray> favoriteArray;

    private void createFavs() {
        CollectionReference questionRef = firebaseFirestore.collection("fav");
        questionRef
                .whereEqualTo("userId", currentUser.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        favorite = snapshot.toObject(Favorite.class);
                    }
                    if (findId(favorite.getList())) {

                        deleteFromFavs();
                    } else {
                        addToFavs();
                    }
                } else {
                    ArrayList<FavoriteArray> favoriteArrays = new ArrayList<>();
                    favoriteArrays.add(new FavoriteArray(id));
                    Favorite favorite = new Favorite(currentUser.getUid(), favoriteArrays);
                    firebaseFirestore.collection("fav").document(currentUser.getUid()).set(favorite);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "onFailure", Toast.LENGTH_SHORT).show();

            }
        });


//        FavoriteArray favoriteArray=new FavoriteArray(id);
//
//      DocumentReference documentReference = firebaseFirestore.collection("Fav")
//                        .document(currentUser.getUid())
//                        .collection("myBooks").document(id);
//                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getActivity(), "This note is Delete", Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity(), "Failed To Delete", Toast.LENGTH_SHORT).show();
//                    }
//                });
//


    }

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

//        favoriteArray = favorite.getList();
//        favoriteArray.add(new FavoriteArray(id));
//        Favorite favorite = new Favorite(currentUser.getUid(), favoriteArray);
//        firebaseFirestore.collection("fav").document(favorite.getUserId()).set(favorite);
//        Toast.makeText(getActivity(), "تمت الاضافة الى المفضلة ", Toast.LENGTH_SHORT).show();
    }

    private void deleteFromFavs() {

        FavoriteArray favoriteArray = new FavoriteArray(id);
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

//        for (int i = 0; i < favoriteArray.size(); i++) {
//            if (favoriteArray.get(i).getBookId().equals(id)) {
//                favoriteArray.remove(i);
//                Favorite favorite = new Favorite(currentUser.getUid(), favoriteArray);
//                firebaseFirestore.collection("fav").document(favorite.getUserId()).set(favorite);
//                Toast.makeText(getActivity(), "تم الازالة من المفضلة ", Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
    }


    private Boolean findId(ArrayList<FavoriteArray> a) {
//        a = favorite.getList();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).getBookId().equals(id)) {
                return true;
            }
        }
        return false;
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
                        Toast.makeText(getActivity(), "color", Toast.LENGTH_SHORT).show();
                        Log.e("Liked", id + "");
                    }
                });
        binding.heartButton.setLiked(false);

        Toast.makeText(getActivity(), "66", Toast.LENGTH_SHORT).show();

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
                        new DownloadFile().execute();
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


//    public class MyAsnyc extends AsyncTask<Void, Void, Void> {
//        public File file;
//        InputStream is;
//
//        protected void doInBackground() throws IOException {
//
//            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//            file = new File(path, "DemoPicture.jpg");
//            try {
//                // Make sure the Pictures directory exists.
//                path.mkdirs();
//
//                URL url = new URL(book.getAudioUrl());
//                /* Open a connection to that URL. */
//                URLConnection ucon = url.openConnection();
//
//                is = ucon.getInputStream();
//
//                OutputStream os = new FileOutputStream(file);
//                byte[] data = new byte[is.available()];
//                is.read(data);
//                os.write(data);
//                is.close();
//                os.close();
//
//            } catch (IOException e) {
//                Log.d("ImageManager", "Error: " + e);
//            }
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            // TODO Auto-generated method stub
//            try {
//                doInBackground();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        protected void onPostExecute() {
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(null,
//                    new String[]{file.toString()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//            Toast.makeText(activity, "ppp", Toast.LENGTH_SHORT).show();
//
//        }
//    }

//    private String imageName = "book.getName_book()";

//    private class saveMedia extends AsyncTask<String, Integer, String> {
//
//        private ProgressDialog progressDialog;
//        private HttpURLConnection httpURLConnection;
//        private InputStream inputStream;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setTitle("Download Image");
//            progressDialog.setMessage("DownLoading...");
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setIndeterminate(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Bitmap bitmap = null;
//            try {
//                URL url = new URL(book.getAudioUrl());
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.connect();
//                inputStream = httpURLConnection.getInputStream();
//                OutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + book.getName_book());
//                int lengthOfFile = httpURLConnection.getContentLength();
//                int count = 0;
//                byte date[] = new byte[1024];
//                long total = 0;
//                while ((count = inputStream.read(date)) != -1) {
//                    total += count;
//                    publishProgress((int) (total * 100 / lengthOfFile));
//                    outputStream.write(date, 0, count);
//                }
//                outputStream.flush();
//                outputStream.close();
//                inputStream.close();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (inputStream != null)
//                        inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                httpURLConnection.disconnect();
//            }
//            return imageName;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            progressDialog.setProgress(values[0]);
//        }
//
//
//        @Override
//        protected void onPostExecute(String bitmap) {
//            super.onPostExecute(bitmap);
//            Toast.makeText(getActivity(), bitmap + " downloaded ", Toast.LENGTH_LONG).show();
//            progressDialog.dismiss();
//            downloadMedia();
//        }
//    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Download Image");
            progressDialog.setMessage("DownLoading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String bitmap) {
            super.onPostExecute(bitmap);
            Toast.makeText(getActivity(), bitmap + " downloaded ", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            downloadMedia();
        }

        @Override
        protected String doInBackground(String... urlParams) {
            int count;
            try {
                URL url = new URL(book.getAudioUrl());
                URLConnection conexion = url.openConnection();
                conexion.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conexion.getContentLength();

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream("/ansys/" + book.getName_book());

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return "Done";
        }
    }

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