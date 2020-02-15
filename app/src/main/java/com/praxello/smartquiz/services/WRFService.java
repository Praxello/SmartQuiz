package com.praxello.smartquiz.services;

import com.praxello.smartquiz.model.GetExamResponse;
import com.praxello.smartquiz.model.allquestion.AllQuestionResponse;
import com.praxello.smartquiz.model.login.LoginResponse;
import com.praxello.smartquiz.model.quiz.UserData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface WRFService {

    @GET("~tailor/smartquiz/user/allquestions.php")
    Call<AllQuestionResponse> allquestions(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/login.php")
    Call<LoginResponse> login(@Field("usrname") String usrname, @Field("uuid") String uuid,@Field("passwrd") String passwrd);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/savequiz.php")
    Call<UserData> savequiz(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/getexam.php")
    Call<GetExamResponse> getTest(@Field("quizid") String quizid);
}
