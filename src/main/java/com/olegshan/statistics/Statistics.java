package com.olegshan.statistics;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Statistics {

    @Id
    private String        id;
    private String        siteName;
    private LocalDateTime run;
    private int           newJobsFoundByRun;
    private int           updatedJobsByRun;

    public void setId(String siteName) {
        id = siteName + run.toString();
    }

    public void incrementNewJobsCount() {
        newJobsFoundByRun = newJobsFoundByRun + 1;
    }

    public void incrementUpdatedJobsCount() {
        updatedJobsByRun = updatedJobsByRun + 1;
    }
}
