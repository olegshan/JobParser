package com.olegshan.service;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 27.09.2016.
 */
@Component
public class JobsUaService implements JobService {

    @Autowired
    private Parser parser;

    private static final String SITE = "Jobs.ua";
    private static final String SITE_TO_PARSE = "http://www.jobs.ua/vacancy/rabota-kiev-java/";
    private static final String URL_PREFIX = "http://www.jobs.ua";
    private static final String[] JOB_LIST = {"class", "post_view"};
    private static final String[] JOB_BOX = {"class", "div_vac_list"};
    private static final String[] TITLE_BOX = {"class", "jvac_view"};
    private static final String[] COMPANY_DATA = {"class", "viewcontcenter"};
    private static final String[] DESCRIPTION_DATA = {"style", "padding-top:12px;"};
    private static final String[] DATE_DATA = {"style", "padding-top:10px"};

    public void parse() {
        parser.parse(this, SITE, SITE_TO_PARSE, URL_PREFIX, JOB_LIST, JOB_BOX, TITLE_BOX,
                COMPANY_DATA, DESCRIPTION_DATA, DATE_DATA);
    }
}
