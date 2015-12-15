package com.quantum.app.mycourse.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.adapter.DayAdapter;
import com.quantum.app.mycourse.util.HttpUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2015/10/29.
 */
public class DayCourseFragment extends Fragment {

    private TextView dTextView;
    private RecyclerView recyclerView;
    private DayAdapter dayAdapter;
    private LinearLayoutManager rLayoutManager;
    private List<String> dataList;
    private String TAG = "DayCourseFragment";

    public  List<String> loadDayCourse(List<String> dataList) {
        dataList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String dayCourse = prefs.getString("day_course", "");
        Log.d(TAG, "dayCourse is -->" +dayCourse);
        String[] dCourse = dayCourse.split(";");
        for(int i = 0; i < dCourse.length; i++) {
            dataList.add(dCourse[i]);
        }
        return dataList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_day, container, false);

        dTextView = (TextView) view.findViewById(R.id.day_of_week);
        dTextView.setText(HttpUtil.getDate());
        recyclerView = (RecyclerView) view.findViewById(R.id.day_recycler_view);
        recyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dataList = loadDayCourse(dataList);
        dayAdapter = new DayAdapter(dataList);
        dayAdapter.setOnClickListener(new DayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
            }
        });
        recyclerView.setAdapter(dayAdapter);

        return view;
    }

}
