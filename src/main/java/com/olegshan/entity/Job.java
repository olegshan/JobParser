package com.olegshan.entity;

import java.util.Date;

/**
 * Created by olegshan on 24.09.2016.
 */
public class Job {

    private String title;
    private String description;
    private String company;
    private String source;
    private String url;
    private Date date;

    public Job(String title, String description, String company, String source, String url, Date date) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.source = source;
        this.url = url;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Description: " + description + "\n" +
                "Company: " + company + "\n" +
                "Source: " + source + "\n" +
                "Date: " + date + "\n" +
                "URL: " + url + "\n\n";
    }
}
