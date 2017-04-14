package com.example.encouragement;

import org.joda.time.Duration;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

/**
 * Manages the configuration
 */
public class Configuration {
    private static Duration encouragementInterval = Seconds.seconds(20).toStandardDuration();

    public static Duration getEncouragementInterval() {
        return encouragementInterval;
    }

    public static void setEncouragementInterval(Duration interval) {
        encouragementInterval = interval;
    }

    public static void setEncouragementIntervalSeconds(int seconds) {
        encouragementInterval = Seconds.seconds(seconds).toStandardDuration();
    }

    public static void setEncouragementIntervalMinutes(int minutes) {
        encouragementInterval = Minutes.minutes(minutes).toStandardDuration();
    }
}
