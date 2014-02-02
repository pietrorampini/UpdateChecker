package com.rampo.updatechecker.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.UpdateCheckerResult;

public class CustomActivity extends Activity implements UpdateCheckerResult {
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle(R.string.custom);
        result = (TextView) findViewById(R.id.result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.xml.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.info:
                startActivity(new Intent(this, Infos.class));
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void custom_impl(View view) {
        UpdateChecker checker = new UpdateChecker(this, this);
        checker.setSuccessfulChecksRequired(2);
        checker.start();
        result.setText(R.string.loading);
    }

    /**
     * mVersionDonwloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param mVersionDonwloadable version downloadable from the Store.
     * @see com.rampo.updatechecker.UpdateChecker#setSuccessfulChecksRequired(int)
     */
    @Override
    public void foundUpdateAndShowIt(String mVersionDonwloadable) {
        result.setText("Update available\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    /**
     * mVersionDonwloadable isn't equal to manifest versionName -> New update available.
     * Show the Notice because it's the first time or the number of the checks made is a multiple of the argument of setSuccessfulChecksRequired(int) method. (If you don't call setSuccessfulChecksRequired(int) the default is 5).
     *
     * @param mVersionDonwloadable version downloadable from the Store.
     * @see com.rampo.updatechecker.UpdateChecker#setSuccessfulChecksRequired(int)
     */
    @Override
    public void foundUpdateAndDontShowIt(String mVersionDonwloadable) {
        result.setText("Already Shown\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    /**
     * mVersionDonwloadable is equal to manifest versionName -> No new update available.
     * Don't show the Notice
     *
     * @param mVersionDonwloadable version downloadable from the Store.
     */
    @Override
    public void upToDate(String mVersionDonwloadable) {
        result.setText("Updated\n" + "Version downloadable: " + mVersionDonwloadable + "\nVersion installed: " + mVersionInstalled());
    }

    public String mVersionInstalled() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return null;
    }
}
