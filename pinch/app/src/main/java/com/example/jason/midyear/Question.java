package com.example.jason.midyear;

public class Question {
    private String category;
    private String shortcut;
    private int question;
    private String answer;

    public Question() {
    }

    public Question(String category,String shortcut,int question, String answer) {
        this.category=category;
        this.shortcut=shortcut;
        this.question = question;
        this.answer = answer;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }
}