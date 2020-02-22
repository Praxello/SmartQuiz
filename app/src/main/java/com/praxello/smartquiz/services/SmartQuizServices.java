package com.praxello.smartquiz.services;

import com.praxello.smartquiz.model.CommonResponse;
import com.praxello.smartquiz.model.CreateQuestionResponse;
import com.praxello.smartquiz.model.CreateQuizResponse;
import com.praxello.smartquiz.model.GetExamResponse;
import com.praxello.smartquiz.model.MyAllQuizResponse;
import com.praxello.smartquiz.model.allquestion.AllQuestionResponse;
import com.praxello.smartquiz.model.categories.GetCategoriesResponse;
import com.praxello.smartquiz.model.login.LoginResponse;
import com.praxello.smartquiz.model.quiz.UserData;
import com.praxello.smartquiz.model.scorecard.ScoreCardResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface SmartQuizServices {

    @Multipart
    @POST("~tailor/smartquiz/uploadimage.php")
    Call<ResponseBody> uploadimage(@PartMap Map<String, RequestBody> params);


    @GET("~tailor/smartquiz/user/allquestions.php")
    Call<AllQuestionResponse> allquestions(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/login.php")
    Call<LoginResponse> login(@Field("usrname") String usrname, @Field("uuid") String uuid,@Field("passwrd") String passwrd);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/savequiz.php")
    Call<UserData> savequiz(@FieldMap Map<String,String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/getexam1.php")
    Call<GetExamResponse> getTest(@Field("quizid") String quizid);

    @GET("~tailor/smartquiz/user/forgetpassword.php")
    Call<UserData> forgetpassword(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/userregister.php")
    Call<UserData> adduserprofile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/updateprofile.php")
    Call<LoginResponse> updateuserprofile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/scorecard.php")
    Call<ScoreCardResponse> getscorecard(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/myallquiz.php")
    Call<MyAllQuizResponse> getAllQuestion(@FieldMap Map<String, String> params);

    @GET("~tailor/smartquiz/user/getcategories.php")
    Call<GetCategoriesResponse> getCategories();

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/createquiz.php")
    Call<CreateQuizResponse> createQuiz(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/updatequiz.php")
    Call<CommonResponse> updateQuiz(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/deletequiz.php")
    Call<CommonResponse> deleteQuiz(@FieldMap Map<String, String> params);

 /*   @FormUrlEncoded
    @POST("~tailor/smartquiz/user/createquizquestion.php")
    Call<CreateQuestionResponse> createQuestion(@FieldMap Map<String, String> params);
  */
    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/createquizquestion1.php")
    Call<CreateQuestionResponse> createQuestion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/updatequizquestion.php")
    Call<CreateQuestionResponse> updateQuestion(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("~tailor/smartquiz/user/deletequizquestion.php")
    Call<CommonResponse> deleteQuizQuestion(@FieldMap Map<String, String> params);

}
