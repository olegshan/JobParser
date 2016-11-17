package com.olegshan.service;

import com.olegshan.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * Created by olegshan on 26.10.2016.
 */
public interface JobService {
    void save(Job job);

    Page<Job> getJobs(PageRequest request);
}
