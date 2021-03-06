package com.praxello.smartmcq.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.ViewQuestionActivity;
import com.praxello.smartmcq.activity.AddQuizActivity;
import com.praxello.smartmcq.activity.MyQuizActivity;
import com.praxello.smartmcq.activity.quiz.QuizActivity;
import com.praxello.smartmcq.model.CommonResponse;
import com.praxello.smartmcq.model.allquestion.QuizBO;
import com.praxello.smartmcq.services.ApiRequestHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyQuizAdapter extends RecyclerView.Adapter<MyQuizAdapter.MyQuizViewHolder> {

    public Context context;
    public ArrayList<QuizBO> quizBOArrayList;
    public static QuizBO quizBO;
    public static String TAG="MyQuizAdapter";

    public MyQuizAdapter(Context context, ArrayList<QuizBO> quizBOArrayList) {
        this.context = context;
        this.quizBOArrayList = quizBOArrayList;
    }

    @NonNull
    @Override
    public MyQuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_myquiz_row,parent,false);
        return new MyQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQuizViewHolder holder, int position) {

        holder.tvTitle.setText(quizBOArrayList.get(position).title);
        holder.tvCategoryId.setText(String.valueOf(quizBOArrayList.get(position).getCategoryTitle()));
        holder.tvPassScore.setText(quizBOArrayList.get(position).getPassingScore() +"%");
        holder.tvNoOfQuestion.setText(String.valueOf(quizBOArrayList.get(position).getQuestions().size()));
        if(quizBOArrayList.get(position).getQuestionTimeout()==0){
            holder.tvTime.setText("NA");
        }else {
            holder.tvTime.setText(quizBOArrayList.get(position).getQuestionTimeout() + " sec");
        }

        String date = quizBOArrayList.get(position).getCreatedAt();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // Log.e(TAG, "simple date format: "+spf.toString());
        Date newDate = null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf = new SimpleDateFormat(" EEE, dd MMM yy");
        date = spf.format(newDate);

        holder.tvDate.setText(date);
        holder.tvDetails.setText(quizBOArrayList.get(position).getDetails());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("SmartQuiz")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                deleteQuiz(quizBOArrayList.get(position).getQuizId(),position);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //Toast.makeText(context, "Delete itemView " , Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity = (Activity) context;
                Intent intent=new Intent(context, AddQuizActivity.class);
                //intent.putExtra("data",quizDataArrayList.get(position).getQuestions());
                //intent.putExtra("data",quizBOArrayList.get(position));

                quizBO=quizBOArrayList.get(position);
                intent.putExtra("data",quizBO);
                intent.putExtra("position",position);
                intent.putExtra("type","update");
                ((Activity) context).startActivityForResult(intent,1);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        holder.llPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizBOArrayList.get(position).getQuestions()!=null){
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, QuizActivity.class);
                    //intent.putExtra("data",quizDataArrayList.get(position).getQuestions());
                    intent.putExtra("data",quizBOArrayList.get(position));
                    intent.putExtra("type","preview");
                    context.startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    Toast.makeText(context, "Preview not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        holder.llCreateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, ViewQuestionActivity.class);
                    //intent.putExtra("data",quizDataArrayList.get(position).getQuestions());
                    MyQuizActivity.mainQuizBO=quizBOArrayList.get(position);
                    intent.putExtra("data",quizBOArrayList.get(position));
                    ((Activity) context).startActivityForResult(intent,3);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizBOArrayList.size();
    }

    public class MyQuizViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_category_id)
        TextView tvCategoryId;
        @BindView(R.id.tv_passcore)
        TextView tvPassScore;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_details)
        TextView tvDetails;
        @BindView(R.id.button_delete)
        TextView tvDelete;
        @BindView(R.id.button_edit)
        TextView tvEdit;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_no_of_questions)
        TextView tvNoOfQuestion;
        @BindView(R.id.ll_preview)
        LinearLayout llPreview;
        @BindView(R.id.ll_share)
        LinearLayout llShare;
        @BindView(R.id.ll_create_question)
        LinearLayout llCreateQuestion;

        public MyQuizViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void deleteQuiz(int quizid,int position){
        Map<String,String> params=new HashMap<>();
        params.put("quizid",String.valueOf(quizid));

        MyQuizActivity.smartQuiz.getApiRequestHelper().deleteQuiz(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CommonResponse commonResponse=(CommonResponse) object;

                Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                if(commonResponse.getResponsecode()==200){
                    Toast.makeText(context, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    quizBOArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,quizBOArrayList.size());
                }else{
                    Toast.makeText(context, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(context, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
