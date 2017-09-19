# Encouragement

App designed to affirm and encourage my son to do his school work more efficiently.

## Playing Audio

[Stack Overflow question](http://stackoverflow.com/questions/18459122/play-sound-on-button-click-android) about playing audio

. [Android-Audio](https://github.com/delight-im/Android-Audio) library for playing audio (not used in prototype)

## Timer

[Timer(Task) = BAD! Do It The Android Way: Use a Handler](http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/) article about using Android handlers

The [ScheduledExecutorService](https://developer.android.com/reference/java/util/concurrent/ScheduledExecutorService.html) seems to be the best way to handle the Encouragement timer in this app. An introduction to using it appears on [Stack Overflow](https://stackoverflow.com/questions/14376470/scheduling-recurring-task-in-android) with more detail in a [second post about problems](https://stackoverflow.com/questions/27872016/scheduledthreadpoolexecutor-for-a-periodic-task-using-retrofit-just-firing-onc).

## Menu

[Options Menu](https://code.tutsplus.com/tutorials/android-sdk-implement-an-options-menu--mobile-9453): article about implementing an options menu

## Large Round Button

The best way to handle the large round "Did it!" button is to create an XML drawable that defines an oval button. This is described on [Stack Overflow](https://stackoverflow.com/questions/9884202/custom-circle-button).

## Activity Communication

When the configuration activity makes a change to the configuration, it needs to notify the main activity. The best way to do this is with a [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html). A description of the technique is available on [Stack Overflow](https://stackoverflow.com/questions/19026515/how-to-use-interface-to-communicate-between-two-activities).

## Background Jobs

[Best Practices for Background Jobs](https://developer.android.com/training/best-background.html): article about implementing Android background services

# Icons

mdpi: 48x48
hdpi: 72x72
xhdpi: 96x96
xxhdpi: 144x144
xxxhdpi: 192x192

Attribution for icon: [chris 論](https://commons.wikimedia.org/wiki/User:Chrkl)
