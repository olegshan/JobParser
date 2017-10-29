package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.parser.Parser;
import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static java.lang.Integer.parseInt;

public class JobParser {

	JobSite jobSite;

	public JobParser(JobSite jobSite) {
		this.jobSite = jobSite;
	}

	public Document getDoc(String siteUrl) throws ParserException {
		try {
			return Jsoup.connect(siteUrl).userAgent("Mozilla").timeout(0).get();
		} catch (IOException e) {
			log.error("Connecting to {} failed", siteUrl);
			throw new ParserException("Failed connecting to " + siteUrl + "\n" + e.getMessage());
		}
	}

	public String getUrl(Elements titleBlock) {
		return jobSite.urlPrefix() + titleBlock.attr("href");
	}

	public Elements getJobBlocks(Document doc) throws ParserException {
		Elements jobBlocks = getElements(doc, jobSite.jobBox());
		check(jobBlocks, "job blocks");
		return jobBlocks;
	}

	public Elements getTitleBlock(Element job) throws ParserException {
		Elements titleBlock = getElements(job, jobSite.titleBox());
		check(titleBlock, "title blocks");
		return titleBlock;
	}

	public String getTitle(Elements titleBlock) {
		return titleBlock.text();
	}

	public String getDescription(Element job, String url) throws ParserException {
		return getElements(job, jobSite.description()).text();
	}

	public String getCompany(Element job, String url) throws ParserException {
		String company = getElements(job, jobSite.company()).text();
		check(company, "company", url);
		return company;
	}

	public LocalDateTime getDate(Element job, String url) throws ParserException {
		String dateLine = getElements(job, jobSite.date()).text();
		check(dateLine, "date", url);
		return getDateByLine(dateLine);
	}

	protected LocalDateTime getDateByLine(String dateLine) {
		String[] dateParts = dateLine.split(jobSite.split());
		MonthsTools.removeZero(dateParts);
		return LocalDate.of(parseInt(dateParts[2]), parseInt(dateParts[1]), parseInt(dateParts[0])).atTime(getTime());
	}

	protected LocalTime getTime() {
		return LocalTime.now(ZoneId.of("Europe/Athens"));
	}

	//in case we parse in January jobs of last December. Needed for jobs.ua and hh.ua
	int getYear(int month) {
		int year;
		if (month > LocalDate.now(ZoneId.of("Europe/Athens")).getMonthValue()) {
			year = LocalDate.now().getYear() - 1;
		} else {
			year = LocalDate.now(ZoneId.of("Europe/Athens")).getYear();
		}
		return year;
	}

	Elements getElements(Element element, JobSite.Holder holder) {
		return getElements(element, holder, false);
	}

	Elements getElements(Element element, JobSite.Holder holder, boolean starting) {
		if (starting)
			return element.getElementsByAttributeValueStarting(holder.key, holder.value);
		return element.getElementsByAttributeValue(holder.key, holder.value);
	}

	void check(Object o, String data) throws ParserException {
		check(o, data, null);
	}

	void check(Object o, String data, String url) throws ParserException {
		String jobUrl = url == null ? "" : url;
		if (o == null || o.toString().trim().length() == 0) {
			log.error("Error getting {} from {}, {}", data, jobSite.name(), jobUrl);
			throw new ParserException("Error getting " + data + " from " + jobSite.name() + "\n" + jobUrl);
		}
	}

	private static final Logger log = LoggerFactory.getLogger(Parser.class);
}
