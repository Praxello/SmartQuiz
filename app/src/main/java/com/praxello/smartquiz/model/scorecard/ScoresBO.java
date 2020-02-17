package com.praxello.smartquiz.model.scorecard;

import androidx.annotation.NonNull;

public class ScoresBO {

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
}
