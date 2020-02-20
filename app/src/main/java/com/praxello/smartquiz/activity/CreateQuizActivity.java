package com.praxello.smartquiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.adapter.CustomSpinnerAdapter;
import com.praxello.smartquiz.adapter.MyQuizAdapter;
import com.praxello.smartquiz.model.CommonResponse;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.model.categories.GetCategoriesBO;
import com.praxello.smartquiz.model.quiz.UserData;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateQuizActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.et_quiz_title)
    EditText etQuizTitle;
    @BindView(R.id.spin_category)
    Spinner spinCategory;
    @BindView(R.id.et_timeout)
    EditText etTimeOut;
    @BindView(R.id.et_passing_score)
    EditText etPassingScore;
    @BindView(R.id.et_quiz_description)
    EditText etQuizDescription;
    @BindView(R.id.btncreatequiz)
    AppCompatButton btnCreateQuiz;
    SmartQuiz smartQuiz;
    int stCategoryId;
    private QuizBO quizBO;
    Toolbar toolbar;


    public static final String TAG="CreateQuizActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();

        if(getIntent().getParcelableExtra("data")!=null){
            quizBO=getIntent().getParcelableExtra("data");
            Log.e(TAG, "onCreate: "+quizBO.toString());
            toolbar.setTitle("Update Quiz");

            //set data to edittext...
            setDataToEditText();
        }else{
            toolbar.setTitle("Create Quiz");
        }

    }

    private void initViews(){
        toolbar=findViewById(R.id.toolbar_createquiz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Create Quiz");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        btnCreateQuiz.setOnClickListener(this);
        etTimeOut.setText("0");

        if(DashBoardActivity.getCategoriesBOArraylist!=null){
            CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(CreateQuizActivity.this,DashBoardActivity.getCategoriesBOArraylist);
            spinCategory.setAdapter(customSpinnerAdapter);
        }

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CreateQuizActivity.this, "Category Id"+DashBoardActivity.getCategoriesBOArraylist.get(position).getCategoryId(), Toast.LENGTH_SHORT).show();
                stCategoryId=DashBoardActivity.getCategoriesBOArraylist.get(position).getCategoryId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDataToEditText(){
        btnCreateQuiz.setText("Update Quiz");
        etQuizTitle.setText(quizBO.getTitle());
        etTimeOut.setText(String.valueOf(quizBO.getQuestionTimeout()));
        etPassingScore.setText(quizBO.getPassingScore());
        etQuizDescription.setText(quizBO.getDetails());
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btncreatequiz:
                /*if(getIntent().getParcelableExtra("data")!=null) {
                    if(isValidated()){
                        addQuiz();
                    }
                }else{

                }*/
                if(isValidated()){
                    updateQuiz();
                }
                break;
        }
    }

    private void addQuiz(){
        Map<String,String> params=new HashMap<>();
        params.put("userId", CommonMethods.getPrefrence(CreateQuizActivity.this, AllKeys.USER_ID));
        params.put("categoryId", String.valueOf(stCategoryId));
        params.put("title",etQuizTitle.getText().toString());
        params.put("details",etQuizDescription.getText().toString());
        params.put("passingScore",etPassingScore.getText().toString());
        params.put("questionTimeout",etTimeOut.getText().toString());
        params.put("quizTimeout",etTimeOut.getText().toString());

        Log.e(TAG, "addQuiz: "+params );

        smartQuiz.getApiRequestHelper().createQuiz(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CommonResponse commonResponse=(CommonResponse) object;

                Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                if(commonResponse.getResponsecode()==200){
                    Toast.makeText(CreateQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //finish();
                      //overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                }else{
                    Toast.makeText(CreateQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(CreateQuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuiz(){
            Map<String,String> params=new HashMap<>();
            params.put("userId", CommonMethods.getPrefrence(CreateQuizActivity.this, AllKeys.USER_ID));
            params.put("categoryId", String.valueOf(stCategoryId));
            params.put("title",etQuizTitle.getText().toString());
            params.put("details",etQuizDescription.getText().toString());
            params.put("passingScore",etPassingScore.getText().toString());
            params.put("questionTimeout",etTimeOut.getText().toString());
            params.put("quizTimeout",etTimeOut.getText().toString());
            params.put("quizId", String.valueOf(quizBO.getQuizId()));

            Log.e(TAG, "updateQuiz: "+params );

            smartQuiz.getApiRequestHelper().updateQuiz(params,new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    CommonResponse commonResponse=(CommonResponse) object;

                    Log.e(TAG, "onSuccess: "+commonResponse.getResponsecode());
                    Log.e(TAG, "onSuccess: "+commonResponse.getMessage());

                    if(commonResponse.getResponsecode()==200){
                       Toast.makeText(CreateQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                         finish();
                      /*  MyQuizAdapter.quizBO.setTitle(etQuizTitle.getText().toString());
                        MyQuizAdapter.quizBO.setDetails(etQuizTitle.getText().toString());
                        MyQuizAdapter.quizBO.setPassingScore(etPassingScore.getText().toString());
                        MyQuizAdapter.quizBO.setCategoryTitle(spinCategory.getSelectedItem().toString());
                      */
                        Intent intent = new Intent();
                        intent.putExtra("editTextValue", "value_here");
                        setResult(RESULT_OK, intent);
                        finish();

                        quizBO.setTitle(etQuizTitle.getText().toString());
                        quizBO.setDetails(etQuizTitle.getText().toString());
                        quizBO.setPassingScore(etPassingScore.getText().toString());
                        quizBO.setCategoryTitle(spinCategory.getSelectedItem().toString());

                        //finish();
                        //overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                    }else{
                        Toast.makeText(CreateQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    Toast.makeText(CreateQuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                }
            });
    }


    private boolean isValidated(){
        if(etQuizTitle.getText().toString().isEmpty()){
            etQuizTitle.setError("Quiz title required!");
            etQuizTitle.requestFocus();
            return false;
        }

        if(etPassingScore.getText().toString().isEmpty()){
            etPassingScore.setError("Passing score required!");
            etPassingScore.requestFocus();
            return false;
        }

        if(etQuizDescription.getText().toString().isEmpty()){
            etQuizDescription.setError("Quiz description required!");
            etQuizDescription.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }

}
