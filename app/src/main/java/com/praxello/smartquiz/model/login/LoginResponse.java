package com.praxello.smartquiz.model.login;


import androidx.annotation.NonNull;

public class LoginResponse {
    LoginData Data;
    public int Responsecode;
    public String Message;

    public LoginData getData() {
        return Data;
    }

    public void setData(LoginData data) {
        Data = data;
    }

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


}