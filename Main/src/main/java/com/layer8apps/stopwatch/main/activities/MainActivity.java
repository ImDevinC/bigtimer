package com.layer8apps.stopwatch.main.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
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

    private enum WindowState {
        NORMAL,
        EXPANDED
    }

    private StartStopFragment ssf;
    private TimeKeeperFragment tkf;
    private ResetFragment rf;
    private LapCounterFragment lcf;

    private LinearLayout contLapFragment;

    private WindowState currentState = WindowState.NORMAL;

    private  LinearLayout.LayoutParams PARAMS = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 8f);

    private void expandWindow() {
        contLapFragment.setLayoutParams(PARAMS);
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

        contLapFragment = (LinearLayout) findViewById(R.id.contLapFragment);

        sendAnalyticsReport();
    }

    @Override
    public void onImageClicked() {
        if (currentState == WindowState.NORMAL) {
            expandWindow();
        } else {
//            shrinkWindow();
        }
    }

    private void sendAnalyticsReport() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        Tracker t = analytics.newTracker(getResources().getString(R.string.ga_id));

        t.setScreenName(Activity.class.getName());

        t.send(new HitBuilders.AppViewBuilder().build());
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
