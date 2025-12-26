package com.example.broadcast;

/*
This app is a message app
the app receive message from the server and the storage them into the file system
when user open the app, firstly all the message will be loaded into a container only with their names
when user click one message, a new Activity will be created to show the details

a message contains images, text, or video
*/

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    LinearLayout message_container;//All the messages will be stored in the container
    EditText chan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main);
        message_container = findViewById(R.id.message);
        chan = findViewById(R.id.channel);

        if (FileHelper.read(this, "channel.txt").equals("not exist"))
            FileHelper.write(this, "channel.txt", "", 0);
        else chan.setText(FileHelper.read(this, "channel.txt"));

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(new Intent(this, GetMessage.class));
        else startService(new Intent(this, GetMessage.class));

        Handler handler=new Handler(Looper.getMainLooper());
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                getMessage();
                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);
    }

    //This function add a message into the whole message_container
    public void add_message(String name, String importance, Intent details) {
        //create a new container a piece of message
        LinearLayout newonel = new LinearLayout(this);
        LinearLayout.LayoutParams paramsl = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsl.setMargins(40, 40, 40, 40);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.TRANSPARENT);
        border.setCornerRadius(15);
        border.setStroke(5, Objects.equals(importance, "3") ? Color.RED : (Objects.equals(importance, "2") ? Color.GREEN : Color.GRAY));
        newonel.setBackground(border);
        newonel.setLayoutParams(paramsl);

        //add a listener to the container
        newonel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(details);
            }
        });

        //add the neme of the message into the container
        TextView newone = new TextView(this);
        newone.setText(name);
        newone.setTextSize(18);
        newone.setTextColor(Objects.equals(importance, "3") ? Color.RED : (Objects.equals(importance, "2") ? Color.GREEN : Color.GRAY));
        newone.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newone.setLayoutParams(params);

        newonel.addView(newone);
        message_container.addView(newonel);
    }

    public void commit(View v) {
        FileHelper.write(this, "channel.txt", chan.getText().toString().substring(0,4), 0);
    }

    //this function is to get the files from phone and get the messages from the files
    public void getMessage() {
        message_container.removeAllViews();
        String[] all = FileHelper.read(this, "message.txt").split("<hhf>");
        String[] allOrder = FileHelper.read(this, "order.txt").split("#&#&");
        int t = 1;
        for (String i : all) {
            String[] tem=i.split("#&#&");
            if(tem.length!=5)continue;
            if (!find(allOrder, String.valueOf(t))) {
                Intent hha = new Intent(MainActivity.this, Details.class);
                hha.putExtra("details", i);
                hha.putExtra("order", String.valueOf(t));
                add_message(tem[0], tem[3], hha);
            }
            t++;
        }
    }

    public void recover(View v) {
        FileHelper.write(MainActivity.this, "order.txt", "", 0);
    }

    private boolean find(String[] a, String b) {
        for (String i : a) {
            if (b.equals(i)) return true;
        }
        return false;
    }

    public void hi(Context c, String a,int mode) {
        Toast.makeText(c, a, mode==0?Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}