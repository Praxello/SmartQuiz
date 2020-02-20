package com.praxello.smartquiz.model.categories;


import java.util.ArrayList;

public class GetCategoriesResponse {

    public int Responsecode;
    public String Message;
    public ArrayList<GetCategoriesBO> Data;

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

    public ArrayList<GetCategoriesBO> getData() {
        return Data;
    }

    public void setData(ArrayList<GetCategoriesBO> data) {
        Data = data;
    }
}
