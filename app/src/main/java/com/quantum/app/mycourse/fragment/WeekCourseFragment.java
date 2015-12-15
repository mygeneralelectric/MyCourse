package com.quantum.app.mycourse.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.adapter.DayAdapter;
import com.quantum.app.mycourse.adapter.WeekAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2015/10/29.
 */
public class WeekCourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private WeekAdapter weekAdapter;
    private GridLayoutManager rGridLayoutManager;
    private List<String> dataList;
    private String TAG = "DayCourseFragment";

    public  List<String> loadWeekCourse(List<String> dataList) {
        dataList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String weekCourse = prefs.getString("week_course", "");
        Log.d(TAG, "weekCourse is -->" +weekCourse);
        String[] wCourse = weekCourse.split(";");
        for(int i = 0; i < wCourse.length; i++) {
            dataList.add(wCourse[i]);
        }
        return dataList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_week, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.week_recycler_view);
        recyclerView.setHasFixedSize(true);
        rGridLayoutManager = new GridLayoutManager(getActivity(), 8);
        recyclerView.setLayoutManager(rGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dataList = loadWeekCourse(dataList);
        weekAdapter = new WeekAdapter(dataList);
        weekAdapter.setOnClickListener(new WeekAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
            }
        });
        recyclerView.setAdapter(weekAdapter);

        return view;
    }

}
