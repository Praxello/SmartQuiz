package com.praxello.smartmcq.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.CommonMethods;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.services.ApiRequestHelper;
import com.praxello.smartmcq.services.SmartQuiz;
import com.praxello.smartmcq.adapter.QuizTopicAdapter;
import com.praxello.smartmcq.model.allquestion.AllQuestionResponse;
import com.praxello.smartmcq.model.allquestion.QuizBO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;


public class QuizTopicsActvity extends AppCompatActivity {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    public AllQuestionResponse questionData;
    SmartQuiz smartQuiz;
    public static final String TAG="QuizTopicsActivity";
    public static Map<String, List<QuizBO>> allQuestionDataMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_topics_actvity);
        ButterKnife.bind(QuizTopicsActvity.this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();

        //get all question..
        allquestions();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_quiztopics);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Quiz type");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        rvType.setLayoutManager(new LinearLayoutManager(QuizTopicsActvity.this));
    }

    public void allquestions() {

        if (CommonMethods.isNetworkAvailable(QuizTopicsActvity.this)) {
            Map<String, String> map = new HashMap<>();
            map.put("userid","1");
            smartQuiz.getApiRequestHelper().allquestions(map, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {

                    //questionData = (QuestionData) object;
                    questionData = (AllQuestionResponse) object;

                    QuizTopicsActvity.allQuestionDataMap=questionData.getData();


                    if (questionData != null) {
                        if (questionData.getResponsecode() == 200) {

                            Log.e(TAG, "onSuccess: "+questionData );

                            if (questionData.getData() != null) {
                                //loadQuizFragment();
                                rvType.setAdapter(new QuizTopicAdapter(QuizTopicsActvity.this,questionData.getData().keySet().toArray()));
                                //rvType.setAdapter(new QuizTopicAdapter(QuizTopicsActvity.this,QuizTopicsActvity.workSheetDataMap.keySet().toArray()));
                            }
                        } else if (!TextUtils.isEmpty(questionData.getMessage())) {
                            //tvError.setVisibility(View.VISIBLE);
                           // tvError.setText(questionData.getMessage());
                        }
                    } else {
                        Toast.makeText(QuizTopicsActvity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    //Log.e("in", "error " + apiResponse);
                    Toast.makeText(QuizTopicsActvity.this, apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(QuizTopicsActvity.this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
