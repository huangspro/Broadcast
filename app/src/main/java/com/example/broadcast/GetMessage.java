package com.example.broadcast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
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

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
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
        Notification n = new NotificationCompat.Builder(this, "foreground_haha").setContentTitle("running").setContentText("this is a service").setSmallIcon(R.drawable.noti).setOngoing(true).build();
        startForeground(1, n);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (!FileHelper.read(GetMessage.this, "channel.txt").substring(0,4).equals("") && !FileHelper.read(GetMessage.this, "channel.txt").substring(0,4).equals("not exist")) {
                            getMessageFromServer(FileHelper.read(GetMessage.this, "channel.txt").substring(0,4));
                            Thread.sleep(3000);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        return START_STICKY;
    }

    boolean getMessageFromServer(String channel) throws IOException {
        Request request = new Request.Builder()
                .url("http://10.21.162.203:45267/" + channel + ".txt")
                .build();
        try (Response response = client.newCall(request).execute()) {
            FileHelper.write(this, "message.txt", response.body().string(), 0);
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent i) {
        return null;
    }
}
