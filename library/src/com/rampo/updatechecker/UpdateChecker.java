/*
 * Copyright (C) 2013 Pietro Rampini "Rampo" - Piko Technologies
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
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public class UpdateChecker extends Fragment implements CheckResultInterface, DialogInterface {

    public static final String ROOT_PLAY_STORE_DEVICE = "market://details?id=";

    private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
    private static final String INT_OF_LAUNCHES_PREF_KEY = "nLaunches";
    private static final String DONT_SHOW_PREF_KEY = "dontShow";
    private static final String NOTICE_TYPE_KEY = "type";
    private static final String SUCCESSFUL_CHECKS_REQUIRED_KEY = "nChecks";
    private static final String PREFS_FILENAME = "updateChecker";
    private static final int NOTICE_NOTIFICATION = 2;
    private static final int NOTICE_DIALOG = 1;

    private FragmentActivity mContext;
    private int mSuccessfulChecksRequired;
    private int mTypeOfNotice;
    private int mNotificationIconResId;

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     * Number of checks after the dialog will be shown: 5 (default)
     *
     * @param fragmentActivity required.
     */
    public static void checkForDialog(FragmentActivity fragmentActivity) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     * Specify the number of checks after the dialog will be shown.
     *
     * @param fragmentActivity         Required.
     * @param successfulChecksRequired The number of checks after the dialog will be shown.
     */
    public static void checkForDialog(FragmentActivity fragmentActivity, int successfulChecksRequired) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_DIALOG);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, successfulChecksRequired);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Number of checks after the dialog will be shown: 5 (default)
     *
     * @param fragmentActivity Required.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
        args.putInt(NOTIFICATION_ICON_RES_ID_KEY, R.drawable.ic_stat_ic_menu_play_store);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity         Required.
     * @param successfulChecksRequired The number of checks after the notification will be shown.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity, int successfulChecksRequired) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
        args.putInt(NOTIFICATION_ICON_RES_ID_KEY, R.drawable.ic_stat_ic_menu_play_store);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, successfulChecksRequired);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity      Required.
     * @param notificationIconResId R.drawable.* resource to set to Notification icon.
     */
    public static void checkForNotification(int notificationIconResId, FragmentActivity fragmentActivity) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
        args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, 5);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Set the notificationIcon Resource Id. Callable in a FragmentActivity
     * Specify the number of checks after the notification will be shown.
     *
     * @param fragmentActivity         Required
     * @param successfulChecksRequired The number of checks after the notification will be shown.
     * @param notificationIconResId    R.drawable.* resource to set to Notification icon.
     */
    public static void checkForNotification(int notificationIconResId, FragmentActivity fragmentActivity, int successfulChecksRequired) {
        FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putInt(NOTICE_TYPE_KEY, NOTICE_NOTIFICATION);
        args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
        args.putInt(SUCCESSFUL_CHECKS_REQUIRED_KEY, successfulChecksRequired);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
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
            if (containsNumber(versionDownloadable)) {
                if (!versionDownloadable.equals(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName)) { // New Update Available
                    if (iDontWantToBeTooMuchInvasive(versionDownloadable) && !hasUserTappedToNotShowNoticeAgain(versionDownloadable)) {
                        if (mTypeOfNotice == NOTICE_NOTIFICATION) { // Selected a checkForNotification(...) method
                            showNotification();
                        } else if (mTypeOfNotice == NOTICE_DIALOG) { // Selected a checkForDialog(...) method
                            showDialog(versionDownloadable);
                        }
                    }
                } else {

                } // No new update available
            } else {
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
    public void versionDownloadableNotFound() {
    }

    /**
     * Get if the user has tapped on "No, thanks" button on dialog for this downloable version.
     * See userHasTappedToNotShowNoticeAgain(...) callback.
     *
     * @param versionDownloadable Version downloadable.
     */
    private boolean hasUserTappedToNotShowNoticeAgain(String versionDownloadable) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = DONT_SHOW_PREF_KEY + versionDownloadable;
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
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(DONT_SHOW_PREF_KEY + versionDownloadable, true);
        editor.commit();
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

    /**
     * This class is a Fragment. Check for the method you have chosen.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (FragmentActivity) activity;
        Bundle args = getArguments();
        mTypeOfNotice = args.getInt(NOTICE_TYPE_KEY);
        mSuccessfulChecksRequired = args.getInt(SUCCESSFUL_CHECKS_REQUIRED_KEY);
        mNotificationIconResId = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
        AsyncCheck asynctask = new AsyncCheck(this, activity.getApplicationContext());
        asynctask.execute();
    }

    /**
     * Since the library check from the Desktop Web Page of the app the Current Version, if there are different apks for the app,
     * the Play Store will shown Varies depending on the device, so the Library can't compare it to versionName installed.
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    public final boolean containsNumber(String string) {
        return string.matches(".*[0-9].*");
    }

    /**
     * Show dialog
     */
    public void showDialog(String versionDownloadable) {
        Dialog dialog = new Dialog(this, versionDownloadable);
        try {
            dialog.show(getActivity().getSupportFragmentManager(), null);
        } catch (NullPointerException activityClosed) {
			/* This happens when the library tries to open a dialog,
			   but the activity is already closed, so generates a NullPointerException.
			   In this way, a force close is avoided.*/
        }
    }

    /**
     * Show Notification
     *
     * @see Notification#show(android.content.Context, int)
     */
    public void showNotification() {
        Notification.show(mContext, mNotificationIconResId);
    }

    /**
     * Show the Dialog/Notification only if it is the first time or if the mChecksMade is multiple of the number specified by you after the dialog will be shown. (If you call the standard methods of checkFor...(...) the default is 5)
     */
    private boolean iDontWantToBeTooMuchInvasive(String versionDownloadable) {
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        String prefKey = INT_OF_LAUNCHES_PREF_KEY + versionDownloadable;
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
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INT_OF_LAUNCHES_PREF_KEY + versionDownloadable, mChecksMade);
        editor.commit();

    }
}

/**
 * Heart of the library. Check if an update is available for download parsing the desktop Play Store page of the app
 */
class AsyncCheck extends AsyncTask<String, Integer, Integer> {
    private static final String ROOT_PLAY_STORE_WEB = "https://play.google.com/store/apps/details?id=";
    private static final String HTML_TAGS_TO_GET_RIGHT_LINE = "</script> </div> <div class=\"details-wrapper\">";
    private static final String HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    private static final String HTML_TAGS_TO_REMOVE_USELESS_CONTENT = "  </div> </div>";
    private static final String LOG_TAG = "UpdateChecker";
    private static final int VERSION_FOUND = 5;
    private static final int ERROR = 7;

    Context context;
    CheckResultInterface resultInterface;
    String versionDownloadable;

    AsyncCheck(CheckResultInterface checkInterface, Context ctx) {
        resultInterface = checkInterface;
        context = ctx;
    }

    @Override
    protected Integer doInBackground(String... notused) {
        if (isNetworkAvailable(context)) {
            try {
                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, 4000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                HttpClient client = new DefaultHttpClient(params);
                HttpGet request = new HttpGet(ROOT_PLAY_STORE_WEB + context.getPackageName()); // Set the right Play Store page by getting package name.
                HttpResponse response = client.execute(request);
                InputStream is = response.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(HTML_TAGS_TO_GET_RIGHT_LINE)) { // Obtain HTML line contaning version available in Play Store
                        String containingVersion = line.substring(line.lastIndexOf(HTML_TAGS_TO_GET_RIGHT_POSITION) + 28);  // Get the String starting with version available + Other HTML tags
                        String[] removingUnusefulTags = containingVersion.split(HTML_TAGS_TO_REMOVE_USELESS_CONTENT); // Remove unseful HTML tags
                        versionDownloadable = removingUnusefulTags[0]; // Obtain version available

                    }
                }

                return VERSION_FOUND;
            } catch (IOException connectionError) {
                logConnectionError();
                return ERROR;
            }
        }
        return ERROR;
    }

    /**
     * return to the Fragment to work with the versionDownloadable if the library found it.
     *
     * @param result
     */
    protected void onPostExecute(Integer result) {
        if (result == VERSION_FOUND) {
            resultInterface.versionDownloadableFound(versionDownloadable);
        } else {
            resultInterface.versionDownloadableNotFound();
        }
    }

    /**
     * Check if a network available
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                connected = ni.isConnected();
            }
        }
        return connected;
    }

    /**
     * Log connection error
     */
    public void logConnectionError() {
        Log.e(LOG_TAG, "Cannot connect to the Internet!");
    }

}