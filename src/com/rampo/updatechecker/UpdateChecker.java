/*
 * Copyright (C) 2013 Pietro Rampini "Rampo"
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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
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
    Thread thread;
    static final String logTag = "UpdateChecker";
    static final String NotificationInstedOfDialogKey = "notificatioInstedOfDialog";
    static final String notificationIconResIdKey = "resId";
    int notificationIconResIdPublic;
    boolean customNotificationIcon;
    Notification noti;

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     *
     * @param fragmentActivity
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see FragmentActivity
     */
    public static void CheckForDialog(FragmentActivity fragmentActivity) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, false);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Callable in a FragmentActivity
     *
     * @param fragmentActivity
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see FragmentActivity
     */
    public static void CheckForNotification(FragmentActivity fragmentActivity) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, true);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    public static void CheckForNotification(FragmentActivity fragmentActivity, int notificationIconResId) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, true);
        args.putInt(notificationIconResIdKey, notificationIconResId);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        Boolean NotificationInstedOfDialogBool = args.getBoolean(NotificationInstedOfDialogKey);
        if (args.getInt(notificationIconResIdKey) != 0)
            notificationIconResIdPublic = args.getInt(notificationIconResIdKey);
        CheckForUpdates(NotificationInstedOfDialogBool);
    }

    /**
     * Heart of the library. Check if an update is available for download by parsing the Desktop Play Store Page of the app
     */
    private void CheckForUpdates(final boolean NotificationInstedOfDialogBool) {
        thread = new Thread() {
            @Override
            public void run() {
                Context context = getActivity().getApplicationContext();
                if (isNetworkAvailable(context)) {
                    HttpParams params = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params, 4000);
                    HttpConnectionParams.setSoTimeout(params, 5000);
                    HttpClient client = new DefaultHttpClient(params);
                    HttpGet request = new HttpGet(getString(R.string.rootPlayStoreWeb) + context.getPackageName()); // Set the right Play Store page by getting package name.
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
                            if (line.contains("</script> </div> <div class=\"details-wrapper\">")) { // Obtain HTML line contaning version available in Play Store
                                String containingVersion = line.substring(line.lastIndexOf("itemprop=\"softwareVersion\"> ") + 28);  // Get the String starting with version available + Other HTML tags
                                String[] removingUnusefulTags = containingVersion.split("  </div> </div>"); // Remove unseful HTML tags
                                String versionDownloadable = removingUnusefulTags[0]; // Obtain version available
                                finalStep(versionDownloadable, NotificationInstedOfDialogBool);
                            }
                        }
                    } catch (IOException e) {
                        logConnectionError();
                    }
                }
            }
        };
        thread.start();
    }

    /**
     * If the version dowloadable from the Play Store is different from the versionName installed notify it to the user.
     *
     * @param versionDownloadable            to compare to versionName of the app.
     * @param NotificationInstedOfDialogBool boolean getting if you have called CheckForDialog o CheckForNotification
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see UpdateChecker#CheckForNotification(android.support.v4.app.FragmentActivity)
     */
    public void finalStep(String versionDownloadable, boolean NotificationInstedOfDialogBool) {
        thread.interrupt();
        Looper.prepare();
        Context context = getActivity().getApplicationContext();
        try {
            if (versionDownloadable.equals(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)) {
            } else {
                if (containsNumber(versionDownloadable)) {
                    if (NotificationInstedOfDialogBool) {
                        showNotification();
                    } else {
                        showDialog();
                    }
                }
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
        boolean containsDigit;

        if (string.matches(".*[0-9].*")) {
            containsDigit = true;
        } else {
            containsDigit = false;
        }

        return containsDigit;
    }

    /**
     * Show Notification
     */
    private void showNotification() throws PackageManager.NameNotFoundException {
        Context context = getActivity().getApplicationContext();
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rootPlayStoreDevice) + context.getPackageName()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
        String appName = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).loadLabel(context.getPackageManager()).toString();
        if (notificationIconResIdPublic == 0) {
            noti = new NotificationCompat.Builder(context)
                    .setTicker(getString(R.string.newUpdataAvailable))
                    .setContentTitle(appName)
                    .setContentText(getString(R.string.newUpdataAvailable))
                    .setSmallIcon(R.drawable.ic_stat_ic_menu_play_store)
                    .setContentIntent(pendingIntent).build();
        } else {
            noti = new NotificationCompat.Builder(context)
                    .setTicker(getString(R.string.newUpdataAvailable))
                    .setContentTitle(appName)
                    .setContentText(getString(R.string.newUpdataAvailable))
                    .setSmallIcon(notificationIconResIdPublic)
                    .setContentIntent(pendingIntent).build();
        }
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }

    public void logConnectionError() {
        Log.e(logTag, "Cannot connect to the Internet!");
    }

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    /**
     * Show dialog
     *
     * @see UpdateCheckerDialog#show(android.support.v4.app.FragmentActivity)
     */
    private void showDialog() {
        UpdateCheckerDialog.show(getActivity());
    }
}

