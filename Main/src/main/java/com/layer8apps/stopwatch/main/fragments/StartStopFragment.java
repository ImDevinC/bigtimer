package com.layer8apps.stopwatch.main.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.layer8apps.stopwatch.main.R;
import com.layer8apps.stopwatch.main.activities.MainActivity;

/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Devin Collins (devin@imdevinc.com) wrote this file as part of the StopWatch
 * project. As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it, you
 * can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */

public class StartStopFragment extends Fragment {

    private TextView txtAction;
    private View view;
    private OnClickListener mOnClickListener;
    private MainActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnClickListener) {
            mOnClickListener = (OnClickListener) activity;
            this.activity = (MainActivity) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_startstop, container, false);

        if (view == null) {
            return null;
        }

        txtAction = (TextView) view.findViewById(R.id.txtStartStop);

        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view.getId());
            }
        });

        updateView();

        return view;
    }

    public void updateView() {
        if (activity.getState() == MainActivity.RunningState.STOPPED) {
            view.setBackgroundColor(getResources().getColor(R.color.start_background));
            txtAction.setText(getResources().getString(R.string.action_start));
        } else if (activity.getState() == MainActivity.RunningState.RUNNING) {
            view.setBackgroundColor(getResources().getColor(R.color.stop_background));
            txtAction.setText(getResources().getString(R.string.action_stop));
        }
    }

    public interface OnClickListener {
        public void onClick(int id);
    }
}
