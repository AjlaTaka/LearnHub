package com.softeng.learnhub.utils;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class SoundManager {

    private static SoundManager instance;
    private final PreferencesManager prefs;
    private static final int VOLUME = 80;

    private SoundManager(PreferencesManager prefs) {
        this.prefs = prefs;
    }

    public static SoundManager getInstance(PreferencesManager prefs) {
        if (instance == null) {
            instance = new SoundManager(prefs);
        }
        return instance;
    }

    public void playCorrect() {
        if (!prefs.isSoundEnabled()) return;
        new Thread(() -> {
            try {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, VOLUME);
                tg.startTone(ToneGenerator.TONE_PROP_BEEP, 120);
                Thread.sleep(150);
                tg.startTone(ToneGenerator.TONE_PROP_BEEP, 180);
                Thread.sleep(200);
                tg.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playWrong() {
        if (!prefs.isSoundEnabled()) return;
        new Thread(() -> {
            try {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, VOLUME);
                tg.startTone(ToneGenerator.TONE_PROP_NACK, 400);
                Thread.sleep(450);
                tg.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playVictory() {
        if (!prefs.isSoundEnabled()) return;
        new Thread(() -> {
            try {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, VOLUME);
                int[] tones = {
                        ToneGenerator.TONE_PROP_ACK,
                        ToneGenerator.TONE_PROP_BEEP,
                        ToneGenerator.TONE_PROP_BEEP2,
                        ToneGenerator.TONE_PROP_BEEP
                };
                for (int tone : tones) {
                    tg.startTone(tone, 120);
                    Thread.sleep(150);
                }
                tg.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void playTimerWarning() {
        if (!prefs.isSoundEnabled()) return;
        new Thread(() -> {
            try {
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 50);
                tg.startTone(ToneGenerator.TONE_PROP_ACK, 80);
                Thread.sleep(100);
                tg.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}