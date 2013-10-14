# Update Checker

> Android User, never miss an Update.

![image](https://raw.github.com/rampo/UpdateChecker/develop/arts/flow_dialog.png)

## Overview

`UpdateChecker` is a class that can be used by Android Developers to increase the number of users update their apps by showing a *"New Update Available"* Notification or Dialog. 

It's based on  [Fragments](http://developer.android.com/guide/components/fragments.html), It checks for new updates downloadable by parsing the Play Store desktop page of your app.

[XDA Thread](http://forum.xda-developers.com/showthread.php?t=2412385)

[Featured on XDA Portal!](http://www.xda-developers.com/android/prod-your-apps-users-to-update-with-updatechecker-library/)

### Changelog
##### Current version: 1.2.0

See [complete ChangeLog](https://github.com/rampo/UpdateChecker/blob/develop/CHANGELOG.md)

## Dependencies
- [Styled Dialogs](https://github.com/inmite/android-styled-dialogs)
- android-support-v4.jar

## Example
- Check out the [Demos](https://github.com/rampo/UpdateChecker/tree/master/demos) folder

## Usage

- Import [Styled Dialogs](https://github.com/inmite/android-styled-dialogs) into the library.

- Import android-support-v4.jar into the library.

- Import the library into you app.

- First, add **INTERNET** and **ACCESS_NETWORK_STATE** permissions to your app's Manifest:

    	<uses-permission android:name="android.permission.INTERNET"/>
    	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    	
- So, in the class you want to use the library extend a `FragmentActivity`  (or, obviously, a [`SherlockFragmentActivity` ](https://github.com/JakeWharton/ActionBarSherlock/blob/master/actionbarsherlock/src/com/actionbarsherlock/app/SherlockFragmentActivity.java)).

- To show a Play-Store-App-like notification if a new update was found:

    	UpdateChecker.checkForNotification(this);
	![Image](https://raw.github.com/rampo/UpdateChecker/develop/arts/notification_only.png)

- To show a dialog if a new update was found:

    	UpdateChecker.checkForDialog(this);
	![Image](https://raw.github.com/rampo/UpdateChecker/develop/arts/dialog.png)


##Important!

- If there is a new update available, when app launched, **not every time the Notification / Dialog will be shown.**
The Notification/Dialog will be shown every 5 times the app ascertain that a new update is available.
It's a precaution to make the library not too invasive. To modify this, see [Customization](https://github.com/rampo/UpdateChecker#customization).

- See [Issue #1](https://github.com/rampo/UpdateChecker/issues/1)

##Customization
- You can modify modify the number of checks after the dialog will be shown. Default is 5.
 
	    UpdateChecker.checkForNotification(this, 10);
	    
	    UpdateChecker.checkForDialog(this, 10);


- You can modify the notification Drawable by calling a variant of checkForNotification(...) method:

        UpdateChecker.checkForNotification(R.drawable.ic_launcher, this)
        
        UpdateChecker.checkForNotification(R.drawable.ic_launcher, this, 10)
![Image](https://raw.github.com/rampo/UpdateChecker/develop/arts/notification_only_custom.png)
        
- See [How to style StyledDialogs](https://github.com/inmite/android-styled-dialogs#how-to-style-all-dialogs), to customize the *New update avaialble* dialog:
![Image](https://raw.github.com/rampo/UpdateChecker/develop/arts/dialog_custom.png)


##Contribution


### Questions

If you have any questions regarding UpdateChecker, [create an issue](https://github.com/rampo/UpdateChecker/issues/new).

### Feaure request

To create a new Feature request, open an issue [here](https://github.com/rampo/UpdateChecker/issues?labels=optimization%2Crequest&page=1&state=open)

I'll try to answer as soon as I find the time.

### Pull requests welcome

Feel free to contribute to UpdateChecker.

Either you found a bug or have created a new and awesome feature, just create a pull request.

Please note, if you're working on a pull request, make sure to use the [develop branch](https://github.com/rampo/UpdateChecker/tree/develop) as your base.

### Discuss

Join in the conversation, check it out the [XDA Thread](http://forum.xda-developers.com/showthread.php?t=2412385)

## Coming soon
 - **Support for apps not published on Google Play**
 - Stackoverflow tag for more specific code problems
 - Preference to disable UpdateChecker
 
## Credits

Author: Pietro Rampini - PiKo Technologies

<a href="https://plus.google.com/u/0/110441803915933521642/posts">
  <img alt="Follow me on Google+"
       src="https://github.com/gabrielemariotti/cardslib/raw/master/demo/images/g+64.png" />
</a>
<a href="https://twitter.com/rampinipietro">
  <img alt="Follow me on Twitter"
       src="https://github.com/gabrielemariotti/cardslib/raw/master/demo/images/twitter64.png" />
</a>
## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)