package com.olegshan.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by olegshan on 24.09.2016.
 */
@Entity
public class Job {

    @Id
    private String url;
    private String title;
    //  Max value for PostgreSQL
    @Column(length = 10485760)
    private String description;
    private String company;
    private String source;
    private LocalDateTime date;
    private String dateToDisplay;

    public Job() {
    }

    public Job(String title, String description, String company, String source, String url, LocalDateTime date) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDateToDisplay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM");
        return date.format(formatter);
    }
}
