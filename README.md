#Update Checker

> Android User, never miss an Update.

![image](https://raw.github.com/rampo/UpdateChecker/master/arts/flow_dialog_small.png)

## Overview

`UpdateChecker` is a class that can be used by Android Developers to increase the number of their apps' updates by showing a *"New update available"* Notification or Dialog. 

It checks for new updates downloadable parsing the Store desktop page of your app.

[XDA Thread](http://forum.xda-developers.com/showthread.php?t=2412385)

[Featured on XDA Portal](http://www.xda-developers.com/android/prod-your-apps-users-to-update-with-updatechecker-library/)

[Get the Demo on Google Play](https://play.google.com/store/apps/details?id=com.rampo.updatechecker.demo)

<a href="https://play.google.com/store/apps/details?id=com.rampo.updatechecker.demo">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_45.png" />
</a>

### Changelog
##### Current version: 2.0.0

See [complete ChangeLog](https://github.com/rampo/UpdateChecker/blob/master/CHANGELOG.md)

## Dependencies
- android-support-v4.jar

## Example
Check out the [source code of the demo](https://github.com/rampo/UpdateChecker/tree/master/demo) or download directly [the apk](https://github.com/rampo/UpdateChecker/tree/master/apk) 

## Usage

- Import android-support-v4.jar into the library.

- Import the library into you app.

- First, add **INTERNET** and **ACCESS_NETWORK_STATE** permissions to your app's Manifest: 

    ```xml		
	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	```
    	
- To show a **dialog** if a new update is found:

    ```java	
    UpdateChecker checker = new UpdateChecker(this);
    checker.start();
    ```  
	![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/dialog.png)

- To show a Play-Store-like **notification** if a new update is found:
		
    ```java		
    UpdateChecker checker = new UpdateChecker(this);
    checker.setNotice(Notice.NOTIFICATION);
    checker.start();
    ```		
    ![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/notification_only.png)

##Important!

- If there is a new update available, when app launched, **not every time the Notification / Dialog will be shown.**
The Notification/Dialog will be shown every 5 times the app ascertain that a new update is available.
It's a precaution to make the library not too invasive. To modify this, see [Customization](https://github.com/rampo/UpdateChecker#customization).

- See [Issue #1](https://github.com/rampo/UpdateChecker/issues/1)

##Customization

- You can modify modify the number of checks after the dialog will be shown. Default is 5.
    
    ```java
    UpdateChecker.checkForNotification(this, 10);
    UpdateChecker.checkForDialog(this, 10);
    ```

- You can modify the notification Drawable by calling a variant of checkForNotification(...) method:
    
    ```java
    UpdateChecker.checkForNotification(R.drawable.ic_launcher, this)
    UpdateChecker.checkForNotification(R.drawable.ic_launcher, this, 10)
    ```
    ![Image](https://raw.github.com/rampo/UpdateChecker/master/arts/notification_only_custom.png)
        


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

Join in the conversation, check out the [XDA Thread](http://forum.xda-developers.com/showthread.php?t=2412385)

## Coming soon
 - Stackoverflow tag for more specific code problems
 - Preference to disable UpdateChecker
 - *Changelog* Functions
 - *Rate this app* Functions
 
## Credits

Wrtiten and maintained by: [Pietro Rampini](https://plus.google.com/u/0/+PietroRampini/posts) - PiKo Technologies

<a href="https://plus.google.com/u/0/+PietroRampini/posts">
  <img alt="Follow me on Google+"
       src="https://github.com/gabrielemariotti/cardslib/raw/master/demo/images/g+64.png" />
</a>
<a href="https://twitter.com/rampinipietro">
  <img alt="Follow me on Twitter"
       src="https://github.com/gabrielemariotti/cardslib/raw/master/demo/images/twitter64.png" />
</a>

The UpdateChecker logo has been created by: [Michael Cook](https://plus.google.com/+michaelcook/posts)
## License

[Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
