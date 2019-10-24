package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.WorkUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class WorkUa extends JobSite {

    @Override
    public String name() {
        return "Work.ua";
    }

    @Override
    public String url() {
        return "https://www.work.ua/jobs-kyiv-java/";
    }

    @Override
    public String urlPrefix() {
        return "https://work.ua";
    }

    @Override
    public String split() {
        return " ";
    }

    @Override
    public Holder jobBox() {
        return Holder.of("class", "card card-hover card-visited wordwrap job-link");
    }

    @Override
    public Holder description() {
        return Holder.of("class", "overflow");
    }

    @Override
    public JobParser getParser() {
        return new WorkUaJobParser(this);
    }
}
