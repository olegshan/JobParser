package com.olegshan.sites;

import com.olegshan.parser.job.HeadHunterUaJobParser;
import com.olegshan.parser.job.JobParser;
import org.springframework.stereotype.Component;

@Component
public class HeadHunterUa implements JobSite {

    private static final String SITE_NAME = "HeadHunter.ua";
    private static final String SITE_URL = "https://hh.ua/search/vacancy?text=java&area=115";
    private static final String URL_PREFIX = "";
    private static final String[] JOB_BOX = {"class", "search-result-description"};
    private static final String[] TITLE_BOX = {"data-qa", "vacancy-serp__vacancy-title"};
    private static final String[] COMPANY_DATA = {"data-qa", "vacancy-serp__vacancy-employer"};
    private static final String[] DESCRIPTION_DATA = {"data-qa", "vacancy-serp__vacancy_snippet_requirement"};
    private static final String[] DATE_DATA = {"data-qa", "vacancy-serp__vacancy-date"};
    private static final String SPLIT = "\u00a0";

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
        return new HeadHunterUaJobParser(this);
    }
}
