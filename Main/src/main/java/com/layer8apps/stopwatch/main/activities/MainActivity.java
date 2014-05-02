package com.layer8apps.stopwatch.main.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.layer8apps.stopwatch.main.BuildConfig;
import com.layer8apps.stopwatch.main.R;
import com.layer8apps.stopwatch.main.fragments.LapCounterFragment;
import com.layer8apps.stopwatch.main.fragments.ResetFragment;
import com.layer8apps.stopwatch.main.fragments.StartStopFragment;
import com.layer8apps.stopwatch.main.fragments.TimeKeeperFragment;

/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Devin Collins (devin@imdevinc.com) wrote this file as part of the StopWatch
 * project. As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it, you
 * can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */

public class MainActivity extends Activity implements StartStopFragment.OnClickListener,
        ResetFragment.OnClickListener, LapCounterFragment.OnImageClickedListener {

    private StartStopFragment ssf;
    private TimeKeeperFragment tkf;
    private ResetFragment rf;
    private LapCounterFragment lcf;

    private LinearLayout llTopContainer;
    private LinearLayout llTimeKeeperContainer;
    private LinearLayout llStartContainer;
    private LinearLayout llResetContainer;
    private LinearLayout llLapCountContainer;

    private enum WindowState {
        NORMAL,
        EXPANDED
    }

    public enum RunningState {
        STOPPED,
        RUNNING
    }

    private WindowState windowState = WindowState.NORMAL;
    private RunningState runningState = RunningState.STOPPED;

    private void expandWindowLandscape() {
        Context context = this;

        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, .8f));
        llTimeKeeperContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, .2f));
        tkf.shrinkFont(context);
        lcf.increaseFont();
    }

    private void expandWindowPortrait() {
        Context context = this;

        llTopContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .7f));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .15f);
        llTimeKeeperContainer.setLayoutParams(params);
        llStartContainer.setLayoutParams(params);

        llResetContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .2f));
        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .8f));

        tkf.shrinkFont(context);
        lcf.increaseFont();
    }

    public RunningState getState() {
        return runningState;
    }

    @Override
    public void onClick(int id) {
        if (id == R.id.txtStartStop) {
            toggleTimer();
        } else if (id == R.id.txtReset) {
            toggleLap();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null) {
            ssf = new StartStopFragment();
            tkf = new TimeKeeperFragment();
            rf = new ResetFragment();
            lcf = new LapCounterFragment();

            getFragmentManager().beginTransaction()
                    .replace(R.id.flStartFrame, ssf, StartStopFragment.class.getName())
                    .replace(R.id.flTimeKeeperFrame, tkf, TimeKeeperFragment.class.getName())
                    .replace(R.id.flResetFrame, rf, ResetFragment.class.getName())
                    .replace(R.id.flLapCounterFrame, lcf, LapCounterFragment.class.getName())
                    .commit();
        } else {
            ssf = (StartStopFragment) getFragmentManager().findFragmentByTag(StartStopFragment.class.getName());
            tkf = (TimeKeeperFragment) getFragmentManager().findFragmentByTag(TimeKeeperFragment.class.getName());
            rf = (ResetFragment) getFragmentManager().findFragmentByTag(ResetFragment.class.getName());
            lcf = (LapCounterFragment) getFragmentManager().findFragmentByTag(LapCounterFragment.class.getName());

            if (savedInstanceState.containsKey("runningState")) {
                runningState = (RunningState) savedInstanceState.getSerializable("runningState");
            }

            if (savedInstanceState.containsKey("windowState")) {
                windowState = (WindowState) savedInstanceState.getSerializable("windowState");
            }
        }

        llTopContainer = (LinearLayout) findViewById(R.id.llTopContainer);
        llTimeKeeperContainer = (LinearLayout) findViewById(R.id.llTimeKeeperContainer);
        llStartContainer = (LinearLayout) findViewById(R.id.llStartContainer);
        llResetContainer = (LinearLayout) findViewById(R.id.llResetContainer);
        llLapCountContainer = (LinearLayout) findViewById(R.id.llLapCountContainer);

        if (!BuildConfig.DEBUG) {
            sendAnalyticsReport();
        }
    }

    @Override
    public void onImageClicked() {
        resizeLayout();
    }

    private void resizeLayout() {
        if (windowState == WindowState.NORMAL) {
            windowState = WindowState.EXPANDED;
        } else if (windowState == WindowState.EXPANDED) {
            windowState = WindowState.NORMAL;
        }

        updateView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("runningState", runningState);
        outState.putSerializable("windowState", windowState);
    }

    private void sendAnalyticsReport() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        Tracker t = analytics.newTracker(getResources().getString(R.string.ga_id));

        t.setScreenName(Activity.class.getName());

        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void shrinkWindowPortrait() {
        Context context = this;

        llTimeKeeperContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .2f));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .4f);
        llTopContainer.setLayoutParams(params);
        llStartContainer.setLayoutParams(params);

        llResetContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .5f));
        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .5f));

        tkf.increaseFont(context);
        lcf.shrinkFont();
    }

    private  void shrinkWindowLandscape() {
        Context context = this;

        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, .5f));
        llTimeKeeperContainer.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, .5f));

        tkf.increaseFont(context);
        lcf.shrinkFont();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void toggleLap() {
        if (runningState == RunningState.STOPPED) {
            tkf.resetCounter();
            lcf.resetLaps();
        } else if (runningState == RunningState.RUNNING) {
            String lapTime = tkf.getLapTime();
            lcf.addLap(lapTime);
            tkf.resetLap();
        }
    }

    private void toggleTimer() {
        if (runningState == RunningState.STOPPED) {
            runningState = RunningState.RUNNING;
        } else {
            runningState = RunningState.STOPPED;
        }

        updateFragmentViews();
    }

    private void updateFragmentViews() {
        rf.updateView();
        ssf.updateView();
        lcf.updateView();
        tkf.updateTimer(true);
    }

    private void updateView() {
        if (windowState == WindowState.EXPANDED && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            expandWindowPortrait();
        } else if (windowState == WindowState.NORMAL && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            shrinkWindowPortrait();
        } else if (windowState == WindowState.EXPANDED && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            expandWindowLandscape();
        } else if (windowState == WindowState.NORMAL && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            shrinkWindowLandscape();
        }
    }
}
