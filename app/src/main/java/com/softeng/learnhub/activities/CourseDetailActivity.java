package com.softeng.learnhub.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.models.Lesson;
import com.softeng.learnhub.utils.DataRepository;
import com.softeng.learnhub.utils.PreferencesManager;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.List;
import java.util.Locale;

/**
 * CourseDetailActivity — shows the lessons for a chosen course.
 *
 * Features:
 *  - Displays up to 4 lessons with expandable detail cards
 *  - Text-to-Speech (TTS) button reads the current lesson aloud
 *    (Android hardware feature — TTS engine)
 *  - "Start Quiz" button navigates to QuizActivity
 *
 * Android hardware feature: TextToSpeech (TTS)
 * Architecture role (MVC): Controller.
 */
public class CourseDetailActivity extends AppCompatActivity {

    // ── TTS Engine ────────────────────────────────────────────────────────────
    private TextToSpeech tts;
    private boolean ttsReady = false;

    private Course course;
    private PreferencesManager prefs;
    private int currentLessonIndex = 0;

    // Views
    private TextView tvCourseTitle, tvCourseDescription, tvBestScore;
    private TextView tvLessonTitle, tvLessonContent, tvLessonCode;
    private CardView cardCode;
    private Button btnPrevLesson, btnNextLesson, btnStartQuiz;
    private ImageButton btnTts;
    private TextView tvLessonProgress;

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
        setContentView(R.layout.activity_course_detail);

        prefs = PreferencesManager.getInstance(this);

        // Retrieve course from Intent
        int courseId = getIntent().getIntExtra(CourseListActivity.EXTRA_COURSE_ID, 0);
        course = DataRepository.getInstance().getCourseById(courseId);

        if (course == null) {
            Toast.makeText(this, "Course not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        bindViews();
        initTts();
        displayLesson(0);
    }

    @Override
    protected void onDestroy() {
        // Always shut down TTS to release engine resources
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // ── Toolbar ───────────────────────────────────────────────────────────────

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(course.getTitle());
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

    // ── View Binding ──────────────────────────────────────────────────────────

    private void bindViews() {
        tvCourseTitle       = findViewById(R.id.tv_course_title);
        tvCourseDescription = findViewById(R.id.tv_course_description);
        tvBestScore         = findViewById(R.id.tv_best_score);
        tvLessonTitle       = findViewById(R.id.tv_lesson_title);
        tvLessonContent     = findViewById(R.id.tv_lesson_content);
        tvLessonCode        = findViewById(R.id.tv_lesson_code);
        cardCode            = findViewById(R.id.card_code_snippet);
        tvLessonProgress    = findViewById(R.id.tv_lesson_progress);
        btnPrevLesson       = findViewById(R.id.btn_prev_lesson);
        btnNextLesson       = findViewById(R.id.btn_next_lesson);
        btnStartQuiz        = findViewById(R.id.btn_start_quiz);
        btnTts              = findViewById(R.id.btn_tts);

        tvCourseTitle.setText(course.getTitle());
        tvCourseDescription.setText(course.getDescription());

        int best = prefs.getBestScore(course.getScoreKey());
        tvBestScore.setText(best > 0 ? "Best: " + best + "%" : "Not attempted yet");

        // Lesson navigation
        btnPrevLesson.setOnClickListener(v -> navigateLesson(-1));
        btnNextLesson.setOnClickListener(v -> navigateLesson(1));

        // Quiz navigation — explicit Intent to QuizActivity
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(CourseListActivity.EXTRA_COURSE_ID, course.getId());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // TTS button
        btnTts.setOnClickListener(v -> speakCurrentLesson());
    }

    // ── Lesson Display ────────────────────────────────────────────────────────

    private void displayLesson(int index) {
        List<Lesson> lessons = course.getLessons();
        if (lessons == null || lessons.isEmpty()) return;

        currentLessonIndex = Math.max(0, Math.min(index, lessons.size() - 1));
        Lesson lesson = lessons.get(currentLessonIndex);

        tvLessonTitle.setText("Lesson " + (currentLessonIndex + 1) + ": " + lesson.getTitle());
        tvLessonContent.setText(lesson.getContent());
        tvLessonProgress.setText((currentLessonIndex + 1) + " / " + lessons.size());

        if (lesson.hasCodeSnippet()) {
            cardCode.setVisibility(View.VISIBLE);
            tvLessonCode.setText(lesson.getCodeSnippet());
        } else {
            cardCode.setVisibility(View.GONE);
        }

        btnPrevLesson.setEnabled(currentLessonIndex > 0);
        btnNextLesson.setEnabled(currentLessonIndex < lessons.size() - 1);
    }

    private void navigateLesson(int direction) {
        // Stop TTS before navigating
        if (tts != null && ttsReady) {
            tts.stop();
        }
        displayLesson(currentLessonIndex + direction);
    }

    // ── Text-to-Speech ────────────────────────────────────────────────────────

    /**
     * Initialises the Android TTS engine.
     * TTS is an Android hardware/OS feature that synthesises speech.
     */
    private void initTts() {
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int langResult = tts.setLanguage(Locale.US);
                if (langResult == TextToSpeech.LANG_MISSING_DATA
                        || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    btnTts.setEnabled(false);
                    Toast.makeText(this, "TTS language not supported on this device.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ttsReady = true;
                }
            } else {
                btnTts.setEnabled(false);
                Toast.makeText(this, "TTS initialisation failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Reads the current lesson title and content aloud using TTS.
     */
    private void speakCurrentLesson() {
        if (!prefs.isTtsEnabled()) {
            Toast.makeText(this, "TTS is disabled in Settings.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ttsReady) {
            Toast.makeText(this, "TTS not ready.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Stop any ongoing speech
        tts.stop();

        List<Lesson> lessons = course.getLessons();
        if (lessons == null || lessons.isEmpty()) return;

        Lesson lesson = lessons.get(currentLessonIndex);
        // Build speech text (strip code snippet — TTS reads it poorly)
        String speechText = "Lesson " + (currentLessonIndex + 1) + ". "
                + lesson.getTitle() + ". " + lesson.getContent();

        tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, "lesson_tts");
        Toast.makeText(this, "Reading lesson aloud…", Toast.LENGTH_SHORT).show();
    }
}
