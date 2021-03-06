package com.praxello.smartmcq.services;

import android.text.Html;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;import com.praxello.smartmcq.AllKeys;
import com.praxello.smartmcq.ConfigUrl;
import com.praxello.smartmcq.model.CommonResponse;
import com.praxello.smartmcq.model.CreateQuestionResponse;
import com.praxello.smartmcq.model.CreateQuizResponse;
import com.praxello.smartmcq.model.GetExamResponse;
import com.praxello.smartmcq.model.MyAllQuizResponse;
import com.praxello.smartmcq.model.allquestion.AllQuestionResponse;
import com.praxello.smartmcq.model.categories.GetCategoriesResponse;
import com.praxello.smartmcq.model.login.LoginResponse;
import com.praxello.smartmcq.model.quiz.UserData;
import com.praxello.smartmcq.model.scorecard.ScoreCardResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestHelper {

    public interface OnRequestComplete {
        void onSuccess(Object object);

        void onFailure(String apiResponse);
    }

    private static ApiRequestHelper instance;
    private SmartQuiz application;
    private SmartQuizServices SmartQuizServices;
    static Gson gson;


    public static synchronized ApiRequestHelper init(SmartQuiz application) {
        if (null == instance) {
            instance = new ApiRequestHelper();
            instance.setApplication(application);
            gson = new GsonBuilder()
                    .setLenient()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//                    .setExclusionStrategies(new ExclusionStrategy() {
//                        @Override
//                        public boolean shouldSkipField(FieldAttributes f) {
//                            return f.getDeclaringClass().equals(RealmObject.class);
//                        }
//
//                        @Override
//                        public boolean shouldSkipClass(Class<?> clazz) {
//                            return false;
//                        }
//                    })
                    .create();
            instance.createRestAdapter();
        }
        return instance;
    }

    public void allquestions(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<AllQuestionResponse> call = SmartQuizServices.allquestions(params);
        call_api(onRequestComplete, call);
    }

    public void login(String username,String uuid,String password, final OnRequestComplete onRequestComplete) {
        Call<LoginResponse> call = SmartQuizServices.login(username,uuid,password);
        call_api_login(onRequestComplete, call);
    }

    public void savequiz(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<UserData> call = SmartQuizServices.savequiz(params);
        call_api_for_quiz(onRequestComplete, call);
    }

    public void getTest(String quizid, final OnRequestComplete onRequestComplete) {
        Call<GetExamResponse> call = SmartQuizServices.getTest(quizid);
        call_api_test(onRequestComplete, call);
    }

    public void getAllQuestion(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<MyAllQuizResponse> call = SmartQuizServices.getAllQuestion(params);
        call_api_all_question(onRequestComplete, call);
    }

    public void createQuiz(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CreateQuizResponse> call = SmartQuizServices.createQuiz(params);
        call_api_create_my_quiz(onRequestComplete, call);
    }

    public void updateQuiz(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CommonResponse> call = SmartQuizServices.updateQuiz(params);
        call_api_create_quiz(onRequestComplete, call);
    }

    public void deleteQuiz(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CommonResponse> call = SmartQuizServices.deleteQuiz(params);
        call_api_create_quiz(onRequestComplete, call);
    }

    public void deleteQuizQuestion(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CommonResponse> call = SmartQuizServices.deleteQuizQuestion(params);
        call_api_create_quiz(onRequestComplete, call);
    }

    public void getCategories(final OnRequestComplete onRequestComplete) {
        Call<GetCategoriesResponse> call = SmartQuizServices.getCategories();
        call_api_all_categories(onRequestComplete, call);
    }

    public void forgetPassword(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<UserData> call = SmartQuizServices.forgetpassword(params);
        call_api_for_quiz(onRequestComplete, call);
    }

    public void adduserprofile(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<UserData> call = SmartQuizServices.adduserprofile(params);
        call_api_for_quiz(onRequestComplete, call);
    }

    public void updateuserprofile(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<LoginResponse> call = SmartQuizServices.updateuserprofile(params);
        call_api_for_update(onRequestComplete, call);
    }

    public void getscorecard(String userid, final OnRequestComplete onRequestComplete) {
        Call<ScoreCardResponse> call = SmartQuizServices.getscorecard(userid);
        call_api_for_scoreboard(onRequestComplete, call);
    }

    public void createquizquestion(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CreateQuestionResponse> call = SmartQuizServices.createQuestion(params);
        call_api_for_create_question(onRequestComplete, call);
    }

    public void updatequizquestion(Map<String, String> params, final OnRequestComplete onRequestComplete) {
        Call<CreateQuestionResponse> call = SmartQuizServices.updateQuestion(params);
        call_api_for_create_question(onRequestComplete, call);
    }


    /*public void savequiz(String userid,String score,String quizid, final OnRequestComplete onRequestComplete) {
        Call<UserData> call = SmartQuizServices.savequiz(userid,score,quizid);
        call_api_for_quiz(onRequestComplete, call);
    }*/

    public void uploadimage(Map<String, RequestBody> params, final OnRequestComplete onRequestComplete) {
        Call<ResponseBody> call = SmartQuizServices.uploadimage(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    Log.e("res", "" + response.code() + "||" + response.message() + "||" + response.body() + "||" + response.headers() + "||" + response.isSuccessful() + "||" + response.raw());
                    if (response.code() == 200 || response.isSuccessful()) {
                        onRequestComplete.onSuccess(null);
                    } else {
                        try {
                            if (response.errorBody() != null && !TextUtils.isEmpty(response.errorBody().toString()))
                                onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                            else
                                onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
                        } catch (IOException e) {
                            onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }



    private void call_api_for_create_question(final OnRequestComplete onRequestComplete, Call<CreateQuestionResponse> call) {
        call.enqueue(new Callback<CreateQuestionResponse>() {
            @Override
            public void onResponse(Call<CreateQuestionResponse> call, Response<CreateQuestionResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateQuestionResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_create_my_quiz(final OnRequestComplete onRequestComplete, Call<CreateQuizResponse> call) {
        call.enqueue(new Callback<CreateQuizResponse>() {
            @Override
            public void onResponse(Call<CreateQuizResponse> call, Response<CreateQuizResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateQuizResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_create_quiz(final OnRequestComplete onRequestComplete, Call<CommonResponse> call) {
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_for_scoreboard(final OnRequestComplete onRequestComplete, Call<ScoreCardResponse> call) {
        call.enqueue(new Callback<ScoreCardResponse>() {
            @Override
            public void onResponse(Call<ScoreCardResponse> call, Response<ScoreCardResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ScoreCardResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_all_categories(final OnRequestComplete onRequestComplete, Call<GetCategoriesResponse> call) {
        call.enqueue(new Callback<GetCategoriesResponse>() {
            @Override
            public void onResponse(Call<GetCategoriesResponse> call, Response<GetCategoriesResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCategoriesResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_for_quiz(final OnRequestComplete onRequestComplete, Call<UserData> call) {
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_for_update(final OnRequestComplete onRequestComplete, Call<LoginResponse> call) {
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }


    private void call_api_test(final OnRequestComplete onRequestComplete, Call<GetExamResponse> call) {
        call.enqueue(new Callback<GetExamResponse>() {
            @Override
            public void onResponse(Call<GetExamResponse> call, Response<GetExamResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetExamResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api_all_question(final OnRequestComplete onRequestComplete, Call<MyAllQuizResponse> call) {
        call.enqueue(new Callback<MyAllQuizResponse>() {
            @Override
            public void onResponse(Call<MyAllQuizResponse> call, Response<MyAllQuizResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyAllQuizResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }


    private void call_api_login(final OnRequestComplete onRequestComplete, Call<LoginResponse> call) {
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }

    private void call_api(final OnRequestComplete onRequestComplete, Call<AllQuestionResponse> call) {
        call.enqueue(new Callback<AllQuestionResponse>() {
            @Override
            public void onResponse(Call<AllQuestionResponse> call, Response<AllQuestionResponse> response) {
                if (response.isSuccessful()) {
                    onRequestComplete.onSuccess(response.body());
                } else {
                    try {
                        onRequestComplete.onFailure(Html.fromHtml(response.errorBody().string()) + "");
                    } catch (IOException e) {
                        onRequestComplete.onFailure("Unproper Response");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllQuestionResponse> call, Throwable t) {
                handle_fail_response(t, onRequestComplete);
            }
        });
    }


    private void handle_fail_response(Throwable t, OnRequestComplete onRequestComplete) {
        if (t.getMessage() != null) {
            if (t.getMessage().contains("Unable to resolve host")) {
                onRequestComplete.onFailure(AllKeys.NO_INTERNET_AVAILABLE);
            } else {
                onRequestComplete.onFailure(Html.fromHtml(t.getLocalizedMessage()) + "");
            }
        } else
            onRequestComplete.onFailure(AllKeys.UNPROPER_RESPONSE);
    }

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    /**
     * REST Adapter Configuration
     */
    private void createRestAdapter() {
//        ObjectMapper objectMapper = new ObjectMapper();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
// add your other interceptors …

// add logging as last interceptor
        httpClient.interceptors().add(logging);
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(ConfigUrl.BASE_URL1)
                        .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.client(httpClient.build()).build();
        SmartQuizServices = retrofit.create(SmartQuizServices.class);
    }

    /**
     * End REST Adapter Configuration
     */

    public SmartQuiz getApplication() {
        return application;
    }

    public void setApplication(SmartQuiz application) {
        this.application = application;
    }

}
