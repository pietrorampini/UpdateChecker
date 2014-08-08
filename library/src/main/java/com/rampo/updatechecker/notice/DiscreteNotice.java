package com.rampo.updatechecker.notice;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.InflateException;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rampo.updatechecker.R;
import com.rampo.updatechecker.utils.DiscreteUtils;

/**
 * Builds and show a Notice like DiscreteAppRater does. Credits to Nicolas Pomepuy
 * See https://github.com/PomepuyN/discreet-app-rate for more info
 * Based on https://github.com/PomepuyN/discreet-app-rate/blob/master/DiscreetAppRate/src/main/java/fr/nicolaspomepuy/discreetapprate/AppRate.java
 *
 * @author Pietro Rampini (rampini.pietro@gmail.com)
 * @since 3.0.0
 */
// se how to use it
public class DiscreteNotice extends Notice {
    private static int view;
    private static ViewGroup mainView;
    private static int mDelay;
    private static boolean mFromTop;
    private static int mTheme;
    public static boolean TOP = true;
    public static boolean BOTTOM = false;
    public static int LIGHT = 0;
    public static int DARK = 1;

    public static void setTheme(int theme) {
        mTheme = theme;
    }

    public static void setGravity(boolean gravity) {
        mFromTop = gravity;
    }

    public static void setDelay(int delay) {
        mDelay = delay;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static void show(final Activity activity) {
        if (view != 0) {
            mainView = new FrameLayout(activity);
            try {
                activity.getLayoutInflater().inflate(view, mainView);
            } catch (InflateException e) {
                mainView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.discrete_notice, null);
                view = 0;
            } catch (Resources.NotFoundException e) {
                mainView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.discrete_notice, null);
                view = 0;
            }
        } else {
            mainView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.discrete_notice, null);
        }


        View close = mainView.findViewById(R.id.dar_close);
        TextView text = (TextView) mainView.findViewById(R.id.text_discrete);
        ViewGroup container = (ViewGroup) mainView.findViewById(R.id.dar_container);

        if (container != null) {
            if (mFromTop) {
                if (container.getParent() instanceof FrameLayout) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) container.getLayoutParams();
                    lp.gravity = Gravity.TOP;
                    container.setLayoutParams(lp);
                } else if (container.getParent() instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) container.getLayoutParams();
                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    container.setLayoutParams(lp);
                }
            } else {
                if (container.getParent() instanceof FrameLayout) {
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) container.getLayoutParams();
                    lp.gravity = Gravity.BOTTOM;
                    container.setLayoutParams(lp);
                } else if (container.getParent() instanceof RelativeLayout) {
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) container.getLayoutParams();
                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    container.setLayoutParams(lp);
                }
            }
        }

        if (text != null) {
            text.setText("New version ready to install");
            final String packageName = activity.getApplicationContext().getPackageName();
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    hideAllViews(activity, mFromTop, mainView);
                    //editor.putBoolean(KEY_CLICKED, true);
                    //commitEditor();
                    //TODO
                }
            });
        }

        if (close != null) {
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAllViews(activity, mFromTop, mainView);
                }
            });

        }
        if (view == 0) {
            if (mTheme == 0) { // theme light
                PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
                Drawable d = activity.getResources().getDrawable(R.drawable.ic_action_remove);
                d.setColorFilter(Color.BLACK, mMode);
                ((ImageView) close).setImageDrawable(d);
                text.setTextColor(Color.BLACK);
                container.setBackgroundColor(0X88ffffff);
                if (Build.VERSION.SDK_INT >= 16) {
                    close.setBackground(activity.getResources().getDrawable(R.drawable.selectable_button_light));
                } else {
                    close.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selectable_button_light));
                }

            } else { // theme dark
                Drawable d = activity.getResources().getDrawable(R.drawable.ic_action_remove);
                d.clearColorFilter();
                ((ImageView) close).setImageDrawable(d);
                container.setBackgroundColor(0Xaa000000);
                if (Build.VERSION.SDK_INT >= 16) {
                    close.setBackground(activity.getResources().getDrawable(R.drawable.selectable_button_dark));
                } else {
                    close.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.selectable_button_dark));
                }

            }
        }
        // Manage translucent themes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && container != null) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            if (mFromTop) {
                boolean isTranslucent = DiscreteUtils.hasFlag(winParams.flags, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (isTranslucent) {
                    if (container.getParent() instanceof FrameLayout) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) container.getLayoutParams();
                        lp.topMargin = DiscreteUtils.getActionStatusBarHeight(activity);
                        container.setLayoutParams(lp);
                    } else if (container.getParent() instanceof RelativeLayout) {
                        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) container.getLayoutParams();
                        lp.topMargin = DiscreteUtils.getActionStatusBarHeight(activity);
                        container.setLayoutParams(lp);
                    }
                }
            } else {
                boolean isTranslucent = DiscreteUtils.hasFlag(winParams.flags, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                if (isTranslucent) {
                    Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                    int orientation = display.getRotation();
                    ViewGroup.MarginLayoutParams lp = null;
                    if (container.getParent() instanceof FrameLayout) {
                        lp = (FrameLayout.LayoutParams) container.getLayoutParams();
                    } else if (container.getParent() instanceof RelativeLayout) {
                        lp = (RelativeLayout.LayoutParams) container.getLayoutParams();
                    }
                    if (lp != null) {
                        switch (orientation) {
                            case Surface.ROTATION_0:
                            case Surface.ROTATION_180:
                                lp.bottomMargin = DiscreteUtils.getSoftbuttonsbarHeight(activity);
                                container.setLayoutParams(lp);
                                break;
                            case Surface.ROTATION_90:
                            case Surface.ROTATION_270:
                                lp.rightMargin = DiscreteUtils.getSoftbuttonsbarWidth(activity);
                                container.setLayoutParams(lp);
                                break;
                        }
                    }
                }
            }
        }


        if (mDelay > 0) {
            activity.getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    displayViews(activity, mFromTop, mainView);
                }
            }, mDelay);
        } else {
            displayViews(activity, mFromTop, mainView);
        }

    }


    private static void displayViews(Activity activity, boolean fromTop, ViewGroup mainView) {
        activity.addContentView(mainView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (mainView != null) {
            Animation fadeInAnimation;
            if (fromTop) {
                fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_from_top);
            } else {
                fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
            }
            mainView.startAnimation(fadeInAnimation);
        }

    }

    private static void hideAllViews(Activity activity, boolean fromTop, final ViewGroup mainView) {
        Animation hideAnimation;
        if (fromTop) {
            hideAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_out_from_top);
        } else {
            hideAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
        }

        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainView.removeAllViews();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mainView.startAnimation(hideAnimation);
    }
}
