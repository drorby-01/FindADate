package com.example.myapplication.Connected.AppFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Connected.AppFragments.Home.HomeFragment;
import com.example.myapplication.Connected.AppFragments.Messages.MessageFromFragment;
import com.example.myapplication.Connected.AppFragments.Profile.ProfileUserFragment;
import com.example.myapplication.Connected.AppFragments.Settings.SettingsFragment;
import com.example.myapplication.R;
import com.example.myapplication.SignUp.Files;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchDateActivity extends AppCompatActivity {

    final static int External_ID = 2;
    final String Home = "home";
    final String Message ="Message";
    final String Setting = "Settings";
    final String Profile = "profile";
    BottomNavigationView bottomNavigationView;

    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();
    }

    BottomNavigationView navigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("CommitTransaction")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            switch (menuItem.getItemId()) {

                case R.id.navigation_profile:
                    if (Build.VERSION.SDK_INT >= 23) {
                        int hasExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasExternalPermission == PackageManager.PERMISSION_GRANTED) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new ProfileUserFragment(), Profile).commit();
                        } else {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_ID);
                        }
                    }
                    return true;
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new HomeFragment(), Home).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.action_container,new SettingsFragment(),Setting).commit();
                    return true;
                case R.id.navigation_messages:
                    getSupportFragmentManager().beginTransaction().replace(R.id.action_container,new MessageFromFragment(),Message).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences sharedPreferences1 =getSharedPreferences("chat",MODE_PRIVATE);

        if(sharedPreferences1.contains("chat") && sharedPreferences1.getString("chat","").equals("1"))
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new MessageFromFragment(), Message).commit();
            sharedPreferences1.edit().clear().commit();
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }
        else {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new HomeFragment(), Home).commit();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == External_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new ProfileUserFragment(), Profile).commit();
            } else {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            int hasExternalPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasExternalPermission == PackageManager.PERMISSION_GRANTED) {
                getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new ProfileUserFragment(), Profile).commit();
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, External_ID);
            }
        }
        setContentView(R.layout.activity_search_date);
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.action_container, new ProfileUserFragment(), Profile).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((mobile != null && !mobile.isConnected()) && (wifi != null && !wifi.isConnected())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchDateActivity.this);
            View view = getLayoutInflater().inflate(R.layout.alertdialog_network_issue, null);
            builder.setView(view);
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            Button button = view.findViewById(R.id.btn_network);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.setWifiEnabled(true);
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
    }
}

