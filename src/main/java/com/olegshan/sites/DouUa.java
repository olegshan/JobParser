package com.olegshan.sites;

import com.olegshan.parser.siteparsers.DouUaJobParser;
import com.olegshan.parser.siteparsers.JobParser;
import org.springframework.stereotype.Component;

@Component
public class DouUa extends JobSite {

    @Override
    public String name() {
        return "Dou.ua";
    }

    @Override
    public String url() {
        return "https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java";
    }

    @Override
    public String split() {
        return " ";
    }

    @Override
    public Holder jobBox() {
        return Holder.of("class", "vacancy");
    }

    @Override
    public Holder titleBox() {
        return Holder.of("class", "vt");
    }

    @Override
    public Holder company() {
        return Holder.of("class", "company");
    }

    @Override
    public Holder description() {
        return Holder.of("class", "sh-info");
    }

    @Override
    public Holder date() {
        return Holder.of("class", "date");
    }

    @Override
    public JobParser getParser() {
        return new DouUaJobParser(this);
    }
}
