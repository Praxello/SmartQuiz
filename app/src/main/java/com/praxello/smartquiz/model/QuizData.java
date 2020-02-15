package com.praxello.smartquiz.model;

public class QuizData {

    public int quiz_typeid;
    public String quiz_type;

    public QuizData(int quiz_typeid, String quiz_type) {
        this.quiz_typeid = quiz_typeid;
        this.quiz_type = quiz_type;
    }

    public int getQuiz_typeid() {
        return quiz_typeid;
    }

    public void setQuiz_typeid(int quiz_typeid) {
        this.quiz_typeid = quiz_typeid;
    }

    public String getQuiz_type() {
        return quiz_type;
    }

    public void setQuiz_type(String quiz_type) {
        this.quiz_type = quiz_type;
    }
}
