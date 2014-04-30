package com.layer8apps.stopwatch.main.activities;

import android.app.Activity;
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

/**
 * Created by devin on 4/29/14.
 */
public class MainActivity extends Activity implements StartStopFragment.OnClickListener,
        ResetFragment.OnClickListener, LapCounterFragment.OnImageClickedListener {

    private StartStopFragment ssf;
    private TimeKeeperFragment tkf;
    private ResetFragment rf;
    private LapCounterFragment lcf;

    private LinearLayout llTopContainer;
    private LinearLayout llMiddleContainer;
    private LinearLayout llBottomContainer;
    private LinearLayout llResetContainer;
    private LinearLayout llLapCountContainer;

    private enum WindowState {
        NORMAL,
        EXPANDED
    }

    private WindowState state = WindowState.NORMAL;

    private void expandWindow() {
        llTopContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .7f));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .15f);
        llMiddleContainer.setLayoutParams(params);
        llBottomContainer.setLayoutParams(params);

        llResetContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .2f));
        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .8f));

        tkf.shrinkFont();
        lcf.increaseFont();

        state = WindowState.EXPANDED;
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

        ssf = (StartStopFragment) getFragmentManager().findFragmentById(R.id.fragStartStop);

        if (ssf == null) {
            ssf = new StartStopFragment();
        }

        tkf = (TimeKeeperFragment) getFragmentManager().findFragmentById(R.id.fragTimeKeeper);

        if (tkf == null) {
            tkf = new TimeKeeperFragment();
        }

        rf = (ResetFragment) getFragmentManager().findFragmentById(R.id.fragReset);

        if (rf == null) {
            rf = new ResetFragment();
        }

        lcf = (LapCounterFragment) getFragmentManager().findFragmentById(R.id.fragLapCount);

        if (lcf == null) {
            lcf = new LapCounterFragment();
        }

        llTopContainer = (LinearLayout) findViewById(R.id.llTopContainer);
        llMiddleContainer = (LinearLayout) findViewById(R.id.llMiddleContainer);
        llBottomContainer = (LinearLayout) findViewById(R.id.llBottomContainer);
        llResetContainer = (LinearLayout) findViewById(R.id.llResetContainer);
        llLapCountContainer = (LinearLayout) findViewById(R.id.llLapCountContainer);

        if (!BuildConfig.DEBUG) {
            sendAnalyticsReport();
        }
    }

    @Override
    public void onImageClicked() {
        if (state == WindowState.NORMAL) {
            expandWindow();
        } else {
            shrinkWindow();
        }
    }

    private void sendAnalyticsReport() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        Tracker t = analytics.newTracker(getResources().getString(R.string.ga_id));

        t.setScreenName(Activity.class.getName());

        t.send(new HitBuilders.AppViewBuilder().build());
    }

    private void shrinkWindow() {
        llMiddleContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .2f));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, .4f);
        llTopContainer.setLayoutParams(params);
        llBottomContainer.setLayoutParams(params);

        llResetContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .5f));
        llLapCountContainer.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, .5f));

        tkf.increaseFont();
        lcf.shrinkFont();

        state = WindowState.NORMAL;
    }

    private void toggleLap() {
        if (ssf.getState() == StartStopFragment.State.STARTED) {
            String lapTime = tkf.getLapTime();
            lcf.addLap(lapTime);
            tkf.resetLap();
        } else if (ssf.getState() == StartStopFragment.State.STOPPED) {
            tkf.resetCounter();
            lcf.resetLaps();
        }
    }

    private void toggleTimer() {
        if (ssf.getState() == StartStopFragment.State.STARTED) {
            tkf.startTimer();
            rf.enableLapMode();
            lcf.startTimer();
        } else {
            tkf.stopTimer();
            rf.enableResetMode();
            lcf.stopTimer();
        }
    }
}
