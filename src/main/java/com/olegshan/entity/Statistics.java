package com.olegshan.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;

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
		id = siteName + "_" + run.truncatedTo(HOURS);
	}

	public void incrementNewJobsCount() {
		newJobsFoundByRun++;
	}

	public void incrementUpdatedJobsCount() {
		updatedJobsByRun++;
	}
}
