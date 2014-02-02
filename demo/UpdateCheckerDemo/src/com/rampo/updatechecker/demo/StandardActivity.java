package com.rampo.updatechecker.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rampo.updatechecker.Notice;
import com.rampo.updatechecker.UpdateChecker;

public class StandardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standard_activity);
        getActionBar().setSubtitle(R.string.standard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.xml.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, Infos.class));
        return super.onOptionsItemSelected(item);

    }

    public void dialog(View view) {
        UpdateChecker checker = new UpdateChecker(this);
        checker.start();
    }

    public void dialog_custom(View view) {
        UpdateChecker checker = new UpdateChecker(this);
        checker.setNoticeIcon(R.drawable.ic_action_info);
        checker.start();
    }

    public void notification(View view) {
        UpdateChecker checker = new UpdateChecker(this);
        checker.setNotice(Notice.NOTIFICATION);
        checker.start();
    }

    public void notification_custom(View view) {
        UpdateChecker checker = new UpdateChecker(this);
        checker.setNotice(Notice.NOTIFICATION);
        checker.setNoticeIcon(R.drawable.ic_action_info);
        checker.start();
    }

    public void go_to_custom_impl(View view) {
        startActivity(new Intent(this, CustomActivity.class));
    }

}
