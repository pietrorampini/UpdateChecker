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
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

public class UpdateChecker extends Fragment {
    
    private static final String LOG_TAG = "UpdateChecker";
    private static final String NOTIFICATION_INSTEAD_OF_DIALOG_KEY = "notificatioInstedOfDialog";
    private static final String NOTIFICATION_ICON_RES_ID_KEY = "resId";
    private static final String INT_OF_LAUNCHES_PREF_KEY = "nlaunches";
    private static final String PREFS_FILENAME = "updateChecker";
    private static final String ROOT_PLAY_STORE_WEB = "https://play.google.com/store/apps/details?id=";
    public  static final String ROOT_PLAY_STORE_DEVICE = "market://details?id=";
    private static final String HTML_TAGS_TO_GET_RIGHT_LINE = "</script> </div> <div class=\"details-wrapper\">";
    private static final String HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    private static final String HTML_TAGS_TO_REMOVE_UNUSEFUL_CONTENT = "  </div> </div>";

    public int notificationIconResIdPublic, numberOfCheckForUpdatedVersion;
    
    private FragmentActivity mContext;
    private Thread mThread;
    
    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     *
     * @param fragmentActivity Required.
     */
    public static void checkForDialog(FragmentActivity fragmentActivity) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NOTIFICATION_INSTEAD_OF_DIALOG_KEY, false);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }
    
    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     *
     * @param fragmentActivity Required.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NOTIFICATION_INSTEAD_OF_DIALOG_KEY, true);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }
    
    /**
     * Show a Notification if an update is available for download. Set the notificationIcon Resource Id. Callable in a FragmentActivity
     *
     * @param fragmentActivity      Required
     * @param notificationIconResId R.drawable.* resource to set to Notification Icon.
     */
    public static void checkForNotification(FragmentActivity fragmentActivity, int notificationIconResId) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NOTIFICATION_INSTEAD_OF_DIALOG_KEY, true);
        args.putInt(NOTIFICATION_ICON_RES_ID_KEY, notificationIconResId);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
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
    public static void CheckForNotification(FragmentActivity fragmentActivity) {
        checkForNotification(fragmentActivity);
    }
    /**
     * This class is a Fragment. Check for the method you have chosen.
     */

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (FragmentActivity) activity;
        Bundle args = getArguments();
        boolean notificationInsteadOfDialog = args.getBoolean(NOTIFICATION_INSTEAD_OF_DIALOG_KEY);
        if (args.getInt(NOTIFICATION_ICON_RES_ID_KEY) != 0)
            notificationIconResIdPublic = args.getInt(NOTIFICATION_ICON_RES_ID_KEY);
        checkForUpdates(notificationInsteadOfDialog);
    }
    
    /**
     * Heart of the library. Check if an update is available for download parsing the desktop Play Store page of the app
     */
    private void checkForUpdates(final boolean notificationInsteadOfDialog) {
        mThread = new Thread() {
            @Override
            public void run() {
                if (isNetworkAvailable(mContext)) {
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, 4000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpGet request = new HttpGet(ROOT_PLAY_STORE_WEB + mContext.getPackageName()); // Set the right Play Store page by getting package name.
                    HttpResponse response = null;
                    try {
                        response = client.execute(request);
                    } catch (IOException e) {
                        logConnectionError();
                    }

                    InputStream is = null;
                    try {
                        is = response.getEntity().getContent();
                    } catch (IOException e) {
                        logConnectionError();
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            if (line.contains(HTML_TAGS_TO_GET_RIGHT_LINE)) { // Obtain HTML line contaning version available in Play Store
                                String containingVersion = line.substring(line.lastIndexOf(HTML_TAGS_TO_GET_RIGHT_POSITION) + 28);  // Get the String starting with version available + Other HTML tags
                                String[] removingUnusefulTags = containingVersion.split(HTML_TAGS_TO_REMOVE_UNUSEFUL_CONTENT); // Remove unseful HTML tags
                                String versionDownloadable = removingUnusefulTags[0]; // Obtain version available
                                finalStep(versionDownloadable, notificationInsteadOfDialog);
                            }
                        }
                    } catch (IOException e) {
                        logConnectionError();
                    }
                }
            }


        };
        mThread.start();
    }

    /**
     * If the version dowloadable from the Play Store is different from the versionName installed notify it to the user.
     *
     * @param versionDownloadable            String to compare to versionName of the app.
     * @param NotificationInstedOfDialogBool boolean getting if you have called CheckForDialog o CheckForNotification
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see UpdateChecker#CheckForNotification(android.support.v4.app.FragmentActivity)
     */
    private void finalStep(String versionDownloadable, boolean NotificationInstedOfDialogBool) {
        mThread.interrupt();
        Looper.prepare();
        try {
            if (containsNumber(versionDownloadable)) {
                if (!versionDownloadable.equals(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName)) { // New Update Available
                    if (iDontWantToBeTooMuchInvasive(versionDownloadable)) {
                        if (NotificationInstedOfDialogBool) {
                            showNotification();
                        } else {
                            showDialog();
                        }
                    }
                } else {
                } // No new update available
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
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
     *
     * @see Dialog#show(android.support.v4.app.FragmentActivity)
     */
    private void showDialog() {
        Dialog.show(mContext);
    }

    /**
     * Show Notification
     *
     * @see Notification#show(android.content.Context, int)
     */
    private void showNotification() {
        Notification.show(mContext, notificationIconResIdPublic);
    }

    /**
     * Log connection error
     */
    public void logConnectionError() {
        Log.e(LOG_TAG, "Cannot connect to the Internet!");
    }

    /**
     * Check if a network available
     */
    public boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /**
     * Show the Dialog/Notification only if it is the first time or divisible for 5.
     */
    private boolean iDontWantToBeTooMuchInvasive(String versionDownloadable) {
        String prefKey = INT_OF_LAUNCHES_PREF_KEY + versionDownloadable;
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        numberOfCheckForUpdatedVersion = prefs.getInt(prefKey, 0);
        if (numberOfCheckForUpdatedVersion % 5 == 0 || numberOfCheckForUpdatedVersion == 0) {
            saveNumberOfChecksForUpdatedVersion(versionDownloadable);
            return true;
        } else {
            saveNumberOfChecksForUpdatedVersion(versionDownloadable);
            return false;
        }
    }

    /**
     * Update number of checks for the versionName of the version downloadable from Play Store.
     */
    private void saveNumberOfChecksForUpdatedVersion(String versionDownloadable) {
        numberOfCheckForUpdatedVersion++;
        SharedPreferences prefs = mContext.getSharedPreferences(PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(INT_OF_LAUNCHES_PREF_KEY + versionDownloadable, numberOfCheckForUpdatedVersion);
        editor.commit();
    }
}

