package com.olegshan.service;

import com.olegshan.entity.Job;

public interface StatisticsService {

	void saveStatistics(String siteName);

	void updateStatistics(Job job, boolean isNew);
}
