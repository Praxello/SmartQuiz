package com.praxello.smartmcq.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.CommonMethods;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.MyQuizActivity;
import com.praxello.smartmcq.activity.ViewQuestionActivity;
import com.praxello.smartmcq.model.CommonResponse;
import com.praxello.smartmcq.model.CreateQuestionResponse;
import com.praxello.smartmcq.model.allquestion.QuestionBO;
import com.praxello.smartmcq.services.ApiRequestHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewQuestionAdapter extends RecyclerView.Adapter<ViewQuestionAdapter.ViewQuestionViewHolder> {

    Context context;
    ArrayList<QuestionBO> questionBOArrayList;
    private int stCategoryId=0;
    private int selectedRadioBtn=0;
    private String TAG="ViewQuestionAdapter";

    public  ViewQuestionAdapter(Context context, ArrayList<QuestionBO> questionBOArrayList) {
        this.context = context;
        this.questionBOArrayList = questionBOArrayList;
    }

    @NonNull
    @Override
    public ViewQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.layout_view_question_row,parent,false);
        return new ViewQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewQuestionViewHolder holder, int position) {

        //Setting data to category adapter...
        setDataToAdapter(holder);

        //Getting data from radio button
        getRadioButtonData(holder);

        if(questionBOArrayList.get(position).getQuestionType()==2){
            holder.llMediaLink.setVisibility(View.VISIBLE);
            holder.llImage.setVisibility(View.VISIBLE);
            holder.llVideo.setVisibility(View.GONE);
        }else if(questionBOArrayList.get(position).getQuestionType()==3){
            holder.llMediaLink.setVisibility(View.VISIBLE);
            holder.llVideo.setVisibility(View.VISIBLE);
            holder.llImage.setVisibility(View.GONE);
        }else{
            holder.llMediaLink.setVisibility(View.GONE);
        }

        holder.spinCategory.setSelection(questionBOArrayList.get(position).getQuestionType()-1);
        holder.etQuestion.setText(questionBOArrayList.get(position).getQuestion());
        holder.etOption1.setText(questionBOArrayList.get(position).getOption1());
        holder.etOption2.setText(questionBOArrayList.get(position).getOption2());
        holder.etOption3.setText(questionBOArrayList.get(position).getOption3());
        holder.etOption4.setText(questionBOArrayList.get(position).getOption4());
        holder.etMediaLink.setText(questionBOArrayList.get(position).getMediaUrl());
        /*if(ViewQuestionActivity.selectedImagePath!=null){
            holder.etMediaLink.setText(questionBOArrayList.get(position).getMediaUrl());
        }else{
            holder.etMediaLink.setText(ViewQuestionActivity.selectedImagePath);
        }*/
        holder.etDetails.setText(questionBOArrayList.get(position).getAnswerDetails());

        if(questionBOArrayList.get(position).getAnswer()==1){
            holder.radioButton1.setChecked(true);
            stCategoryId=1;
        }else if(questionBOArrayList.get(position).getAnswer()==2){
            holder.radioButton2.setChecked(true);
            stCategoryId=2;
        }else if(questionBOArrayList.get(position).getAnswer()==3){
            holder.radioButton3.setChecked(true);
            stCategoryId=3;
        }else if(questionBOArrayList.get(position).getAnswer()==4){
            holder.radioButton4.setChecked(true);
            stCategoryId=4;
        }

        holder.btnUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidated(holder)){
                    updateQuizQuestion(holder,position);
                }
            }
        });

        holder.btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("SmartQuiz")
                        .setMessage("Are you sure you want to delete question?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                //deleteComment(commentsDataArrayList.get(position).getSessionId(), commentsDataArrayList.get(position).getCommentId(), position);
                                deleteQuizQuestion(questionBOArrayList.get(position).getQuestionId(),position);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        holder.btnMediaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewQuestionActivity) context).onClickCalled();
            }
        });

        holder.btnViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewQuestionActivity) context).viewImagePreview(questionBOArrayList.get(position).getQuestionId());
            }
        });

        holder.btnViewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionBOArrayList.get(position).getMediaUrl()==null || questionBOArrayList.get(position).getMediaUrl().isEmpty()) {
                    // CommonMethods.openBrowser(mContext, advertisements.get(position).getAdVideourl());
                    Toast.makeText(context, "Video not available!", Toast.LENGTH_SHORT).show();
                }else{
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+questionBOArrayList.get(position).getMediaUrl())));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionBOArrayList.size();
    }

    public static class ViewQuestionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.spin_category)
        Spinner spinCategory;
        @BindView(R.id.et_question)
        EditText etQuestion;
        @BindView(R.id.et_option1)
        EditText etOption1;
        @BindView(R.id.et_option2)
        EditText etOption2;
        @BindView(R.id.et_option3)
        EditText etOption3;
        @BindView(R.id.et_option4)
        EditText etOption4;
        public static EditText etMediaLink;
        @BindView(R.id.et_details)
        EditText etDetails;
        @BindView(R.id.radioGroup)
        RadioGroup radioGroup;
        @BindView(R.id.radioButton1)
        RadioButton radioButton1;
        @BindView(R.id.radioButton2)
        RadioButton radioButton2;
        @BindView(R.id.radioButton3)
        RadioButton radioButton3;
        @BindView(R.id.radioButton4)
        RadioButton radioButton4;
        @BindView(R.id.btn_updatequestion)
        AppCompatButton btnUpdateQuestion;
        @BindView(R.id.btn_deletequestion)
        AppCompatButton btnDeleteQuestion;
        @BindView(R.id.btn_medialink)
        AppCompatButton btnMediaLink;
        @BindView(R.id.ll_medialink)
        LinearLayout llMediaLink;
        @BindView(R.id.btn_view_image)
        AppCompatButton btnViewImage;
        @BindView(R.id.btn_view_video)
        AppCompatButton btnViewVideo;
        @BindView(R.id.ll_video)
        LinearLayout llVideo;
        @BindView(R.id.ll_image)
        LinearLayout llImage;


        public ViewQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            etMediaLink=itemView.findViewById(R.id.et_medialink);
        }
    }

    private void setDataToAdapter(@NonNull ViewQuestionViewHolder holder){
        String[] arraySpinner = new String[] {"Normal","Image","Video"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinCategory.setAdapter(adapter);

        holder.spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stCategoryId=position+1;
                if(stCategoryId==2){
                   holder.llMediaLink.setVisibility(View.VISIBLE);
                   holder.llImage.setVisibility(View.VISIBLE);
                    holder.llVideo.setVisibility(View.GONE);
                }else if(stCategoryId==3){
                   holder.llMediaLink.setVisibility(View.VISIBLE);
                   holder.llVideo.setVisibility(View.VISIBLE);
                    holder.llImage.setVisibility(View.GONE);
                }else{
                    holder.llMediaLink.setVisibility(View.GONE);
                }
                //Toast.makeText(AddNewQuestionActivity.this, "Category Id"+stCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getRadioButtonData(@NonNull ViewQuestionViewHolder holder){
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton1) {
                    selectedRadioBtn=1;
                    //Toast.makeText(context, selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton2) {
                    selectedRadioBtn=2;
                   // Toast.makeText(context, selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton3) {
                    selectedRadioBtn=3;
                    // Toast.makeText(context, selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.radioButton4) {
                    selectedRadioBtn=4;
                    // Toast.makeText(context, selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateQuizQuestion(@NonNull ViewQuestionViewHolder holder,int position){
        Map<String,String> params=new HashMap<>();

        params.put("questionId",String.valueOf(questionBOArrayList.get(position).getQuestionId()));
        params.put("questionType",String.valueOf(stCategoryId));
        params.put("quizId",String.valueOf(questionBOArrayList.get(position).getQuizId()));
        params.put("question",holder.etQuestion.getText().toString());
        params.put("option1",holder.etOption1.getText().toString());
        params.put("option2",holder.etOption2.getText().toString());
        params.put("option3",holder.etOption3.getText().toString());
        params.put("option4",holder.etOption4.getText().toString());
        params.put("answer",String.valueOf(selectedRadioBtn));
        params.put("answerDetails",holder.etDetails.getText().toString());
        if(stCategoryId==3){
            params.put("mediaUrl",ViewQuestionViewHolder.etMediaLink.getText().toString());
        }else if(stCategoryId==2){
            params.put("mediaUrl","http://103.127.146.5/~tailor/smartquiz/questionpics/"+ questionBOArrayList.get(position).getQuestionId()+".jpg");
        }else{
            params.put("mediaUrl","");
        }


        Log.e(TAG, "updateQuizQuestion: "+params );

        ViewQuestionActivity.smartQuiz.getApiRequestHelper().updatequizquestion(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CreateQuestionResponse createQuestionResponse=(CreateQuestionResponse) object;

                Log.e(TAG, "onSuccess: "+createQuestionResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+createQuestionResponse.getMessage());

                if(createQuestionResponse.getResponsecode()==200){
                    Toast.makeText(context, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(ViewQuestionActivity.selectedImagePath!=null){
                        ((ViewQuestionActivity) context).uploadImageRetrofit(questionBOArrayList.get(position).getQuestionId());
                    }
                      QuestionBO questionBO=new QuestionBO(questionBOArrayList.get(position).getQuestionId(),
                                questionBOArrayList.get(position).getQuestionType(),
                                questionBOArrayList.get(position).getQuizId(),
                                holder.etQuestion.getText().toString(),
                                holder.etOption1.getText().toString(),
                                holder.etOption2.getText().toString(),
                                holder.etOption3.getText().toString(),
                                holder.etOption4.getText().toString(),
                                selectedRadioBtn,
                                holder.etDetails.getText().toString(),
                                1,
                                "http://103.127.146.5/~tailor/smartquiz/questionpics/"+ CommonMethods.getPrefrence(context, AllKeys.USER_ID)+".jpg"
                        );
                        MyQuizActivity.mainQuizBO.getQuestions().set(position,questionBO);
                        notifyItemChanged(position,MyQuizActivity.mainQuizBO.getQuestions());
                        MyQuizActivity.myQuizAdapter.notifyDataSetChanged();


                }else{
                    Toast.makeText(context, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(context, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteQuizQuestion(int questionId,int position){
        Map<String,String> params=new HashMap<>();
        params.put("questionId",String.valueOf(questionId));

        ViewQuestionActivity.smartQuiz.getApiRequestHelper().deleteQuizQuestion(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CommonResponse commonResponse=(CommonResponse) object;

                Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                if(commonResponse.getResponsecode()==200){
                    Toast.makeText(context, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    MyQuizActivity.mainQuizBO.getQuestions().remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,MyQuizActivity.mainQuizBO.getQuestions().size());
                    MyQuizActivity.myQuizAdapter.notifyDataSetChanged();
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

    private boolean isValidated(@NonNull ViewQuestionViewHolder holder){
        if(holder.etQuestion.getText().toString().isEmpty()){
            Toast.makeText(context, "Question required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(holder.etOption1.getText().toString().isEmpty()){
            Toast.makeText(context, "Option1 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(holder.etOption2.getText().toString().isEmpty()){
            Toast.makeText(context, "Option2 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(holder.etOption3.getText().toString().isEmpty()){
            Toast.makeText(context, "Option3 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(holder.etOption4.getText().toString().isEmpty()){
            Toast.makeText(context, "Option4 required!", Toast.LENGTH_SHORT).show();
            return false;
        }
/*
        if(stCategoryId!=0){
            if(holder.etMediaLink.getText().toString().isEmpty()){
                Toast.makeText(context, "Media link required!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }*/

        if(holder.etDetails.getText().toString().isEmpty()){
            Toast.makeText(context, "Details required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (holder.radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(context, "Please select one right option!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}
