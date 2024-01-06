package com.codefornature.dao;

import com.codefornature.ConnectionManager;
import com.codefornature.model.MerchandiseModel;
import com.codefornature.model.NewsModel;
import com.codefornature.model.TriviaModel;
import com.codefornature.model.UserTriviaModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TriviaDAO {
    public static List<Trivia> readFile(String text_file) {
        List<Trivia> list_trivia = new ArrayList<>();

        try {
            BufferedReader inputStream = new BufferedReader(new FileReader("./src/main/resources/assets/text/" + text_file));
            String input;

            while ((input = inputStream.readLine()) != null) {
                String question = input;
                String list_choices1 = inputStream.readLine();
                String[] choices = list_choices1.split(",");
                String correct_answer = inputStream.readLine();
                inputStream.readLine();

                Trivia trivia_question = new Trivia(question, choices, 0, false, correct_answer);
                list_trivia.add(trivia_question);
            }
        } catch (IOException e) {
            System.out.println("File was not found!");
        }

        return list_trivia;
    }

    public int getNumberOfTriviasDistributed(int user_id) throws SQLException {
        String query = "select count(*) from user_trivia where user_id = ?";
        int record = -1;
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        record = rs.getInt(1);
                    }
                }
            }
        }
        return record;
    }

    public void insertTriviaDistributedRecord(int user_id, int startingNum, int endingNum) throws SQLException {
        String query = "INSERT INTO user_trivia(user_id, trivia_number) VALUES(?, ?)";
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                for(int i = startingNum; i <= endingNum; i++){
                    ps.setInt(1, user_id);
                    ps.setInt(2, i);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    public List<UserTriviaModel> getTriviasByUser(int user_id) throws SQLException {
        String query = "SELECT * from user_trivia where user_id = ?";
        List<UserTriviaModel> triviaList = new ArrayList<>();
        try(Connection con = ConnectionManager.getConnection()){
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setInt(1, user_id);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        int triviaNum = rs.getInt("trivia_number");
                        boolean isAnswered = rs.getBoolean("isAnswered");
                        UserTriviaModel trivia = new UserTriviaModel(triviaNum, isAnswered);
                        triviaList.add(trivia);
                    }
                }
            }
        }
        return triviaList;
    }
}
