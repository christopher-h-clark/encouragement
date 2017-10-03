package com.example.encouragement;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String ActivityTag = "SettingsActivity";

    private static final String EncouragementIntervalKey = "pref_encouragement_interval";

    /**
     * Creates the UI from the saved state
     * @param savedInstanceState the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || EncouragementPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows encouragement preferences.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class EncouragementPreferenceFragment
            extends PreferenceFragment
            implements OnSharedPreferenceChangeListener {

        /** Format string for encouragement interval summary */
        private String IntervalSummaryFormat = null;

        /**
         * Initializes the format string from the string table. This cannot be done before object is
         * fully constructed, so directly initializing a String constant doesn't work.
         */
        private void initializeFormat() {
            if(IntervalSummaryFormat == null) {
                IntervalSummaryFormat = getString(R.string.pref_encouragement_interval_summary_format);
            }
        }

        /**
         * Handles event triggered when the app instantiates this preference fragment.
         *
         * @param savedInstanceState the saved application state
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_encouragement);
            setHasOptionsMenu(true);

            // Initialize summary to saved value for preference
            initializeFormat();
            setSummaryFromValue(EncouragementIntervalKey, IntervalSummaryFormat);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * Handles preference change event to display current setting, in accordance with Android
         * user experience guidelines.
         *
         * @param preferences the shared preferences for the app
         * @param key the preference key of the preference that changed
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
            Log.d(ActivityTag, "Preference change: preference = " + key);
            if(key.equals(EncouragementIntervalKey)) {
                initializeFormat();
                setSummaryFromValue(key, IntervalSummaryFormat);
            }
        }

        /**
         * Handles the event triggered when the app resumes. Registers this SettingsActivity to
         * receive events when the shared preferences change.
         */
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        /**
         * Handles the event triggered when the app pauses. Stops this SettingsActivity from
         * receiving events when the shared preferences change.
         */
        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        /**
         * Sets the summary of an {@code EditTextPreference} to a formatted version of its value.
         * Requires {@code preferenceKey} to indicate an {@code EditTextPreference}.
         *
         * @param preferenceKey the key of an {@code EditTextPreference}
         * @param format formats the value for display. It must have exactly one format specifier
         *               for a string. The simplest value is {@code "%s"}.
         */
        private void setSummaryFromValue(String preferenceKey, String format) {
            try {
                EditTextPreference pref = (EditTextPreference) findPreference(preferenceKey);
                String summary = String.format(format, pref.getText());
                pref.setSummary(summary);
            } catch(Exception ex) {
                // Do nothing
            }
        }
    }
}
