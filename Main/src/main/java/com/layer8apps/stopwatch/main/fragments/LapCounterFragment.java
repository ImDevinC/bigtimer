package com.layer8apps.stopwatch.main.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.layer8apps.stopwatch.main.R;
import com.layer8apps.stopwatch.main.activities.MainActivity;

import java.util.ArrayList;

/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * Devin Collins (devin@imdevinc.com) wrote this file as part of the StopWatch
 * project. As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it, you
 * can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */

public class LapCounterFragment extends ListFragment {
    private LapArrayAdapter adapter;
    private ArrayList<String> lapList = new ArrayList<String>();

    private View view;
    private ImageView imgResize;
    private OnImageClickedListener mOnImageClickedListener;
    private MainActivity activity;

    private enum WindowState {
        NORMAL,
        EXPANDED
    }

    private WindowState state = WindowState.NORMAL;

    public void addLap(String time) {
        adapter.add(String.valueOf(adapter.getCount() + 1) + ": " + time);

        adapter.notifyDataSetChanged();
    }

    public void increaseFont() {
        state = WindowState.EXPANDED;
        imgResize.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_off));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnImageClickedListener) {
            mOnImageClickedListener = (OnImageClickedListener) activity;
            this.activity = (MainActivity) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnImageClickedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey("laps")) {
            lapList = savedInstanceState.getStringArrayList("laps");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter = new LapArrayAdapter(inflater.getContext(), lapList);

        view = inflater.inflate(R.layout.fragment_lapcount, container, false);

        if (view == null) {
            return null;
        }

        imgResize = (ImageView) view.findViewById(R.id.imgResize);
        imgResize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnImageClickedListener.onImageClicked();
            }
        });

        updateView();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("laps", lapList);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    public void resetLaps() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void shrinkFont() {
        state = WindowState.NORMAL;
        imgResize.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_on));
    }

    public void updateView() {
        if (activity.getState() == MainActivity.RunningState.STOPPED) {
            view.setBackgroundColor(getResources().getColor(R.color.laptime_stopped_background));
        } else {
            view.setBackgroundColor(getResources().getColor(R.color.laptime_background));
        }
    }

    public interface OnImageClickedListener {
        public void onImageClicked();
    }

    private class LapArrayAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final ArrayList<String> laps;

        private LapArrayAdapter(Context context, ArrayList<String> laps) {
            super(context, R.layout.laprow_layout, laps);
            this.context = context;
            this.laps = laps;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.laprow_layout, parent, false);

            TextView txtLap = (TextView) row.findViewById(R.id.txtLapRow);

            txtLap.setText(laps.get(position));

            if (state == WindowState.NORMAL) {
                txtLap.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText);
            } else {
                txtLap.setTextAppearance(getActivity().getBaseContext(), R.style.WhiteText_Big);
            }

            return row;
        }
    }
}
