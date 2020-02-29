package com.praxello.smartmcq.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.praxello.smartmcq.R;
import com.praxello.smartmcq.model.quiz.UserData;
import com.praxello.smartmcq.services.ApiRequestHelper;
import com.praxello.smartmcq.services.SmartQuiz;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.etMobileNumber)
    public EditText etMail;
    @BindView(R.id.btnreset)
    public AppCompatButton btnReset;
    SmartQuiz smartQuiz;
    private static final String TAG="ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidated()){
                    resendPassword(etMail.getText().toString());
                }
            }
        });
    }

    private void resendPassword(String mailId){
        Map<String,String> params=new HashMap<>();
        params.put("mobile",mailId);

        smartQuiz.getApiRequestHelper().forgetPassword(params,new ApiRequestHelper.OnRequestComplete() {
                    @Override
                    public void onSuccess(Object object) {
                        UserData userData=(UserData) object;

                        Log.e(TAG, "onSuccess: "+userData.getResponsecode());
                        Log.e(TAG, "onSuccess: "+userData.getMessage());

                        if(userData.getResponsecode()==200){
                            Toast.makeText(ForgotPasswordActivity.this, userData.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        }else{
                            Toast.makeText(ForgotPasswordActivity.this, userData.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String apiResponse) {
                        Toast.makeText(ForgotPasswordActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private Boolean isValidated() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (etMail.getText().toString().isEmpty()) {
            etMail.setError("Email required!");
            etMail.setFocusable(true);
            etMail.requestFocus();
            return false;
        }

        if(!etMail.getText().toString().matches(emailPattern)){
            etMail.setError("Invalid email!");
            etMail.setFocusable(true);
            etMail.requestFocus();
            return false;
        }
        return true;
    }
}
