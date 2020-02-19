package com.praxello.smartquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.praxello.smartquiz.model.allquestion.QuestionBO;
import com.praxello.smartquiz.model.allquestion.QuizBO;

import java.util.ArrayList;

public class QuizBOExam implements Parcelable {

    public int quizId;
    public int userId;
    public int categoryId ;
    public String title;
    public String details;
    public int questionTimeout;
    public int quizTimeout;
    public String createdAt;
    public String updatedAt;
    public int isActive;
    public String categoryTitle;
    public ArrayList<QuestionBO> questions;


    protected QuizBOExam(Parcel in) {
        quizId = in.readInt();
        userId = in.readInt();
        categoryId = in.readInt();
        title = in.readString();
        details = in.readString();
        questionTimeout = in.readInt();
        quizTimeout = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        isActive = in.readInt();
        categoryTitle = in.readString();
        questions = in.createTypedArrayList(QuestionBO.CREATOR);
    }

    public static final Creator<QuizBOExam> CREATOR = new Creator<QuizBOExam>() {
        @Override
        public QuizBOExam createFromParcel(Parcel in) {
            return new QuizBOExam(in);
        }

        @Override
        public QuizBOExam[] newArray(int size) {
            return new QuizBOExam[size];
        }
    };

    /* @Override
        public int hashCode() {
            return quizId;
        }
    */
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

    public int getQuestionTimeout() {
        return questionTimeout;
    }

    public void setQuestionTimeout(int questionTimeout) {
        this.questionTimeout = questionTimeout;
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

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public ArrayList<QuestionBO> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionBO> questions) {
        questions = questions;
    }

    @NonNull
    @Override
    public String toString() {
        return  this.quizId+" \n"+this.title+"\n "+this.categoryTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(quizId);
        dest.writeInt(userId);
        dest.writeInt(categoryId);
        dest.writeString(title);
        dest.writeString(details);
        dest.writeInt(questionTimeout);
        dest.writeInt(quizTimeout);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(isActive);
        dest.writeString(categoryTitle);
        dest.writeTypedList(questions);
    }


}
