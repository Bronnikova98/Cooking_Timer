package com.example.cookingtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;

public class DumplingsActivity extends AppCompatActivity {

    private TextView timerView;
    private MyTimer timer;
    private Button start;
    private Button reset;
    private EditText myTimeText;
    private TextView exceptionText;
    private static final int NOTIFY_ID = 100;
    private Handler handler;
    private NotificationManager notificationManager;
    private final String CHANEL = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumplings);

        timerView = findViewById(R.id.textView8);
        start = findViewById(R.id.button4);
        reset = findViewById(R.id.button5);
        myTimeText = findViewById(R.id.myTime);
        exceptionText = findViewById(R.id.exeText);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null) {
                    timer.cancel();
                }
                String time_str = timerView.getText().toString();
                String[] arr = time_str.split(":");
                try{
                    int min = Integer.parseInt(arr[0]);
                    int sec = Integer.parseInt(arr[1]);
                    long mils = (min * 60 + sec) * 1000;
                    timer = new MyTimer(mils, 1000);
                    timer.start();
                }catch (NumberFormatException nfe){
                    exceptionText.setVisibility(View.VISIBLE);
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("Начать");
                start.getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                timerView.setText("00:00");
                timerView.setTextColor(Color.parseColor("#4CAF50"));
                if (timer != null){
                    timer.cancel();}
                timerView.setText("00:10");

                exceptionText.setVisibility(View.INVISIBLE);

            }
        });

        myTimeText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (timer != null){
                    timer.cancel();}

                timerView.setText(s.toString());
            }

        });

        handler = new Handler();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANEL, "Main chanel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }

    }

    public class MyTimer extends CountDownTimer {
        public MyTimer(long millisInFuture, long contDownInInterval){
            super(millisInFuture,contDownInInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int sec = (int) Math. ceil(millisUntilFinished/1000);
            int min = (int) Math.floor(sec/60);
            sec -= min * 60;

            timerView.setText(String.format("%02d:%02d", min,sec));
        }

        @Override
        public void onFinish() {
            timerView.setText("00:00");
            timerView.setTextColor(Color.parseColor("#873939"));
            start.setText("Готово!");
            start.getBackground().setColorFilter(Color.parseColor("#873939"), PorterDuff.Mode.MULTIPLY);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent notificationIntent = new Intent(DumplingsActivity.this, DumplingsActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(DumplingsActivity.this, 0, notificationIntent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DumplingsActivity.this, CHANEL)
                            .setSmallIcon(R.drawable.timericon)
                            .setContentTitle("Время вышло")
                            .setChannelId(CHANEL)
                            .setContentText("Пора переходить к следующему шагу")
                            .setContentIntent(contentIntent)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

                    Notification notification = builder.build();
                    notification.defaults = Notification.DEFAULT_ALL;

                    notificationManager.notify(NOTIFY_ID, builder.build());
                }
            }, 5000);

        }
    }
}