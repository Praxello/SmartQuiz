package com.praxello.smartquiz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.scorecard.ScoresBO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreBoardAdapter extends RecyclerView.Adapter<ScoreBoardAdapter.ScoreBoardViewHolder> {

    public Context context;
    public ArrayList<ScoresBO> scoresBOArrayList;

    public ScoreBoardAdapter(Context context, ArrayList<ScoresBO> scoresBOArrayList) {
        this.context = context;
        this.scoresBOArrayList = scoresBOArrayList;
    }

    @NonNull
    @Override
    public ScoreBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_scoreboard_row, parent, false);
        return new ScoreBoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreBoardViewHolder holder, int position) {
        holder.tvTitle.setText(scoresBOArrayList.get(position).title);
        holder.tvCategoryId.setText(String.valueOf(scoresBOArrayList.get(position).getCategoryTitle()));
        holder.tvPassScore.setText(String.valueOf(scoresBOArrayList.get(position).getPassingScore()));

        if(scoresBOArrayList.get(position).getScore() >= scoresBOArrayList.get(position).getPassingScore()){
            Glide.with(context).load(R.drawable.passed).into(holder.ivPassFail);
        }else{
            Glide.with(context).load(R.drawable.failed).into(holder.ivPassFail);
        }

        String date = scoresBOArrayList.get(position).getQuizDate();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        // Log.e(TAG, "simple date format: "+spf.toString());
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat(" EEE,dd MMM yy");
        date = spf.format(newDate);

        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return scoresBOArrayList.size();
    }

    public class ScoreBoardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivpassfail)
        ImageView ivPassFail;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_category_id)
        TextView tvCategoryId;
        @BindView(R.id.tv_passcore)
        TextView tvPassScore;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public ScoreBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
