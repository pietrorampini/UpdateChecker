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

/**
 * Type of notice used to alert the user if a new update has been found. Settable with setNotice(Notice). Default is Dialog.
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 * @see com.rampo.updatechecker.UpdateChecker#setNotice(Notice)
 */
public class Notice {
    public static final Notice NOTIFICATION = new Notice(0);
    public static final Notice DIALOG = new Notice(1);

    int mNotice;

    public Notice(int notice) {
        mNotice = notice;
    }
}