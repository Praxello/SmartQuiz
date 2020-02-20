package com.praxello.smartquiz.model;

import androidx.annotation.NonNull;

import com.praxello.smartquiz.model.allquestion.QuizBO;

import java.util.ArrayList;

public class MyAllQuizResponse {

    public int Responsecode;
    public String Message;
    public ArrayList<QuizBO> Data;

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<QuizBO> getData() {
        return Data;
    }

    public void setData(ArrayList<QuizBO> data) {
        Data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "Responsecode"+getResponsecode()+"\n Message"+getMessage()+"\n Data "+getData();
    }
}
