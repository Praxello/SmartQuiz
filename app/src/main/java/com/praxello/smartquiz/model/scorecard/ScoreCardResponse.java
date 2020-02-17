package com.praxello.smartquiz.model.scorecard;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ScoreCardResponse {

    public String Message;
    public int Responsecode;
    public ArrayList<ScoresBO> Scores;
    public ArrayList<AvailableBO> Available;
    public ArrayList<AttemptsBO> Attempts;

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
}
