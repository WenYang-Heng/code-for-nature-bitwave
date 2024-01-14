package com.codefornature.model;

public class UserTriviaModel {
    private int triviaNumber;
    private int attempts;
    private boolean isAnswered;

    public UserTriviaModel(int triviaNumber, boolean isAnswered, int attempts){
        this.triviaNumber = triviaNumber;
        this.isAnswered = isAnswered;
        this.attempts = attempts;
    }

    public int getTriviaNumber() {
        return triviaNumber;
    }

    public int getAttempts(){
        return attempts;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public void setAttempts(int attempts){
        this.attempts = attempts;
    }
}
