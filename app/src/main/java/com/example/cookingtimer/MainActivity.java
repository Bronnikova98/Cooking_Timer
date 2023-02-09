package com.example.cookingtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button dumplings;
    private Button borsch;
    private Button pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dumplings = findViewById(R.id.button);
        borsch = findViewById(R.id.button2);
        pie = findViewById(R.id.button3);

        dumplings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DumplingsActivity.class);
                startActivity(intent);
            }
        });

        borsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BorschActivity.class);
                startActivity(intent);
            }
        });

        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PieActivity.class);
                startActivity(intent);
            }
        });
    }
}