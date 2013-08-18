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
    private Thread thread;
    private static final String logTag = "UpdateChecker";
    private static final String NotificationInstedOfDialogKey = "notificatioInstedOfDialog";
    private static final String notificationIconResIdKey = "resId";
    int notificationIconResIdPublic;
    private boolean versionDownloadableFound;

    /**
     * Show a Dialog if an update is available for download. Callable in a FragmentActivity.
     *
     * @param fragmentActivity Required.
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
     * @param fragmentActivity Required.
     */
    public static void CheckForNotification(FragmentActivity fragmentActivity) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, true);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * Show a Notification if an update is available for download. Set the notificationIcon Resource Id. Callable in a FragmentActivity
     *
     * @param fragmentActivity      Required
     * @param notificationIconResId R.drawable.* resource to set to Notification Icon.
     */
    public static void CheckForNotification(FragmentActivity fragmentActivity, int notificationIconResId) {
        android.support.v4.app.FragmentTransaction content = fragmentActivity.getSupportFragmentManager().beginTransaction();
        UpdateChecker updateChecker = new UpdateChecker();
        Bundle args = new Bundle();
        args.putBoolean(NotificationInstedOfDialogKey, true);
        args.putInt(notificationIconResIdKey, notificationIconResId);
        updateChecker.setArguments(args);
        content.add(updateChecker, null).commit();
    }

    /**
     * This class is a Fragment. Check for the method you have chosen.
     */
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
     * Heart of the library. Check if an update is available for download parsing the desktop Play Store page of the app
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
                            	versionDownloadableFound = true;
                                String containingVersion = line.substring(line.lastIndexOf("itemprop=\"softwareVersion\"> ") + 28);  // Get the String starting with version available + Other HTML tags
                                String[] removingUnusefulTags = containingVersion.split("  </div> </div>"); // Remove unseful HTML tags
                                String versionDownloadable = removingUnusefulTags[0]; // Obtain version available
                                finalStep(versionDownloadable, NotificationInstedOfDialogBool);
                                Log.e(logTag, versionDownloadable);
                            }
                            if (!versionDownloadableFound){ // Cannot find version downloadable in Play Store. Log It.
                            	logCannotFindVersionName();
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
     * @param versionDownloadable            String to compare to versionName of the app.
     * @param NotificationInstedOfDialogBool boolean getting if you have called CheckForDialog o CheckForNotification
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see UpdateChecker#CheckForNotification(android.support.v4.app.FragmentActivity)
     */
    private void finalStep(String versionDownloadable, boolean NotificationInstedOfDialogBool) {
        thread.interrupt();
        Looper.prepare();
        Context context = getActivity().getApplicationContext();
        try {
            if (containsNumber(versionDownloadable)) {
            } else {
                if (!versionDownloadable.equals(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)) {
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
        if (string.matches(".*[0-9].*")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Show dialog
     *
     * @see Dialog#show(android.support.v4.app.FragmentActivity)
     */
    private void showDialog() {
        Dialog.show(getActivity());
    }

    /**
     * Show Notification
     *
     * @see Notification#show(android.content.Context, int)
     */
    private void showNotification() {
        Notification.show(getActivity(), 1);
    }
    
    /**
     *  Cannot find versionName, probably this app hasn't benn published on Play Store
     */
	private void logCannotFindVersionName() {	
		Log.e(logTag, "Cannot find versionName, probably this app hasn't benn published on Play Store");
	}
    /**
     * Log connection error
     */
    public void logConnectionError() {
        Log.e(logTag, "Cannot connect to the Internet!");
    }

    /**
     * Check if a network available
     */
    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}

