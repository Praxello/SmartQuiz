package com.praxello.smartquiz.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.praxello.smartquiz.AllKeys;
import com.praxello.smartquiz.CommonMethods;
import com.praxello.smartquiz.R;
import com.praxello.smartquiz.model.login.LoginResponse;
import com.praxello.smartquiz.services.ApiRequestHelper;
import com.praxello.smartquiz.services.SmartQuiz;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_title)
    TextView tvTitle;
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

        //setData to editext..
        setDataToEditTexts();
    }

    private void initViews(){
        Toolbar toolbar=findViewById(R.id.toolbar_update);
        toolbar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Update");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);


        btnSignUp.setOnClickListener(this);
        etBirthDate.setOnClickListener(this);
        etEmailAddress.setEnabled(false);
        etEmailAddress.setFocusable(false);
        etEmailAddress.setClickable(false);

        tvTitle.setVisibility(View.GONE);
        btnSignUp.setText("Update");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnsignup:
                if(CommonMethods.isNetworkAvailable(UpdateProfileActivity.this)){
                    if(isValidated()){
                        updateUserProfile();
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

    private void setDataToEditTexts(){

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.FIRST_NAME).equals(AllKeys.DNF)){
            etFirstName.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.FIRST_NAME));
        }
        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.LAST_NAME).equals(AllKeys.DNF)){
            etLastName.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.LAST_NAME));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.MOBILE).equals(AllKeys.DNF)){
            etMobileNumber.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.MOBILE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.EMAIL).equals(AllKeys.DNF)){
            etEmailAddress.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.EMAIL));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.CITY).equals(AllKeys.DNF)){
            etCity.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.CITY));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.STATE).equals(AllKeys.DNF)){
            etState.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.STATE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.COUNTRY).equals(AllKeys.DNF)){
            etCountry.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.COUNTRY));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PINCODE).equals(AllKeys.DNF)){
            etPincode.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PINCODE));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH).equals(AllKeys.DNF)){
            etBirthDate.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.ADDRESS).equals(AllKeys.DNF)){
            etAddress.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.ADDRESS));
        }

        if(!CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PASSWORD).equals(AllKeys.DNF)){
            etPassword.setText(CommonMethods.getPrefrence(UpdateProfileActivity.this, AllKeys.PASSWORD));
        }
    }

    private void updateUserProfile(){
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

        smartQuiz.getApiRequestHelper().updateuserprofile(params,new ApiRequestHelper.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                LoginResponse loginResponse=(LoginResponse) object;

                Log.e(TAG, "onSuccess: "+loginResponse.getResponsecode());
                Log.e(TAG, "onSuccess: "+loginResponse.getMessage());

                if(loginResponse.getResponsecode()==200){
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.USER_ID, loginResponse.getData().getUserId());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.FIRST_NAME, loginResponse.getData().getFirstName());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.LAST_NAME, loginResponse.getData().getLastName());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.USER_TYPE, loginResponse.getData().getUserType());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.MOBILE, loginResponse.getData().getMobile());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.EMAIL, loginResponse.getData().getEmail());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.CITY, loginResponse.getData().getCity());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.STATE, loginResponse.getData().getState());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.COUNTRY, loginResponse.getData().getCountry());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.PINCODE, loginResponse.getData().getPincode());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.DATEOFBIRTH, loginResponse.getData().getBirthDate());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.ADDRESS, loginResponse.getData().getAddress());
                    CommonMethods.setPreference(UpdateProfileActivity.this, AllKeys.PASSWORD, loginResponse.getData().getPassword());


                    Toast.makeText(UpdateProfileActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(UpdateProfileActivity.this,DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }else{
                    Toast.makeText(UpdateProfileActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String apiResponse) {
                Toast.makeText(UpdateProfileActivity.this, apiResponse, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private Boolean isValidated(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(etFirstName.getText().toString().isEmpty()){
            //etFirstName.setError("Firstname required!");
            Toast.makeText(smartQuiz, "Firstname required!", Toast.LENGTH_SHORT).show();
            etFirstName.requestFocus();
            return false;
        }

        if(etLastName.getText().toString().isEmpty()){
            //etLastNameLayout.setError("Lastname required!");
            Toast.makeText(smartQuiz, "Lastname required!", Toast.LENGTH_SHORT).show();
            etLastNameLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().isEmpty()){
            //etMobileLayout.setError("Mobile number required!");
            Toast.makeText(smartQuiz, "Mobile number required!", Toast.LENGTH_SHORT).show();
            etMobileLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().length()!=10){
            // etMobileLayout.setError("Invalid mobile number!");
            Toast.makeText(smartQuiz, "Invalid mobile number!", Toast.LENGTH_SHORT).show();
            etMobileLayout.requestFocus();
            return false;
        }

        if(etMobileNumber.getText().toString().startsWith("1") || etMobileNumber.getText().toString().startsWith("2") ||
                etMobileNumber.getText().toString().startsWith("3") || etMobileNumber.getText().toString().startsWith("4")
                || etMobileNumber.getText().toString().startsWith("5")){

            // etMobileLayout.setError("Invalid mobile number!");
            Toast.makeText(smartQuiz, "Invalid mobile number!", Toast.LENGTH_SHORT).show();
            etMobileLayout.requestFocus();
            return false;
        }

        if(etEmailAddress.getText().toString().isEmpty()){
            //etEmailAddressLayout.setError("Email required!");
            Toast.makeText(smartQuiz, "Email required!", Toast.LENGTH_SHORT).show();
            etEmailAddressLayout.requestFocus();
            return false;
        }

        if(!etEmailAddress.getText().toString().matches(emailPattern)){
            //etEmailAddressLayout.setError("Invalid email!");
            Toast.makeText(smartQuiz, "Invalid email!", Toast.LENGTH_SHORT).show();
            etEmailAddressLayout.setFocusable(true);
            etEmailAddressLayout.requestFocus();
            return false;
        }

        if(etPincode.getText().toString().isEmpty()){
            // etPincodeLayout.setError("Pincode required!");
            Toast.makeText(smartQuiz, "Pincode required!", Toast.LENGTH_SHORT).show();
            etPincodeLayout.requestFocus();
            return false;
        }

        if(etPincode.getText().toString().length()!=6){
            //etPincodeLayout.setError("Invalid mobile number!");
            Toast.makeText(smartQuiz, "Invalid mobile number!", Toast.LENGTH_SHORT).show();
            etPincodeLayout.requestFocus();
            return false;
        }
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