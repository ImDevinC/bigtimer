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
public class ResetFragment extends Fragment {

    private View view;
    private TextView txtAction;
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
        view = inflater.inflate(R.layout.fragment_reset, container, false);

        if (view == null) {
            return null;
        }

        txtAction = (TextView) view.findViewById(R.id.txtReset);

        txtAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view.getId());
            }
        });

        return view;
    }

    public void enableLapMode() {
        txtAction.setText("Lap");
        view.setBackgroundColor(getResources().getColor(R.color.lap_background));
    }

    public void enableResetMode() {
        txtAction.setText("Reset");
        view.setBackgroundColor(getResources().getColor(R.color.reset_background));
    }

    public interface OnClickListener {
        public void onClick(int id);
    }
}
