package com.olegshan.service;

import com.olegshan.AbstractTest;
import com.olegshan.entity.Job;
import com.olegshan.repository.JobRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JobServiceTest extends AbstractTest {

    private static final String JOB_ONE_URL = "http://somesite.ua/company1/vacancy1";

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;

    @Before
    public void setUp() throws Exception {
        Job job = new Job("Java Developer", "Experienced Java developer needed", "Luxoft", "Dou.ua",
                JOB_ONE_URL, LocalDateTime.now(ZoneId.of("Europe/Athens")));
        jobService.save(job);
    }

    @Test
    public void jobInSetUpMethodWasSavedProperly() throws Exception {
        Job job = jobRepository.findOne(JOB_ONE_URL);
        assertNotNull("The job with url " + JOB_ONE_URL + " should be present in database", job);
        assertEquals("Java Developer", job.getTitle());
        assertEquals("Experienced Java developer needed", job.getDescription());
        assertEquals("Luxoft", job.getCompany());
        assertEquals("Dou.ua", job.getSource());
        assertEquals(LocalDate.now(), job.getDate().toLocalDate());
        assertEquals("There should be only one element in the database", jobRepository.findAll().size(), 1);
    }

    @Test
    public void savingOfNewJobWithTheSameUrlAndDifferenDateUpdatesExistingJob() throws Exception {
        Job job = new Job("Java Programmer", "Experienced Java developer needed", "Epam", "Dou.ua",
                JOB_ONE_URL, LocalDateTime.now(ZoneId.of("Europe/Athens")).minusDays(3));
        jobService.save(job);
        job = jobRepository.findOne(JOB_ONE_URL);
        assertEquals("Java Programmer", job.getTitle());
        assertEquals("Experienced Java developer needed", job.getDescription());
        assertEquals("Epam", job.getCompany());
        assertEquals("Dou.ua", job.getSource());
        assertEquals("newJob's date should be 3 days earlier from now",
                LocalDate.now().minusDays(3), job.getDate().toLocalDate());
        assertEquals("There should be only one element in the database after updating",
                jobRepository.findAll().size(), 1);
    }

    @Test
    public void savingOfJobWithTheSameUrlAndSameDateDoesNotUpdateExistingJob() throws Exception {
        Job job = new Job("Java Guru", "Very experienced", "GlobalLogic", "Work.ua",
                JOB_ONE_URL, LocalDateTime.now(ZoneId.of("Europe/Athens")));
        jobService.save(job);
        job = jobRepository.findOne(JOB_ONE_URL);
        assertEquals("Java Developer", job.getTitle());
        assertEquals("Experienced Java developer needed", job.getDescription());
        assertEquals("Luxoft", job.getCompany());
        assertEquals("Dou.ua", job.getSource());
        assertEquals(LocalDate.now(), job.getDate().toLocalDate());
        assertEquals("There should be only one element in the database", jobRepository.findAll().size(), 1);
    }

    @After
    public void tearDown() throws Exception {
        jobRepository.deleteAll();
    }
}