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

import android.app.Activity;
import android.content.pm.PackageManager;

public class Comparator {
    /**
     * Compare the string versionDownloadable to the version installed of the app.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     */
    public static boolean isVersionDownloadableNewer(Activity mActivity, String versionDownloadable) {
        String versionInstalled = null;
        try {
            versionInstalled = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if (versionInstalled.equals(versionDownloadable)) { // If it is equal, no new version downloadable
            return false;
        } else {
            return versionCompareNumerically(versionDownloadable, versionInstalled) > 0; // Return if the versionDownloadble is newer than the installed
        }
    }

    /**
     * https://stackoverflow.com/questions/6701948/efficient-way-to-compare-version-strings-in-java
     * <p/>
     * Compares two version strings.
     * <p/>
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     * The result is a positive integer if str1 is _numerically_ greater than str2.
     * The result is zero if the strings are _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public static Integer versionCompareNumerically(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        try {
            // compare first non-equal ordinal number
            if (i < vals1.length && i < vals2.length) {
                int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
                return Integer.signum(diff);
            }
            // the strings are equal or one string is a substring of the other
            // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
            else {
                return Integer.signum(vals1.length - vals2.length);
            }
        } catch (NumberFormatException e) {
            // Possibly there are different versions of the app in the store, so we can't check.
            return 0;
        }
    }

}
