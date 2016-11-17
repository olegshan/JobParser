package com.olegshan.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Created by olegshan on 24.09.2016.
 */
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

    public Job(String url, String title, String description, String company, String source, LocalDateTime date) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.company = company;
        this.source = source;
        this.date = date;
    }

    public String getDateToDisplay() {
        return date.format(ofPattern("d MMMM"));
    }
}
