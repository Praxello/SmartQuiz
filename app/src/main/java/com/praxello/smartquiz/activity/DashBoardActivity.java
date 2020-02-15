package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.quiz.QuizActivity;
import com.praxello.smartquiz.model.GetExamResponse;
import com.praxello.smartquiz.model.login.LoginResponse;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_generalquiz)
    public LinearLayout llGeneralQuiz;
    @BindView(R.id.ll_taketest)
    public LinearLayout llTakeTest;
    SmartQuiz smartQuiz;
    private static String TAG="DashBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(DashBoardActivity.this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation..
        initViews();
    }

    private void initViews() {
        llGeneralQuiz.setOnClickListener(this);
        llTakeTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_generalquiz:
                Intent intent = new Intent(DashBoardActivity.this, QuizTopicsActvity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.ll_taketest:

                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_dialog_view, viewGroup, false);
                builder.setView(dialogView);
                EditText etQuizID=dialogView.findViewById(R.id.etquizid);
                AppCompatButton btnSubmit=dialogView.findViewById(R.id.btnsubmit);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etQuizID.getText().toString().isEmpty()){
                            etQuizID.setError("Quiz Id required");
                            etQuizID.requestFocus();
                            etQuizID.setFocusable(true);
                        }else{
                            //loadTest(etQuizID.getText().toString());
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
        }
    }

    private void loadTest(String quizid){
        smartQuiz.getApiRequestHelper().getTest(quizid,new ApiRequestHelper.OnRequestComplete() {
                    @Override
                    public void onSuccess(Object object) {
                        GetExamResponse getExamResponse=(GetExamResponse) object;

                        Log.e(TAG, "onSuccess: "+getExamResponse.getResponsecode());
                        Log.e(TAG, "onSuccess: "+getExamResponse.getMessage());
                        Log.e(TAG, "onSuccess: "+getExamResponse.getData());

                        if(getExamResponse.getResponsecode()==200){
                            Intent intent=new Intent(DashBoardActivity.this, QuizActivity.class);
                            intent.putExtra("data",getExamResponse.getData());
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                        }else{
                            Toast.makeText(DashBoardActivity.this, getExamResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String apiResponse) {
                        Toast.makeText(DashBoardActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                    }
                });
         }
}
