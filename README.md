# Encouragement

My son has trouble focusing on his school work. To address this problem, I took inspiration from an article I'd read about why certain applications, games, web sites, or experiences are addictive. To summarize, the gamification of certain activities where users get frequent micro-rewards triggers a dopamine release in the brain, which is a hormone that makes one feel good. Using this theory, we experimented with a Staples Easy Button and had good results. This app is, essentially, an Easy Button that can be played through headphones so it can be used in a classroom environment.

It has two main features:
* Plays an affirmation when the user presses the big "Did it!" button. It selects an affirmation sound at random from a set of three.
* Plays an encouragement when the user hasn't pressed the "Did it!" in a configurable amount of time. It selects an encouragement sound at random from a set of four.

Initial results were promising, but the affirmations don't seem to have quite the same effect that they used to. In addition, the encouragements seem more distracting than helpful.

## Playing Audio

[Stack Overflow question](http://stackoverflow.com/questions/18459122/play-sound-on-button-click-android) about playing audio

. [Android-Audio](https://github.com/delight-im/Android-Audio) library for playing audio (not used in prototype)

## Timer

[Timer(Task) = BAD! Do It The Android Way: Use a Handler](http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/) article about using Android handlers

The [ScheduledExecutorService](https://developer.android.com/reference/java/util/concurrent/ScheduledExecutorService.html) seems to be the best way to handle the Encouragement timer in this app. An introduction to using it appears on [Stack Overflow](https://stackoverflow.com/questions/14376470/scheduling-recurring-task-in-android) with more detail in a [second post about problems](https://stackoverflow.com/questions/27872016/scheduledthreadpoolexecutor-for-a-periodic-task-using-retrofit-just-firing-onc). These discussions do not describe how to correctly turn off the periodic task. To do so, invoke the `cancel()` method on the `ScheduledFuture` returned from the `scheduleAtFixedRate()` method. It's shown in the example in the [ScheduledExecutorService](https://developer.android.com/reference/java/util/concurrent/ScheduledExecutorService.html) documentation, but not specifically mentioned otherwise.

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

# Installation

To install on a phone, generate a [signed APK file](https://www.udacity.com/wiki/ud853/course_resources/creating-a-signed-apk).
