package com.rampo.updatechecker;

/**
 * Created by Rampo on 20/10/13.
 */
public class Store {
    public static final Store GOOGLE_PLAY = new Store(0);
    public static final Store AMAZON = new Store(1);

    int mStore;

    public Store(int store) {
        mStore = store;
    }
}
