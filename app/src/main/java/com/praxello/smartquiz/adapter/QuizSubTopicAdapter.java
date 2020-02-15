package com.praxello.smartquiz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.quiz.QuizActivity;
import com.praxello.smartquiz.model.allquestion.QuizBO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizSubTopicAdapter extends RecyclerView.Adapter<QuizSubTopicAdapter.QuizSubTopicViewHolder> {

    public Context context;
    public List<QuizBO> quizDataArrayList;
    private final int[] backgroundColors = {R.color.black, R.color.blue700, R.color.deepgreen800,
            R.color.deepOrange800, R.color.deepPurple800, R.color.colorAccent, R.color.yellow800};

    public QuizSubTopicAdapter(Context context, List<QuizBO> quizDataArrayList) {
        this.context = context;
        this.quizDataArrayList = quizDataArrayList;
    }

    @NonNull
    @Override
    public QuizSubTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_quiz_sub_topic_row,parent,false);
        return new QuizSubTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizSubTopicViewHolder holder, int position) {

        holder.tvSubType.setText(quizDataArrayList.get(position).title);

        int bgColor = ContextCompat.getColor(context, backgroundColors[position % 7]);
        holder.ivBullet.setBackgroundColor(bgColor);

        holder.cvSubTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent=new Intent(context, QuizActivity.class);
                intent.putExtra("data",quizDataArrayList.get(position));
                context.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizDataArrayList.size();
    }

    public class QuizSubTopicViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cardview_sub_topic)
        CardView cvSubTopic;
        @BindView(R.id.ivbullet)
        View ivBullet;
        @BindView(R.id.tv_sub_type)
        TextView tvSubType;

        public QuizSubTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
