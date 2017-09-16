package com.example.encouragement;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static Random random = new Random();
//    private Handler encourageHandler = new Handler();
//    private static Runnable encourageAction;

    private static MediaPlayer[] affirm = new MediaPlayer[4];
//    private static MediaPlayer[] encourage = new MediaPlayer[3];

    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.app_toolbar);
//        setSupportActionBar(toolbar);

        affirm[0] = MediaPlayer.create(this, R.raw.great_job);
        affirm[1] = MediaPlayer.create(this, R.raw.fantastic);
        affirm[2] = MediaPlayer.create(this, R.raw.awesome);
        affirm[3] = MediaPlayer.create(this, R.raw.youre_doing_great);
//        encourage[0] = MediaPlayer.create(this, R.raw.keep_going);
//        encourage[1] = MediaPlayer.create(this, R.raw.next_step);
//        encourage[2] = MediaPlayer.create(this, R.raw.youre_smart_you_can_do_it);
        initializeEncouragement();

        // Get wake lock to keep screen on
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Encouragement");
        this.wakeLock.acquire();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.wakeLock.release();
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
                stopEncouragement();
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
        affirm[index].start();
        stopEncouragement();
        initializeEncouragement();
    }

    public void playPauseEncouragements(View view) {
        ImageButton encButton = (ImageButton)view;
        String tag = (String)encButton.getTag();
        String paused = getResources().getString(R.string.paused);
        String playing = getResources().getString(R.string.playing);
        if(tag.equals(paused)) {
            encButton.setImageResource(android.R.drawable.ic_media_pause);
            encButton.setTag(playing);
            startEncouragement();
        } else {
            encButton.setImageResource(android.R.drawable.ic_media_play);
            encButton.setTag(paused);
            stopEncouragement();
        }
    }

    public void initializeEncouragement() {
        Intent intent = new Intent(EncouragementService.Init, null, this, EncouragementService.class);
        this.startService(intent);
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
        Intent intent = new Intent(EncouragementService.Start, null, this, EncouragementService.class);
        this.startService(intent);
    }

    public void stopEncouragement() {
        Intent intent = new Intent(EncouragementService.Stop, null, this, EncouragementService.class);
        this.startService(intent);
//        encourageHandler.removeCallbacks(encourageAction);
    }

    public void resetEncouragement() {
        Intent intent = new Intent(EncouragementService.Reset, null, this, EncouragementService.class);
        this.startService(intent);
    }
}
