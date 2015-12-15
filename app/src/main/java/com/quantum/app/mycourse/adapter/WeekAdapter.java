package com.quantum.app.mycourse.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quantum.app.mycourse.R;

import java.util.List;

/**
 * Created by hua on 2015/9/8.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {

    private List<String> dataList;

    public static class WeekViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout weekRelativeLayout;
        private CardView cardView;
        private TextView cTextView;

        public WeekViewHolder(View itemView) {
            super(itemView);
            weekRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.week_relative_layout);
            cardView = (CardView) itemView.findViewById(R.id.week_card_view);
            cTextView = (TextView) itemView.findViewById(R.id.week_text_view);
        }

    }

    public WeekAdapter(List<String> myDataList) {
        this.dataList = myDataList;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_item, parent, false);
        WeekViewHolder viewHolder = new WeekViewHolder(view);
        return viewHolder;
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener rOnItemClickListener;

    public void setOnClickListener(OnItemClickListener rOnItemClickListener) {
        this.rOnItemClickListener = rOnItemClickListener;
    }

    @Override
    public void onBindViewHolder(final WeekViewHolder viewHolder, final int position) {
        viewHolder.cTextView.setText(dataList.get(position));
        if (rOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rOnItemClickListener.onItemClick(viewHolder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
