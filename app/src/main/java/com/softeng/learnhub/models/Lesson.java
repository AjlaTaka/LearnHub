package com.softeng.learnhub.models;

import java.io.Serializable;

/**
 * Represents a single lesson/topic within a course.
 * Contains title, a detailed content string, and an optional code snippet.
 */
public class Lesson implements Serializable {

    private String title;
    private String content;
    private String codeSnippet;  // optional — shown in a monospace code block
    private boolean isCompleted;

    public Lesson(String title, String content, String codeSnippet) {
        this.title = title;
        this.content = content;
        this.codeSnippet = codeSnippet;
        this.isCompleted = false;
    }

    public Lesson(String title, String content) {
        this(title, content, null);
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public String getTitle()           { return title; }
    public String getContent()         { return content; }
    public String getCodeSnippet()     { return codeSnippet; }
    public boolean hasCodeSnippet()    { return codeSnippet != null && !codeSnippet.isEmpty(); }
    public boolean isCompleted()       { return isCompleted; }
    public void setCompleted(boolean c){ this.isCompleted = c; }
}
