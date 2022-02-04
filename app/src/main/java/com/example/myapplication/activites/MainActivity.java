package com.example.myapplication.activites;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.myapplication.R;
import com.example.myapplication.constants.Constants;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.exoplayer.ExoPlayerActivity;
import com.example.myapplication.fragments.AboutUsFragment;
import com.example.myapplication.fragments.BookDesFragment;
import com.example.myapplication.fragments.ChangePasswordFragment;
import com.example.myapplication.fragments.ExploreFragment;
import com.example.myapplication.fragments.FavBookFragment;
import com.example.myapplication.fragments.LibraryFragment;
import com.example.myapplication.fragments.SettingsFragment;
import com.example.myapplication.fragments.ShowBooksFragment;
import com.example.myapplication.fragments.UserFragment;
import com.example.myapplication.models.MyEventBus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);

        Intent notificationIntent = new Intent(this, ShowNotification.class);
        PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(contentIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + AlarmManager.INTERVAL_HALF_HOUR * 1, AlarmManager.INTERVAL_DAY * 2, contentIntent);

        Log.e("onCreate Main ", "onCreate");
        //-------------------------------------bottomNavigationView---------------------
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    openFragment(ExploreFragment.newInstance("", ""));
                } else if (item.getItemId() == R.id.book) {
                    openFragment(LibraryFragment.newInstance("", ""));
                } else if (item.getItemId() == R.id.settings) {
                    openFragment(SettingsFragment.newInstance("", ""));
                } else {
                    openFragment(UserFragment.newInstance("", ""));
                }
                return true;
            }
        });
        openFragment(ExploreFragment.newInstance("", ""));
        //-----------------------------------

    }


    void openFragment(Fragment fragemnt) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, fragemnt);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onEvent(MyEventBus event) {

        if (event.getType() == Constants.FROM_EXPLORE_TO_MOST_LISTENED) {
            openFragment(ShowBooksFragment.newInstance(event.getTitle()));
        }
        else if (event.getType() == Constants.FROM_EXPLORE_TO_LATEST_PUBLISHED) {
            openFragment(ShowBooksFragment.newInstance(event.getTitle()));
        }
        else if (event.getType() == Constants.FROM_USER_TO_CHANGE_PASSWORD) {
            openFragment(ChangePasswordFragment.newInstance("", ""));
        } else if (event.getType() == Constants.FROM_CHANGE_PASSWORD_TO_USER) {
            openFragment(UserFragment.newInstance("", ""));
        } else if (event.getType() == Constants.FROM_BOOK_DES_TO_EXO_PLAYER) {
//            openFragment(ExoPlayerFragment.newInstance(event.getTitle(),event.getDuration()));
            Intent intent=new Intent(this, ExoPlayerActivity.class);
            intent.putExtra(Constants.INTENT_KEY_ID,event.getTitle());
            intent.putExtra(Constants.INTENT_KEY_DURATION,event.getDuration());
            startActivity(intent);
            Animatoo.animateSlideUp(this);
        } else if (event.getType() == Constants.FROM_SETTINGS_TO_FAV) {
            openFragment(FavBookFragment.newInstance("", ""));
        } else if (event.getType() == Constants.FROM_SETTINGS_TO_ABOUT_US) {
            openFragment(AboutUsFragment.newInstance("", ""));
        } else if (event.getType() == Constants.FROM_ADAPTER_TO_BOOK_DES) {
            openFragment(BookDesFragment.newInstance(event.getTitle(), event.getCat()));
//            BookDesFragment bookDesFragment = new BookDesFragment();
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("SampleObject", event.getBooks());
//            bookDesFragment.setArguments(bundle);
//            openFragment(bookDesFragment);
//            Toast.makeText(MainActivity.this, "FROM_EXPLORE_TO_LATEST_PUBLISHED", Toast.LENGTH_SHORT).show();
        }
    }

}