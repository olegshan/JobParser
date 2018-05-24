package com.olegshan.parser;

import com.olegshan.sites.JobSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.olegshan.util.TimeUtil.LOCAL_TIME_ZONE;

@Component
public class Performer {

	private List<JobSite> sites;
	private Parser        parser;
	private boolean       isParsingRunning;

	@Autowired
	public Performer(List<JobSite> sites, Parser parser) {
		this.sites = sites;
		this.parser = parser;
	}

	@Scheduled(cron = "0 1 7-23 * * *", zone = LOCAL_TIME_ZONE)
	public void perform() {
		if (isParsingRunning)
			return;
		isParsingRunning = true;
		for (JobSite jobSite : sites) {
			parser.parse(jobSite);
		}
		isParsingRunning = false;
	}
}
