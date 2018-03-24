package com.olegshan.repository;

import com.olegshan.statistics.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, String> {
}
