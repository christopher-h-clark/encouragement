package com.example.encouragement;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;

/**
 * Configuration user interface back end
 */
public class ConfigurationActivity extends AppCompatActivity {

    /** Tag placed in configuration notification intents */
    public static final String ConfigChangeIntentTag = "config-changed";

    /**
     * Sets up behaviors when UI is created
     *
     * @param savedInstanceState Not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

//        long sec = Configuration.getEncouragementInterval().getStandardSeconds();
        int sec = Configuration.getEncouragementInterval();
        EditText encIntervalText = (EditText)findViewById(R.id.encIntervalText);
        encIntervalText.setText(String.format(Locale.getDefault(), "%d", sec));

        encIntervalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if(!text.isEmpty()) {
                    try {
                        int seconds = Integer.parseInt(text);
//                        Configuration.setEncouragementIntervalSeconds(seconds);
                        Configuration.setEncouragementInterval(seconds);
                        notifyConfigurationChange();
                    } catch(NumberFormatException ex) {
                        // Ignore
                    }
                }
            }
        });
    }

    /**
     * Broadcasts Intent when configuration changes
     */
    private void notifyConfigurationChange() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ConfigChangeIntentTag));
    }
}
