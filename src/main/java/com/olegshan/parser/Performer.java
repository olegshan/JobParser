package com.olegshan.parser;

import com.olegshan.sites.JobSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Performer {

	private List<JobSite> sites;
	private Parser        parser;

	@Autowired
	public Performer(List<JobSite> sites, Parser parser) {
		this.sites = sites;
		this.parser = parser;
	}

	@Scheduled(cron = "0 1 7-23 * * *", zone = "Europe/Athens")
	public void perform() {
		log.error("\n\n!!!!!!!!!!!!!!!!!!!!!!!!!! count of sites: {}\n\n", sites.size());
		for (JobSite jobSite : sites) {
			log.error("\n\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			log.error("\nWill parse {}\n\n", jobSite.name());

			parser.parse(jobSite);
		}
	}

	private static final Logger log = LoggerFactory.getLogger(Performer.class);
}
