package com.example.supercalculator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.supercalculator.ui.Derivatives;
import com.example.supercalculator.ui.History;
import com.example.supercalculator.ui.Matrices;
import com.example.supercalculator.ui.Settings;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;

//общий класс Activity для всех разделов
public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    NotificationManager nm;
    NavigationView navView;
    ProgressBar pb;
    AppBarLayout abl;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                toolbar = findViewById(R.id.toolbar);
                Fragment fr = new Settings();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.nav_host_fragment, fr);
                fragmentTransaction.commit();
                toolbar.setTitle(getString(R.string.settings));
                return true;
            case R.id.action_history:
                toolbar = findViewById(R.id.toolbar);
                Fragment fr1 = new History();
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fm1.beginTransaction();
                fragmentTransaction1.add(R.id.nav_host_fragment, fr1);
                fragmentTransaction1.commit();
                toolbar.setTitle(getString(R.string.history));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = findViewById(R.id.pb);
        pb.setVisibility(View.GONE);

        abl = findViewById(R.id.appBar);
        //pb2 = pb;
        abl.removeView(pb);
        toolbar.addView(pb);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.RIGHT;
        pb.setLayoutParams(lp);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_matrix, R.id.nav_derivatives, R.id.nav_vectors, R.id.nav_about, R.id.nav_exit, R.id.nav_formulas, R.id.nav_history, R.id.nav_rate, R.id.nav_settings, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navView = findViewById(R.id.nav_view);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("settings")));
            String line = "";
            int temp = 0;
            while ((line = reader.readLine()) != null) {
                if (temp == 1) {
                    if (line.contains("d")) {
                        navView.setBackgroundColor(Color.parseColor("#252525"));
                    }
                    if (line.contains("w")) {
                        navView.setBackgroundColor(Color.parseColor("#00AD9D"));
                    }
                }
                temp++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 12);
        calendar.set(Calendar.SECOND, 30);

        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.RTC, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            finish();
            Toast.makeText(this, R.string.will_be_missing, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), R.string.double_press_to_exit, Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
