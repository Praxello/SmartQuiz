package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.praxello.smartquiz.R;

import butterknife.BindView;

public class MyQuizActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);
    }
}
