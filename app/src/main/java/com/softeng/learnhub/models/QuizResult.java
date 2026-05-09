package com.softeng.learnhub.models;

import java.io.Serializable;

/**
 * Holds the outcome of a completed quiz session.
 * Passed from QuizActivity to ResultActivity via Intent extras.
 */
public class QuizResult implements Serializable {

    private int courseId;
    private String courseName;
    private int totalQuestions;
    private int correctAnswers;
    private long timeTakenMillis;

    public QuizResult(int courseId, String courseName,
                      int totalQuestions, int correctAnswers, long timeTakenMillis) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.timeTakenMillis = timeTakenMillis;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getCourseId()          { return courseId; }
    public String getCourseName()     { return courseName; }
    public int getTotalQuestions()    { return totalQuestions; }
    public int getCorrectAnswers()    { return correctAnswers; }
    public long getTimeTakenMillis()  { return timeTakenMillis; }

    /**
     * Returns score as a percentage (0–100).
     */
    public int getScorePercent() {
        if (totalQuestions == 0) return 0;
        return (int) Math.round((correctAnswers * 100.0) / totalQuestions);
    }

    /**
     * Returns a letter grade based on the percentage score.
     */
    public String getLetterGrade() {
        int pct = getScorePercent();
        if (pct >= 90) return "A";
        if (pct >= 80) return "B";
        if (pct >= 70) return "C";
        if (pct >= 60) return "D";
        return "F";
    }

    /**
     * Returns a motivational message based on performance.
     */
    public String getFeedbackMessage() {
        int pct = getScorePercent();
        if (pct == 100) return "Perfect Score! Outstanding work! 🎉";
        if (pct >= 80)  return "Excellent! You have a strong grasp of this topic!";
        if (pct >= 60)  return "Good effort! Review the explanations and try again.";
        if (pct >= 40)  return "Keep practicing! Re-read the lessons and retry.";
        return "Don't give up! Study the material carefully and try again.";
    }

    /**
     * Returns formatted time string "M:SS".
     */
    public String getFormattedTime() {
        long seconds = timeTakenMillis / 1000;
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}
