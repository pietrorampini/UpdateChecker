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

/**
 * Returns the result of ASyncCheck to UpdateChecker.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public interface ASyncCheckResult {
    /**
     * If the library found a version available on the Store, and it's different from the installed one, notify it to the user.
     *
     * @param versionDownloadable String to compare to the version installed of the app.
     */
    public void versionDownloadableFound(String versionDownloadable);

    /**
     * Can't get the versionName from the Store.
     * See #1
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    public void multipleApksPublished();

    /**
     * Can't download the store page.
     */
    public void networkError();

    /**
     * Can't find the store page for this app.
     */
    public void appUnpublished();

    /**
     * The check returns null for new version downloadble
     */
    public void storeError();

}