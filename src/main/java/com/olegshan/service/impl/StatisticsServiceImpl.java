package com.olegshan.service.impl;

import com.olegshan.entity.Job;
import com.olegshan.service.StatisticsService;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private final AtomicInteger newJobs     = new AtomicInteger();
	private final AtomicInteger updatedJobs = new AtomicInteger();

	private static final Gauge newJobsFoundPerRun = Gauge.build()
			.name("new_jobs_per_run")
			.help("New jobs per run.")
			.labelNames("site_name")
			.register();

	private static final Gauge updatedJobsFoundPerRun = Gauge.build()
			.name("updated_jobs_per_run")
			.help("Updated jobs per run.")
			.labelNames("site_name")
			.register();

	private static final Counter totalJobsCount = Counter.build()
			.name("total_jobs_count")
			.help("Total jobs count.")
			.labelNames("site_name")
			.register();

	@Override
	public void updateStatistics(Job job, boolean isNew) {
		if (isNew) {
			newJobs.incrementAndGet();
			totalJobsCount
					.labels(job.getSource())
					.inc();
		} else {
			updatedJobs.incrementAndGet();
		}
	}

	@Override
	public void saveStatistics(String siteName) {

		newJobsFoundPerRun
				.labels(siteName)
				.set(newJobs.get());

		updatedJobsFoundPerRun
				.labels(siteName)
				.set(updatedJobs.get());

		newJobs.set(0);
		updatedJobs.set(0);
	}
}