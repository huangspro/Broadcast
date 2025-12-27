package com.example.broadcast;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendMessage extends AppCompatActivity {
    TextView title, name, time, importance, content;
    OkHttpClient okHttpClient = new OkHttpClient();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.send);

        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        time = findViewById(R.id.time);
        importance = findViewById(R.id.importance);
        content = findViewById(R.id.content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            String year = String.valueOf(now.getYear());
            String month = String.valueOf(now.getMonthValue()); // 1-12
            String day = String.valueOf(now.getDayOfMonth());
            String hour = String.valueOf(now.getHour());
            String minute = String.valueOf(now.getMinute());
            String second = String.valueOf(now.getSecond());
            time.setText(year + "-" + month + "-" + day + "-" + hour + ":" + minute + ":" + second);
        }


    }

    public void send(View v) {
        String Title = title.getText().toString();
        String Name = name.getText().toString();
        String Time = time.getText().toString();
        String Importance = importance.getText().toString();
        String Content = content.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = new FormBody.Builder().add("channel", FileHelper.read(SendMessage.this, "channel.txt").substring(0, 4)).add("message", Title + "#&#&" + Name + "#&#&" + Time + "#&#&" + Importance + "#&#&" + Content + "<hhf>").build();
                Request request = new Request.Builder().url("http://10.21.162.203:45267/sendMessage.php").post(requestBody).build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(() -> Toast.makeText(SendMessage.this, "failed", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(() -> Toast.makeText(SendMessage.this, Title + "#&#&" + Name + "#&#&" + Time + "#&#&" + Importance + "#&#&" + Content + "<hhf>", Toast.LENGTH_LONG).show());
                        finish();
                    }
                });
            }
        }).start();

    }
}