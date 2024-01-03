package com.codefornature.model;

import java.time.LocalDate;

public class NewsModel {
    private String title;
    private String newsUrl;
    private LocalDate date;

    public NewsModel(String title, String newsUrl, LocalDate date) {
        this.title = title;
        this.newsUrl = newsUrl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "title='" + title + '\'' +
                ", newsUrl='" + newsUrl + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
