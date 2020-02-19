package com.praxello.smartquiz.activity;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.fragment.ProgressFragment;
import com.praxello.smartquiz.fragment.ScoreBoardFragment;
import com.praxello.smartquiz.model.scorecard.AttemptsBO;
import com.praxello.smartquiz.model.scorecard.AvailableBO;
import com.praxello.smartquiz.model.scorecard.ScoreCardResponse;
import com.praxello.smartquiz.model.scorecard.ScoresBO;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyScoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnprogress)
    AppCompatButton btnProgress;
    @BindView(R.id.btnscorecard)
    AppCompatButton btnScoreCard;
    public static final String TAG="MyScoreActivity";
    ArrayList<ScoresBO> scoresBOArrayList;
    SmartQuiz smartQuiz;
    public static ArrayList<AttemptsBO> attemptsBOArrayList;
    public static Map<String, Integer> allAttemptsDataMap;
    public static ArrayList<AvailableBO> availableBOArrayList;
    private String whichFragment="";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation
        initViews();

        //loadData
        loadData();
    }

    private void initViews(){
        toolbar=findViewById(R.id.toolbar_scoreboard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Completed Levels");
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
                loadFragment("Progress");
                break;

            case R.id.btnscorecard:
                whichFragment="Scorecard";
                loadFragment("Scorecard");
                break;
        }
    }

    public void loadFragment(String whichFragment) {
        if(whichFragment.equals("Progress")){
            ProgressFragment progressFragment= new ProgressFragment();
            loadFragment(progressFragment);
            toolbar.setTitle("Completed Levels");
        }else{
            ScoreBoardFragment scoreBoardFragment= new ScoreBoardFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("scores",scoresBOArrayList);
            scoreBoardFragment.setArguments(bundle);
            loadFragment(scoreBoardFragment);
            toolbar.setTitle("Score Board");
        }
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void loadData(){
        smartQuiz.getApiRequestHelper().getscorecard(CommonMethods.getPrefrence(MyScoreActivity.this, AllKeys.USER_ID),new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                ScoreCardResponse scoreCardResponse=(ScoreCardResponse) object;

                Log.e(TAG, "onSuccess: "+scoreCardResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+scoreCardResponse.getMessage());
                Log.e(TAG, "onSuccess: "+scoreCardResponse);

                if(scoreCardResponse.getResponsecode()==200){
                    Toast.makeText(smartQuiz, scoreCardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if(scoreCardResponse.getScores()!=null){
                        scoresBOArrayList=scoreCardResponse.getScores();
                        //Log.e(TAG, "onSuccess: size of scoresBOArrayList "+scoresBOArrayList.size() );
                       // ScoreBoardAdapter scoreBoardAdapter=new ScoreBoardAdapter(getContext(),scoresBOArrayList);
                        //rvScoreBoard.setAdapter(scoreBoardAdapter);
                    }

                    if(scoreCardResponse.getAvailable()!=null){

                        allAttemptsDataMap=new HashMap<>();
                        MyScoreActivity.attemptsBOArrayList=scoreCardResponse.getAttempts();
                        if(scoreCardResponse.getAttempts()!=null){
                            for(AttemptsBO temp:MyScoreActivity.attemptsBOArrayList){
                                allAttemptsDataMap.put(temp.categoryTitle,temp.total);
                            }
                        }
                        //allAttemptsDataMap=  scoreCardResponse.getAttempts();

                        availableBOArrayList=scoreCardResponse.getAvailable();
                        if(availableBOArrayList!=null){
                            ProgressFragment progressFragment= new ProgressFragment();
                            loadFragment(progressFragment);
                        }

                    }

                }else{
                    Toast.makeText(smartQuiz, scoreCardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(smartQuiz, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_my_score, menu);
        return true;
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
