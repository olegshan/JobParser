package com.olegshan.service;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class DouService implements JobService {

    @Autowired
    private Parser parser;

    private static final String SITE = "Dou.ua";
    private static final String SITE_TO_PARSE = "https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java";
    private static final String[] JOB_BOX = {"class", "vacancy"};
    private static final String[] TITLE_BOX = {"class", "vt"};
    private static final String URL_PREFIX = "";
    private static final String[] COMPANY_DATA = {"class", "company"};
    private static final String[] DESCRIPTION_DATA = {"class", "sh-info"};
    private static final String[] DATE_DATA = {"class", "date"};

    public void parse() {
        parser.parse(this, SITE, SITE_TO_PARSE, JOB_BOX, TITLE_BOX, URL_PREFIX,
                COMPANY_DATA, DESCRIPTION_DATA, DATE_DATA);
    }
}
