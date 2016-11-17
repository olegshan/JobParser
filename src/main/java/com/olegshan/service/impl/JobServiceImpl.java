package com.olegshan.service.impl;

import com.olegshan.entity.Job;
import com.olegshan.repository.JobRepository;
import com.olegshan.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);
    private JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void save(Job job) {
        if (jobExists(job)) {
            update(job);
        } else {
            jobRepository.save(job);
            LOGGER.info("New job '{}' on {} found", job.getTitle(), job.getSource());
        }
    }

    private boolean jobExists(Job job) {
        return jobRepository.findOne(job.getUrl()) != null;
    }

    private void update(Job job) {
        Job jobFromDb = jobRepository.findOne(job.getUrl());
        LocalDate jobFromDbDate = jobFromDb.getDate().toLocalDate();
        LocalDate jobDate = job.getDate().toLocalDate();
        if (!jobFromDbDate.equals(jobDate)) {
            jobRepository.save(job);
            LOGGER.info("Job '{}', {} updated", job.getTitle(), job.getUrl());
        }
    }

    public Page<Job> getJobs(PageRequest request) {
        return jobRepository.findAll(request);
    }
}
