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
    final String logTag = "newUpdateAvailable";
    static final String NotificationInstedOfDialogKey = "notificatioInstedOfDialog";

    public static void CheckForDialog(FragmentActivity activity) {
        android.support.v4.app.FragmentTransaction content = activity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, false);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    public static void CheckForNotification(FragmentActivity activity) {
        android.support.v4.app.FragmentTransaction content = activity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, true);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        Boolean NotificationInstedOfDialogBool = args.getBoolean(NotificationInstedOfDialogKey);
        CheckForUpdates(NotificationInstedOfDialogBool);
    }

    private void CheckForUpdates(final boolean NotificationInstedOfDialogBool) {
        thread = new Thread() {
            @Override
            public void run() {
                Context context = getActivity().getApplicationContext();
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
                        if (line.contains("</script> </div> <div class=\"details-wrapper\">")) { // Obtained HTML line contaning version available in Play Store
                            String containingVersion = line.substring(line.lastIndexOf("itemprop=\"softwareVersion\"> ") + 28);  // Get a String starting with version available + Other HTML tags
                            String[] removingUnusefulTags = containingVersion.split("  </div> </div>"); // Remove unseful HTML tags
                            String versionDownloadable = removingUnusefulTags[0]; // Obtain version available
                            finalStep(versionDownloadable, NotificationInstedOfDialogBool);
                        }
                    }
                } catch (IOException e) {
                    logConnectionError();
                }
            }
        };
        thread.start();
    }

    public void finalStep(String versionDownloadable, boolean NotificationInstedOfDialogBool) {
        thread.interrupt();
        Looper.prepare();
        Context context = getActivity().getApplicationContext();
        try {
            if (versionDownloadable.equals(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)) {
            } else {
                if (containsNumber(versionDownloadable)) {
                    if (NotificationInstedOfDialogBool == true) {
                        showNotification();
                    } else {
                        showDialog();
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    public final boolean containsNumber(String string) {
        boolean containsDigit;

        if (string.matches(".*[0-9].*")) {
            containsDigit = true;
        } else {
            containsDigit = false;
        }

        return containsDigit;
    }

    private void showNotification() {
        Context context = getActivity().getApplicationContext();
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rootPlayStoreDevice) + context.getPackageName()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
        Notification noti;
        noti = new NotificationCompat.Builder(context)
                .setTicker("New Update Available")
                .setContentTitle("BO2 Full Guide")
                .setContentText("1 new update found")
                .setSmallIcon(R.drawable.play_store_notification_icon)
                .setContentIntent(pendingIntent).build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }

    public void logConnectionError() {
        Log.e(logTag, "Cannot connect to the Internet!");
    }

    private void showDialog() {
        UpdateCheckerDialog.show(getActivity());
    }
}

