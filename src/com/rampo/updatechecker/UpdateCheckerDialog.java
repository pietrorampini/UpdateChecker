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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class UpdateCheckerDialog extends SimpleDialogFragment {
    public static void show(FragmentActivity activity) {
        new UpdateCheckerDialog().show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public Builder build(Builder builder) throws PackageManager.NameNotFoundException {
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            dismiss();
        }
        final SharedPreferences.Editor editor = prefs.edit();
        builder.setTitle
                (context.getString(R.string.newUpdataAvailable));
        builder.setMessage(context.getString(R.string.downloadFor) + context.getPackageManager().getApplicationLabel(context.getPackageManager().getApplicationInfo(context.getPackageName(), 0)));
        builder.setPositiveButton("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMarket();
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dismiss();
            }
        });
        builder.setNeutralButton("Later", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setNegativeButton("No Thanks", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
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
