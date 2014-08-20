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
import android.support.v4.app.FragmentActivity;

import com.rampo.updatechecker.notice.Dialog;
import com.rampo.updatechecker.notice.Notice;
import com.rampo.updatechecker.notice.Notification;
import com.rampo.updatechecker.store.Store;

/**
 * UpdateChecker is a class that can be used by Android Developers to increase the number of their apps' updates.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public class UpdateChecker implements ASyncCheckResult, UpdateCheckerResult {

    public static final String ROOT_PLAY_STORE_DEVICE = "market://details?id=";
    public static final String PREFS_FILENAME = "updateChecker";
    public static final String DONT_SHOW_AGAIN_PREF_KEY = "dontShow";
    private static final String SUCCESSFUL_CHEKS_PREF_KEY = "nLaunches";

    static Store DEFAULT_STORE = Store.GOOGLE_PLAY;
    static int DEFAULT_SUCCESSFUL_CHECKS_REQUIRED = 5;
    static int DEFAULT_NOTICE_ICON_RES_ID = 0;
    static Notice DEFAULT_NOTICE = Notice.DIALOG;

    static Activity mActivity;
    static Store mStore;
    static int mSuccessfulChecksRequired;
    static Notice mNotice;
    static int mNoticeIconResId;
    static UpdateCheckerResult mLibraryResultCallaback;
    static ASyncCheckResult mCheckResultCallback;
    static boolean mCustomImplementation;

    public UpdateChecker(Activity activity) {
        mActivity = activity;
        mStore = DEFAULT_STORE;
        mSuccessfulChecksRequired = DEFAULT_SUCCESSFUL_CHECKS_REQUIRED;
        mNotice = DEFAULT_NOTICE;
        mNoticeIconResId = DEFAULT_NOTICE_ICON_RES_ID;
        mCheckResultCallback = this;
        mLibraryResultCallaback = this;
        mCustomImplementation = false;
    }

    public UpdateChecker(Activity activity, UpdateCheckerResult updateCheckerResult) {
        mActivity = activity;
        mStore = DEFAULT_STORE;
        mSuccessfulChecksRequired = DEFAULT_SUCCESSFUL_CHECKS_REQUIRED;
        mNotice = DEFAULT_NOTICE;
        mNoticeIconResId = DEFAULT_NOTICE_ICON_RES_ID;
        mCheckResultCallback = this;
        mLibraryResultCallaback = updateCheckerResult;
        mCustomImplementation = true;
    }

    /**
     * Set the store where download the app page from. Default is Google Play.
     *
     * @param store Store to set
     * @see com.rampo.updatechecker.store.Store
     * @see com.rampo.updatechecker.store.Store#GOOGLE_PLAY
     */
    public static void setStore(Store store) {
        mStore = store;
    }

    /**
     * Set the checks successful necessary to show the Notice. Default is 5.
     *
     * @param checksRequired checks required to set
     * @see com.rampo.updatechecker.notice.Notice
     */
    public static void setSuccessfulChecksRequired(int checksRequired) {
        mSuccessfulChecksRequired = checksRequired;
    }

    /**
     * Set the type of notice used to alert the user if a new update has been found. Default is Dialog.
     *
     * @param notice to set.
     * @see com.rampo.updatechecker.notice.Notice
     * @see com.rampo.updatechecker.notice.Notice#DIALOG
     */
    public static void setNotice(Notice notice) {
        mNotice = notice;
        if (mCustomImplementation) {
            throw new IllegalStateException("You can't set Notice when you choose a custom implementation.\nThe Notice is controlled manually by you with the callbacks.\nTo call setNotice() use the UpdateChecker constructor with one argument.");
        }
    }

    /**
     * Set the Notifcation or Dialog icon. If you don't call this, the Notifcation will have the default Play Store Notification Icon as icon and the Dialog will have no icon.
     *
     * @param noticeIconResId Res Id of the icon to set.
     * @see com.rampo.updatechecker.notice.Notification
     * @see com.rampo.updatechecker.notice.Dialog
     */
    public static void setNoticeIcon(int noticeIconResId) {
        mNoticeIconResId = noticeIconResId;
        if (mCustomImplementation) {
            throw new IllegalStateException("You can't set the notice Icon when you choose a custom implementation.\nThe Notice is controlled manually by you with the callbacks.\nTo call setNotice() use the UpdateChecker constructor with one argument.");
        }
    }

    /**
     * Start the process
     */
    public static void start() {
        ASyncCheck asynctask = new ASyncCheck(mStore, mCheckResultCallback, mActivity);
        asynctask.execute();
    }

    /**
     * If the library found a version available on the Store, and it's different from the installed one, notify it to the user.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     */
    @Override
    public void versionDownloadableFound(String versionDownloadable) {
        if (Comparator.isVersionDownloadableNewer(mActivity, versionDownloadable)) {
            if (hasToShowNotice(versionDownloadable) && !hasUserTappedToNotShowNoticeAgain(versionDownloadable)) {
                mLibraryResultCallaback.foundUpdateAndShowIt(versionDownloadable);
            } else {
                mLibraryResultCallaback.foundUpdateAndDontShowIt(versionDownloadable);
            }
        } else { // No new update available
            mLibraryResultCallaback.returnUpToDate(versionDownloadable);
        }

    }

    /**
     * Can't get the versionName from Play Store.
     * See #1 error.
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    @Override
    public void multipleApksPublished() {
        mLibraryResultCallaback.returnMultipleApksPublished();
    }

    /**
     * Can't download the store page.
     */
    @Override
    public void networkError() {
        mLibraryResultCallaback.returnNetworkError();
    }

    /**
     * Can't find the store page for this app.
     */
    @Override
    public void appUnpublished() {
        mLibraryResultCallaback.returnAppUnpublished();
    }

    /**
     * The check returns null for new version downloadble
     */
    @Override
    public void storeError() {
        mLibraryResultCallaback.returnStoreError();
    }

    /**
     * versionDownloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param versionDownloadable version downloadable from the Store.
     * @see com.rampo.updatechecker.UpdateChecker#setSuccessfulChecksRequired(int)
     */
    @Override
    public void foundUpdateAndShowIt(String versionDownloadable) {
        if (mNotice == Notice.NOTIFICATION) {
            showNotification();
        } else if (mNotice == Notice.DIALOG) {
            showDialog(versionDownloadable);
        }
    }

    /**
     * versionDownloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param versionDownloadable version downloadable from the Store.
     * @see com.rampo.updatechecker.UpdateChecker#setSuccessfulChecksRequired(int)
     */
    @Override
    public void foundUpdateAndDontShowIt(String versionDownloadable) {

    }

    /**
     * versionDownloadable is equal to manifest versionName -> No new update available.
     * Don't show the Notice
     *
     * @param versionDonwloadable version downloadable from the Store.
     */
    @Override
    public void returnUpToDate(String versionDonwloadable) {

    }

    @Override
    public void returnMultipleApksPublished() {

    }

    @Override
    public void returnNetworkError() {

    }

    @Override
    public void returnAppUnpublished() {

    }

    @Override
    public void returnStoreError() {

    }

    /**
     * Get if the user has tapped on "No, thanks" button on dialog for this downloable version.
     *
     * @param versionDownloadable version downloadable from the Store.
     * @see com.rampo.updatechecker.notice.Dialog#userHasTappedToNotShowNoticeAgain(android.content.Context, String)
     */
    private boolean hasUserTappedToNotShowNoticeAgain(String versionDownloadable) {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = DONT_SHOW_AGAIN_PREF_KEY + versionDownloadable;
        return prefs.getBoolean(prefKey, false);
    }

    /**
     * Show the Notice only if it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     */
    private boolean hasToShowNotice(String versionDownloadable) {
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = SUCCESSFUL_CHEKS_PREF_KEY + versionDownloadable;
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
     * Update number of checks for this version downloadable from the Store.
     */
    private void saveNumberOfChecksForUpdatedVersion(String versionDownloadable, int mChecksMade) {
        mChecksMade++;
        SharedPreferences prefs = mActivity.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SUCCESSFUL_CHEKS_PREF_KEY + versionDownloadable, mChecksMade);
        editor.commit();

    }

    /**
     * Show Dialog
     */
    public void showDialog(String versionDownloadable) {
        Dialog.show(mActivity, mStore, versionDownloadable, mNoticeIconResId);
    }

    /**
     * Show Notification
     */
    public static void showNotification() {
        Notification.show(mActivity, mStore, mNoticeIconResId);
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
