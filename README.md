# Update Checker

> Android User, never miss an Update.

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
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/code_permissions.png?login=rampo&token=e002eea59f6436f20c7af0b088c17a15)

So, in the class you want to use the library extend a FragmentActivity (or, obviously, a [SherlockFragmentActivity](https://github.com/JakeWharton/ActionBarSherlock/blob/master/actionbarsherlock/src/com/actionbarsherlock/app/SherlockFragmentActivity.java)).

Show a notification if a new updated was found:

    UpdateChecker.CheckForNotification(this);
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/code_noti_standard.png?login=rampo&token=e30fc4dc0ecc7a8dd0108cde2849fc64)
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/noti_standard.png?login=rampo&token=9748e4286d445646604f65317a460f45)

Show a dialog if a new updated was found

    UpdateChecker.CheckForDialog(this);
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/code_dialog.png?login=rampo&token=3393b80781b1a08fa9d91ca5d7a01fe4)
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/dialog_standard.png?login=rampo&token=25af58a7f40b050980746ccdc149cac3)


##Important!

- If there is a new update available, when app launched, **not every time the Notification / Dialog will be shown.**
The Notification/Dialog will be shown every 5 times the app ascertain that a new update is available.
It's a precaution to make the library not too invasive.

- See [Issue #1](https://github.com/rampo/UpdateChecker/issues/1)

##Customization

- You can modify the notification Drawable by calling a variant of CheckForNotification method:

        UpdateChecker.CheckForNotification(this, R.drawable.ic_launcher);
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/noti_custom.png?login=rampo&token=006f69a349f66c549cfee66f47bb4b29)
        
- See [How to style StyledDialogs](https://github.com/inmite/android-styled-dialogs#how-to-style-all-dialogs), to customize the *New update avaialble* dialog
![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/dialog_custom.png?login=rampo&token=e90f49819afdf05c7f09ae891a0499ea)

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
