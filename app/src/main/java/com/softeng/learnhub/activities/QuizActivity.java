package com.softeng.learnhub.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.softeng.learnhub.R;
import com.softeng.learnhub.models.Course;
import com.softeng.learnhub.models.Question;
import com.softeng.learnhub.models.QuizResult;
import com.softeng.learnhub.utils.DataRepository;
import com.softeng.learnhub.utils.PreferencesManager;
import com.softeng.learnhub.utils.SoundManager;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ_RESULT = "extra_quiz_result";

    private static final long TIMER_DURATION_MS = 30000L;
    private static final long TIMER_INTERVAL_MS = 1000L;

    private Course course;
    private List<Question> questions;
    private PreferencesManager prefs;
    private SoundManager soundManager;

    private int currentIndex = 0;
    private int correctCount = 0;
    private boolean answered = false;
    private long quizStartTime;

    private CountDownTimer countDownTimer;
    private long timeLeftMillis = TIMER_DURATION_MS;

    private TextView tvQuestionNumber;
    private TextView tvQuestionText;
    private TextView tvTimer;
    private TextView tvExplanation;

    private ProgressBar progressBar;
    private ProgressBar timerProgress;

    private Button btnOptionA;
    private Button btnOptionB;
    private Button btnOptionC;
    private Button btnOptionD;
    private Button btnNext;

    private CardView cardExplanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferencesManager.getInstance(this);

        if (prefs.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        prefs = PreferencesManager.getInstance(this);
        soundManager = SoundManager.getInstance(prefs);

        int courseId = getIntent().getIntExtra(CourseListActivity.EXTRA_COURSE_ID, -1);
        course = DataRepository.getInstance().getCourseById(courseId);

        if (course == null) {
            Toast.makeText(this, "Course not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        questions = course.getQuestions();

        if (questions == null || questions.isEmpty()) {
            Toast.makeText(this, "No questions available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setupToolbar();
        bindViews();

        quizStartTime = System.currentTimeMillis();
        displayQuestion(currentIndex);
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit Quiz?")
                .setMessage("Your progress will be lost.")
                .setPositiveButton("Quit", (dialog, which) -> {
                    cancelTimer();
                    QuizActivity.super.onBackPressed();
                })
                .setNegativeButton("Continue", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(course.getTitle() + " Quiz");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindViews() {
        tvQuestionNumber = findViewById(R.id.tv_question_number);
        tvQuestionText   = findViewById(R.id.tv_question_text);
        tvTimer          = findViewById(R.id.tv_timer);
        tvExplanation    = findViewById(R.id.tv_explanation);
        progressBar      = findViewById(R.id.progress_questions);
        timerProgress    = findViewById(R.id.progress_timer);
        btnOptionA       = findViewById(R.id.btn_option_a);
        btnOptionB       = findViewById(R.id.btn_option_b);
        btnOptionC       = findViewById(R.id.btn_option_c);
        btnOptionD       = findViewById(R.id.btn_option_d);
        btnNext          = findViewById(R.id.btn_next_question);
        cardExplanation  = findViewById(R.id.card_explanation);

        progressBar.setMax(questions.size());

        btnOptionA.setOnClickListener(v -> handleAnswer(0));
        btnOptionB.setOnClickListener(v -> handleAnswer(1));
        btnOptionC.setOnClickListener(v -> handleAnswer(2));
        btnOptionD.setOnClickListener(v -> handleAnswer(3));

        btnNext.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < questions.size()) {
                displayQuestion(currentIndex);
            } else {
                finishQuiz();
            }
        });
    }

    private void displayQuestion(int index) {
        answered = false;
        cardExplanation.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        timeLeftMillis = TIMER_DURATION_MS;

        Question q = questions.get(index);

        tvQuestionNumber.setText("Question " + (index + 1) + " of " + questions.size());
        tvQuestionText.setText(q.getQuestionText());
        progressBar.setProgress(index + 1);

        String[] opts = q.getOptions();
        btnOptionA.setText("A. " + opts[0]);
        btnOptionB.setText("B. " + opts[1]);
        btnOptionC.setText("C. " + opts[2]);
        btnOptionD.setText("D. " + opts[3]);

        resetButtonColors();
        setOptionsEnabled(true);
        cancelTimer();
        startTimer();
    }

    private void startTimer() {
        timerProgress.setMax((int) (TIMER_DURATION_MS / 1000));
        timerProgress.setProgress((int) (TIMER_DURATION_MS / 1000));

        countDownTimer = new CountDownTimer(TIMER_DURATION_MS, TIMER_INTERVAL_MS) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                int secsLeft = (int) (millisUntilFinished / 1000);
                tvTimer.setText(secsLeft + "s");
                timerProgress.setProgress(secsLeft);

                if (secsLeft <= 10) {
                    tvTimer.setTextColor(Color.parseColor("#F44336"));
                    // Play warning beep every 5 seconds when low
                    if (secsLeft == 10 || secsLeft == 5) {
                        soundManager.playTimerWarning();
                    }
                } else {
                    tvTimer.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onFinish() {
                if (!answered) {
                    tvTimer.setText("0s");
                    soundManager.playWrong();
                    revealAnswer(-1);
                    Toast.makeText(QuizActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void handleAnswer(int selectedIndex) {
        if (answered) return;
        answered = true;
        cancelTimer();
        setOptionsEnabled(false);

        Question q = questions.get(currentIndex);
        boolean correct = q.isCorrect(selectedIndex);

        if (correct) {
            correctCount++;
            soundManager.playCorrect();
        } else {
            soundManager.playWrong();
        }

        revealAnswer(selectedIndex);
    }

    private void revealAnswer(int selectedIndex) {
        Question q = questions.get(currentIndex);
        int correctIdx = q.getCorrectAnswerIndex();

        Button[] buttons = { btnOptionA, btnOptionB, btnOptionC, btnOptionD };

        buttons[correctIdx].setBackgroundColor(Color.parseColor("#4CAF50"));
        buttons[correctIdx].setTextColor(Color.WHITE);

        if (selectedIndex >= 0 && selectedIndex != correctIdx) {
            buttons[selectedIndex].setBackgroundColor(Color.parseColor("#F44336"));
            buttons[selectedIndex].setTextColor(Color.WHITE);
        }

        tvExplanation.setText("Explanation:\n" + q.getExplanation());
        cardExplanation.setVisibility(View.VISIBLE);

        if (currentIndex < questions.size() - 1) {
            btnNext.setText("Next Question");
        } else {
            btnNext.setText("See Results");
        }
        btnNext.setVisibility(View.VISIBLE);
    }

    private void finishQuiz() {
        long timeTaken = System.currentTimeMillis() - quizStartTime;

        QuizResult result = new QuizResult(
                course.getId(),
                course.getTitle(),
                questions.size(),
                correctCount,
                timeTaken
        );

        boolean isNewBest = prefs.saveScore(course.getScoreKey(), result.getScorePercent());
        prefs.incrementQuizzesTaken();

        if (result.getScorePercent() >= 60) {
            prefs.markCourseCompleted(course.getCompletionKey());
        }

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(EXTRA_QUIZ_RESULT, result);
        intent.putExtra("is_new_best", isNewBest);
        startActivity(intent);
        finish();
    }

    private void resetButtonColors() {
        Button[] buttons = { btnOptionA, btnOptionB, btnOptionC, btnOptionD };
        for (Button btn : buttons) {
            btn.setBackgroundColor(Color.parseColor("#E0E0E0"));
            btn.setTextColor(Color.BLACK);
        }
    }

    private void setOptionsEnabled(boolean enabled) {
        btnOptionA.setEnabled(enabled);
        btnOptionB.setEnabled(enabled);
        btnOptionC.setEnabled(enabled);
        btnOptionD.setEnabled(enabled);
    }
}