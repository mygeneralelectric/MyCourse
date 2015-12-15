package com.quantum.app.mycourse.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.quantum.app.mycourse.R;
import com.quantum.app.mycourse.util.MyApplication;
import java.util.List;

/**
 * Created by hua on 2015/9/8.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private List<String> dataList;
    public int itemsCount = 0;

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;

    public static class DayViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout dayRelativeLayout;
        private CardView cardView;
        private TextView cTextView;

        public DayViewHolder(View itemView) {
            super(itemView);
            dayRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.day_relative_layout);
            cardView = (CardView) itemView.findViewById(R.id.day_card_view);
            cTextView = (TextView) itemView.findViewById(R.id.day_text_view);
        }
    }

    public DayAdapter(List<String> myDataList) {
        this.dataList = myDataList;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_item, parent, false);
        DayViewHolder viewHolder = new DayViewHolder(view);
        return viewHolder;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener rOnItemClickListener;

    public void setOnClickListener(OnItemClickListener rOnItemClickListener) {
        this.rOnItemClickListener = rOnItemClickListener;
    }

    private void runEnterAnimation(View view, int position) {

        if (position >= 5) {
            return;
        }
        if (position > lastAnimatedPosition) {

            lastAnimatedPosition = position;
            view.setTranslationY(view.getContext().getResources().getDisplayMetrics().heightPixels);
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .setStartDelay((position+1) * 100)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(final DayViewHolder viewHolder, final int position) {
        runEnterAnimation(viewHolder.itemView, position);
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

    public void updateItems() {
        itemsCount = 5;
        notifyDataSetChanged();
    }

}
