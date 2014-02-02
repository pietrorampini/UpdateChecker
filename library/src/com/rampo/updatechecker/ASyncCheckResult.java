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
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 */
public interface ASyncCheckResult {
    /**
     * versionName got from Play Store. Go back to fragment.
     *
     * @param versionDownloadable
     */
    public void versionDownloadableFound(String versionDownloadable);

    /**
     * Can't get the versionName from Play Store
     * Connection error or #1
     *
     * @see <a href="https://github.com/rampo/UpdateChecker/issues/1">Issue #1</a>
     */
    public void multipleApksPublished();
    public void networkError();
}