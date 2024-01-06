package com.codefornature.dao;

public class ChoiceDAO {
    private final String answer;
    private final char option;
    private boolean condition;

    public ChoiceDAO(char option, String answer, boolean condition) {
        this.option = option;
        this.answer = answer;
        this.condition = condition;
    }

    public String getAnswer() {
        return this.answer;
    }

    public boolean isCorrect() {
        return this.condition;
    }

    public char getOption() {
        return this.option;
    }

    public void correctAnswer() {
        this.condition = true;
    }
}
