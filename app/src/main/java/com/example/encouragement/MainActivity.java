package com.example.encouragement;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity
        extends AppCompatActivity
        /* implements SharedPreferences.OnSharedPreferenceChangeListener */ {

    /** Activity tag for log statements */
    private static final String ActivityTag = "MainActivity";

    private static final String EncouragementEnabledKey = "pref_encouragement_enabled";
    private static final String EncouragementIntervalKey = "pref_encouragement_interval";
    private static final String EncouragementIntervalDefault = "20";

    /** Random number generator to select which affirmation to use */
    private static Random random = new Random();

    /** Pre-loaded affirmation sounds */
    private static MediaPlayer[] affirm = new MediaPlayer[4];
    /** Pre-loaded encouragement sounds */
    private static MediaPlayer[] encourage;

    /** Executor to manage regular execution of encouragements */
    private ScheduledExecutorService encouragementExecutor;
    private ScheduledFuture<?> encouragementHandler;

    /** Receiver for configuration change notifications */
    private BroadcastReceiver configChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(ActivityTag, "received configuration change notification");
            stopEncouragement();
            startEncouragement();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(ActivityTag, "in onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.v(ActivityTag, "created main window");

        initializeAffirmation();
        Log.v(ActivityTag, "Loaded affirmation sounds");
        initializeEncouragement();
        Log.v(ActivityTag, "Loaded encouragement sounds");

        // Register for configuration change notifications
        LocalBroadcastManager.getInstance(this).registerReceiver(this.configChangeReceiver, new IntentFilter());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopEncouragement();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_config:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Plays a message in response to pressing the "Did it!" button
     *
     * @param view the "Did it!" button
     */
    public void playMessage(View view) {
        int index = random.nextInt(affirm.length);
        Log.v(ActivityTag, "Playing affirmation message");
        affirm[index].start();
        stopEncouragement();
        startEncouragement();
    }

    /**
     * Toggles between playing and pausing encouragements.
     *
     * @param view the button that was pressed to trigger this event handler.
     */
    public void playPauseEncouragements(View view) {
        ImageButton encButton = (ImageButton)view;
        String tag = (String)encButton.getTag();
        String paused = getResources().getString(R.string.paused);
        String playing = getResources().getString(R.string.playing);
        if(tag.equals(paused)) {
            Log.v(ActivityTag, "Restarting encouragements");
            encButton.setImageResource(android.R.drawable.ic_media_pause);
            encButton.setTag(playing);
            startEncouragement();
        } else {
            Log.v(ActivityTag, "Pausing encouragements");
            encButton.setImageResource(android.R.drawable.ic_media_play);
            encButton.setTag(paused);
            stopEncouragement();
        }
    }

    /**
     * Initializes affirmation sounds
     */
    public void initializeAffirmation() {
        Log.v(ActivityTag, "Initializing affirmations");
        affirm[0] = MediaPlayer.create(this, R.raw.great_job);
        affirm[1] = MediaPlayer.create(this, R.raw.fantastic);
        affirm[2] = MediaPlayer.create(this, R.raw.awesome);
        affirm[3] = MediaPlayer.create(this, R.raw.youre_doing_great);
    }

    /**
     * Initializes and initiates recurring encouragements.
     */
    public void initializeEncouragement() {
        if(encourage == null) {
            Log.v(ActivityTag, "Initializing encouragement sounds");
            encourage = new MediaPlayer[3];
            encourage[0] = MediaPlayer.create(this, R.raw.keep_going);
            encourage[1] = MediaPlayer.create(this, R.raw.next_step);
            encourage[2] = MediaPlayer.create(this, R.raw.youre_smart_you_can_do_it);
        }

        if(encouragementExecutor == null) {
            Log.v(ActivityTag, "Instantiating encouragement executor");
            this.encouragementExecutor = Executors.newScheduledThreadPool(2);
        }

        Log.v(ActivityTag, "Starting encouragements");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = prefs.getBoolean(EncouragementEnabledKey, true);
        String intervalText = prefs.getString(EncouragementIntervalKey, EncouragementIntervalDefault);
        int interval;
        try {
            interval = Integer.parseInt(intervalText);
        } catch(Exception ex) {
            Log.w(ActivityTag, "Invalid encouragement interval: using 20 seconds");
            interval = 20;
        }
        Log.d(ActivityTag, "Loaded configuration: enabled = " + enabled + "; interval = " + intervalText + " (" + interval + ")");
        if(enabled) {
            this.encouragementHandler = this.encouragementExecutor.scheduleAtFixedRate(
                    new EncouragementTask(encourage), interval, interval, TimeUnit.SECONDS);
        }
    }

    /**
     * Starts recurring encouragements.
     */
    public void startEncouragement() {
        initializeEncouragement();
    }

    /**
     * Stops recurring encouragements.
     */
    public void stopEncouragement() {
        if(this.encouragementHandler != null) {
            this.encouragementHandler.cancel(true);
        }
    }
}
