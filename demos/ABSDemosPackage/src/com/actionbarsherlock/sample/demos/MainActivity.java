package com.actionbarsherlock.sample.demos;

import com.rampo.updatechecker.UpdateChecker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		UpdateChecker.CheckForDialog(this);
		// or UpdateChecker.CheclForNotification(this);
		// or UpdateChecker.CheclForNotification(this, R.drawable.ic_launcher);

		TextView credits = (TextView) findViewById(R.id.credits);
		credits.setText(Html.fromHtml(getString(R.string.credits, createLink(getString(R.string.profile_url), "Pietro Rampini"))));
		credits.setMovementMethod(LinkMovementMethod.getInstance());
		
		String gitHubLink = createLink(getString(R.string.repo_url), "GitHub");
		TextView attrs = (TextView) findViewById(R.id.attributions);
		attrs.setText(Html.fromHtml(getString(R.string.attributions, createLink("http://www.apache.org/licenses/LICENSE-2.0 ", "Apache License, V2"), gitHubLink)));
		attrs.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView info = (TextView) findViewById(R.id.info);
		info.setText(Html.fromHtml(getString(R.string.info, createLink("https://github.com/JakeWharton/ActionBarSherlock", "ActionBarSherlock"))));
		info.setMovementMethod(LinkMovementMethod.getInstance());
		
		TextView feedback = (TextView) findViewById(R.id.feedback);
		feedback.setText(Html.fromHtml(getString(R.string.feedback, gitHubLink)));
		feedback.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private String createLink(String url, String title) {
		return String.format("<a href=\"%s\">%s</a>", url, title);
	}
}
