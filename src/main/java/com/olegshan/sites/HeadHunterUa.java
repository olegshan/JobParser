package com.olegshan.sites;

import com.olegshan.parser.siteparsers.HeadHunterUaJobParser;
import com.olegshan.parser.siteparsers.JobParser;
import org.springframework.stereotype.Component;

import static com.olegshan.parser.siteparsers.JobParser.NBSP;

@Component
public class HeadHunterUa implements JobSite {

	private static final String SITE_NAME        = "HeadHunter.ua";
	private static final String SITE_URL         = "https://hh.ua/search/vacancy?text=java&area=115";
	private static final String URL_PREFIX       = "";
	private static final String SPLIT            = NBSP;
	private static final Holder JOB_BOX          = Holder.of("data-qa", "vacancy-serp__vacancy vacancy-serp__vacancy_premium");
	private static final Holder TITLE_BOX        = Holder.of("data-qa", "vacancy-serp__vacancy-title");
	private static final Holder COMPANY_DATA     = Holder.of("data-qa", "vacancy-serp__vacancy-employer");
	private static final Holder DESCRIPTION_DATA = Holder.of("data-qa", "vacancy-serp__vacancy_snippet_requirement");
	private static final Holder DATE_DATA        = Holder.of("class", "vacancy-serp-item__publication-date");


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
		return new HeadHunterUaJobParser(this);
	}
}
