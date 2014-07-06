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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rampo.updatechecker.data.Constants;
import com.rampo.updatechecker.store.Store;
import com.rampo.updatechecker.utils.Network;

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
 * Heart of the library. Check if an update is available for download parsing the desktop Play Store/Amazon App Store page of your app.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
class ASyncCheck extends AsyncTask<String, Integer, Integer> {
    private static final String PLAY_STORE_ROOT_WEB = "https://play.google.com/store/apps/details?id=";
    private static final String PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    private static final String PLAY_STORE_HTML_TAGS_TO_REMOVE_USELESS_CONTENT = "  </div> </div>";
    private static final String PLAY_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER = "We're sorry, the requested URL was not found on this server.";

    private static final String AMAZON_STORE_ROOT_WEB = "http://www.amazon.com/gp/mas/dl/android?p=";
    private static final String AMAZON_STORE_HTML_TAGS_TO_GET_RIGHT_LINE = "<li><strong>Version:</strong>";
    private static final String AMAZON_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER = "<title>Amazon.com: Apps for Android</title>";

    private static final int VERSION_DOWNLOADABLE_FOUND = 0;
    private static final int MULTIPLE_APKS_PUBLISHED = 1;
    private static final int NETWORK_ERROR = 2;
    private static final int PACKAGE_NOT_PUBLISHED = 3;
    private static final int STORE_ERROR = 4;

    Store mStore;
    Context mContext;
    ASyncCheckResult mResultInterface;
    String mVersionDownloadable;

    ASyncCheck(Store store, ASyncCheckResult resultInterface, Context activity) {
        this.mStore = store;
        this.mResultInterface = resultInterface;
        this.mContext = activity;
    }

    @Override
    protected Integer doInBackground(String... notused) {
        if (Network.isAvailable(mContext)) {
            try {
                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, 4000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                HttpClient client = new DefaultHttpClient(params);
                if (mStore == Store.GOOGLE_PLAY) {
                    HttpGet request = new HttpGet(PLAY_STORE_ROOT_WEB + mContext.getPackageName()); // Set the right Play Store page by getting package name.
                    HttpResponse response = client.execute(request);
                    InputStream is = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION)) { // Obtain HTML line contaning version available in Play Store
                            String containingVersion = line.substring(line.lastIndexOf(PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION) + 28);  // Get the String starting with version available + Other HTML tags
                            String[] removingUnusefulTags = containingVersion.split(PLAY_STORE_HTML_TAGS_TO_REMOVE_USELESS_CONTENT); // Remove useless HTML tags
                            mVersionDownloadable = removingUnusefulTags[0]; // Obtain version available
                        } else if (line.contains(PLAY_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER)) { // This packages has not been found in Play Store
                            return PACKAGE_NOT_PUBLISHED;
                        }
                    }
                    if (mVersionDownloadable == null) {
                        return STORE_ERROR;
                    } else if (containsNumber(mVersionDownloadable)) {
                        return VERSION_DOWNLOADABLE_FOUND;
                    } else {
                        return MULTIPLE_APKS_PUBLISHED;
                    }
                } else if (mStore == Store.AMAZON) {
                    HttpGet request = new HttpGet(AMAZON_STORE_ROOT_WEB + mContext.getPackageName()); // Set the right Amazon App Store page by getting package name.
                    HttpResponse response = client.execute(request);
                    InputStream is = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(AMAZON_STORE_HTML_TAGS_TO_GET_RIGHT_LINE)) { // Obtain HTML line contaning version available in Amazon App Store
                            String versionDownloadableWithTags = line.substring(38); // Get the String starting with version available + Other HTML tags
                            mVersionDownloadable = versionDownloadableWithTags.substring(0, versionDownloadableWithTags.length() - 5); // Remove useless HTML tags
                            if (mVersionDownloadable == null) {
                                return STORE_ERROR;
                            } else return VERSION_DOWNLOADABLE_FOUND;
                        } else if (line.contains(AMAZON_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER)) { // This packages has not been found in Amazon App Store
                            return PACKAGE_NOT_PUBLISHED;
                        }
                    }
                }
            } catch (IOException connectionError) {
                Network.logConnectionError();
                return NETWORK_ERROR;
            }
        } else {
            return NETWORK_ERROR;
        }
        return null;
    }

    /**
     * Return to UpdateChecker class to work with the versionDownloadable if the library found it.
     *
     * @param result
     */
    @Override
    protected void onPostExecute(Integer result) {
        if (result == VERSION_DOWNLOADABLE_FOUND) {
            mResultInterface.versionDownloadableFound(mVersionDownloadable);
        } else if (result == NETWORK_ERROR) {
            mResultInterface.networkError();
            Network.logConnectionError();
        } else if (result == MULTIPLE_APKS_PUBLISHED) {
            mResultInterface.multipleApksPublished();
            Log.e(Constants.LOG_TAG, "Multiple APKs published ");
        } else if (result == PACKAGE_NOT_PUBLISHED) {
            mResultInterface.appUnpublished();
            Log.e(Constants.LOG_TAG, "App unpublished");
        } else if (result == STORE_ERROR) {
            mResultInterface.storeError();
            Log.e(Constants.LOG_TAG, "Store page format error");
        }
    }

    /**
     * Since the library check from the Desktop Web Page of the app the "Current Version" field, if there are different apks for the app,
     * the Play Store will shown "Varies depending on the device", so the library can't compare it to versionName installed.
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    public final boolean containsNumber(String string) {
        return string.matches(".*[0-9].*");
    }
}
