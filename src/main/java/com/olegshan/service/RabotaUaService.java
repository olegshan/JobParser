package com.olegshan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 26.09.2016.
 */
@Component
public class RabotaUaService implements JobService {

    @Autowired
    private ParseHelper parseHelper;

    private String site = "Rabota.ua";
    private String siteToParse = "http://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2";
    private String[] jobBox = {"class", "v"};
    private String[] titleBox = {"class", "t"};
    private String urlPrefix = "http://rabota.ua";
    private String[] companyData = {"class", "rua-p-c-default"};
    private String[] descriptionData = {"class", "d"};
    private String[] dateData = {"", ""};

    public void parse() {
        parseHelper.parse(this, site, siteToParse, jobBox, titleBox, urlPrefix, companyData, descriptionData, dateData);
    }
}
