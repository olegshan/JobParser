package com.olegshan.sites;

import com.olegshan.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class WorkUa implements JobSite {

    @Autowired
    private Parser parser;

    private static final String SITE_NAME = "Work.ua";
    private static final String SITE_URL = "https://www.work.ua/jobs-kyiv-java/";
    private static final String URL_PREFIX = "https://work.ua";
    private static final String[] JOB_BOX = {"class", "card card-hover card-visited job-link"};
    private static final String[] TITLE_BOX = {"", ""};
    private static final String[] COMPANY_DATA = {"class", "dl-horizontal"};
    private static final String[] DESCRIPTION_DATA = {"class", "text-muted overflow"};
    private static final String[] DATE_DATA = {"", ""};
    private static final String SPLIT = "\\.";

    public void parse() {
        parser.parse(this);
    }

    public String getSiteName() {
        return SITE_NAME;
    }

    public String getSiteUrl() {
        return SITE_URL;
    }

    public String getUrlPrefix() {
        return URL_PREFIX;
    }

    public String[] getJobBox() {
        return JOB_BOX;
    }

    public String[] getTitleBox() {
        return TITLE_BOX;
    }

    public String[] getCompanyData() {
        return COMPANY_DATA;
    }

    public String[] getDescriptionData() {
        return DESCRIPTION_DATA;
    }

    public String[] getDateData() {
        return DATE_DATA;
    }

    public String getSplit() {
        return SPLIT;
    }
}
