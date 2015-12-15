package com.quantum.app.mycourse.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quantum.app.mycourse.R;

/**
 * Created by hua on 2015/10/29.
 */
public class GradePointFragment extends Fragment {

    private CardView cardView;
    private TextView gradePoint;
    private String mGradePoint;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade_point, container, false);

        cardView = (CardView) view.findViewById(R.id.point_card_view);
        cardView.getBackground().setAlpha(120);
        gradePoint = (TextView) view.findViewById(R.id.grade_point);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mGradePoint = prefs.getString("m_grade_point", "");
        gradePoint.setText("你的绩点为 ： " + mGradePoint);
        gradePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return view;
    }

}
