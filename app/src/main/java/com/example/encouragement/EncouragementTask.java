package com.example.encouragement;

import android.media.MediaPlayer;
import android.util.Log;

import java.util.Random;

/**
 * Plays encouragement messages
 */
public class EncouragementTask implements Runnable {

    /** Activity tag for logging statements */
    private static final String ActivityTag = "EncouragementTask";

    /** Pre-loaded encouragement sounds */
    private static MediaPlayer[] encourage;
    /** Random number generator */
    private static Random random = new Random();

    /**
     * Creates a new instance of EncouragementTask.
     */
    public EncouragementTask() {
    }

    /**
     * Creates a new instance of EncouragementTask. Sets the encouragement sound effects.
     * @param encouragements
     */
    public EncouragementTask(MediaPlayer[] encouragements) {
        this.encourage = encouragements;
    }

    /**
     * Performs an encouragement
     */
    @Override
    public void run() {
        Log.i(ActivityTag, "Playing encouragement message");
        int index = random.nextInt(encourage.length);
        encourage[index].start();
    }

    /**
     * Sets the encouragement sound effects
     *
     * @param encouragements an array of MediaPlayer objects containing the encouragement sound effects
     */
    public void setEncouragements(MediaPlayer[] encouragements) {
        this.encourage = encouragements;
    }
}
