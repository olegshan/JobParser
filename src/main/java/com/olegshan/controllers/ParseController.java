package com.olegshan.controllers;

import com.olegshan.entity.Job;
import com.olegshan.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by olegshan on 24.09.2016.
 */
@Controller
public class ParseController {

    @RequestMapping("/")
    public String index() {
        findJobs();
        return "index";
    }

    @ModelAttribute("jobs")
    public List<Job> findJobs() {
        List<Job> allJobs = new ArrayList<>();

        allJobs.addAll(new RabotaUaService().getJobs());
        allJobs.addAll(new WorkUaService().getJobs());
        allJobs.addAll(new DouService().getJobs());
        allJobs.addAll(new JobsUaService().getJobs());
        allJobs.addAll(new HeadHunterService().getJobs());

        allJobs.sort(Comparator.comparing(Job::getDate).reversed());

        return allJobs;
    }
}