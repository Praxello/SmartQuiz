package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.praxello.smartquiz.R;
import com.praxello.smartquiz.activity.quiz.QuizFragment;
import com.praxello.smartquiz.fragment.ProgressFragment;
import com.praxello.smartquiz.fragment.ScoreBoardFragment;
import com.praxello.smartquiz.widget.ColorArcProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyScoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnprogress)
    AppCompatButton btnProgress;
    @BindView(R.id.btnscorecard)
    AppCompatButton btnScoreCard;
    public static final String TAG="MyScoreActivity";
    private String whichFragment="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);

        //basic intialisation
        initViews();

        ProgressFragment progressFragment= new ProgressFragment();
        loadFragment(progressFragment);

    }

    private void initViews(){

        Toolbar toolbar=findViewById(R.id.toolbar_scoreboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("ScoreBoard");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        btnProgress.setOnClickListener(this);
        btnScoreCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnprogress:
                whichFragment="Progress";
                loadProgressFragment();
                break;


            case R.id.btnscorecard:
                whichFragment="Scorecard";
                loadProgressFragment();
                break;
        }
    }

    public void loadProgressFragment() {
        if(whichFragment.equals("Progress")){
            ProgressFragment progressFragment= new ProgressFragment();
            loadFragment(progressFragment);
        }else{
            ScoreBoardFragment scoreBoardFragment= new ScoreBoardFragment();
            loadFragment(scoreBoardFragment);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

}
