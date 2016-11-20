package com.olegshan.service;

import com.olegshan.AbstractTest;
import com.olegshan.entity.Job;
import com.olegshan.repository.JobRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobServiceTest extends AbstractTest {

    private static final String JOB_URL = "http://somesite.ua/company/vacancy";
    private static final int CURRENT_PAGE = 1;
    private static final int PAGE_SIZE = 5;

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;

    @Before
    public void setUp() throws Exception {
        Job job;
        Random random = new Random(20);
        for (int i = 0; i < 10; i++) {
            //jobs are saved into database with random dates
            job = new Job("Title" + i, "Description" + i, "Company" + i, "Site" + i, JOB_URL + i,
                    LocalDateTime.now(ZoneId.of("Europe/Athens")).minusDays(random.nextInt()));
            jobService.save(job);
        }
    }

    @Test
    public void jobsInSetUpMethodWereSaved() throws Exception {
        assertEquals("There should be 10 elements in the database", jobRepository.findAll().size(), 10);
    }

    @Test
    public void savingOfNewJobWithTheSameUrlAndDifferentDateUpdatesExistingJob() throws Exception {
        Job job = jobRepository.findOne(JOB_URL + 5);
        assertEquals("Title5", job.getTitle());
        LocalDateTime newDate = job.getDate().minusDays(1);
        job.setDate(newDate);
        job.setTitle("New title");
        jobService.save(job);

        job = jobRepository.findOne(JOB_URL + 5);
        assertEquals("New title", job.getTitle());
        assertEquals(newDate, job.getDate());
        assertEquals("There should be still 10 elements in the database after updating",
                jobRepository.findAll().size(), 10);
    }

    @Test
    public void savingOfJobWithTheSameUrlAndSameDateDoesNotUpdateExistingJob() throws Exception {
        Job job = jobRepository.findOne(JOB_URL + 7);
        assertEquals("Title7", job.getTitle());
        job.setTitle("New title");
        jobService.save(job);

        job = jobRepository.findOne(JOB_URL + 7);
        assertEquals("Title7", job.getTitle());
        assertEquals("There should be still 10 elements in the database", jobRepository.findAll().size(), 10);
    }

    @Test
    public void jobsAreRetrievedFromDatabaseSortedByDateInDescendingOrder() throws Exception {
        Page<Job> jobs = jobService.getJobs(new PageRequest(CURRENT_PAGE, PAGE_SIZE, Sort.Direction.DESC, "date"));
        assertTrue("The jobs should be sorted from new to old", isSorted(jobs));
    }

    @After
    public void tearDown() throws Exception {
        jobRepository.deleteAll();
    }

    private boolean isSorted(Page<Job> page) {
        List<Job> list = page.getContent();
        return IntStream.range(0, PAGE_SIZE - 1).allMatch(i -> list.get(i).getDate()
                .compareTo(list.get(i + 1).getDate()) > 0);
    }
}