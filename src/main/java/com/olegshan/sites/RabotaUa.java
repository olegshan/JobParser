package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.RabotaUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class RabotaUa extends JobSite {

    @Override
    public String name() {
        return "Rabota.ua";
    }

    @Override
    public String url() {
        return "https://rabota.ua/jobsearch/vacancy_list?regionId=1&keyWords=java";
    }

    @Override
    public String urlPrefix() {
        return "https://rabota.ua";
    }

    @Override
    public Holder jobBox() {
        return Holder.of("class", "f-vacancylist-vacancyblock");
    }

    @Override
    public Holder titleBox() {
        return Holder.of("class", "fd-beefy-gunso");
    }

    @Override
    public Holder company() {
        return Holder.of("class", "f-vacancylist-companyname");
    }

    @Override
    public Holder description() {
        return Holder.of("class", "f-vacancylist-shortdescr");
    }

    @Override
    public JobParser getParser() {
        return new RabotaUaJobParser(this);
    }
}
