package com.rampo.updatechecker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
/**
 * Created by Rampo on 18/08/13.
 */
@SuppressWarnings("static-access")
public class Notification {

    public static void show(Context context, int notificationIconResIdPublic) {
        android.app.Notification noti;
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.rootPlayStoreDevice) + context.getPackageName()));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FILL_IN_ACTION);
        String appName = null;
        try {
            appName = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if (notificationIconResIdPublic == 0) {
            noti = new NotificationCompat.Builder(context)
                    .setTicker(context.getString(R.string.newUpdataAvailable))
                    .setContentTitle(appName)
                    .setContentText(context.getString(R.string.newUpdataAvailable))
                    .setSmallIcon(R.drawable.ic_stat_ic_menu_play_store)
                    .setContentIntent(pendingIntent).build();
        } else {
            noti = new NotificationCompat.Builder(context)
                    .setTicker(context.getString(R.string.newUpdataAvailable))
                    .setContentTitle(appName)
                    .setContentText(context.getString(R.string.newUpdataAvailable))
                    .setSmallIcon(notificationIconResIdPublic)
                    .setContentIntent(pendingIntent).build();
        }
        noti.flags = android.app.Notification.FLAG_AUTO_CANCEL;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noti);
    }
}
