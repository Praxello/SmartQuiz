package com.praxello.smartmcq.model;

import com.praxello.smartmcq.model.allquestion.QuizBO;

public class CreateQuizResponse {

    public int Responsecode;
    public int Id;
    public String Message;
    public QuizBO NewRecord;

    public int getResponsecode() {
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

    public QuizBO getNewRecord() {
        return NewRecord;
    }

    public void setNewRecord(QuizBO newRecord) {
        NewRecord = newRecord;
    }
}
