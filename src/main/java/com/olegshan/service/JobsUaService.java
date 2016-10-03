package com.olegshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 27.09.2016.
 */
@Component
public class JobsUaService implements JobService {

    @Autowired
    private ParseHelper parseHelper;

    private String site = "Jobs.ua";
    private String siteToParse = "http://www.jobs.ua/vacancy/rabota-kiev-java/";
    private String[] jobBox = {"class", "div_vac_list"};
    private String[] titleBox = {"class", "jvac_view"};
    private String urlPrefix = "http://www.jobs.ua";
    private String[] companyData = {"class", "viewcontcenter"};
    private String[] descriptionData = {"style", "padding-top:12px;"};
    private String[] dateData = {"style", "padding-top:10px"};

    public void parse() {
        parseHelper.parse(this, site, siteToParse, jobBox, titleBox, urlPrefix, companyData, descriptionData, dateData);
    }
}
