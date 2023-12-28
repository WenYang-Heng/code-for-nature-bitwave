package com.codefornature.dao;

import com.codefornature.model.NewsModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            NewsModel news = new NewsModel(title, newsUrl, date);
            newsList.add(news);
        }
        return newsList;
    }
}
