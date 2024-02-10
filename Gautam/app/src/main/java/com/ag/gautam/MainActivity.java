package com.ag.gautam;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setStatusBarColor(getResources().getColor(R.color.your_status_bar_color));

        // Find the ImageView by its ID
        ImageView gifImageView = findViewById(R.id.gifImageView);

        // Load the GIF and start the animation
        loadImageAndPlayAudio(gifImageView, R.drawable.ajaykaimg);

        // Delay the transition to the next activity after 15 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
                // Finish the current activity
                finish();
            }
        }, 10000); // 15 seconds delay
        // Delay the transition to the next activity after 15 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Apply fade-out animation
                Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                gifImageView.startAnimation(fadeOut);

                // Delay the start of SecondActivity to allow time for the fade-out animation
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Start the next activity with fade-in animation
//                        startActivityWithFade(new Intent(MainActivity.this, SecondActivity.class));
//                        // Finish the current activity
//                        finish();
//                    }
//                }, 500); // Delayed start after fade-out animation duration
            }
        }, 0); // 15 seconds delay
    }


    // Method to load an image using Glide, start the animation, and play audio
    private void loadImageAndPlayAudio(ImageView imageView, int gifResourceId) {
        // Load the GIF
        Glide.with(this)
                .asGif()
                .load(gifResourceId)
                .into(new CustomGifDrawableImageViewTarget(imageView).getView());

        // Start the audio playback
        mediaPlayer = MediaPlayer.create(this, R.raw.ajaykasound);
        mediaPlayer.setLooping(true); // Set looping if needed
        mediaPlayer.start();
    }

    // Custom target to handle GIF drawable and start the animation
    private static class CustomGifDrawableImageViewTarget extends ImageViewTarget<GifDrawable> {

        CustomGifDrawableImageViewTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(@Nullable GifDrawable resource) {
            if (resource != null) {
                view.setImageDrawable(resource);
                resource.start(); // Start the GIF animation
            }
        }
    }
    @SuppressLint("NewApi")
    private void startActivityWithFade(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply fade-in transition for Lollipop and higher versions
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            startActivity(intent, bundle);
        } else {
            // Default transition for lower versions
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
