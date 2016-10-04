package com.olegshan.service;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class RabotaUaService implements JobService {

    @Autowired
    private Parser parser;

    private static final String SITE = "Rabota.ua";
    private static final String SITE_TO_PARSE = "http://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String[] JOB_BOX = {"class", "v"};
    private static final String[] TITLE_BOX = {"class", "t"};
    private static final String URL_PREFIX = "http://rabota.ua";
    private static final String[] COMPANY_DATA = {"class", "rua-p-c-default"};
    private static final String[] DESCRIPTION_DATA = {"class", "d"};
    private static final String[] DATE_DATA = {"", ""};

    public void parse() {
        parser.parse(this, SITE, SITE_TO_PARSE, JOB_BOX, TITLE_BOX, URL_PREFIX,
                COMPANY_DATA, DESCRIPTION_DATA, DATE_DATA);
    }
}
