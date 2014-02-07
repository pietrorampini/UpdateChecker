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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Builds a notification to alert the user if a new update is found.
 * @see com.rampo.updatechecker.Notice#NOTIFICATION
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public class Notification {
    public static void show(Context mContext, Store mStore, int mNotificationIconResId) {
        android.app.Notification notification;
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateChecker.ROOT_PLAY_STORE_DEVICE + mContext.getPackageName()));
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, myIntent, Intent.FILL_IN_ACTION);
        String appName = null;
        try {
            appName = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), 0).loadLabel(mContext.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setTicker(mContext.getString(R.string.newUpdateAvailable))
                .setContentTitle(appName)
                .setContentText(mContext.getString(R.string.newUpdateAvailable))
                .setContentIntent(pendingIntent).build();

        if (mNotificationIconResId == 0) {
            if (mStore == Store.GOOGLE_PLAY) {
                builder.setSmallIcon(R.drawable.ic_stat_play_store);
            } else if (mStore == Store.AMAZON) {
                builder.setSmallIcon(R.drawable.ic_stat_amazon);
            }
        } else {
            builder.setSmallIcon(mNotificationIconResId);
        }
        notification = builder.build();
        notification.flags = android.app.Notification.FLAG_AUTO_CANCEL;
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
