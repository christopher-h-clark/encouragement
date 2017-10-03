package com.example.encouragement;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.os.PowerManager;
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
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /** Activity tag for log statements */
    private static final String ActivityTag = "MainActivity";

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
            Log.i(ActivityTag, "received configuration change notification");
            stopEncouragement();
            startEncouragement();
        }
    };

    /** Wake lock to prevent phone from going into sleep mode */
//    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ActivityTag, "in onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.i(ActivityTag, "created main window");

//        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
//        setSupportActionBar(toolbar);

        initializeAffirmation();
        Log.i(ActivityTag, "Loaded affirmation sounds");
        initializeEncouragement();
        Log.i(ActivityTag, "Loaded encouragement sounds");

        // Register for configuration change notifications
        LocalBroadcastManager.getInstance(this).registerReceiver(this.configChangeReceiver, new IntentFilter());
        // Get wake lock to keep screen on
//        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
//        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Encouragement");
//        this.wakeLock.acquire();
//        Log.i("MainActivity", "Acquired wake lock");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopEncouragement();
//        this.wakeLock.release();
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
//                stopEncouragement();
//                Intent intent = new Intent(this, ConfigurationActivity.class);
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i(ActivityTag, "Shared preference changed: " + key);
    }

    /**
     * Plays a message in response to pressing the "Did it!" button
     *
     * @param view the "Did it!" button
     */
    public void playMessage(View view) {
        int index = random.nextInt(affirm.length);
        Log.i(ActivityTag, "Playing affirmation message");
        affirm[index].start();
        stopEncouragement();
        startEncouragement();
    }

    public void playPauseEncouragements(View view) {
        ImageButton encButton = (ImageButton)view;
        String tag = (String)encButton.getTag();
        String paused = getResources().getString(R.string.paused);
        String playing = getResources().getString(R.string.playing);
        if(tag.equals(paused)) {
            Log.i(ActivityTag, "Restarting encouragements");
            encButton.setImageResource(android.R.drawable.ic_media_pause);
            encButton.setTag(playing);
            startEncouragement();
        } else {
            Log.i(ActivityTag, "Pausing encouragements");
            encButton.setImageResource(android.R.drawable.ic_media_play);
            encButton.setTag(paused);
            stopEncouragement();
        }
    }

    /**
     * Initializes affirmation sounds
     */
    public void initializeAffirmation() {
        Log.i(ActivityTag, "Initializing affirmations");
        affirm[0] = MediaPlayer.create(this, R.raw.great_job);
        affirm[1] = MediaPlayer.create(this, R.raw.fantastic);
        affirm[2] = MediaPlayer.create(this, R.raw.awesome);
        affirm[3] = MediaPlayer.create(this, R.raw.youre_doing_great);
    }

    public void initializeEncouragement() {
        if(encourage == null) {
            Log.i(ActivityTag, "Initializing encouragement sounds");
            encourage = new MediaPlayer[3];
            encourage[0] = MediaPlayer.create(this, R.raw.keep_going);
            encourage[1] = MediaPlayer.create(this, R.raw.next_step);
            encourage[2] = MediaPlayer.create(this, R.raw.youre_smart_you_can_do_it);
        }

        if(encouragementExecutor == null) {
            Log.i(ActivityTag, "Instantiating encouragement executor");
            this.encouragementExecutor = Executors.newScheduledThreadPool(2);
        }

        Log.i(ActivityTag, "Starting encouragements");
//        int interval = Configuration.getEncouragementInterval();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = prefs.getBoolean("pref_encouragement_enabled", true);
        String intervalText = prefs.getString("pref_encouragement_interval", "20");
        int interval;
        try {
            interval = Integer.parseInt(intervalText);
        } catch(Exception ex) {
            Log.w(ActivityTag, "Invalid encouragement interval: using 20 seconds");
            interval = 20;
        }
        Log.i(ActivityTag, "Loaded configuration: enabled = " + enabled + "; interval = " + intervalText + " (" + interval + ")");
        if(enabled) {
            this.encouragementHandler = this.encouragementExecutor.scheduleAtFixedRate(
                    new EncouragementTask(encourage), interval, interval, TimeUnit.SECONDS);
        }
    }

    public void startEncouragement() {
        initializeEncouragement();
    }

    public void stopEncouragement() {
        if(this.encouragementHandler != null) {
            this.encouragementHandler.cancel(true);
        }
    }
}
