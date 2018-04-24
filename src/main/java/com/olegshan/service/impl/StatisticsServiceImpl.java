package com.olegshan.service.impl;

import com.olegshan.entity.Job;
import com.olegshan.entity.Statistics;
import com.olegshan.repository.StatisticsRepository;
import com.olegshan.service.StatisticsService;
import io.prometheus.client.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private StatisticsRepository statisticsRepository;

	private final Counter newJobsCounter = Counter.build()
			.name("new_jobs_counter")
			.help("New jobs counter.")
			.labelNames("site_name")
			.register();

	private final Counter updatedJobsCounter = Counter.build()
			.name("updated_jobs_counter")
			.help("Updated jobs counter.")
			.labelNames("site_name")
			.register();

	@Autowired
	public StatisticsServiceImpl(StatisticsRepository statisticsRepository) {
		this.statisticsRepository = statisticsRepository;
	}

	@Override
	public void updateStatistics(Statistics statistics, Job job, boolean isNew) {
		if (isNew) {
			log.error("\n\n**** SERVICE: incrementNewJobsCount by job {}\n\n", job.getTitle());
			statistics.incrementNewJobsCount();
			newJobsCounter
					.labels(job.getSource())
					.inc();
		} else {
			log.error("\n\n**** SERVICE: incrementUpdatedJobsCount {}\n\n", job.getTitle());
			statistics.incrementUpdatedJobsCount();
			updatedJobsCounter
					.labels(job.getSource())
					.inc();
		}
	}

	@Override
	public void saveStatistics(Statistics statistics, String siteName) {
		statistics.setRun(LocalDateTime.now().truncatedTo(SECONDS));
		statistics.setId(siteName);
		statistics.setSiteName(siteName);
		if (!statisticsRepository.exists(statistics.getId())) {
			log.error("\n\n^^^^^^^^^^^^^^^^^ SERVICE: saveStatistics of {} with {} new jobs and {} updated jobs\n\n",
					siteName, statistics.getNewJobsFoundByRun(), statistics.getUpdatedJobsByRun());
			statisticsRepository.save(statistics);
		} else
			log.error("\n\n######################## SERVICE: statistics with id {} exists\n\n", statistics.getId());
	}

	private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);
}
