package com.praxello.smartmcq.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.CommonMethods;
import com.praxello.smartmcq.DemoFaceDistanceActivity;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.activity.quiz.QuizActivity;
import com.praxello.smartmcq.model.GetExamResponse;
import com.praxello.smartmcq.model.categories.GetCategoriesBO;
import com.praxello.smartmcq.model.categories.GetCategoriesResponse;
import com.praxello.smartmcq.services.ApiRequestHelper;
import com.praxello.smartmcq.services.SmartQuiz;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ll_generalquiz)
    public LinearLayout llGeneralQuiz;
    @BindView(R.id.ll_taketest)
    public LinearLayout llTakeTest;
    @BindView(R.id.ll_logout)
    public LinearLayout llLogOut;
    @BindView(R.id.ll_myscore)
    public LinearLayout llMyScore;
    @BindView(R.id.ll_about)
    public LinearLayout llAbout;
    @BindView(R.id.ll_account)
    public LinearLayout llAccount;
    @BindView(R.id.iv_share)
    public ImageView ivShare;
    @BindView(R.id.tv_introslider)
    public TextView tvIntroSlider;
    SmartQuiz smartQuiz;
    private static String TAG = "DashBoardActivity";
    AlertDialog alertDialog;
    public static ArrayList<GetCategoriesBO> getCategoriesBOArraylist=new ArrayList<GetCategoriesBO>();
    String[] array={"Use MCQs for quick objective test","Share with your students, collegeues easily","Use media for more interactive tests","Get your results on the spot"};
    int i=0;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(DashBoardActivity.this);
        smartQuiz = (SmartQuiz) getApplication();

        new CountDownTimer(3000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {

                tvIntroSlider.setText(array[i]);
                i++;
                if(i== array.length-1) i=0;
                start();
            }
        }.start();

        //basic intialisation..
        initViews();

        //load Categories data
        if(count==0){
            loadCategoriesData();
        }
    }

    private void initViews() {
        llGeneralQuiz.setOnClickListener(this);
        llTakeTest.setOnClickListener(this);
        llLogOut.setOnClickListener(this);
        llMyScore.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llAccount.setOnClickListener(this);
        ivShare.setOnClickListener(this);

        DashBoardActivity.getCategoriesBOArraylist.add(new GetCategoriesBO(0,"Select Category",1));
        /*TextView tvTitle = findViewById(R.id.tv_title);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/greatvibes-regular.otf");
        tvTitle.setTypeface(face);*/

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/noteworthy-bold.ttf");
        tvIntroSlider.setTypeface(face);
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

            case R.id.ll_myscore:
                intent = new Intent(DashBoardActivity.this, MyScoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.ll_taketest:
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_dialog_view, viewGroup, false);
                builder.setView(dialogView);
                EditText etQuizID = dialogView.findViewById(R.id.etquizid);
                AppCompatButton btnSubmit = dialogView.findViewById(R.id.btnsubmit);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etQuizID.getText().toString().isEmpty()) {
                            etQuizID.setError("Quiz Id required");
                            etQuizID.requestFocus();
                            etQuizID.setFocusable(true);
                        } else {
                            loadTest(etQuizID.getText().toString());
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                break;

            case R.id.ll_account:
                intent = new Intent(DashBoardActivity.this, UpdateProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.ll_about:
                intent = new Intent(DashBoardActivity.this, MyQuizActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.iv_share:
                intent = new Intent(DashBoardActivity.this, DemoFaceDistanceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Here is the share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));*/
                break;

            case R.id.ll_logout:
                new android.app.AlertDialog.Builder(DashBoardActivity.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure you want to logout?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.USER_ID, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.FIRST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.LAST_NAME, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.MOBILE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.EMAIL, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.CITY, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.STATE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.COUNTRY, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.PINCODE, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.DATEOFBIRTH, AllKeys.DNF);
                                CommonMethods.setPreference(DashBoardActivity.this, AllKeys.ADDRESS, AllKeys.DNF);
                                Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                                Toast.makeText(DashBoardActivity.this, "See you again!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
        }
    }

    private void loadTest(String quizid) {
        smartQuiz.getApiRequestHelper().getTest(quizid, new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                GetExamResponse getExamResponse = (GetExamResponse) object;
                alertDialog.dismiss();
                Log.e(TAG, "onSuccess: " + getExamResponse.getResponsecode());
                Log.e(TAG, "onSuccess: " + getExamResponse.getMessage());
                // Log.e(TAG, "onSuccess: "+getExamResponse.getData().size());

                if (getExamResponse.getResponsecode() == 200) {
                    if(getExamResponse.getData().getQuestions()!=null && getExamResponse.getData().getQuestions().size()>0){
                        Intent intent = new Intent(DashBoardActivity.this, QuizActivity.class);
                        //intent.putParcelableArrayListExtra("data_test",getExamResponse.getData());
                        intent.putExtra("data", getExamResponse.getData());
                        intent.putExtra("type","general");
                        startActivity(intent);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                    }else{
                        Toast.makeText(smartQuiz, "Invalid Quiz-ID!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(DashBoardActivity.this, getExamResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(DashBoardActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadCategoriesData() {
       // getCategoriesBOArraylist=new ArrayList<>();

        smartQuiz.getApiRequestHelper().getCategories( new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                GetCategoriesResponse getCategoriesResponse = (GetCategoriesResponse) object;
                //alertDialog.dismiss();
                Log.e(TAG, "onSuccess: " + getCategoriesResponse.getResponsecode());
                Log.e(TAG, "onSuccess: " + getCategoriesResponse.getMessage());
                // Log.e(TAG, "onSuccess: "+getExamResponse.getData().size());

                if (getCategoriesResponse.getResponsecode() == 200) {
                    if(getCategoriesResponse.getData()!=null){
                        if(DashBoardActivity.getCategoriesBOArraylist!=null){
                            for(int i=0;i<getCategoriesResponse.getData().size();i++){
                                DashBoardActivity.getCategoriesBOArraylist.add(getCategoriesResponse.Data.get(i));
                            }
                        }
                        Log.e(TAG, "onSuccess: "+DashBoardActivity.getCategoriesBOArraylist.toString() );
                        count++;
                    }
                 } else {
                    Toast.makeText(DashBoardActivity.this, getCategoriesResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(DashBoardActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });

    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //finishAffinity();
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
