package com.example.broadcast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Details extends AppCompatActivity {
    String[] items;
    String order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.detail);

        Intent message_detail = getIntent();
        String text = message_detail.getStringExtra("details");
        order = message_detail.getStringExtra("order");
        items = text.split("#&#&");
        getMessageFromText(text);
    }

    public void getMessageFromText(String text) {
        TextView element = findViewById(R.id.message_title);
        element.setText("title: " + items[0]);
        element = findViewById(R.id.send_people);
        element.setText("sender: " + items[1]);
        element = findViewById(R.id.send_time);
        element.setText("time: " + items[2]);
        element = findViewById(R.id.importance);
        element.setText("importance: " + items[3]);
        element = findViewById(R.id.details);
        element.setText(items[4]);
    }

    public void keepUnsee(View v) {
        finish();
    }

    public void see(View v) {
        FileHelper.write(Details.this, "order.txt", order + "#&#&", 1);
        finish();
    }
}
