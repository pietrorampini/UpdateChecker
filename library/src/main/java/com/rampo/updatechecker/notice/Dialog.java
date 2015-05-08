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
package com.rampo.updatechecker.notice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.rampo.updatechecker.R;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.store.Store;

/**
 * Builds and show a Dialog if a new update has been found. This is the default Notice.
 * I've used the old AlertDialog API because newer APIs require FragmentActivity.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 * @see Notice#DIALOG
 */
public class Dialog {

    public static void show(final Context context, final Store store, final String versionDownloadable, final int dialogIconResId) {
        try {
            String storeName = null;
            if (store == Store.GOOGLE_PLAY) {
                storeName = context.getString(R.string.googlePlay);
            } else if (store == Store.AMAZON) {
                storeName = context.getString(R.string.amazonStore);
            }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            String appName = null;
            try {
                appName = (String) context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0));
            } catch (PackageManager.NameNotFoundException ignored) {
            }
            alertDialogBuilder.setTitle(context.getResources().getString(R.string.newUpdateAvailable));
            alertDialogBuilder.setMessage(context.getResources().getString(R.string.downloadFor, appName, storeName))
                    .setCancelable(true)
                    .setPositiveButton(context.getString(R.string.dialogPositiveButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToMarket(context);
                            dialog.cancel();
                        }
                    })
                    .setNeutralButton(context.getString(R.string.dialogNeutralButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(context.getString(R.string.dialogNegativeButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userHasTappedToNotShowNoticeAgain(context, versionDownloadable);
                            dialog.cancel();
                        }

                    });
            if (dialogIconResId != 0) {
                alertDialogBuilder.setIcon(dialogIconResId);
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
        /*   Happens when the library tries to open a dialog,
             but the activity is already closed, so generates a NullPointerException, IllegalStateException or BadTokenException.
			 In this way, a force close is avoided.*/
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
