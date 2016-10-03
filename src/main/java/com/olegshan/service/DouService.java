package com.olegshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class DouService implements JobService {

    @Autowired
    private ParseHelper parseHelper;

    private String site = "Dou.ua";
    private String siteToParse = "https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java";
    private String[] jobBox = {"class", "vacancy"};
    private String[] titleBox = {"class", "vt"};
    private String urlPrefix = "";
    private String[] companyData = {"class", "company"};
    private String[] descriptionData = {"class", "sh-info"};
    private String[] dateData = {"class", "date"};

    public void parse() {
        parseHelper.parse(this, site, siteToParse, jobBox, titleBox, urlPrefix, companyData, descriptionData, dateData);
    }
}
