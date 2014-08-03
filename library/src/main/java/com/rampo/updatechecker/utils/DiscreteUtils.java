package com.rampo.updatechecker.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.util.TypedValue;

public class DiscreteUtils {

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

    public static int getSoftbuttonsbarHeight(Activity activity) {
        if (!getInternalBoolean(activity.getResources(), SHOW_NAV_BAR_RES_NAME)) {
            return 0;
        }
        return getInternalDimensionSize(activity.getResources(), NAV_BAR_HEIGHT_RES_NAME);
    }

    public static int getSoftbuttonsbarWidth(Activity activity) {
        if (!getInternalBoolean(activity.getResources(), SHOW_NAV_BAR_RES_NAME)) {
            return 0;
        }
        return getInternalDimensionSize(activity.getResources(), NAV_BAR_WIDTH_RES_NAME);
    }

    public static int getStatusBarHeight(Activity activity) {
        return getInternalDimensionSize(activity.getResources(), STATUS_BAR_HEIGHT_RES_NAME);
    }


    public static int getActionStatusBarHeight(Activity activity) {
        return getStatusBarHeight(activity) + getActionBarHeight(activity);
    }

    public static int getActionBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TypedValue tv = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            }
        }
        return 0;
    }

    public static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static boolean getInternalBoolean(Resources res, String key) {
        int resourceId = res.getIdentifier(key, "bool", "android");
        return (resourceId > 0) ? res.getBoolean(resourceId) : false;
    }

    public static boolean hasFlag(int flags, int flag) {
        return (flags & flag) == flag;
    }
}
