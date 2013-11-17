package com.rampo.updatechecker;

/**
 * Created by Rampo on 21/10/13.
 */
public interface UpdateCheckerResult {
    public void foundUpdateAndShowIt(String versionDownloadable);
    public void foundUpdateAndDontShowIt(String versionDownloadable);
    public void upToDate();
}
