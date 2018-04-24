package com.olegshan.service;

import com.olegshan.entity.Job;
import com.olegshan.entity.Statistics;

public interface StatisticsService {

	void saveStatistics(Statistics statistics, String siteName);

	void updateStatistics(Statistics statistics, Job job, boolean isNew);
}
