package com.codefornature.dao;

import com.codefornature.model.NewsModel;
import com.codefornature.model.TriviaModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TriviaDAO {
    public TriviaDAO(int user_id, Date register_date) {
    }

    public TriviaModel getTrvia() throws IOException {
        TriviaModel trivia = null;
        BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/assets/text/TriviaSample.txt"));
        String line;
        while((line = in.readLine()) != null){

        }
        return trivia;
    }
}
