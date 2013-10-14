package com.actionbarsherlock.sample.demos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rampo.updatechecker.UpdateChecker;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.xml.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, Infos.class));
		return super.onOptionsItemSelected(item);

	}

	public void dialog(View view) {
		UpdateChecker.checkForDialog(this, 1);

	}

	public void notification(View view) {
		UpdateChecker.checkForNotification(this, 1);

	}

	public void notification_custom(View view) {
		UpdateChecker.checkForNotification(R.drawable.ic_launcher, this, 1);

	}
}
