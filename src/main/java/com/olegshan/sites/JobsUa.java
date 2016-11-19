package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.JobsUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class JobsUa implements JobSite {

    private static final String SITE_NAME = "Jobs.ua";
    private static final String SITE_URL = "http://www.jobs.ua/vacancy/rabota-kiev-java/";
    private static final String URL_PREFIX = "http://www.jobs.ua";
    private static final String[] JOB_BOX = {"class", "div_vac_list"};
    private static final String[] TITLE_BOX = {"class", "jvac_view"};
    private static final String[] COMPANY_DATA = {"class", "viewcontcenter"};
    private static final String[] DESCRIPTION_DATA = {"style", "padding-top:12px;"};
    private static final String[] DATE_DATA = {"style", "padding-top:10px"};
    private static final String SPLIT = "\\.";

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

    @Override
    public JobParser getParser() {
        return new JobsUaJobParser(this);
    }
}
