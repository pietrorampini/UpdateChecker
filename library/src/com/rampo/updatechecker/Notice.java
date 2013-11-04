package com.rampo.updatechecker;

/**
 * Created by Rampo on 20/10/13.
 */
public class Notice {
    public static final Notice NOTIFICATION = new Notice(0);
    public static final Notice DIALOG = new Notice(1);

    int mNotice;

    public Notice(int notice) {
        mNotice = notice;
    }
}
