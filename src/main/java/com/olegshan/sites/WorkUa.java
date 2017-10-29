package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.WorkUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class WorkUa implements JobSite {

	private static final String SITE_NAME        = "Work.ua";
	private static final String SITE_URL         = "https://www.work.ua/jobs-kyiv-java/";
	private static final String URL_PREFIX       = "https://work.ua";
	private static final String SPLIT            = "\\.";
	private static final Holder JOB_BOX          = Holder.of("class", "card card-hover card-visited job-link");
	private static final Holder TITLE_BOX        = Holder.of("", "");
	private static final Holder COMPANY_DATA     = Holder.of("class", "dl-horizontal");
	private static final Holder DESCRIPTION_DATA = Holder.of("class", "text-muted overflow");
	private static final Holder DATE_DATA        = Holder.of("", "");

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
		return new WorkUaJobParser(this);
	}
}
