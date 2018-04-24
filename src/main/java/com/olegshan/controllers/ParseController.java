package com.olegshan.controllers;

import com.olegshan.entity.Job;
import com.olegshan.entity.Statistics;
import com.olegshan.repository.StatisticsRepository;
import com.olegshan.service.JobService;
import com.olegshan.tools.PageBox;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

@Controller
public class ParseController {

	private static final int PAGE_SIZE = 40;
	private JobService           jobService;
	private StatisticsRepository statisticsRepository;

	@Autowired
	public ParseController(JobService jobService, StatisticsRepository statisticsRepository) {
		this.jobService = jobService;
		this.statisticsRepository = statisticsRepository;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView showJobs(@RequestParam(value = "page", required = false) Integer page) {

		ModelAndView modelAndView = new ModelAndView("index");
		int currentPageNumber = (page == null || page < 1) ? 0 : page - 1;

		Pageable request = new PageRequest(currentPageNumber, PAGE_SIZE, Sort.Direction.DESC, "date");
		Page<Job> jobs = jobService.getJobs(request);
		PageBox pageBox = new PageBox(jobs.getTotalPages(), jobs.getNumber());

		modelAndView.addObject("jobs", jobs);
		modelAndView.addObject("pageBox", pageBox.getPageBox());

		return modelAndView;
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public ModelAndView showStatistics() {

		ModelAndView modelAndView = new ModelAndView("statistics");
		List<Statistics> stats = statisticsRepository.findAll();
		Collections.reverse(stats);
		modelAndView.addObject("statistics", stats);
		return modelAndView;
	}

	@RequestMapping("/about")
	public String about() {
		return "about";
	}

	@RequestMapping(path = "/metrics")
	public void metrics(Writer responseWriter) throws IOException {
		TextFormat.write004(responseWriter, CollectorRegistry.defaultRegistry.metricFamilySamples());
		responseWriter.close();
	}
}