package com.example.Audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class AudioService extends Service {


    public static String MESSAGE_KEY;
    String storageUri;

    MediaPlayer mediaPlayer  = new MediaPlayer();
    public AudioService() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this,"서비스 시작",Toast.LENGTH_LONG).show();
        boolean message  = intent.getExtras().getBoolean(AudioService.MESSAGE_KEY);
        storageUri= intent.getStringExtra("voicePath");
        if(message){
            try {
                mediaPlayer.setDataSource(storageUri);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mediaPlayer.stop();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
