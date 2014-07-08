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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.rampo.updatechecker.R;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.store.Store;

/**
 * Builds and show a Notification if a new update has been found.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 * @see Notice#NOTIFICATION
 */
public class Notification {
    public static void show(Context context, Store store, int notificationIconResId) {
        android.app.Notification notification;
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.ROOT_PLAY_STORE_DEVICE + context.getPackageName()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
        String appName = null;
        try {
            appName = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(context.getString(R.string.newUpdateAvailable))
                .setContentTitle(appName)
                .setContentText(context.getString(R.string.newUpdateAvailable))
                .setContentIntent(pendingIntent).build();

        if (notificationIconResId == 0) {
            if (store == Store.GOOGLE_PLAY) {
                builder.setSmallIcon(R.drawable.ic_stat_play_store);
            } else if (store == Store.AMAZON) {
                builder.setSmallIcon(R.drawable.ic_stat_amazon);
            }
        } else {
            builder.setSmallIcon(notificationIconResId);
        }
        notification = builder.build();
        notification.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}