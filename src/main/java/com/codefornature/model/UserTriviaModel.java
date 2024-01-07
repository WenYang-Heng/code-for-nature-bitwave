package com.codefornature.model;

public class UserTriviaModel {
    private int triviaNumber;
    private boolean isAnswered;

    public UserTriviaModel(int triviaNumber, boolean isAnswered){
        this.triviaNumber = triviaNumber;
        this.isAnswered = isAnswered;
    }

    public int getTriviaNumber() {
        return triviaNumber;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }
}
