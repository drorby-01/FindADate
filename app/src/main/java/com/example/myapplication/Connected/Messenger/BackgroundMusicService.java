package com.example.myapplication.Connected.Messenger;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class BackgroundMusicService extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.imagine);
            mediaPlayer.setVolume((float)0.5,(float)0.5);
            mediaPlayer.setLooping(true);

        }
        SharedPreferences sharedPreferences = getSharedPreferences("song", MODE_PRIVATE);
        int length = sharedPreferences.getInt("stop", 0);


        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int length =mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        SharedPreferences sharedPreferences= getSharedPreferences("song",MODE_PRIVATE);
        SharedPreferences.Editor edit =sharedPreferences.edit();
        edit.putInt("stop",length);
        edit.commit();
    }
}
