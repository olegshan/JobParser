package com.olegshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class WorkUaService implements JobService {

    @Autowired
    private ParseHelper parseHelper;

    private String site = "Work.ua";
    private String siteToParse = "https://www.work.ua/jobs-kyiv-java/";
    private String[] jobBox = {"class", "card card-hover card-visited job-link"};
    private String[] titleBox = {"", ""};
    private String urlPrefix = "https://work.ua";
    private String[] companyData = {"class", "dl-horizontal"};
    private String[] descriptionData = {"class", "text-muted overflow"};
    private String[] dateData = {"", ""};

    public void parse() {
        parseHelper.parse(this, site, siteToParse, jobBox, titleBox, urlPrefix, companyData, descriptionData, dateData);
    }
}
