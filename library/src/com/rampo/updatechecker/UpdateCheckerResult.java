/*
 * Copyright (C) 2014 Pietro Rampini - PiKo Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
package com.rampo.updatechecker;

/**
 * Created by Rampo on 21/10/13.
 */
public interface UpdateCheckerResult {
    public void foundUpdateAndShowIt(String versionDownloadable);
    public void foundUpdateAndDontShowIt(String versionDownloadable);
    public void upToDate();
}
