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

import java.util.ArrayList;

/**
 * Created by devin on 4/29/14.
 */
public class LapCounterFragment extends ListFragment {

    private LapArrayAdapter adapter;
    private ArrayList<String> lapList = new ArrayList<String>();

    private View view;
    private OnImageClickedListener mOnImageClickedListener;
    private ImageView imgResize;

    private enum WindowState {
        NORMAL,
        EXPANDED
    }

    private WindowState state = WindowState.NORMAL;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnImageClickedListener) {
            mOnImageClickedListener = (OnImageClickedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement OnImageClickedListener");
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

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setListAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    public void startTimer() {
        view.setBackgroundColor(getResources().getColor(R.color.laptime_background));
    }

    public void stopTimer() {
        view.setBackgroundColor(getResources().getColor(R.color.laptime_stopped_background));
    }

    public void addLap(String time) {
        adapter.add(String.valueOf(adapter.getCount() + 1) + ": " + time);

        adapter.notifyDataSetChanged();
    }

    public void resetLaps() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    public void setResizeImage(int id) {
        imgResize.setImageDrawable(getResources().getDrawable(id));
    }

    public void increaseFont() {
        state = WindowState.EXPANDED;
        imgResize.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_off));
    }

    public void shrinkFont() {
        state = WindowState.NORMAL;
        imgResize.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_on));
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
