package com.praxello.smartquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.praxello.smartquiz.model.allquestion.QuizBO;

public class GetExamResponse implements Parcelable {
    public String Message;
    //public ArrayList<QuestionBO> Data;
    public QuizBO Data;
    public int Responsecode;

    protected GetExamResponse(Parcel in) {
        Message = in.readString();
        Data = in.readParcelable(QuizBO.class.getClassLoader());
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

    public QuizBO getData() {
        return Data;
    }

    public void setData(QuizBO data) {
        Data = data;
    }

    public int getResponsecode() {
        return Responsecode;
    }

    public void setResponsecode(int responsecode) {
        Responsecode = responsecode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeParcelable(Data, flags);
        dest.writeInt(Responsecode);
    }
}
