/*
 * Copyright (C) 2014 Pietro Rampini - PiKo Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rampo.updatechecker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public class UpdateChecker implements ASyncCheckResult, DialogInterface, UpdateCheckerResult {

    public static final String ROOT_PLAY_STORE_DEVICE = "market://details?id=";

    private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
    private static final String INT_OF_SUCCESSFUL_CHEKS_PREF_KEY = "nLaunches";
    private static final String DONT_SHOW_AGAIN_PREF_KEY = "dontShow";
    private static final String PREFS_FILENAME = "updateChecker";

    static Store DEFAULT_STORE = Store.GOOGLE_PLAY;
    static int DEFAULT_SUCCESSFUL_CHECKS_REQUIRED = 5;
    static Notice DEFAULT_NOTICE = Notice.DIALOG;

    static Activity mActivity;
    static Store mStore;
    static int mSuccessfulChecksRequired;
    static Notice mNotice;
    static int mNotificationIconResId;
    static UpdateCheckerResult mLibraryResultCallaback;
    static ASyncCheckResult mCheckResultCallback;

    public UpdateChecker(Activity activity) {
        mActivity = activity;
        mStore = DEFAULT_STORE;
        mSuccessfulChecksRequired = DEFAULT_SUCCESSFUL_CHECKS_REQUIRED;
        mNotice = DEFAULT_NOTICE;
        mCheckResultCallback = this;
        mLibraryResultCallaback = this;
    }

    public UpdateChecker(Activity activity, UpdateCheckerResult updateCheckerResult) {
        mActivity = activity;
        mStore = DEFAULT_STORE;
        mSuccessfulChecksRequired = DEFAULT_SUCCESSFUL_CHECKS_REQUIRED;
        mNotice = DEFAULT_NOTICE;
        mCheckResultCallback = this;
        mLibraryResultCallaback = updateCheckerResult;
    }

    public static void setStore(Store store) {
        mStore = store;
    }

    public static void setSuccessfulChecksRequired(int checksRequired) {
        mSuccessfulChecksRequired = checksRequired;
    }

    public static void setNotice(Notice notice) {
        mNotice = notice;
    }

    public static void setNotificationIcon(int notificationIconResId) {
        mNotificationIconResId = notificationIconResId;
    }

    public static void start() {
        ASyncCheck asynctask = new ASyncCheck(mStore, mCheckResultCallback, mActivity);
        asynctask.execute();
        Log.d("UpdateChecker", "dasda");
    }

    /**
     * If the library found a version available on Play Store, and it's different from the installed one (from manifest's versionName), notify it to the user.
     *
     * @param versionDownloadable String to compare to versionName of the app.
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see UpdateChecker#CheckForNotification(android.support.v4.app.FragmentActivity)
     */
    @Override
    public void versionDownloadableFound(String versionDownloadable) {
        try {
            if (!versionDownloadable.equals(mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName)) { // New Update Available
                if (haveToRespectSuccessfulChecksRequired(versionDownloadable) && !hasUserTappedToNotShowNoticeAgain(versionDownloadable)) {
                    mLibraryResultCallaback.foundUpdateAndShowIt(versionDownloadable);
                } else {
                    mLibraryResultCallaback.foundUpdateAndDontShowIt(versionDownloadable);
                }
            } else { // No new update available
                mLibraryResultCallaback.upToDate();
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    /**
     * Can't get the versionName from Play Store
     * Connection error or #1
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    @Override
    public void multipleApksPublished() {

    }

    @Override
    public void networkError() {

    }

    @Override
    public void foundUpdateAndShowIt(String versionDownloadable) {
        Log.d("UpdateChecker", "teet");
        if (mNotice == Notice.NOTIFICATION) { // Selected a checkForNotification(...) method
            showNotification();
        } else if (mNotice == Notice.DIALOG) { // Selected a checkForDialog(...) method
            showDialog(versionDownloadable);
        }
    }

    @Override
    public void foundUpdateAndDontShowIt(String versionDownloadable) {

    }

    @Override
    public void upToDate() {

    }

    /**
     * Get if the user has tapped on "No, thanks" button on dialog for this downloable version.
     * See userHasTappedToNotShowNoticeAgain(...) callback.
     *
     * @param versionDownloadable Version downloadable.
     */
    private boolean hasUserTappedToNotShowNoticeAgain(String versionDownloadable) {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = DONT_SHOW_AGAIN_PREF_KEY + versionDownloadable;
        return prefs.getBoolean(prefKey, false);
    }

    /**
     * User has tapped on "No, thanks" button on dialog.
     * See hasUserTappedToNotShowNoticeAgain(...) boolean.
     *
     * @param versionDownloadable Update name to don't show any notice again about.
     */
    @Override
    public void userHasTappedToNotShowNoticeAgain(String versionDownloadable) {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DONT_SHOW_AGAIN_PREF_KEY + versionDownloadable, true);
        editor.commit();
    }


    /**
     * Show dialog
     */
    public void showDialog(String versionDownloadable) {
        Dialog dialog = new Dialog(this, versionDownloadable);
        try {
        } catch (NullPointerException activityClosed) {
            /* This happens when the library tries to open a dialog,
               but the activity is already closed, so generates a NullPointerException.
			   In this way, a force close is avoided.*/
        } catch (IllegalStateException activityClosed) {
        } /* Catch blocks happens when the library tries to open a dialog,
             but the activity is already closed, so generates a NullPointerException or IllegalStateException.
			 In this way, a force close is avoided.*/
    }

    /**
     * Show Notification
     *
     * @see Notification#show(android.content.Context, int)
     */
    public static void showNotification() {
        Notification.show(mActivity, mNotificationIconResId);
    }

    /**
     * Show the Dialog/Notification only if it is the first time or if the mChecksMade is multiple of the number specified by you after the dialog will be shown. (If you call the standard methods of checkFor...(...) the default is 5)
     */
    private boolean haveToRespectSuccessfulChecksRequired(String versionDownloadable) {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = INT_OF_SUCCESSFUL_CHEKS_PREF_KEY + versionDownloadable;
        int mChecksMade = prefs.getInt(prefKey, 0);
        if (mChecksMade % mSuccessfulChecksRequired == 0 || mChecksMade == 0) {
            saveNumberOfChecksForUpdatedVersion(versionDownloadable, mChecksMade);
            return true;
        } else {
            saveNumberOfChecksForUpdatedVersion(versionDownloadable, mChecksMade);
            return false;
        }
    }

    /**
     * Update number of checks for the versionName of the version downloadable from Play Store.
     */
    private void saveNumberOfChecksForUpdatedVersion(String versionDownloadable, int mChecksMade) {
        mChecksMade++;
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INT_OF_SUCCESSFUL_CHEKS_PREF_KEY + versionDownloadable, mChecksMade);
        editor.commit();

    }

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     * Number of checks after the dialog will be shown: 5 (default)
     *
     * @param fragmentActivity required.
     */
    @Deprecated
    public static void checkForDialog(FragmentActivity fragmentActivity) {
    }

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     * Specify the number of checks after the dialog will be shown.
     *
     * @param fragmentActivity         Required.
     * @param successfulChecksRequired The number of checks after the dialog will be shown.
     */
    @Deprecated
    public static void checkForDialog(FragmentActivity fragmentActivity, int successfulChecksRequired) {
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Number of checks after the dialog will be shown: 5 (default)
     *
     * @param fragmentActivity Required.
     */
    @Deprecated
    public static void checkForNotification(FragmentActivity fragmentActivity) {
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity         Required.
     * @param successfulChecksRequired The number of checks after the notification will be shown.
     */
    @Deprecated
    public static void checkForNotification(FragmentActivity fragmentActivity, int successfulChecksRequired) {
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity      Required.
     * @param notificationIconResId R.drawable.* resource to set to Notification icon.
     */
    @Deprecated
    public static void checkForNotification(int notificationIconResId, FragmentActivity fragmentActivity) {
    }

    /**
     * Show a Notification if an update is available for download. Set the notificationIcon Resource Id. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity         Required
     * @param successfulChecksRequired The number of checks after the notification will be shown.
     * @param notificationIconResId    R.drawable.* resource to set to Notification icon.
     */
    @Deprecated
    public static void checkForNotification(int notificationIconResId, FragmentActivity fragmentActivity, int successfulChecksRequired) {
    }

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     *
     * @param fragmentActivity Required.
     * @deprecated use {@link #checkForDialog(FragmentActivity)} instead.
     */
    @Deprecated
    public static void CheckForDialog(FragmentActivity fragmentActivity) {
        checkForDialog(fragmentActivity);
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     *
     * @param fragmentActivity Required.
     * @deprecated use {@link #checkForNotification(FragmentActivity)} instead.
     */
    @Deprecated
    public static void CheckForNotification(FragmentActivity fragmentActivity) {
        checkForNotification(fragmentActivity);
    }

    /**
     * Show a Notification if an update is available for download. Set the notificationIcon Resource Id. Callable in a FragmentActivity
     *
     * @param fragmentActivity      Required
     * @param notificationIconResId R.drawable.* resource to set to Notification Icon.
     * @deprecated use {@link #checkForNotification(FragmentActivity, int)} instead.
     */
    @Deprecated
    public static void CheckForNotification(FragmentActivity fragmentActivity, int notificationIconResId) {
        checkForNotification(fragmentActivity, notificationIconResId);
    }
}
