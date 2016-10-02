package com.olegshan.repository;

import com.olegshan.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by olegshan on 02.10.2016.
 */
public interface JobRepository extends JpaRepository<Job, String> {
}
