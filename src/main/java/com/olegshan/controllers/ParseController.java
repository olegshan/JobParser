package com.olegshan.controllers;

import com.olegshan.entity.Job;
import com.olegshan.service.JobService;
import com.olegshan.tools.PageBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by olegshan on 24.09.2016.
 */
@Controller
public class ParseController {

    private static final int PAGE_SIZE = 40;
    private JobService jobService;

    @Autowired
    public ParseController(JobService jobService) {
        this.jobService = jobService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView showJobs(@RequestParam(value = "page", required = false) Integer page) {

        ModelAndView modelAndView = new ModelAndView("index");
        int currentPageNumber = (page == null || page < 1) ? 0 : page - 1;

        Page<Job> jobs = jobService.getJobs(new PageRequest(currentPageNumber, PAGE_SIZE, Sort.Direction.DESC, "date"));
        PageBox pageBox = new PageBox(jobs.getTotalPages(), jobs.getNumber());

        modelAndView.addObject("jobs", jobs);
        modelAndView.addObject("pageBox", pageBox.getPageBox());

        return modelAndView;
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }
}