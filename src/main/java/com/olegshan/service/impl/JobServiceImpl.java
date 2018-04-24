package com.olegshan.service.impl;

import com.olegshan.entity.Job;
import com.olegshan.entity.Statistics;
import com.olegshan.notifier.Notifier;
import com.olegshan.repository.JobRepository;
import com.olegshan.service.JobService;
import com.olegshan.service.StatisticsService;
import com.olegshan.social.JTwitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JobServiceImpl implements JobService {

	private JobRepository     jobRepository;
	private StatisticsService statisticsService;
	private JTwitter          twitter;
	private Notifier          notifier;

	private Statistics statistics;

	@Autowired
	public JobServiceImpl(
			JobRepository jobRepository,
			StatisticsService statisticsService,
			JTwitter twitter,
			Notifier notifier
	) {
		this.jobRepository = jobRepository;
		this.statisticsService = statisticsService;
		this.twitter = twitter;
		this.notifier = notifier;
		statistics = new Statistics();
	}

	public void save(Job job) {
		if (jobRepository.exists(job.getUrl())) {
			update(job);
		} else {
			saveAndTweet(job, true);
			log.info("New job '{}' on {} found", job.getTitle(), job.getSource());
		}
	}

	private void update(Job job) {
		Job jobFromDb = jobRepository.findOne(job.getUrl());
		LocalDate jobFromDbDate = jobFromDb.getDate().toLocalDate();
		LocalDate jobDate = job.getDate().toLocalDate();
		if (!jobFromDbDate.equals(jobDate)) {
			saveAndTweet(job, false);
			log.info("Job '{}', {}, was updated", job.getTitle(), job.getUrl());
		} else
			log.error("\n\n////////////// SERVICE: will not update job {}, {}\n\n", job.getTitle(), job.getSource());
	}

	private void saveAndTweet(Job job, boolean isNew) {
		saveJob(job);
		updateStatistics(job, isNew);
		twitter.tweet(job);
	}

	private void updateStatistics(Job job, boolean isNew) {
		statisticsService.updateStatistics(statistics, job, isNew);
	}

	@Override
	public void saveStatistics(String siteName) {
		statisticsService.saveStatistics(statistics, siteName);
		statistics = new Statistics();
	}

	public Page<Job> getJobs(Pageable request) {
		return jobRepository.findAll(request);
	}

	private void saveJob(Job job) {
		try {
			jobRepository.save(job);
		} catch (Exception e) {
			log.error("Error while saving job '{}', {} into database", job.getTitle(), job.getUrl());
			notifier.notifyAdmin("Error while saving following job into database: '" +
					job.getTitle() + "', " + job.getUrl() + "\n\n" + e.getMessage());
		}
	}

	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);
}
