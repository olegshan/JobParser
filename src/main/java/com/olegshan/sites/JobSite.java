package com.olegshan.sites;

/**
 * Created by olegshan on 28.09.2016.
 */
public interface JobSite {

    void parse();

    String getSiteName();

    String getSiteUrl();

    String getUrlPrefix();

    String[] getJobBox();

    String[] getTitleBox();

    String[] getCompanyData();

    String[] getDescriptionData();

    String[] getDateData();

    String getSplit();
}
