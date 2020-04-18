package com.example.myweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import pl.droidsonroids.gif.GifImageView;


public class SplashScreen extends AppCompatActivity {

    TextView view;
    ProgressBar bar;
    GifImageView gifImageView;
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        view=findViewById(R.id.txt_splash);
        bar=findViewById(R.id.progressBar);
        gifImageView=findViewById(R.id.gifView);

        new CountDownTimer(6000,60) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter++;
                view.setText(counter + " %");
            }

            @Override
            public void onFinish() {

                view.setText("Complete");
                bar.setVisibility(View.GONE);
                gifImageView.setImageResource(R.drawable.change);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        SplashScreen.this.startActivity(intent);
                        SplashScreen.this.finish();
                    }
                }, 6000);
            }
        }.start();
    }
}

