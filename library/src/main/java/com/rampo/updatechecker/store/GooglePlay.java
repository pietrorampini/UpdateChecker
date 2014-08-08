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

public class GooglePlay extends VersionDownloadableSource {
    public static final String PLAY_STORE_ROOT_WEB = "https://play.google.com/store/apps/details?id=";
    public static final String PLAY_STORE_HTML_TAGS_TO_GET_RIGHT_POSITION = "itemprop=\"softwareVersion\"> ";
    public static final String PLAY_STORE_HTML_TAGS_TO_REMOVE_USELESS_CONTENT = "  </div> </div>";
    public static final String PLAY_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER = "We're sorry, the requested URL was not found on this server.";
}
