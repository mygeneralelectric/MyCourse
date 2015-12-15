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

import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.adapter.DayAdapter;
import com.quantum.app.mycourse.adapter.ScoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2015/10/29.
 */
public class ScoreFragment extends Fragment {

    private RecyclerView recyclerView;
    private ScoreAdapter scoreAdapter;
    private LinearLayoutManager rLayoutManager;
    private List<String> dataList;
    private String TAG = "ScoreFragment";

    public  List<String> loadScore(List<String> dataList) {
        dataList = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String mScore = prefs.getString("m_score", "");
        Log.d(TAG, "dayCourse is -->" + mScore);
        String[] score = mScore.split(";");
        for(int i = 0; i < score.length; i++) {
            dataList.add(score[i]);
        }
        return dataList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.score_recycler_view);
        recyclerView.setHasFixedSize(true);
        rLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        dataList = loadScore(dataList);
        scoreAdapter = new ScoreAdapter(dataList);
        scoreAdapter.setOnClickListener(new ScoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
            }
        });
        recyclerView.setAdapter(scoreAdapter);

        return view;
    }

}
