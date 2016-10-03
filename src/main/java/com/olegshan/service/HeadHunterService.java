package com.olegshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 28.09.2016.
 */
@Component
public class HeadHunterService implements JobService {

    @Autowired
    private ParseHelper parseHelper;

    private String site = "HeadHunter.ua";
    private String siteToParse = "https://hh.ua/search/vacancy?text=java&area=115";
    private String[] jobBox = {"class", "search-result-description"};
    private String[] titleBox = {"data-qa", "vacancy-serp__vacancy-title"};
    private String urlPrefix = "";
    private String[] companyData = {"data-qa", "vacancy-serp__vacancy-employer"};
    private String[] descriptionData = {"data-qa", "vacancy-serp__vacancy_snippet_requirement"};
    private String[] dateData = {"data-qa", "vacancy-serp__vacancy-date"};

    public void parse() {
        parseHelper.parse(this, site, siteToParse, jobBox, titleBox, urlPrefix, companyData, descriptionData, dateData);
    }
}
