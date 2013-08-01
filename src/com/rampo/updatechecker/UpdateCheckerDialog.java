package com.rampo.updatechecker;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class UpdateCheckerDialog extends SimpleDialogFragment {
    public static void show(FragmentActivity activity) {
        new UpdateCheckerDialog().show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public Builder build(Builder builder) {
        builder.setTitle("aaa");
        builder.setMessage("");
        builder.setPositiveButton("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setNegativeButton("", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder;
    }
}
