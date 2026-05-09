package com.softeng.learnhub.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesManager — centralized SharedPreferences helper.
 *
 * Handles all data persistence for:
 *  - User profile (name, student ID)
 *  - Quiz scores per course (best score)
 *  - App settings (sound, TTS, dark mode)
 *  - Course completion status
 *
 * Architecture role (MVC): part of the Model layer.
 */
public class PreferencesManager {

    // SharedPreferences file names
    private static final String PREF_USER    = "learnhub_user";
    private static final String PREF_SCORES  = "learnhub_scores";
    private static final String PREF_SETTINGS = "learnhub_settings";

    // User keys
    private static final String KEY_USERNAME    = "username";
    private static final String KEY_STUDENT_ID  = "student_id";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_TOTAL_QUIZZES = "total_quizzes_taken";

    // Settings keys
    public static final String KEY_SOUND_ENABLED = "sound_enabled";
    public static final String KEY_TTS_ENABLED    = "tts_enabled";
    public static final String KEY_DARK_MODE      = "dark_mode";
    public static final String KEY_SHOW_TIMER     = "show_timer";

    private static PreferencesManager instance;
    private final SharedPreferences userPrefs;
    private final SharedPreferences scorePrefs;
    private final SharedPreferences settingsPrefs;

    // ── Singleton ─────────────────────────────────────────────────────────────

    private PreferencesManager(Context context) {
        Context appCtx = context.getApplicationContext();
        userPrefs     = appCtx.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
        scorePrefs    = appCtx.getSharedPreferences(PREF_SCORES, Context.MODE_PRIVATE);
        settingsPrefs = appCtx.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesManager(context);
        }
        return instance;
    }

    // ── User Profile ──────────────────────────────────────────────────────────

    public String getUsername() {
        return userPrefs.getString(KEY_USERNAME, "Student");
    }

    public void setUsername(String name) {
        userPrefs.edit().putString(KEY_USERNAME, name).apply();
    }

    public String getStudentId() {
        return userPrefs.getString(KEY_STUDENT_ID, "");
    }

    public void setStudentId(String id) {
        userPrefs.edit().putString(KEY_STUDENT_ID, id).apply();
    }

    public boolean isFirstLaunch() {
        return userPrefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void setFirstLaunchComplete() {
        userPrefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    public int getTotalQuizzesTaken() {
        return userPrefs.getInt(KEY_TOTAL_QUIZZES, 0);
    }

    public void incrementQuizzesTaken() {
        int current = getTotalQuizzesTaken();
        userPrefs.edit().putInt(KEY_TOTAL_QUIZZES, current + 1).apply();
    }

    // ── Score Management ──────────────────────────────────────────────────────

    /**
     * Saves a new score for a course if it is better than the current best.
     * @param courseScoreKey  The key returned by Course.getScoreKey()
     * @param score           The percentage score (0–100)
     * @return true if this is a new best score
     */
    public boolean saveScore(String courseScoreKey, int score) {
        int current = getBestScore(courseScoreKey);
        if (score > current) {
            scorePrefs.edit().putInt(courseScoreKey, score).apply();
            return true;
        }
        return false;
    }

    /**
     * Returns the best score percentage for a course (0 if never taken).
     */
    public int getBestScore(String courseScoreKey) {
        return scorePrefs.getInt(courseScoreKey, 0);
    }

    /**
     * Returns the sum of all best scores across all 5 courses (max 500).
     */
    public int getTotalScore() {
        int total = 0;
        for (int i = 0; i < 5; i++) {
            total += getBestScore("score_course_" + i);
        }
        return total;
    }

    /**
     * Mark a course as completed.
     */
    public void markCourseCompleted(String completionKey) {
        scorePrefs.edit().putBoolean(completionKey, true).apply();
    }

    /**
     * Check if a course has been completed (scored >= 60%).
     */
    public boolean isCourseCompleted(String completionKey) {
        return scorePrefs.getBoolean(completionKey, false);
    }

    // ── Settings ──────────────────────────────────────────────────────────────

    public boolean isSoundEnabled() {
        return settingsPrefs.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        settingsPrefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public boolean isTtsEnabled() {
        return settingsPrefs.getBoolean(KEY_TTS_ENABLED, true);
    }

    public void setTtsEnabled(boolean enabled) {
        settingsPrefs.edit().putBoolean(KEY_TTS_ENABLED, enabled).apply();
    }

    public boolean isDarkMode() {
        return settingsPrefs.getBoolean(KEY_DARK_MODE, false);
    }

    public void setDarkMode(boolean enabled) {
        settingsPrefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isTimerShown() {
        return settingsPrefs.getBoolean(KEY_SHOW_TIMER, true);
    }

    public void setTimerShown(boolean shown) {
        settingsPrefs.edit().putBoolean(KEY_SHOW_TIMER, shown).apply();
    }

    // ── Reset ─────────────────────────────────────────────────────────────────

    /**
     * Clears all scores (for testing or reset purposes).
     */
    public void resetAllScores() {
        scorePrefs.edit().clear().apply();
    }
}
