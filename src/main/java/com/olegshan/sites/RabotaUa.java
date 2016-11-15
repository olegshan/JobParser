package com.olegshan.sites;

import com.olegshan.parser.job.JobParser;
import com.olegshan.parser.job.RabotaUaJobParser;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class RabotaUa implements JobSite {
    private static final String SITE_NAME = "Rabota.ua";
    private static final String SITE_URL = "http://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2";
    private static final String URL_PREFIX = "http://rabota.ua";
    private static final String[] JOB_BOX = {"class", "v f h", "v h", "v"};
    private static final String[] TITLE_BOX = {"class", "t"};
    private static final String[] COMPANY_DATA = {"class", "rua-p-c-default"};
    private static final String[] DESCRIPTION_DATA = {"class", "d"};
    private static final String[] DATE_DATA = {"", ""};
    private static final String SPLIT = "";

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
        return new RabotaUaJobParser(this);
    }
}
