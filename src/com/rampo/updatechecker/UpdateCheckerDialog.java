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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
/**
 * Extends SimpleDialogFragment class of StyledDialogs: https://github.com/inmite/android-styled-dialogs
 * @see SimpleDialogFragment
 */
public class UpdateCheckerDialog extends SimpleDialogFragment {
    final static String sharedPreferencesGeneralKey = "updateChecker";
    final static String sharedPreferencesDontShowAgainKey = "dontshowagain";

    /**
     * Show this Dialog if you have added the method UpdateChecker.CheckForDialog(); in a FragmentActivity
     * @see UpdateChecker#CheckForDialog(android.support.v4.app.FragmentActivity)
     * @see FragmentActivity
     */
    public static void show(FragmentActivity activity) {
        new UpdateCheckerDialog().show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public Builder build(Builder builder) throws NameNotFoundException {
        Context context = getActivity().getApplicationContext();
        String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        String appName = (String) context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0));
        SharedPreferences prefs = context.getSharedPreferences(sharedPreferencesGeneralKey + versionName, 0);

        if (prefs.getBoolean(sharedPreferencesDontShowAgainKey, false)) {dismiss();}
        final SharedPreferences.Editor editor = prefs.edit();
        builder.setTitle(context.getString(R.string.newUpdataAvailable));
        builder.setMessage(context.getString(R.string.downloadFor) + appName);
        builder.setPositiveButton(context.getString(R.string.dialogPositiveButton), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarket();
                if (editor != null) {
                    editor.putBoolean(sharedPreferencesDontShowAgainKey, true);
                    editor.commit();
                }
                dismiss();
            }
        });
        builder.setNeutralButton(context.getString(R.string.dialogNeutralButton), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    Long date_firstLaunch = System.currentTimeMillis();
                    editor.putLong("date_firstlaunch", date_firstLaunch);
                    editor.commit();
                }
                dismiss();
            }
        });

        builder.setNegativeButton(context.getString(R.string.dialogNegativeButton), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(sharedPreferencesDontShowAgainKey, true);
                    editor.commit();
                }
                dismiss();
            }
        });
        return builder;
    }

    private void goToMarket() {
        Context context = getActivity().getApplicationContext();
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rootPlayStoreDevice) + context.getPackageName())));
    }
}
