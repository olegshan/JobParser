package com.olegshan.repository;

import com.olegshan.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, String> {
}
