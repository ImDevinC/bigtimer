package com.layer8apps.stopwatch.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.layer8apps.stopwatch.main.R;
import com.layer8apps.stopwatch.main.activities.MainActivity;

import java.util.concurrent.TimeUnit;


/**
 * Created by devin on 4/29/14.
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

    private void displayTime() {
        String result = formatTimeString(lCurrentTime);
        txtCurrentTime.setText(result);

        result = formatTimeString(lTotalTime);
        txtTotalTime.setText("Total: " + result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time, container, false);

        if (view == null) {
            return null;
        }

        txtTotalTime = (TextView) view.findViewById(R.id.txtTotalTime);
        txtCurrentTime = (TextView) view.findViewById(R.id.txtThisLap);

        return view;
    }

    private String formatTimeString(long time) {
        String result = String.format("%02d:%02d.%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)),
                TimeUnit.MILLISECONDS.toMillis(time) -
                        TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(time))
        );

        if (result.indexOf(".") > 0 && result.length() - result.indexOf(".") > 1) {
            result = result.substring(0, result.indexOf(".") + 2);
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

    public void startTimer() {
        txtTotalTime.setText(START_TOTAL_TIME);
        txtCurrentTime.setText(START_LAP_TIME);
        lStartTime = SystemClock.elapsedRealtime();
        handler.postDelayed(updateTimerMethod, 0);
    }

    public void stopTimer() {
        lTimeSwap = lCurrentTime;
        handler.removeCallbacks(updateTimerMethod);
    }

    public void increaseFont() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
        params.addRule(RelativeLayout.BELOW, R.id.txtTotalTime);

        txtCurrentTime.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText_Big);
        txtCurrentTime.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

        txtTotalTime.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText);
        txtTotalTime.setLayoutParams(params);
    }

    public void shrinkFont() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

        txtCurrentTime.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText_MediumBig);
        txtCurrentTime.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.txtThisLap);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);

        txtTotalTime.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText_Small);
        txtTotalTime.setLayoutParams(params);
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
