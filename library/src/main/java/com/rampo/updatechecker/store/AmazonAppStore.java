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

public class AmazonAppStore extends VersionDownloadableSource {
    public static final String AMAZON_STORE_ROOT_WEB = "http://www.amazon.com/gp/mas/dl/android?p=";
    public static final String AMAZON_STORE_HTML_TAGS_TO_GET_RIGHT_LINE = "<li><strong>Version:</strong>";
    public static final String AMAZON_STORE_PACKAGE_NOT_PUBLISHED_IDENTIFIER = "<title>Amazon.com: Apps for Android</title>";
}
