package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.JobsUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class JobsUa implements JobSite {

	private static final String SITE_NAME        = "Jobs.ua";
	private static final String SITE_URL         = "https://jobs.ua/vacancy/kiev/rabota-java";
	private static final String URL_PREFIX       = "";
	private static final String SPLIT            = " ";
	private static final Holder JOB_BOX          = Holder.of("class", "b-vacancy__item js-item_list");
	private static final Holder TITLE_BOX        = Holder.of("class", "b-vacancy__top__title js-item_title");
	private static final Holder COMPANY_DATA     = Holder.of("class", "b-vacancy__tech__item");
	private static final Holder DESCRIPTION_DATA = Holder.of("class", "b-vacancy-full__block b-text");
	private static final Holder DATE_DATA        = Holder.of("class", "b-vacancy-full__tech__item");

	@Override
	public String name() {
		return SITE_NAME;
	}

	@Override
	public String url() {
		return SITE_URL;
	}

	@Override
	public String urlPrefix() {
		return URL_PREFIX;
	}

	@Override
	public String split() {
		return SPLIT;
	}

	@Override
	public Holder jobBox() {
		return JOB_BOX;
	}

	@Override
	public Holder titleBox() {
		return TITLE_BOX;
	}

	@Override
	public Holder company() {
		return COMPANY_DATA;
	}

	@Override
	public Holder description() {
		return DESCRIPTION_DATA;
	}

	@Override
	public Holder date() {
		return DATE_DATA;
	}

	@Override
	public JobParser getParser() {
		return new JobsUaJobParser(this);
	}
}
