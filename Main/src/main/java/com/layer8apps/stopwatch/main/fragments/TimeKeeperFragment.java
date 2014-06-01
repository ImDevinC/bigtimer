package com.layer8apps.stopwatch.main.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.layer8apps.stopwatch.main.R;
import com.layer8apps.stopwatch.main.activities.MainActivity;

import java.util.concurrent.TimeUnit;


/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Devin Collins (devin@imdevinc.com) wrote this file as part of the StopWatch
 * project. As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it, you
 * can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */

public class TimeKeeperFragment extends Fragment {

    private final String START_TOTAL_TIME = "Total: 00:00.0";
    private final String START_LAP_TIME = "00:00.0";

    private long lStartTime = 0L;
    private long lCurrentTime = 0L;
    private long lTimeSwap = 0L;
    private long lTotalTime = 0L;
    private long lTimeSaved = 0L;
    private Handler handler = new Handler();

    private TextView txtTotalTime;
    private TextView txtCurrentTime;
    private MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (MainActivity) activity;
    }

    private void displayTime() {
        String result = formatTimeString(lCurrentTime);
        txtCurrentTime.setText(result);

        result = formatTimeString(lTotalTime);
        txtTotalTime.setText("Total: " + result);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.containsKey("startTime")) {
            lStartTime = savedInstanceState.getLong("startTime");
        }
        if (savedInstanceState.containsKey("currentTime")) {
            lCurrentTime = savedInstanceState.getLong("currentTime");
        }
        if (savedInstanceState.containsKey("timeSwap")) {
            lTimeSwap = savedInstanceState.getLong("timeSwap");
        }
        if (savedInstanceState.containsKey("totalTime")) {
            lTotalTime = savedInstanceState.getLong("totalTime");
        }
        if (savedInstanceState.containsKey("timeSaved")) {
            lTimeSaved = savedInstanceState.getLong("timeSaved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        if (view == null) {
            return null;
        }

        txtTotalTime = (TextView) view.findViewById(R.id.txtTotalTime);
        txtCurrentTime = (TextView) view.findViewById(R.id.txtThisLap);

        updateTimer(false);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("startTime", lStartTime);
        outState.putLong("currentTime", lCurrentTime);
        outState.putLong("timeSwap", lTimeSwap);
        outState.putLong("totalTime", lTotalTime);
        outState.putLong("timeSaved", lTimeSaved);
    }

    private String formatTimeString(long time) {
        String result = String.format("%02d:%02d.%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)),
                TimeUnit.MILLISECONDS.toMillis(time) -
                        TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(time))
        );

        if (result.lastIndexOf(".") > 0) {
            int iMs = Integer.parseInt(result.substring(result.lastIndexOf(".") + 1));

            if (iMs < 100) {
                iMs = 0;
            } else {
                iMs = iMs / 100;
            }

            result = result.substring(0, result.lastIndexOf(".") + 1) + String.valueOf(iMs);
        }

        return result;
    }

    public String getLapTime() {
        return formatTimeString(lCurrentTime);
    }

    public void resetCounter() {
        txtTotalTime.setText(START_TOTAL_TIME);
        txtCurrentTime.setText(START_LAP_TIME);
        lStartTime = 0L;
        lCurrentTime = 0L;
        lTimeSwap = 0L;
        lTotalTime = 0L;
        lTimeSaved = 0L;
    }

    public void resetLap() {
        lTimeSaved = lCurrentTime + lTimeSaved;
        lStartTime = SystemClock.elapsedRealtime();
        lTimeSwap = 0L;
    }

    public void updateTimer(boolean bResetTime) {
        if (activity.getState() == MainActivity.RunningState.STOPPED) {
            lTimeSwap = lCurrentTime;
            handler.removeCallbacks(updateTimerMethod);
            displayTime();
        } else {
            if (bResetTime) {
                lStartTime = SystemClock.elapsedRealtime();
            }

            handler.postDelayed(updateTimerMethod, 0);
        }
    }

    public void increaseFont(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
        params.addRule(RelativeLayout.BELOW, R.id.txtTotalTime);

        txtCurrentTime.setTextAppearance(context, R.style.WhiteText_Big);
        txtCurrentTime.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

        txtTotalTime.setTextAppearance(context, R.style.WhiteText);
        txtTotalTime.setLayoutParams(params);
    }

    public void shrinkFont(Context context) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

        txtCurrentTime.setTextAppearance(context, R.style.WhiteText_MediumBig);
        txtCurrentTime.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);

        txtTotalTime.setTextAppearance(context, R.style.WhiteText_Small);
        txtTotalTime.setLayoutParams(params);
        txtTotalTime.setPadding(0, 0, 10, 0);
    }

    private Runnable updateTimerMethod = new Runnable() {

        @Override
        public void run() {
            lCurrentTime = (SystemClock.elapsedRealtime() - lStartTime) + lTimeSwap;
            lTotalTime = lCurrentTime + lTimeSaved;
            displayTime();
            handler.postDelayed(this, 0);
        }
    };
}
