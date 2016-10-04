package com.olegshan.service;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class WorkUaService implements JobService {

    @Autowired
    private Parser parser;

    private static final String SITE = "Work.ua";
    private static final String SITE_TO_PARSE = "https://www.work.ua/jobs-kyiv-java/";
    private static final String[] JOB_BOX = {"class", "card card-hover card-visited job-link"};
    private static final String[] TITLE_BOX = {"", ""};
    private static final String URL_PREFIX = "https://work.ua";
    private static final String[] COMPANY_DATA = {"class", "dl-horizontal"};
    private static final String[] DESCRIPTION_DATA = {"class", "text-muted overflow"};
    private static final String[] DATE_DATA = {"", ""};

    public void parse() {
        parser.parse(this, SITE, SITE_TO_PARSE, JOB_BOX, TITLE_BOX, URL_PREFIX,
                COMPANY_DATA, DESCRIPTION_DATA, DATE_DATA);
    }
}
