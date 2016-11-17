package com.olegshan.sites;

import com.olegshan.parser.job.DouUaJobParser;
import com.olegshan.parser.job.JobParser;
import org.springframework.stereotype.Component;


@Component
public class DouUa implements JobSite {

    private static final String SITE_NAME = "Dou.ua";
    private static final String SITE_URL = "https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java";
    private static final String URL_PREFIX = "";
    private static final String[] JOB_BOX = {"class", "vacancy"};
    private static final String[] TITLE_BOX = {"class", "vt"};
    private static final String[] COMPANY_DATA = {"class", "company"};
    private static final String[] DESCRIPTION_DATA = {"class", "sh-info"};
    private static final String[] DATE_DATA = {"class", "date"};
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
        return new DouUaJobParser(this);
    }
}
