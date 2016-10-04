package com.olegshan.service;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 28.09.2016.
 */
@Component
public class HeadHunterService implements JobService {

    @Autowired
    private Parser parser;

    private static final String SITE = "HeadHunter.ua";
    private static final String SITE_TO_PARSE = "https://hh.ua/search/vacancy?text=java&area=115";
    private static final String[] JOB_BOX = {"class", "search-result-description"};
    private static final String[] TITLE_BOX = {"data-qa", "vacancy-serp__vacancy-title"};
    private static final String URL_PREFIX = "";
    private static final String[] COMPANY_DATA = {"data-qa", "vacancy-serp__vacancy-employer"};
    private static final String[] DESCRIPTION_DATA = {"data-qa", "vacancy-serp__vacancy_snippet_requirement"};
    private static final String[] DATE_DATA = {"data-qa", "vacancy-serp__vacancy-date"};

    public void parse() {
        parser.parse(this, SITE, SITE_TO_PARSE, JOB_BOX, TITLE_BOX, URL_PREFIX,
                COMPANY_DATA, DESCRIPTION_DATA, DATE_DATA);
    }
}
