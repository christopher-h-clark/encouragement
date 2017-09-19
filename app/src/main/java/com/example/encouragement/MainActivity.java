package com.example.encouragement;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

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
                Intent intent = new Intent(this, ConfigurationActivity.class);
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
        int interval = Configuration.getEncouragementInterval();
        this.encouragementHandler = this.encouragementExecutor.scheduleAtFixedRate(
                new EncouragementTask(encourage), interval, interval, TimeUnit.SECONDS);
//        Intent intent = new Intent(EncouragementService.Init, null, this, EncouragementService.class);
//        this.startService(intent);

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
    }

    public void startEncouragement() {
        initializeEncouragement();
//        Intent intent = new Intent(EncouragementService.Start, null, this, EncouragementService.class);
//        this.startService(intent);
    }

    public void stopEncouragement() {
        //encouragementExecutor.shutdownNow();
        this.encouragementHandler.cancel(true);
//        Intent intent = new Intent(EncouragementService.Stop, null, this, EncouragementService.class);
//        this.startService(intent);
    }
}
