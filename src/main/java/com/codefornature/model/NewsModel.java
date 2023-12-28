package com.codefornature.model;

public class NewsModel {
    private String title;
    private String newsUrl;
    private String date;

    public NewsModel(String title, String newsUrl, String date) {
        this.title = title;
        this.newsUrl = newsUrl;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
