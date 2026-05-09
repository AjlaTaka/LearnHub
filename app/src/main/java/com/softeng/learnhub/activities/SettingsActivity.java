package com.softeng.learnhub.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.utils.PreferencesManager;

/**
 * SettingsActivity — user preferences panel.
 *
 * Settings persisted via SharedPreferences:
 *  - Sound effects enabled/disabled
 *  - Text-to-Speech enabled/disabled
 *  - Dark Mode on/off
 *  - Show/hide quiz timer
 *  - Reset all scores
 *
 * Architecture role (MVC): Controller — reads/writes PreferencesManager (Model).
 */
public class SettingsActivity extends AppCompatActivity {

    private PreferencesManager prefs;
    private Switch switchSound, switchTts, switchDarkMode, switchTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferencesManager.getInstance(this);

        setupToolbar();
        bindSwitches();
        bindResetButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindSwitches() {
        switchSound    = findViewById(R.id.switch_sound);
        switchTts      = findViewById(R.id.switch_tts);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        switchTimer    = findViewById(R.id.switch_timer);

        // Load current values from SharedPreferences
        switchSound.setChecked(prefs.isSoundEnabled());
        switchTts.setChecked(prefs.isTtsEnabled());
        switchDarkMode.setChecked(prefs.isDarkMode());
        switchTimer.setChecked(prefs.isTimerShown());

        switchSound.setOnCheckedChangeListener((btn, checked) -> {
            prefs.setSoundEnabled(checked);
            showToast(checked ? "Sound enabled" : "Sound disabled");
        });

        switchTts.setOnCheckedChangeListener((btn, checked) -> {
            prefs.setTtsEnabled(checked);
            showToast(checked ? "Text-to-Speech enabled" : "Text-to-Speech disabled");
        });

        switchDarkMode.setOnCheckedChangeListener((btn, checked) -> {
            prefs.setDarkMode(checked);
            // Apply dark mode immediately
            AppCompatDelegate.setDefaultNightMode(
                    checked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );
        });

        switchTimer.setOnCheckedChangeListener((btn, checked) -> {
            prefs.setTimerShown(checked);
            showToast(checked ? "Quiz timer shown" : "Quiz timer hidden");
        });
    }

    private void bindResetButton() {
        CardView btnReset = findViewById(R.id.card_reset_scores);
        btnReset.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Reset All Scores")
                    .setMessage("This will permanently delete all your quiz scores and completion badges. This cannot be undone.")
                    .setPositiveButton("Reset", (d, w) -> {
                        prefs.resetAllScores();
                        Toast.makeText(this, "All scores reset.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
