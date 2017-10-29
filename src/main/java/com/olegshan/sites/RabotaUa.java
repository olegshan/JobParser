package com.olegshan.sites;

import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.parser.siteparsers.RabotaUaJobParser;
import org.springframework.stereotype.Component;

@Component
public class RabotaUa implements JobSite {

	private static final String SITE_NAME        = "Rabota.ua";
	private static final String SITE_URL         = "https://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2";
	private static final String URL_PREFIX       = "http://rabota.ua";
	private static final String SPLIT            = "";
	private static final Holder JOB_BOX          = Holder.of("class", "f-vacancylist-vacancyblock");
	private static final Holder TITLE_BOX        = Holder.of("class", "fd-beefy-gunso");
	private static final Holder COMPANY_DATA     = Holder.of("class", "f-vacancylist-companyname");
	private static final Holder DESCRIPTION_DATA = Holder.of("class", "f-vacancylist-shortdescr");
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
		return new RabotaUaJobParser(this);
	}
}
