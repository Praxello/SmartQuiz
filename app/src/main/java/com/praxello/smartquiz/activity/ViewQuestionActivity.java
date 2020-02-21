package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.services.SmartQuiz;
import com.praxello.smartquiz.widget.slidingitemrecyclerview.SlidingItemMenuRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewQuestionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_create_quiz_question)
    SlidingItemMenuRecyclerView rvCreateQuizQuestion;

    private static String TAG="ViewQuestionActivity";

    public static SmartQuiz smartQuiz;
    @BindView(R.id.btn_addquizquestion)
    AppCompatButton btnAddQuizQuestion;
    QuizBO quizBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        smartQuiz = (SmartQuiz) getApplication();
        ButterKnife.bind(this);

        if (getIntent().getParcelableExtra("data") != null) {
            //questionBO=getIntent().getParcelableArrayListExtra("data");
            quizBO = getIntent().getParcelableExtra("data");
        }

        //basic intialisation..
        initViews();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_create_question);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Question");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnAddQuizQuestion.setOnClickListener(this);
        rvCreateQuizQuestion.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addquizquestion:
                Intent intent=new Intent(ViewQuestionActivity.this, AddNewQuestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("data",quizBO);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
    }
}
