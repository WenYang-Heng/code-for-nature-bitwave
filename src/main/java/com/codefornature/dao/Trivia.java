package com.codefornature.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trivia {
    private final String question;
    private final List<String> list_choices;
    private int num_attempts;
    private boolean done;
    private final String correctAnswer;
//    private final ChoiceDAO correctChoice;

    public Trivia(String question, String[] list_choice, int num_attempt,
                  boolean done, String correctAnswer) {
        this.question = question;
        this.list_choices = new ArrayList<>(Arrays.asList(list_choice));
        this.num_attempts = 1;
        this.done = false;
        this.correctAnswer = correctAnswer;
//        this.correctChoice = correctChoice;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<String> getChoices() {
        return this.list_choices;
    }

    public int getAttempt() {
        return this.num_attempts;
    }

    public void countAttempt() {
        this.num_attempts += 1;
    }

    public boolean getDone() {
        return this.done;
    }

    public void isDone() {
        this.done = true;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

//    public ChoiceDAO getCorrectChoice() {
//        return this.correctChoice;
//    }

    @Override
    public String toString() {
        return "Trivia{" +
                "question='" + question + '\'' +
                ", list_choices=" + list_choices +
                ", num_attempts=" + num_attempts +
                ", done=" + done +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
