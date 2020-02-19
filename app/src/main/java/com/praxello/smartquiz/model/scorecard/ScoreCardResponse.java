package com.praxello.smartquiz.model.scorecard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ScoreCardResponse implements Parcelable {

    public String Message;
    public int Responsecode;
    public ArrayList<ScoresBO> Scores;
    public ArrayList<AvailableBO> Available;
    public ArrayList<AttemptsBO> Attempts;

    protected ScoreCardResponse(Parcel in) {
        Message = in.readString();
        Responsecode = in.readInt();
        Scores = in.createTypedArrayList(ScoresBO.CREATOR);
        Available = in.createTypedArrayList(AvailableBO.CREATOR);
        Attempts = in.createTypedArrayList(AttemptsBO.CREATOR);
    }

    public static final Creator<ScoreCardResponse> CREATOR = new Creator<ScoreCardResponse>() {
        @Override
        public ScoreCardResponse createFromParcel(Parcel in) {
            return new ScoreCardResponse(in);
        }

        @Override
        public ScoreCardResponse[] newArray(int size) {
            return new ScoreCardResponse[size];
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

    public ArrayList<ScoresBO> getScores() {
        return Scores;
    }

    public void setScores(ArrayList<ScoresBO> scores) {
        Scores = scores;
    }

    public ArrayList<AvailableBO> getAvailable() {
        return Available;
    }

    public void setAvailable(ArrayList<AvailableBO> available) {
        Available = available;
    }

    public ArrayList<AttemptsBO> getAttempts() {
        return Attempts;
    }

    public void setAttempts(ArrayList<AttemptsBO> attempts) {
        Attempts = attempts;
    }

    @NonNull
    @Override
    public String toString() {
        return "Response code \n "+Responsecode+"\n message "+getMessage()+"\n ScoresBO"+getScores()+"\n AvailableBO "+getAvailable()+"\n AttemptsBo "+getAttempts();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Message);
        dest.writeInt(Responsecode);
        dest.writeTypedList(Scores);
        dest.writeTypedList(Available);
        dest.writeTypedList(Attempts);
    }
}
