package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.JobsUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class JobsUa implements JobSite {

    private static final String SITE_NAME = "Jobs.ua";
    private static final String SITE_URL = "https://jobs.ua/vacancy/kiev/rabota-java";
    private static final String URL_PREFIX = "";
    private static final String[] JOB_BOX = {"class", "b-vacancy__item js-item_list"};
    private static final String[] TITLE_BOX = {"class", "b-vacancy__top__title js-item_title"};
    private static final String[] COMPANY_DATA = {"class", "b-vacancy__tech__item"};
    private static final String[] DESCRIPTION_DATA = {"class", "b-vacancy-full__block b-text"};
    private static final String[] DATE_DATA = {"class", "b-vacancy__tech__item b-vacancy__tech__item-top"};
    private static final String SPLIT = " ";

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
