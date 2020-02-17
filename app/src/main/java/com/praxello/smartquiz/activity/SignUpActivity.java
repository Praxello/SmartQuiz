package com.praxello.smartquiz.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.quiz.UserData;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.etfirstname)
    EditText etFirstName;
    @BindView(R.id.etlastname)
    EditText etLastName;
    @BindView(R.id.etemailaddress)
    EditText etEmailAddress;
    @BindView(R.id.etmobile)
    EditText etMobileNumber;
    @BindView(R.id.etaddress)
    EditText etAddress;
    @BindView(R.id.etcity)
    EditText etCity;
    @BindView(R.id.etstate)
    EditText etState;
    @BindView(R.id.etcountry)
    EditText etCountry;
    @BindView(R.id.etpincode)
    EditText etPincode;
    @BindView(R.id.etbirthdate)
    EditText etBirthDate;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnsignup)
    AppCompatButton btnSignUp;
    @BindView(R.id.etfirstnamelayout)
    TextInputLayout etFirstNameLayout;
    @BindView(R.id.etlastnameinputlayout)
    TextInputLayout etLastNameLayout;
    @BindView(R.id.etemailaddresslayout)
    TextInputLayout etEmailAddressLayout;
    @BindView(R.id.etpincodelayout)
    TextInputLayout etPincodeLayout;
    @BindView(R.id.etmobilelayout)
    TextInputLayout etMobileLayout;
    private int mYear, mMonth, mDay;
    public static final String TAG="SignUpActivity";
    SmartQuiz smartQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        smartQuiz = (SmartQuiz) getApplication();

        //basic intialisation...
        initViews();
    }

    private void initViews(){
        btnSignUp.setOnClickListener(this);
        etBirthDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnsignup:
                if(CommonMethods.isNetworkAvailable(SignUpActivity.this)){
                    if(isValidated()){
                        addUserProfile();
                    }
                }
                break;


            case R.id.etbirthdate:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    private void addUserProfile(){
        Map<String,String> params=new HashMap<>();
        params.put("firstname",etFirstName.getText().toString());
        params.put("lastname",etLastName.getText().toString());
        params.put("email",etEmailAddress.getText().toString());
        params.put("mobile",etMobileNumber.getText().toString());
        params.put("address",etAddress.getText().toString());
        params.put("city",etCity.getText().toString());
        params.put("state",etState.getText().toString());
        params.put("pincode",etPincode.getText().toString());
        params.put("country",etCountry.getText().toString());
        params.put("password",etPassword.getText().toString());
        params.put("birthdate",etBirthDate.getText().toString());

        Log.e(TAG, "addUserProfile: "+params );

        smartQuiz.getApiRequestHelper().adduserprofile(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                UserData userData=(UserData) object;

                Log.e(TAG, "onSuccess: "+userData.getResponsecode());
                Log.e(TAG, "onSuccess: "+userData.getMessage());

                if(userData.getResponsecode()==200){
                    Toast.makeText(SignUpActivity.this, userData.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    Toast.makeText(SignUpActivity.this, userData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(SignUpActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean isValidated(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(etFirstName.getText().toString().isEmpty()){
            etFirstName.setError("Firstname required!");
            etFirstName.requestFocus();
            return false;
        }

        if(etLastName.getText().toString().isEmpty()){
            etLastNameLayout.setError("Lastname required!");
            etLastNameLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().isEmpty()){
            etMobileLayout.setError("Mobile number required!");
            etMobileLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().length()!=10){
            etMobileLayout.setError("Invalid mobile number!");
            etMobileLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().startsWith("1") || etMobileNumber.getText().toString().startsWith("2") ||
                etMobileNumber.getText().toString().startsWith("3") || etMobileNumber.getText().toString().startsWith("4")
                || etMobileNumber.getText().toString().startsWith("5")){

            etMobileLayout.setError("Invalid mobile number!");
            etMobileLayout.requestFocus();
            return false;
        }

        if(etEmailAddress.getText().toString().isEmpty()){
            etEmailAddressLayout.setError("Email required!");
            etEmailAddressLayout.requestFocus();
            return false;
        }

        if(!etEmailAddress.getText().toString().matches(emailPattern)){
            etEmailAddressLayout.setError("Invalid email!");
            etEmailAddressLayout.setFocusable(true);
            etEmailAddressLayout.requestFocus();
            return false;
        }

        if(etPincode.getText().toString().isEmpty()){
            etPincodeLayout.setError("Pincode required!");
            etPincodeLayout.requestFocus();
            return false;
        }

        if(etPincode.getText().toString().length()!=6){
            etPincodeLayout.setError("Invalid mobile number!");
            etPincodeLayout.requestFocus();
            return false;
        }
        return true;
    }
}
