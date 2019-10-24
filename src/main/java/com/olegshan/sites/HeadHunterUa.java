package com.olegshan.sites;

import com.olegshan.parser.siteparsers.HeadHunterUaJobParser;
import com.olegshan.parser.siteparsers.JobParser;
import org.springframework.stereotype.Component;

import static com.olegshan.parser.siteparsers.JobParser.NBSP;

@Component
public class HeadHunterUa extends JobSite {

    @Override
    public String name() {
        return "HeadHunter.ua";
    }

    @Override
    public String url() {
        return "https://hh.ua/search/vacancy?text=java&area=115";
    }

    @Override
    public String split() {
        return NBSP;
    }

    @Override
    public Holder jobBox() {
        return Holder.of("data-qa", "vacancy-serp__vacancy");
    }

    @Override
    public Holder titleBox() {
        return Holder.of("data-qa", "vacancy-serp__vacancy-title");
    }

    @Override
    public Holder company() {
        return Holder.of("data-qa", "vacancy-serp__vacancy-employer");
    }

    @Override
    public Holder description() {
        return Holder.of("data-qa", "vacancy-serp__vacancy_snippet_requirement");
    }

    @Override
    public Holder date() {
        return Holder.of("class", "vacancy-serp-item__publication-date");
    }

    @Override
    public JobParser getParser() {
        return new HeadHunterUaJobParser(this);
    }
}
