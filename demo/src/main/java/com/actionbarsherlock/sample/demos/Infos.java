package com.actionbarsherlock.sample.demos;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class Infos extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setSubtitle("Infos");

        TextView credits = (TextView) findViewById(R.id.credits);
        credits.setText(Html.fromHtml(getString(R.string.credits, createLink(getString(R.string.profile_url), "Pietro Rampini"))));
        credits.setMovementMethod(LinkMovementMethod.getInstance());

        String gitHubLink = createLink(getString(R.string.repo_url), "GitHub");
        TextView attrs = (TextView) findViewById(R.id.attributions);
        attrs.setText(Html.fromHtml(getString(R.string.attributions, createLink("http://www.apache.org/licenses/LICENSE-2.0 ", "Apache License, V2"), gitHubLink)));
        attrs.setMovementMethod(LinkMovementMethod.getInstance());

        TextView info = (TextView) findViewById(R.id.abs);
        info.setText(Html.fromHtml(getString(R.string.infos, createLink("https://github.com/JakeWharton/ActionBarSherlock", "ActionBarSherlock"))));
        info.setMovementMethod(LinkMovementMethod.getInstance());

        TextView feedback = (TextView) findViewById(R.id.feedback);
        feedback.setText(Html.fromHtml(getString(R.string.feedback, gitHubLink)));
        feedback.setMovementMethod(LinkMovementMethod.getInstance());

        TextView androidicons = (TextView) findViewById(R.id.androidicons);
        androidicons.setText(Html.fromHtml(getString(R.string.androidicons, createLink("http://www.androidicons.com/", "Androidicons.com"))));
        androidicons.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String createLink(String url, String title) {
        return String.format("<a href=\"%s\">%s</a>", url, title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}