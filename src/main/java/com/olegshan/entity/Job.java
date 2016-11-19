package com.olegshan.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Entity
@Data
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

    public String getDateToDisplay() {
        return date.format(ofPattern("dd MMMM"));
    }
}
