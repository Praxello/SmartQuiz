package com.praxello.smartquiz.activity.retrofit;

import android.app.Application;
import android.content.Context;

public class SmartQuiz extends Application {

    private ApiRequestHelper apiRequestHelper;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();

    }

    private void doInit() {

        this.apiRequestHelper = ApiRequestHelper.init(this);
    }

    public synchronized ApiRequestHelper getApiRequestHelper() {
        return apiRequestHelper;
    }
}

