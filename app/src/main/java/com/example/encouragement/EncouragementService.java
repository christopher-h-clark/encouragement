package com.example.encouragement;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;

import java.util.Random;

/**
 * Handles asynchronous encouragement tasks on a separate thread from the UI
 */
public class EncouragementService extends IntentService {
    /** Action name to initialize service */
    public static final String Init = "com.example.encouragement.action.Init";
    /** Action name to start service */
    public static final String Start = "com.example.encouragement.action.Start";
    /** Action name to stop service */
    public static final String Stop = "com.example.encouragement.action.Stop";

    private static Random random = new Random();
    private static Handler encourageHandler = new Handler();
    private static Runnable encourageAction;
    private static MediaPlayer[] encourage = new MediaPlayer[3];

    /**
     * Creates a new instance of EncouragementService
     */
    public EncouragementService() {
        super("EncouragementService");
    }

    /**
     * Handles intents sent to this service
     *
     * @param intent the intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            final String action = intent.getAction();
//            if(Init.equals(action)) {
//                handleInit();
//                handleStart();
//            } else if(Start.equals(action)) {
//                handleStart();
//            } else if (Stop.equals(action)) {
//                handleStop();
//            }
//        }
    }

    /**
     * Initializes the encouragement sounds
     */
//    private void handleInit() {
//        encourage[0] = MediaPlayer.create(this, R.raw.keep_going);
//        encourage[1] = MediaPlayer.create(this, R.raw.next_step);
//        encourage[2] = MediaPlayer.create(this, R.raw.youre_smart_you_can_do_it);
//    }

    /**
     * Starts the encouragement timer
     */
//    private void handleStart() {
//        final long encourageInterval = Configuration.getEncouragementInterval().getMillis();
//        encourageAction = new Runnable() {
//            @Override
//            public void run() {
//                int index = random.nextInt(encourage.length);
//                encourage[index].start();
//                encourageHandler.postDelayed(encourageAction, encourageInterval);
//            }
//        };
//        encourageHandler.postDelayed(encourageAction, encourageInterval);
//    }

    /**
     * Stops the encouragements
     */
//    private void handleStop() {
//        encourageHandler.removeCallbacks(encourageAction);
//    }
}
