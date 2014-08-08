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
package com.rampo.updatechecker.store;

public class GitHubGradleProperties extends VersionDownloadableSource {
    private static String mLink;
    private static int mVersionToCompare;
    public static int VERSION_NAME;
    public static int VERSION_CODE;
    private static String VERSION_NAME_PREFIX = "VERSION_NAME=";
    private static String VERSION_CODE_PREFIX = "VERSION_CODE=";

    public static void setPage(String link) {
        mLink = link;
    }

    public static String getPage() {
        return mLink;
    }

    public static void setVersionToCompare(int typeOfVersion) {
        mVersionToCompare = typeOfVersion;
    }

    public static int getVersionToCompare() {
        return mVersionToCompare;
    }
}
