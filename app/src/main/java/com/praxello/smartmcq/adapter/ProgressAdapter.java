package com.praxello.smartmcq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moos.library.HorizontalProgressView;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.MyScoreActivity;
import com.praxello.smartmcq.model.scorecard.AvailableBO;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {

    public Context context;
    public ArrayList<AvailableBO> availableBOArrayList;


    public ProgressAdapter(Context context, ArrayList<AvailableBO> availableBOArrayList) {
        this.context = context;
        this.availableBOArrayList = availableBOArrayList;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_progress_row,parent,false);
        return new ProgressViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {


        holder.tvTitle.setText(availableBOArrayList.get(position).categoryTitle);
        try {
            if (MyScoreActivity.allAttemptsDataMap.containsKey(availableBOArrayList.get(position).getCategoryTitle())) {
                //int done = ProgressFragment.allAttemptsDataMap.get(availableBOArrayList.get(position).getCategoryTitle());
                float done = 1;
                //calculate percetage
                float total = availableBOArrayList.get(position).total;
                holder.horizontalProgressView.setStartProgress(0);
                float finalPerc = (done * 100f) / total;

                holder.horizontalProgressView.setEndProgress(finalPerc);
              //  holder.horizontalProgressView.setStartColor(R.color.yellow800);
               // holder.horizontalProgressView.setEndColor(R.color.green);
                holder.horizontalProgressView.setStartColor(Color.parseColor("#f9a825"));
                holder.horizontalProgressView.setEndColor(Color.parseColor("#43a047"));

                //holder.horizontalProgressView.setTrackEnabled(true);
                holder.horizontalProgressView.startProgressAnimation();
            } else {
                holder.horizontalProgressView.setStartProgress(0);
                holder.horizontalProgressView.setEndProgress(0);
                holder.horizontalProgressView.setStartColor(Color.parseColor("#f9a825"));
                holder.horizontalProgressView.setEndColor(Color.parseColor("#43a047"));

                holder.horizontalProgressView.setStartColor(R.color.yellow800);
                holder.horizontalProgressView.setEndColor(R.color.green);


                //holder.horizontalProgressView.setTrackEnabled(true);
                holder.horizontalProgressView.startProgressAnimation();

            }
        }catch (Exception e){
            e.printStackTrace();
            holder.horizontalProgressView.setStartProgress(0);
            holder.horizontalProgressView.setEndProgress(0);
           // holder.horizontalProgressView.setStartColor(R.color.yellow800);
            //holder.horizontalProgressView.setEndColor(R.color.green);
            holder.horizontalProgressView.setStartColor(Color.parseColor("#f9a825"));
            holder.horizontalProgressView.setEndColor(Color.parseColor("#43a047"));
            holder.horizontalProgressView.startProgressAnimation();
        }

    }

    @Override
    public int getItemCount() {
        return availableBOArrayList.size();
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.progressView_horizontal)
        HorizontalProgressView horizontalProgressView;

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
