package com.rampo.updatechecker.demo;

import com.rampo.updatechecker.UpdateChecker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		UpdateChecker.CheckForNotification(this);
	}
}
