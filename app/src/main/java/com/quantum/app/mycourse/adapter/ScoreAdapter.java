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
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private List<String> dataList;

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private RelativeLayout scoreRelativeLayout;
        private TextView cTextView;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.score_card_view);
            scoreRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.score_recycler_view);
            cTextView = (TextView) itemView.findViewById(R.id.score_text_view);
        }

    }

    public ScoreAdapter(List<String> myDataList) {
        this.dataList = myDataList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        ScoreViewHolder viewHolder = new ScoreViewHolder(view);
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
    public void onBindViewHolder(final ScoreViewHolder viewHolder, final int position) {
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
