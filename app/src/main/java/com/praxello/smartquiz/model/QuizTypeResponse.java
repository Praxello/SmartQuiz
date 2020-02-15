package com.praxello.smartquiz.model;

import java.util.ArrayList;

public class QuizTypeResponse {

    public int Responsecode;
    public ArrayList<QuizData> Data;
    public String Message;

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    public ArrayList<QuizData> getData() {
        return Data;
    }

    public void setData(ArrayList<QuizData> data) {
        Data = data;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
