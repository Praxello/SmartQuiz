package com.praxello.smartmcq.model.allquestion;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

public class AllQuestionResponse implements Parcelable {

    public String Message;

    public int Responsecode;

    HashMap<String, List<QuizBO>> Data;

    protected AllQuestionResponse(Parcel in) {
        Message = in.readString();
        Responsecode = in.readInt();
    }

    public static final Creator<AllQuestionResponse> CREATOR = new Creator<AllQuestionResponse>() {
        @Override
        public AllQuestionResponse createFromParcel(Parcel in) {
            return new AllQuestionResponse(in);
        }

        @Override
        public AllQuestionResponse[] newArray(int size) {
            return new AllQuestionResponse[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    public HashMap<String, List<QuizBO>> getData()  {
        if(this.Data==null){
            Data= new HashMap<String, List<QuizBO>>();
        }
        return Data;
    }

    public void setData(HashMap<String, List<QuizBO>> data) {
        Data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getData()+"";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeInt(Responsecode);
    }
}
