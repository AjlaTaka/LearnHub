package com.softeng.learnhub.models;

import java.io.Serializable;
import java.util.List;

/**
 * Model class representing a Software Engineering course.
 * Implements Serializable so it can be passed between Activities via Intents.
 * Follows MVC pattern — pure data, no UI logic.
 */
public class Course implements Serializable {

    // Course identifiers (used as SharedPreferences keys)
    public static final int COURSE_DSA      = 0;
    public static final int COURSE_OS       = 1;
    public static final int COURSE_DB       = 2;
    public static final int COURSE_OOP      = 3;
    public static final int COURSE_NETWORKS = 4;

    private int id;
    private String title;
    private String subtitle;
    private String description;
    private String iconName;      // drawable resource name
    private int colorResId;       // accent color resource
    private int totalLessons;
    private List<Lesson> lessons;
    private List<Question> questions;

    public Course(int id, String title, String subtitle, String description,
                  String iconName, int colorResId, int totalLessons) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.iconName = iconName;
        this.colorResId = colorResId;
        this.totalLessons = totalLessons;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getId()              { return id; }
    public String getTitle()        { return title; }
    public String getSubtitle()     { return subtitle; }
    public String getDescription()  { return description; }
    public String getIconName()     { return iconName; }
    public int getColorResId()      { return colorResId; }
    public int getTotalLessons()    { return totalLessons; }
    public List<Lesson> getLessons()         { return lessons; }
    public List<Question> getQuestions()     { return questions; }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setLessons(List<Lesson> lessons)         { this.lessons = lessons; }
    public void setQuestions(List<Question> questions)   { this.questions = questions; }

    /**
     * Returns a SharedPreferences key for storing this course's best score.
     * Format: "score_course_{id}"
     */
    public String getScoreKey() {
        return "score_course_" + id;
    }

    /**
     * Returns a SharedPreferences key for storing completion status.
     */
    public String getCompletionKey() {
        return "completed_course_" + id;
    }
}
