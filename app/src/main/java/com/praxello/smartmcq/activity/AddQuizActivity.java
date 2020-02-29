package com.praxello.smartmcq.activity;

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
import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.CommonMethods;
import com.praxello.smartmcq.R;
import com.praxello.smartmcq.adapter.CustomSpinnerAdapter;
import com.praxello.smartmcq.model.CommonResponse;
import com.praxello.smartmcq.model.CreateQuizResponse;
import com.praxello.smartmcq.model.allquestion.QuizBO;
import com.praxello.smartmcq.services.ApiRequestHelper;
import com.praxello.smartmcq.services.SmartQuiz;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddQuizActivity extends AppCompatActivity implements View.OnClickListener{

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
    String stCategoryName;
    private QuizBO quizBO;
    Toolbar toolbar;
    public static final String TAG="AddQuizActivity";

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
            CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(AddQuizActivity.this,DashBoardActivity.getCategoriesBOArraylist);
            spinCategory.setAdapter(customSpinnerAdapter);
        }

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddQuizActivity.this, "Category Id"+DashBoardActivity.getCategoriesBOArraylist.get(position).getCategoryId(), Toast.LENGTH_SHORT).show();
                stCategoryId=DashBoardActivity.getCategoriesBOArraylist.get(position).getCategoryId();
                stCategoryName=DashBoardActivity.getCategoriesBOArraylist.get(position).getCategoryTitle();
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
        etPassingScore.setText(String.valueOf(quizBO.getPassingScore()));
        etQuizDescription.setText(quizBO.getDetails());

        for(int i=0;i<DashBoardActivity.getCategoriesBOArraylist.size();i++){
            if(quizBO.getCategoryTitle().equals(DashBoardActivity.getCategoriesBOArraylist.get(i).getCategoryTitle())){
                spinCategory.setSelection(i);
                break;
            }
        }
        //spinCategory.setPrompt("android");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btncreatequiz:

                if(getIntent().getStringExtra("type").equals("add")){
                    if(isValidated()){
                        addQuiz();
                    }
                }else if(getIntent().getStringExtra("type").equals("update")){
                    if(isValidated()){
                        updateQuiz();
                    }
                }

                break;
        }
    }

    private void addQuiz(){
        Map<String,String> params=new HashMap<>();
        params.put("userId", CommonMethods.getPrefrence(AddQuizActivity.this, AllKeys.USER_ID));
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
                CreateQuizResponse createQuizResponse=(CreateQuizResponse) object;

                Log.e(TAG, "onSuccess: "+createQuizResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+createQuizResponse.getMessage());

                if(createQuizResponse.getResponsecode()==200){

                  /*  try{*/
                        QuizBO quizBO1=new QuizBO(createQuizResponse.getNewRecord().getQuizId(),
                                createQuizResponse.getNewRecord().getUserId(),
                                stCategoryId,
                                etQuizTitle.getText().toString(),
                                etQuizDescription.getText().toString(),
                                Integer.parseInt(etPassingScore.getText().toString()),
                                Integer.parseInt(etTimeOut.getText().toString()),
                                Integer.parseInt(etTimeOut.getText().toString()),
                                createQuizResponse.getNewRecord().getCreatedAt(),
                                createQuizResponse.getNewRecord().getUpdatedAt(),
                                1,
                                stCategoryName,
                                createQuizResponse.getNewRecord().getQuestions());

                        MyQuizActivity.quizBOArrayList.add(quizBO1);
                        MyQuizActivity.mainQuizBO=quizBO1;
                        MyQuizActivity.myQuizAdapter.notifyDataSetChanged();
                        finish();
                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate);

                  /*  }catch (Exception e){
                        e.printStackTrace();
                    }
*/
                    Toast.makeText(AddQuizActivity.this, createQuizResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(AddQuizActivity.this,ViewQuestionActivity.class);
                    intent.putExtra("data",quizBO1);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                    //clear all data of editext.
                    clearAllData();
                }else{
                    Toast.makeText(AddQuizActivity.this, createQuizResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(AddQuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAllData(){
          etQuizTitle.setText("");
          etTimeOut.setText("");
          etPassingScore.setText("");
          etQuizDescription.setText("");
    }

    private void updateQuiz(){
            Map<String,String> params=new HashMap<>();
            params.put("userId", CommonMethods.getPrefrence(AddQuizActivity.this, AllKeys.USER_ID));
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
                       Toast.makeText(AddQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                         finish();
                        QuizBO quizBO1=new QuizBO(quizBO.getQuizId(),
                                quizBO.getUserId(),
                                stCategoryId,
                                etQuizTitle.getText().toString(),
                                etQuizDescription.getText().toString(),
                                Integer.parseInt(etPassingScore.getText().toString()),
                                Integer.parseInt(etTimeOut.getText().toString()),
                                Integer.parseInt(etTimeOut.getText().toString()),
                                quizBO.getCreatedAt(),
                                quizBO.getUpdatedAt(),
                                1,
                                stCategoryName,
                                quizBO.getQuestions());

                        int position=getIntent().getIntExtra("position",0);
                        MyQuizActivity.quizBOArrayList.set(position,quizBO1);
                        finish();
                        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_translate);
                    }else{
                        Toast.makeText(AddQuizActivity.this, commonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    Toast.makeText(AddQuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
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

        if(stCategoryId==0){
            Toast.makeText(smartQuiz, "Please select category!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int passingScore= Integer.parseInt(etPassingScore.getText().toString());
        if(passingScore>100){
            etPassingScore.setError("Passing score should be between 0 to 100!");
            etPassingScore.requestFocus();
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
