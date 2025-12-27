package com.example.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetMessage extends Service {
    OkHttpClient client = new OkHttpClient();
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        mediaPlayer=MediaPlayer.create(this,R.raw.alert);
        mediaPlayer.setLooping(true);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel c = new NotificationChannel("foreground_haha", "nitification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager m = getSystemService(NotificationManager.class);
            m.createNotificationChannel(c);
        }
    }

    @Override
    public int onStartCommand(Intent i, int d, int s) {
        Notification n = new NotificationCompat.Builder(this, "foreground_haha").setContentTitle("Click to view message").setContentText("Receiving data").setSmallIcon(R.drawable.noti).setOngoing(true).build();
        startForeground(1, n);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (!FileHelper.read(GetMessage.this, "channel.txt").substring(0,4).equals("") && !FileHelper.read(GetMessage.this, "channel.txt").substring(0,4).equals("not exist")) {
                            getMessageFromServer(FileHelper.read(GetMessage.this, "channel.txt").substring(0,4));
                            if(mediaPlayer.isPlaying()){Thread.sleep(10000);mediaPlayer.stop();}
                            else Thread.sleep(3000);
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return START_STICKY;
    }

    void getMessageFromServer(String channel) throws IOException {
        Request request = new Request.Builder()
                .url("http://10.21.162.203:45267/" + channel + ".txt")
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res=response.body().string();
            if(!response.isSuccessful())FileHelper.write(this, "message.txt", "failed#&#&failed#&#&failed#&#&3#&#&failed", 0);
            if(response.code()==404)FileHelper.write(this, "message.txt", "channel not found#&#&failed#&#&failed#&#&3#&#&failed", 0);
            else FileHelper.write(this, "message.txt", res, 0);
            if(res.contains("#&#&3#&#&")){
                if(!mediaPlayer.isPlaying())mediaPlayer.start();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer=null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent i) {
        return null;
    }
}
