package com.example.encouragement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;

public class ConfigurationActivity extends AppCompatActivity {

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
                    } catch(NumberFormatException ex) {
                        // Ignore
                    }
                }
            }
        });
    }
}
