package com.ljwx.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClickAreaImageView view = findViewById(R.id.img);
        view.setOnClickListenerTag1(new ClickAreaImageView.TouchAreaListener() {
            @Override
            public void call() {
                Toast.makeText(MainActivity.this, "tag1", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
