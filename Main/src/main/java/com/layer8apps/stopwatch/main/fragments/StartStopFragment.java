package com.layer8apps.stopwatch.main.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layer8apps.stopwatch.main.R;

/**
 * Created by devin on 4/29/14.
 */
public class StartStopFragment extends Fragment {

    public enum State {
        STOPPED,
        STARTED,
        PAUSED
    }

    private State state;
    private TextView txtAction;
    private View view;
    private OnClickListener mOnClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnClickListener) {
            mOnClickListener = (OnClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_startstop, container, false);

        if (view == null) {
            return null;
        }

        state = State.STOPPED;

        txtAction = (TextView) view.findViewById(R.id.txtStartStop);

        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAction(view.getId());
            }
        });

        return view;
    }

    private void toggleAction(int id) {
        if (state == State.STOPPED) {
            startTimer();
        } else {
            stopTimer();
        }
        mOnClickListener.onClick(id);
    }

    private void startTimer() {
        view.setBackgroundColor(getResources().getColor(R.color.stop_background));
        txtAction.setText(getResources().getString(R.string.action_stop));
        state = State.STARTED;
    }

    private void stopTimer() {
        view.setBackgroundColor(getResources().getColor(R.color.start_background));
        txtAction.setText(getResources().getString(R.string.action_start));
        state = State.STOPPED;
    }

    public State getState() {
        return state;
    }

    public interface OnClickListener {
        public void onClick(int id);
    }
}
