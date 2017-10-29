package com.olegshan.sites;

import com.olegshan.parser.siteparsers.DouUaJobParser;
import com.olegshan.parser.siteparsers.JobParser;
import org.springframework.stereotype.Component;

@Component
public class DouUa implements JobSite {

	private static final String SITE_NAME        = "Dou.ua";
	private static final String SITE_URL         = "https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java";
	private static final String URL_PREFIX       = "";
	private static final String SPLIT            = " ";
	private static final Holder JOB_BOX          = Holder.of("class", "vacancy");
	private static final Holder TITLE_BOX        = Holder.of("class", "vt");
	private static final Holder COMPANY_DATA     = Holder.of("class", "company");
	private static final Holder DESCRIPTION_DATA = Holder.of("class", "sh-info");
	private static final Holder DATE_DATA        = Holder.of("class", "date");


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
		return new DouUaJobParser(this);
	}
}
