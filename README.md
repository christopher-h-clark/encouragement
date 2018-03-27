# Encouragement

My son has trouble focusing on his school work. To address this problem, I took inspiration from an article I'd read about why certain applications, games, web sites, or experiences are addictive. To summarize, the gamification of certain activities where users get frequent micro-rewards triggers a dopamine release in the brain, which is a hormone that makes one feel good. Using this theory, we experimented with a Staples Easy Button and had good results. This app is, essentially, an Easy Button that can be played through headphones so it can be used in a classroom environment.

It has two main features:
* Plays an affirmation when the user presses the big "Did it!" button. It selects an affirmation sound at random from a set of three.
* Plays an encouragement when the user hasn't pressed the "Did it!" in a configurable amount of time. It selects an encouragement sound at random from a set of four.

For more information, see the [wiki](https://github.com/christopher-h-clark/encouragement/wiki).

## Attributions

Icon: [chris 論](https://commons.wikimedia.org/wiki/User:Chrkl). See also [SMirC-thumbsup](https://commons.wikimedia.org/wiki/File:SMirC-thumbsup.svg).

Also see [Credits](https://github.com/christopher-h-clark/encouragement/wiki/Credits)

## Design

A detailed description of the design is available on [Design](https://github.com/christopher-h-clark/encouragement/wiki/Design).

## Building and Installation

The Encouragement app was built using Android Studio 2.3.1 and targets Android SDK version 25.



## Menu

[Options Menu](https://code.tutsplus.com/tutorials/android-sdk-implement-an-options-menu--mobile-9453): article about implementing an options menu

## Large Round Button

The best way to handle the large round "Did it!" button is to create an XML drawable that defines an oval button. This is described on [Stack Overflow](https://stackoverflow.com/questions/9884202/custom-circle-button).

## Activity Communication

When the configuration activity makes a change to the configuration, it needs to notify the main activity. The best way to do this is with a [LocalBroadcastManager](https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html). A description of the technique is available on [Stack Overflow](https://stackoverflow.com/questions/19026515/how-to-use-interface-to-communicate-between-two-activities).

# Icons

mdpi: 48x48
hdpi: 72x72
xhdpi: 96x96
xxhdpi: 144x144
xxxhdpi: 192x192

# Installation

To install on a phone, generate a [signed APK file](https://www.udacity.com/wiki/ud853/course_resources/creating-a-signed-apk).
