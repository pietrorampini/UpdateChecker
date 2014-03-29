Change Log
===============================================================================

Version [2.0.0](https://github.com/rampo/UpdateChecker/releases/tag/v2.0.0) *(2014-03-26)*
----------------------------
 * LIB: Complete Gradle support!
 * LIB: Added Hungarian translation (thanks to [@Pistabaaa](https://github.com/Pistabaaa))
 * LIB: Removed `StyledDialogs` dependency 
 * LIB: Removed `FragmentActivity` requirement 
 * LIB: Support for Amazon App Store
 * LIB: Now you can specify an icon also for Dialog
 * LIB: Added the possibility to use a custom implementation. Manage yourself what to do if a new update is available  by implementing  `UpdateCheckerResult` instead of show a Dialog or Notification. See [Custom Implementation](https://github.com/rampo/UpdateChecker/blob/master/CUSTOMIZATION.md#custom-implementation).
 * LIB: Added `Notice` and `Store` classes to manager better the chooses of the dev
 * LIB: Removed `DialogInterface` 
 * LIB: `UpdateChecker.java` rewritten; now completely static
 * LIB: Moved the `ASyncCheck` in its own class
 * DEMO: New icon 
 * DEMO: Demo app rewritten.
 * DEMO: Demo app redesigned: branded to red
 * DEMO: Added `CustomActivity`. See above
 * <del>DEMO: Set package of demo app to `com.rampo.updatechecker.demo` to publish the package on Play Store.</del>
 * <del>DEMO: Demo app publised on Play Store</del> (Play Store problems)
 
Version [1.2.5](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.5) *(2013-12-29)*
----------------------------
 * LIB: Added Slovak translation (thanks to [@pylerSM](https://github.com/pylerSM))
 
Version [1.2.4](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.4) *(2013-12-11)*
----------------------------
 * LIB: Added Croatian translation (thanks to [@kristijandraca](https://github.com/kristijandraca))
 
Version [1.2.3](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.3) *(2013-12-10)*
----------------------------
 * LIB: Added Korean translation (thanks to [@rino0601](https://github.com/rino0601))
 
Version [1.2.2](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.2) *(2013-12-02)*
----------------------------
 * LIB: Bugs fixed (Fixed [#26](https://github.com/rampo/UpdateChecker/issues/26) and [#27](https://github.com/rampo/UpdateChecker/issues/27))
 
Version [1.2.1](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.1) *(2013-11-07)*
----------------------------
 * LIB: Bugs fixed (Fixed [#24](https://github.com/rampo/UpdateChecker/issues/24) <del>and </del>[<del>#27</del>](https://github.com/rampo/UpdateChecker/issues/27))

Version [1.2.0](https://github.com/rampo/UpdateChecker/releases/tag/v1.2.0) *(2013-10-14)*
----------------------------
 * LIB: Now uses ASyncTask insted of Thread. (Fixed [#7](https://github.com/rampo/UpdateChecker/issues/7))
 * LIB: Implemented "No, thanks" button in dialog. (If the user tap on this button, the library will not show again the dialog/notification for this update.)
 * LIB: 2 new interfaces to facilitate the reading of *UpdateChecker.java*.
 * DEMO: App redesigned and rewritten (now uses custom style).
	* DEMO: New Infos activity.
	* DEMO: New buttons for tests.
	* DEMO: New icon.
 	* DEMO: New style for Dialog.
 * DEMO: Removed ActionBarSherlock dependency.
 * DEMO: *minSdkVersion* set to 11.

Version [1.1.1](https://github.com/rampo/UpdateChecker/releases/tag/v1.1.1) *(2013-05-25)*
----------------------------
 * LIB: Strings improved.
 
Version [1.1.0](https://github.com/rampo/UpdateChecker/releases/tag/v1.1.0) *(2013-08-24)*
----------------------------
* LIB: Customize the number of checks after the dialog/notification will be shown with new methods.
* LIB: Added Dutch translation.
* LIB: Fixed translation errors.
* DEMO: Demo app rewritten.
 
Version [1.0.0](https://github.com/rampo/UpdateChecker/releases/tag/v1.0.0) *(2013-08-19)*
----------------------------
 * First Release.
