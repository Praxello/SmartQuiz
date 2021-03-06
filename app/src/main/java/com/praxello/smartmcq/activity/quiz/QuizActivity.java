package com.praxello.smartmcq.activity.quiz;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.praxello.smartmcq.R;
import com.praxello.smartmcq.services.SmartQuiz;
import com.praxello.smartmcq.model.allquestion.QuizBO;

import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView tvError;
    public int totalScore = 0;
    public int currentQuesPos = 0;
    SmartQuiz smartQuiz;
    public static final String TAG = "QuizActivity";
    public QuizBO quizBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();

        Log.e(TAG, "onCreate:size getIntent " + getIntent().getParcelableArrayListExtra("data_test"));

        if (getIntent().getParcelableExtra("data") != null) {
            //questionBO=getIntent().getParcelableArrayListExtra("data");
            quizBO = getIntent().getParcelableExtra("data");
            loadQuizFragment();
        }

        //Log.e(TAG, "onCreate: "+quizBO.getQuestions().get(0));
        // allquestions();
    }

    private void initViews() {
        //Toolbar intialisation...
        Toolbar toolbar = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(getIntent().getStringExtra("type").equals("general")){
            toolbar.setTitle("Smart Quiz");
        }else if(getIntent().getStringExtra("type").equals("preview")){
            toolbar.setTitle("Quiz Preview");
        }

        toolbar.setTitleTextColor(Color.BLACK);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tv_error);
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
        Log.e(TAG, "loadQuizFragment: ");
        QuizFragment quizFragment = new QuizFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("question", quizBO.getQuestions().get(currentQuesPos));
        bundle.putParcelable("quiz_bo", quizBO);
        bundle.putString("type",getIntent().getStringExtra("type"));
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

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(smartQuiz, "Please complete quiz!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        if(getIntent().getStringExtra("type").equals("preview")){
            super.onBackPressed();
        }else{

        }
    }

}
