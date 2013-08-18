# Update Checker

Android User, never miss an Update.

## Overview

**Update Checker** is a class that can be used by Android Developers to show a *"New Update Available"* Notification or Dialog. 

It's based on  [Fragments](http://developer.android.com/guide/components/fragments.html), It check for new updates downloadable by parsing the Play Store desktop page of your app.

### Changelog
#### Current version: 1.0.0

####[1.0.0](https://github.com/keyboardsurfer/Crouton/tree/1.8)

- First Release

## Dependencies
- [Styled Dialogs](https://github.com/inmite/android-styled-dialogs)
- android-support-v4.jar

## Usage

Import [Styled Dialogs](https://github.com/inmite/android-styled-dialogs) into the library.

Import android-support-v4.jar into the library.

Import the library into you app.

First, Add **INTERNET** and **ACCESS_NETWORK_STATE** permissions to your app's Manifest:

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/permissions.png?login=rampo&token=f92ee11bc4a2597f62cf8b2b2dc98bb7 "CheckForDialog();")

So, in the class you want to use the library extend a FragmentActivity (or, obviously, a [SherlockFragmentActivity](https://github.com/JakeWharton/ActionBarSherlock/blob/master/actionbarsherlock/src/com/actionbarsherlock/app/SherlockFragmentActivity.java)).

Show a notification if a new updated was found:

    UpdateChecker.CheckForNotification(this);
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/activity_notification.png?login=rampo&token=1d5dc537e83d272ae9ee0a9e502b4c06 "CheckForDialog();")

Show a dialog if a new updated was found

    UpdateChecker.CheckForDialog(this);
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/activity_dialog.png?login=rampo&token=86fbbf02361265434d72ef758a573660 "CheckForDialog();")


##Important!

If there is a new update available, when app launched, **not every time the Notification / Dialog will be shown.**
The Notification/Dialog will be shown every 5 times the app ascertain that a new update is available.
It's a precaution to make the library not too invasive.

- See [Issue #1](https://github.com/rampo/UpdateChecker/issues/1)

## Contribution

###Questions

Questions regarding Crouton can be asked on [StackOverflow, using the crouton tag](http://stackoverflow.com/questions/tagged/crouton).

### Pull requests welcome

Feel free to contribute to UpdateChecker.

Either you found a bug or have created a new and awesome feature, just create a pull request.

If you want to start to create a new feature or have any other questions regarding Crouton, [file an issue](https://github.com/keyboardsurfer/Crouton/issues/new).
I'll try to answer as soon as I find the time.

Please note, if you're working on a pull request, make sure to use the [develop branch](https://github.com/keyboardsurfer/Crouton/tree/develop) as your base.

### Coming soon
 - A
 - B

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

## Attributions

Lead Developer: Pietro "Rampo" - PiKo Technologies

The Crouton logo has been created by [Marie Schweiz](http://marie-schweiz.de).
