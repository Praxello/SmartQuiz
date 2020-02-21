package com.praxello.smartquiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.CreateQuestionResponse;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.model.quiz.UserData;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewQuestionActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG="AddNewQuestionActivity";
    public static SmartQuiz smartQuiz;

    @BindView(R.id.spin_category)
    Spinner spinCategory;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.et_option1)
    EditText etOption1;
    @BindView(R.id.et_option2)
    EditText etOption2;
    @BindView(R.id.et_option3)
    EditText etOption3;
    @BindView(R.id.et_option4)
    EditText etOption4;
    @BindView(R.id.et_medialink)
    EditText etMediaLink;
    @BindView(R.id.et_details)
    EditText etDetails;
    @BindView(R.id.btn_createquestion)
    AppCompatButton btnCreateQuestion;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioButton3)
    RadioButton radioButton3;
    @BindView(R.id.radioButton4)
    RadioButton radioBuuton4;
    QuizBO quizBO;
    private int stCategoryId=0;
    private int selectedRadioBtn=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        smartQuiz = (SmartQuiz) getApplication();
        ButterKnife.bind(this);

        if (getIntent().getParcelableExtra("data") != null) {
            //questionBO=getIntent().getParcelableArrayListExtra("data");
            quizBO = getIntent().getParcelableExtra("data");
            Log.e(TAG, "onCreate: "+quizBO.toString() );
        }

        //basic intialisation..
        initViews();

        //set Data to adapter spin category
        setDataToAdapter();

        //getRadio button selected data....
        getRadioButtonData();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_addnewquestion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("New Question");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnCreateQuestion.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_createquestion:
                if(isValidated()){
                    createQuestion();
                }
                break;
        }
    }

    private void setDataToAdapter(){
        String[] arraySpinner = new String[] {"Normal","Image","Video"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(adapter);

        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stCategoryId=position+1;
                //Toast.makeText(AddNewQuestionActivity.this, "Category Id"+stCategoryId, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRadioButtonData(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton1) {
                    selectedRadioBtn=1;
                    //Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton2) {
                    selectedRadioBtn=2;
                    //Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton3) {
                    selectedRadioBtn=3;
                   // Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }else if(checkedId == R.id.radioButton4) {
                    selectedRadioBtn=4;
                   // Toast.makeText(getApplicationContext(), selectedRadioBtn+"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createQuestion(){
        Map<String,String> params=new HashMap<>();
        params.put("questionType",String.valueOf(stCategoryId));
        params.put("quizId",String.valueOf(quizBO.getQuizId()));
        params.put("question",etQuestion.getText().toString());
        params.put("option1",etOption1.getText().toString());
        params.put("option2",etOption2.getText().toString());
        params.put("option3",etOption3.getText().toString());
        params.put("option4",etOption4.getText().toString());
        params.put("answer",String.valueOf(selectedRadioBtn));
        params.put("answerDetails",etDetails.getText().toString());
        params.put("mediaUrl",etMediaLink.getText().toString());

        Log.e(TAG, "createQuestion: "+params );

        smartQuiz.getApiRequestHelper().createquizquestion(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                CreateQuestionResponse createQuestionResponse=(CreateQuestionResponse) object;

                Log.e(TAG, "onSuccess: "+createQuestionResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+createQuestionResponse.getMessage());

                if(createQuestionResponse.getResponsecode()==200){
                    Toast.makeText(AddNewQuestionActivity.this, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    //clear all fields data
                    resetAllData();

                }else{
                    Toast.makeText(AddNewQuestionActivity.this, createQuestionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(AddNewQuestionActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetAllData(){
        etQuestion.setText("");
        etOption1.setText("");
        etOption2.setText("");
        etOption3.setText("");
        etOption4.setText("");
        etMediaLink.setText("");
        etDetails.setText("");
    }

    private boolean isValidated(){
        if(etQuestion.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Question required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption1.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option1 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption2.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option2 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption3.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option3 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etOption4.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Option4 required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(stCategoryId==2 || stCategoryId==3){
            if(etMediaLink.getText().toString().isEmpty()){
                Toast.makeText(smartQuiz, "Media link required!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if(etDetails.getText().toString().isEmpty()){
            Toast.makeText(smartQuiz, "Details required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(smartQuiz, "Please select one right option!", Toast.LENGTH_SHORT).show();
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
