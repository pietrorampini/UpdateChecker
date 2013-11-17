package com.rampo.updatechecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
 * Heart of the library. Check if an update is available for download parsing the desktop Play Store page of the app
 */
class ASyncCheck extends AsyncTask<String, Integer, Integer> {
    private static final String ROOT_PLAY_STORE_WEB = "https://play.google.com/store/apps/details?id=";
    private static final String HTML_TAGS_TO_GET_RIGHT_LINE = "</script> </div> <div class=\"details-wrapper\">";
    private static final String HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    private static final String HTML_TAGS_TO_REMOVE_USELESS_CONTENT = "  </div> </div>";
    private static final String LOG_TAG = "UpdateChecker";
    private static final int VERSION_DOWNLOADABLE_FOUND = 0;
    private static final int MULTIPLE_APKS_PUBLISHED = 1;
    private static final int NETWORK_ERROR = 2;

    Store store;
    Context context;
    ASyncCheckResult resultInterface;
    String versionDownloadable;

    ASyncCheck(Store store, ASyncCheckResult resultInterface, Context activity) {
        this.store = store;
        this.resultInterface = resultInterface;
        this.context = activity;
    }

    @Override
    protected Integer doInBackground(String... notused) {
        if (isNetworkAvailable(context)) {
            try {
                Log.d(LOG_TAG, "zcas");
                HttpParams params = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(params, 4000);
                HttpConnectionParams.setSoTimeout(params, 5000);
                HttpClient client = new DefaultHttpClient(params);
                if (store == Store.GOOGLE_PLAY) {
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

                } else if (store == Store.AMAZON) {

                }

                if (containsNumber(versionDownloadable)) {
                    return VERSION_DOWNLOADABLE_FOUND;
                } else {
                    return MULTIPLE_APKS_PUBLISHED;
                }
            } catch (IOException connectionError) {
                logConnectionError();
                return NETWORK_ERROR;
            }
        } else {
            return NETWORK_ERROR;
        }
    }

    /**
     * return to the Fragment to work with the versionDownloadable if the library found it.
     *
     * @param result
     */
    @Override
    protected void onPostExecute(Integer result) {
        if (result == VERSION_DOWNLOADABLE_FOUND) {
            resultInterface.versionDownloadableFound(versionDownloadable);
            Log.d(LOG_TAG, "teet");
        } else if (result == NETWORK_ERROR) {
            resultInterface.networkError();
            Log.d(LOG_TAG, "a");
        } else if (result == MULTIPLE_APKS_PUBLISHED) {
            resultInterface.multipleApksPublished();
            Log.d(LOG_TAG, "b");
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