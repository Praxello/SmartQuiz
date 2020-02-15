package com.praxello.smartquiz.activity.quiz;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.retrofit.ApiRequestHelper;
import com.praxello.smartquiz.activity.retrofit.SmartQuiz;
import com.praxello.smartquiz.model.allquestion.AllQuestionResponse;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;


public class QuizActivity extends AppCompatActivity {

    //@BindView(R.id.progressBar)
    ProgressBar progressBar;
    //@BindView(R.id.tv_error)
    TextView tvError;
    //public QuestionData questionData;
    public AllQuestionResponse questionData;
    public int totalScore = 0;
    public int currentQuesPos = 0;
    SmartQuiz smartQuiz;
    public static final String TAG="QuizActivity";
    QuizBO quizBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();
        //basic intialisation...
        initViews();

        quizBO=getIntent().getParcelableExtra("data");

        loadQuizFragment();
        Log.e(TAG, "onCreate: "+quizBO.getQuestions().get(0));


       // allquestions();
    }

    private void initViews(){
        //Toolbar intialisation...
        Toolbar toolbar=findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Smart Quiz");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        progressBar=findViewById(R.id.progressBar);
        tvError=findViewById(R.id.tv_error);
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

   /* public void allquestions() {

        if (CommonMethods.isNetworkAvailable(QuizActivity.this)) {
            Map<String, String> map = new HashMap<>();
            map.put("userid","1");
            progressBar.setVisibility(View.VISIBLE);

            smartQuiz.getApiRequestHelper().allquestions(map, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    //questionData = (QuestionData) object;
                    questionData = (AllQuestionResponse) object;

                    if (questionData != null) {
                        if (questionData.getResponsecode() == 200) {

                            Log.e(TAG, "onSuccess: "+questionData );

                            if (questionData.getData() != null) {
                               //loadQuizFragment();
                            }
                        } else if (!TextUtils.isEmpty(questionData.getMessage())) {
                            tvError.setVisibility(View.VISIBLE);
                            tvError.setText(questionData.getMessage());
                        }
                    } else {
                        Toast.makeText(QuizActivity.this, AllKeys.SERVER_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);
                    //Log.e("in", "error " + apiResponse);
                    Toast.makeText(QuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(QuizActivity.this, AllKeys.NO_INTERNET_AVAILABLE, Toast.LENGTH_SHORT).show();
        }

    }*/

    public void loadQuizFragment() {
        QuizFragment quizFragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("question", quizBO.getQuestions().get(currentQuesPos));
        quizFragment.setArguments(bundle);
        loadFragment(quizFragment);
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_my_score, menu);
        return true;
    }


}