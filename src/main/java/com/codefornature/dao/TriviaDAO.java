package com.codefornature.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriviaDAO {
    private List<Trivia> list_trivia;
    private int user_point;
    public static List<Trivia> readFile(String text_file) {
        List<Trivia> list_trivia = new ArrayList<>();

        try {
            BufferedReader inputStream = new BufferedReader(new FileReader("./src/main/resources/assets/text/" + text_file));
            String input;

            while ((input = inputStream.readLine()) != null) {
                /*String[] parts = input.split(",");
                String question = parts[0];
                String[] choices = parts[1].split(",");
                String correct_answer = parts[2];
                */
                String question = input;
                String list_choices1 = inputStream.readLine();
                String[] choices = list_choices1.split(",");
                String correct_answer = inputStream.readLine();
                inputStream.readLine();

                int choices_length = choices.length;
                char option = 'A';
                List<ChoiceDAO> list_choices = new ArrayList<>();
                ChoiceDAO correctChoice = null;

                for(int i = 0; i < choices_length; i++) {
                    if (correct_answer.equals(choices[i])) {
                        ChoiceDAO t = new ChoiceDAO(option, correct_answer, true);
                        list_choices.add(t);
                        correctChoice = t;

                    } else {
                        ChoiceDAO f = new ChoiceDAO(option, choices[i], false);
                        list_choices.add(f);
                    }
                    option += 1;
                }

                Trivia trivia_question = new Trivia(question, choices, 0, false, correct_answer, correctChoice);
                list_trivia.add(trivia_question);
            }

        } catch (IOException e) {
            System.out.println("File was not found!");
        }

        return list_trivia;
    }
}
