package com.softeng.learnhub.models;

import java.io.Serializable;

/**
 * Represents a single multiple-choice quiz question.
 * Contains the question text, four options, the index of the correct answer (0-3),
 * and an explanation shown after the user answers.
 */
public class Question implements Serializable {

    private String questionText;
    private String[] options;          // Always exactly 4 options
    private int correctAnswerIndex;    // 0 = A, 1 = B, 2 = C, 3 = D
    private String explanation;        // Shown as feedback after answering

    public Question(String questionText, String[] options,
                    int correctAnswerIndex, String explanation) {
        if (options.length != 4) {
            throw new IllegalArgumentException("A question must have exactly 4 options.");
        }
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getQuestionText()       { return questionText; }
    public String[] getOptions()          { return options; }
    public int getCorrectAnswerIndex()    { return correctAnswerIndex; }
    public String getCorrectAnswer()      { return options[correctAnswerIndex]; }
    public String getExplanation()        { return explanation; }

    /**
     * Checks whether the given option index is the correct answer.
     */
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }
}
