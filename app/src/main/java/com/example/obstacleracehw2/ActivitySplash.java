package com.example.obstacleracehw2;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class ActivitySplash extends AppCompatActivity {
    private ImageView splash_IMG_car;
    private static final long ANIM_DUR = 3000;
    private MediaPlayer engineSound;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViews();
        splash_IMG_car.setVisibility(View.INVISIBLE);
        showCarMoving(splash_IMG_car);
    }

    private void showCarMoving(final ImageView v) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(height);
        v.setScaleX(1.0f);
        v.setScaleY(1.0f);
        v.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationY(-height)
                .setDuration(ANIM_DUR)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }


                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        syncronizeCarAndSound();
    }

    private void findViews() {
        splash_IMG_car = findViewById(R.id.splash_IMG_car);
        engineSound = MediaPlayer.create(ActivitySplash.this, R.raw.engine_sound_efect);
    }

    private void animationDone() {
        Intent intent = new Intent(this, ActivityMenu.class);
        startActivity(intent);
        finish();
    }

    private void syncronizeCarAndSound() {
        handler.postDelayed(new Runnable() {
            public void run() {
                engineSound.start();
            }
        }, 1000);
    }
}