package com.rampo.updatechecker.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.rampo.updatechecker.UpdateChecker;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateChecker.CheckForDialog(this);
        // or UpdateChecker.CheckForNotification(this);
        // or UpdateChecker.CheckForNotification(this, R.drawable.ic_launcher);
    }
}
