package com.codefornature.dao;

import com.codefornature.model.NewsModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewsDAO {
    String rootPath;

    public List<NewsModel> getNews() throws IOException {
        List<NewsModel> newsList = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/assets/text/NewsSample.txt"));
        String line;
        while((line = in.readLine()) != null){
            String title = line;
            String newsUrl = in.readLine();
            String date = in.readLine();
            in.readLine();
            NewsModel news = new NewsModel(title, newsUrl, convertStringToDate(date));
            newsList.add(news);
        }
        return newsList;
    }

    public LocalDate convertStringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);
        return LocalDate.parse(date, formatter);
    }
}
