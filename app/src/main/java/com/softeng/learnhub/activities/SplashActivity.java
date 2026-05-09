package com.softeng.learnhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.softeng.learnhub.R;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * SplashActivity — the launcher entry point of LearnHub.
 *
 * Responsibilities:
 *  1. Show the app logo with entrance animations
 *  2. Wait 2 seconds (brand impression time)
 *  3. Navigate to MainActivity via explicit Intent
 *
 * No back-stack entry is kept (finish() is called).
 * Architecture role (MVC): Controller — minimal logic, delegates to prefs/model.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION_MS = 2200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferencesManager prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // ── Animate logo and tagline ──────────────────────────────────────────

        ImageView logo     = findViewById(R.id.splash_logo);
        TextView  appName  = findViewById(R.id.splash_app_name);
        TextView  tagline  = findViewById(R.id.splash_tagline);

        // Scale + fade in for logo
        AnimationSet logoAnim = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(700);
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(700);
        logoAnim.addAnimation(scale);
        logoAnim.addAnimation(fadeIn);
        logo.startAnimation(logoAnim);

        // Fade in text with delay
        AlphaAnimation textFade = new AlphaAnimation(0f, 1f);
        textFade.setDuration(600);
        textFade.setStartOffset(600);
        textFade.setFillAfter(true);
        appName.startAnimation(textFade);

        AlphaAnimation taglineFade = new AlphaAnimation(0f, 1f);
        taglineFade.setDuration(600);
        taglineFade.setStartOffset(900);
        taglineFade.setFillAfter(true);
        tagline.startAnimation(taglineFade);

        // ── Delayed navigation ────────────────────────────────────────────────

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            // Finish so user cannot press Back to return to splash
            finish();
            // Smooth transition
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, SPLASH_DURATION_MS);
    }
}
