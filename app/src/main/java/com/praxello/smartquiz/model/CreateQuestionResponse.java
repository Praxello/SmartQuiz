package com.praxello.smartquiz.model;

import com.praxello.smartquiz.model.allquestion.QuestionBO;

import java.util.ArrayList;

public class CreateQuestionResponse {

    public int Responsecode;
    public int Id;
    public String Message;
    public ArrayList<QuestionBO> NewRecord;

    public int  getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<QuestionBO> getNewRecord() {
        return NewRecord;
    }

    public void setNewRecord(ArrayList<QuestionBO> newRecord) {
        NewRecord = newRecord;
    }
}
