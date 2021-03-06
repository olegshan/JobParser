package com.olegshan.service;

import com.olegshan.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {

    void save(Job job);

    Page<Job> getJobs(Pageable request);
}
