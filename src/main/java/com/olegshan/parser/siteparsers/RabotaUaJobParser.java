package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.sites.JobSite.Holder;
import com.olegshan.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static com.olegshan.util.TimeUtil.localTimeZone;
import static java.lang.Integer.parseInt;

public class RabotaUaJobParser extends JobParser {

	public RabotaUaJobParser(JobSite jobSite) {
		super(jobSite);
	}

	@Override
	public String getUrl(Elements titleBlock) {
		return jobSite.urlPrefix() + titleBlock
				.get(0)
				.getElementsByTag("a")
				.attr("href");
	}

	@Override
	public Elements getTitleBlock(Element job) throws ParserException {
		Elements titleBlock = getElements(job, jobSite.titleBox(), true);
		check(titleBlock, "title blocks");
		return titleBlock;
	}

	@Override
	public String getDescription(Element job, String url) {
		return getElements(job, jobSite.description(), true).text();
	}

	@Override
	public String getCompany(Element job, String url) {
		String company = removeNbsp(getElements(job, jobSite.company(), true).text());
		if (company.length() == 0)
			company = "Anonymous employer";
		return company;
	}

	/**
	 * There are several problems here.
	 * First: there are different types of date tags, used on rabota.ua on different pages
	 * Second: sometimes date format is dd.mm.yyyy, sometimes — yyyy-mm-dd and sometimes — dd mmm yyyy.
	 * Third: sometimes there is no date at all.
	 */
	@Override
	public LocalDateTime getDate(Element job, String url) throws ParserException {

		Document dateDoc = getDoc(url);
		String dateLine;

		Elements dateElements = getElements(dateDoc, Holder.of("id", "d-date"));

		if (!dateElements.isEmpty())
			dateLine = getElements(dateElements.get(0), Holder.of("class", "d-ph-value")).text();
		else {
			dateLine = getElements(dateDoc, Holder.of("itemprop", "datePosted")).text();
			if (dateLine == null || dateLine.trim().length() == 0) {
				try {
					dateLine = getElements(dateDoc, Holder.of("class", "f-date-holder"), true).first().text();
				} catch (Exception e) {
					//no date at all, sometimes it happens
					LocalDateTime ldt = LocalDateTime.now(localTimeZone());
					log.warn("There was no date for job {}, return current date {}", url, ldt);
					return ldt;
				}
			}
		}
		return getDateByLine(dateLine, url);
	}

	private LocalDateTime getDateByLine(String dateLine, String url) throws ParserException {
		String[] dateParts;
		int year, month, day;

		if (Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}", dateLine)) {

			dateParts = dateLine.split("\\.");
			TimeUtil.removeZero(dateParts);
			year = parseInt(dateParts[2]);
			month = parseInt(dateParts[1]);
			day = parseInt(dateParts[0]);

		} else if (Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dateLine)) {

			dateParts = dateLine.split("-");
			TimeUtil.removeZero(dateParts);
			year = parseInt(dateParts[0]);
			month = parseInt(dateParts[1]);
			day = parseInt(dateParts[2]);

		} else if (Pattern.matches("\\d{2} [а-я]{3} \\d{4}", dateLine)) {

			dateParts = dateLine.split(" ");
			TimeUtil.removeZero(dateParts);
			day = parseInt(dateParts[0]);
			month = TimeUtil.MONTHS.get(dateParts[1]);
			year = parseInt(dateParts[2]);

		} else
			throw new ParserException("Cannot parse date of following job: " + url + "\ndateLine is: " + dateLine);

		return LocalDate.of(year, month, day).atTime(getTime());
	}

	private static final Logger log = LoggerFactory.getLogger(RabotaUaJobParser.class);
}
