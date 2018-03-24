package com.olegshan.service.impl;

import com.olegshan.entity.Job;
import com.olegshan.notifier.Notifier;
import com.olegshan.repository.JobRepository;
import com.olegshan.repository.StatisticsRepository;
import com.olegshan.service.JobService;
import com.olegshan.social.JTwitter;
import com.olegshan.statistics.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class JobServiceImpl implements JobService {

	private JobRepository        jobRepository;
	private StatisticsRepository statisticsRepository;
	private JTwitter             twitter;
	private Notifier             notifier;

	private Statistics statistics;

	@Autowired
	public JobServiceImpl(
			JobRepository jobRepository,
			StatisticsRepository statisticsRepository,
			JTwitter twitter,
			Notifier notifier
	) {
		this.jobRepository = jobRepository;
		this.statisticsRepository = statisticsRepository;
		this.twitter = twitter;
		this.notifier = notifier;
		statistics = new Statistics();
	}

	public void save(Job job) {
		if (jobRepository.exists(job.getUrl())) {
			log.error("\n\n------- SERVICE: Job exists, will try to update {}, {}\n\n", job.getTitle(), job.getSource());
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
			log.error("\n\n////////////// SERVICE: will not update job {}, {}", job.getTitle(), job.getSource());
	}

	private void saveAndTweet(Job job, boolean isNew) {
		saveJob(job);
		updateStatistics(job.getTitle(), isNew);
		twitter.tweet(job);
	}

	private void updateStatistics(String jobTitle, boolean isNew) {
		if (isNew) {
			log.error("\n\n**** SERVICE: incrementNewJobsCount by job {}\n\n", jobTitle);
			statistics.incrementNewJobsCount();
		} else {
			log.error("\n\n**** SERVICE: incrementUpdatedJobsCount {}\n\n", jobTitle);
			statistics.incrementUpdatedJobsCount();
		}
	}

	@Override
	public void saveStatistics(String siteName) {
		statistics.setRun(LocalDateTime.now().truncatedTo(MINUTES));
		statistics.setId(siteName);
		statistics.setSiteName(siteName);
		if (!statisticsRepository.exists(statistics.getId())) {
			log.error("\n\n^^^^^^^^^^^^^^^^^ SERVICE: saveStatistics of {} with {} new jobs and {} updated jobs\n\n",
					siteName, statistics.getNewJobsFoundByRun(), statistics.getUpdatedJobsByRun());
			statisticsRepository.save(statistics);
		}
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
