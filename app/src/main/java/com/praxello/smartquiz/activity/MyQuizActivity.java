package com.praxello.smartquiz.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.adapter.MyQuizAdapter;
import com.praxello.smartquiz.model.MyAllQuizResponse;
import com.praxello.smartquiz.model.allquestion.QuizBO;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;
import com.praxello.smartquiz.widget.slidingitemrecyclerview.SlidingItemMenuRecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyQuizActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rv_myquiz)
    SlidingItemMenuRecyclerView rvMyQuiz;
    private static String TAG="MyQuizActivity";
    public static SmartQuiz smartQuiz;
    @BindView(R.id.btn_addquiz)
    AppCompatButton btnAddQuiz;
    public static MyQuizAdapter myQuizAdapter;
    public static ArrayList<QuizBO> quizBOArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);
        smartQuiz = (SmartQuiz) getApplication();
        ButterKnife.bind(this);
        //basic intialisation..
        initViews();

        //load all quiz
        loadQuiz();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_myquiz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("MyQuiz");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        //button intialisation..
        btnAddQuiz.setOnClickListener(this);
        rvMyQuiz.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadQuiz(){
        Map<String,String> params=new HashMap<>();
        params.put("userid",CommonMethods.getPrefrence(MyQuizActivity.this, AllKeys.USER_ID));

        smartQuiz.getApiRequestHelper().getAllQuestion(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                MyAllQuizResponse myAllQuizResponse=(MyAllQuizResponse) object;

                Log.e(TAG, "onSuccess: "+myAllQuizResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+myAllQuizResponse.getMessage());
                // Log.e(TAG, "onSuccess: "+getExamResponse.getData().size());

                if(myAllQuizResponse.getResponsecode()==200){
                    if(myAllQuizResponse.getData()!=null){
                        quizBOArrayList=myAllQuizResponse.getData();

                        myQuizAdapter=new MyQuizAdapter(MyQuizActivity.this,quizBOArrayList);
                        rvMyQuiz.setAdapter(myQuizAdapter);
                    }
                }else{
                    Toast.makeText(MyQuizActivity.this, myAllQuizResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(MyQuizActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_addquiz:
                Intent intent = new Intent(MyQuizActivity.this, AddQuizActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("type","add");
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(MyQuizAdapter.quizBO!=null){
                myQuizAdapter.notifyDataSetChanged();
            }
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
