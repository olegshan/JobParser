package com.olegshan.controllers;

import com.olegshan.entity.Job;
import com.olegshan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    DouService douService;
    @Autowired
    RabotaUaService rabotaUaService;
    @Autowired
    JobsUaService jobsUaService;
    @Autowired
    WorkUaService workUaService;
    @Autowired
    HeadHunterService headHunterService;

    @RequestMapping("/")
    public String index(Model model) {
        List<Job> allJobs = new ArrayList<>();

        allJobs.addAll(douService.getJobs());
        allJobs.addAll(rabotaUaService.getJobs());
        allJobs.addAll(workUaService.getJobs());
        allJobs.addAll(jobsUaService.getJobs());
        allJobs.addAll(headHunterService.getJobs());

        allJobs.sort(Comparator.comparing(Job::getDate).reversed());
        model.addAttribute("jobs", allJobs);

        return "index";
    }
}