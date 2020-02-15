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
import com.praxello.smartquiz.activity.QuizSubTopicsActivity;
import com.praxello.smartquiz.model.QuizData;
import com.praxello.smartquiz.model.allquestion.QuizBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizTopicAdapter extends RecyclerView.Adapter<QuizTopicAdapter.QuizTopicViewHolder> {

    public Context context;
    public Object[] stringArray;


    private final int[] backgroundColors = {R.color.black, R.color.blue700, R.color.deepgreen800,
            R.color.deepOrange800, R.color.deepPurple800, R.color.colorAccent, R.color.yellow800};


    /*public QuizTopicAdapter(Context context, ArrayList<QuizData> quizDataArrayList) {
        this.context = context;
        this.quizDataArrayList = quizDataArrayList;
    }*/

    public QuizTopicAdapter(Context context, Object[] stringArray) {
        this.context = context;
        this.stringArray = stringArray;
    }

    @NonNull
    @Override
    public QuizTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_quiz_topic_row,parent,false);
        return new QuizTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizTopicViewHolder holder, int position) {
        // in onBindViewHolder
        int bgColor = ContextCompat.getColor(context, backgroundColors[position % 7]);
        holder.view1.setBackgroundColor(bgColor);
        holder.view2.setBackgroundColor(bgColor);

        holder.tvType.setText(stringArray[position].toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                Intent intent=new Intent(context, QuizSubTopicsActivity.class);
                intent.putExtra("catergory_type",stringArray[position].toString());
                context.startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringArray.length;
    }

    public class QuizTopicViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cardview)
        CardView cardView;
        @BindView(R.id.view1)
        View view1;
        @BindView(R.id.view2)
        View view2;
        @BindView(R.id.tv_type)
        TextView tvType;

        public QuizTopicViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
