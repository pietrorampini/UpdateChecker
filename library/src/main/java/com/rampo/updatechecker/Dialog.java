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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Builds a dialog to alert the user if a new update is found. This is the default Notice.
 * I've used the old AlertDialog API because newer APIs require FragmentActivity.
 * @see com.rampo.updatechecker.Notice#DIALOG
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public class Dialog {

    public static void show(final Context mContext, final Store mStore, final String mVersionDownloadable, final int mDialogIconResId) {
        try {
            String storeName = null;
            if (mStore == Store.GOOGLE_PLAY){
                storeName = mContext.getString(R.string.googlePlay);
            }
            else if (mStore == Store.AMAZON){
                storeName = mContext.getString(R.string.amazonStore);
            }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            String appName = null;
            try {
                appName = (String) mContext.getPackageManager().getApplicationLabel(mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0));
            } catch (PackageManager.NameNotFoundException ignored) {
            }
            alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.newUpdateAvailable));
            alertDialogBuilder.setMessage(mContext.getResources().getString(R.string.downloadFor, appName, storeName))
                    .setCancelable(true)
                    .setPositiveButton(mContext.getString(R.string.dialogPositiveButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToMarket(mContext);
                            dialog.cancel();
                        }
                    })
                    .setNeutralButton(mContext.getString(R.string.dialogNeutralButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(mContext.getString(R.string.dialogNegativeButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userHasTappedToNotShowNoticeAgain(mContext, mVersionDownloadable);
                            dialog.cancel();
                        }

                    });
            if (mDialogIconResId != 0) {
                alertDialogBuilder.setIcon(mDialogIconResId);
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (NullPointerException activityClosed) {
        /*   Happens when the library tries to open a dialog,
             but the activity is already closed, so generates a NullPointerException or IllegalStateException.
			 In this way, a force close is avoided.*/
        } catch (IllegalStateException activityClosed) {
            // See up
        }
    }

    private static void userHasTappedToNotShowNoticeAgain(Context mContext, String mVersionDownloadable) {
        SharedPreferences prefs = mContext.getSharedPreferences(UpdateChecker.PREFS_FILENAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(UpdateChecker.DONT_SHOW_AGAIN_PREF_KEY + mVersionDownloadable, true);
        editor.commit();
    }

    private static void goToMarket(Context mContext) {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.ROOT_PLAY_STORE_DEVICE + mContext.getPackageName())));

    }
}