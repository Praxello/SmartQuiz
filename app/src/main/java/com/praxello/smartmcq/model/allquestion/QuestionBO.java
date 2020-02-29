package com.praxello.smartmcq.model.allquestion;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class QuestionBO implements Parcelable {

    public int questionId;
    public int questionType;
    public int quizId;
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public int answer;
    public String answerDetails;
    public int isActive;
    public String mediaUrl;

    public QuestionBO(int questionId, int questionType, int quizId, String question, String option1, String option2, String option3, String option4, int answer, String answerDetails, int isActive, String mediaUrl) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.quizId = quizId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.answerDetails = answerDetails;
        this.isActive = isActive;
        this.mediaUrl = mediaUrl;
    }

    protected QuestionBO(Parcel in) {
        questionId = in.readInt();
        questionType = in.readInt();
        quizId = in.readInt();
        question = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        answer = in.readInt();
        answerDetails = in.readString();
        isActive = in.readInt();
        mediaUrl=in.readString();
    }

    public static final Creator<QuestionBO> CREATOR = new Creator<QuestionBO>() {
        @Override
        public QuestionBO createFromParcel(Parcel in) {
            return new QuestionBO(in);
        }

        @Override
        public QuestionBO[] newArray(int size) {
            return new QuestionBO[size];
        }
    };

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String   getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "question_id"+this.questionId+"\n questionType "+questionType+"\n quidId "+quizId+"\n question "+question+"\noption1 "+option1+
                "\n option2 "+option2+"\n option3 "+option3+"\n option4 "+option4+ "\n answer"+ answer+"\n answerDetails "+answerDetails+"\n mediaUrl"+mediaUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionId);
        dest.writeInt(questionType);
        dest.writeInt(quizId);
        dest.writeString(question);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeString(option4);
        dest.writeInt(answer);
        dest.writeString(answerDetails);
        dest.writeInt(isActive);
        dest.writeString(mediaUrl);
    }
}
