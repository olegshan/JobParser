package com.olegshan.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by olegshan on 24.09.2016.
 */
public class Job {

    private String title;
    private String description;
    private String company;
    private String source;
    private String url;
    private LocalDate date;
    private String dateToDisplay;

    public Job(String title, String description, String company, String source, String url, LocalDate date) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateToDisplay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM");
        return date.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (title != null ? !title.equals(job.title) : job.title != null) return false;
        if (description != null ? !description.equals(job.description) : job.description != null) return false;
        if (company != null ? !company.equals(job.company) : job.company != null) return false;
        if (source != null ? !source.equals(job.source) : job.source != null) return false;
        if (url != null ? !url.equals(job.url) : job.url != null) return false;
        return date != null ? date.equals(job.date) : job.date == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
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
