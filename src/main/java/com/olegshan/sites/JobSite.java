package com.olegshan.sites;

import com.olegshan.parser.job.JobParser;

public interface JobSite {

    String getSiteName();

    String getSiteUrl();

    String getUrlPrefix();

    String[] getJobBox();

    String[] getTitleBox();

    String[] getCompanyData();

    String[] getDescriptionData();

    String[] getDateData();

    String getSplit();

    default JobParser getParser() {
        return new JobParser(this);
    }
}
