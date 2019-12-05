package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication.Connected.AppFragments.SearchDateActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("always connected",MODE_PRIVATE);
                if(sharedPreferences.contains("EmailPlusPassword"))
                {
                    Intent intent = new Intent(MainActivity.this, SearchDateActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this,LoadingScreenActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },2000);
    }
}
