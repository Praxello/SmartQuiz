package com.praxello.smartmcq.model.scorecard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ScoresBO implements Parcelable {

    public int answerId;
    public int quizId;
    public int userId;
    public int score;
    public String quizDate;
    public int categoryId;
    public String title;
    public String details;
    public int passingScore;
    public int questionTimeout;
    public String createdAt;
    public String updatedAt;
    public int quizTimeout;
    public int isActive;
    public String categoryTitle;

    protected ScoresBO(Parcel in) {
        answerId = in.readInt();
        quizId = in.readInt();
        userId = in.readInt();
        score = in.readInt();
        quizDate = in.readString();
        categoryId = in.readInt();
        title = in.readString();
        details = in.readString();
        passingScore = in.readInt();
        questionTimeout = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        quizTimeout = in.readInt();
        isActive = in.readInt();
        categoryTitle = in.readString();
    }

    public static final Creator<ScoresBO> CREATOR = new Creator<ScoresBO>() {
        @Override
        public ScoresBO createFromParcel(Parcel in) {
            return new ScoresBO(in);
        }

        @Override
        public ScoresBO[] newArray(int size) {
            return new ScoresBO[size];
        }
    };

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getQuizDate() {
        return quizDate;
    }

    public void setQuizDate(String quizDate) {
        this.quizDate = quizDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getQuestionTimeout() {
        return questionTimeout;
    }

    public void setQuestionTimeout(int questionTimeout) {
        this.questionTimeout = questionTimeout;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getQuizTimeout() {
        return quizTimeout;
    }

    public void setQuizTimeout(int quizTimeout) {
        this.quizTimeout = quizTimeout;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    @NonNull
    @Override
    public String toString() {
        return "\nanswerId "+answerId+"\n quizId"+quizId+"\n userId"+userId+"\n score"+getScore()+"\n quizDate"+getQuizDate()+
                "\n categoryId" +getCategoryId()+"\n title"+getTitle()+"\n Details"+getDetails()+"\n passingScore"+passingScore+"\n questionTimeout"+questionTimeout;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(answerId);
        dest.writeInt(quizId);
        dest.writeInt(userId);
        dest.writeInt(score);
        dest.writeString(quizDate);
        dest.writeInt(categoryId);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeInt(passingScore);
        dest.writeInt(questionTimeout);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(quizTimeout);
        dest.writeInt(isActive);
        dest.writeString(categoryTitle);
    }
}
