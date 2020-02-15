package com.praxello.smartquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.praxello.smartquiz.model.allquestion.QuestionBO;

import java.util.ArrayList;

public class GetExamResponse implements Parcelable {
    public String Message;
    public ArrayList<QuestionBO> Data;
    public int Responsecode;

    protected GetExamResponse(Parcel in) {
        Message = in.readString();
        Data = in.createTypedArrayList(QuestionBO.CREATOR);
        Responsecode = in.readInt();
    }

    public static final Creator<GetExamResponse> CREATOR = new Creator<GetExamResponse>() {
        @Override
        public GetExamResponse createFromParcel(Parcel in) {
            return new GetExamResponse(in);
        }

        @Override
        public GetExamResponse[] newArray(int size) {
            return new GetExamResponse[size];
        }
    };

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ArrayList<QuestionBO> getData() {
        return Data;
    }

    public void setData(ArrayList<QuestionBO> data) {
        Data = data;
    }

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getResponsecode()+"\n get data"+getData()+"\n getMessage "+getMessage();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeTypedList(Data);
        dest.writeInt(Responsecode);
    }
}
