package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.sites.JobSite.Holder;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class JobsUaJobParser extends JobParser {

	public JobsUaJobParser(JobSite jobSite) {
		super(jobSite);
	}

	@Override
	public Elements getJobBlocks(Document doc) throws ParserException {
		Elements jobBlocks = getElements(doc, jobSite.jobBox());
		check(jobBlocks, "job blocks");

		// ad block on jobs.ua has the same tags as the job blocks, so it should be removed
		for (int i = 0; i < jobBlocks.size(); i++) {
			if (getElements(jobBlocks.get(i), Holder.of("class", "b-city__title b-city__companies-title"), true)
					.text()
					.contains("VIP компании в Украине:")
					) {
				jobBlocks.remove(i);
			}
		}
		return jobBlocks;
	}

	@Override
	public String getDescription(Element job, String url) throws ParserException {
		Document descDoc = getDoc(url);
		String description = getElements(descDoc, jobSite.description()).text();
		if (description.startsWith("Описание вакансии ")) {
			description = description.substring("Описание вакансии ".length());
		}
		return description.length() > 250 ? description.substring(0, 250) + ("...") : description;
	}

	@Override
	public LocalDateTime getDate(Element job, String url) throws ParserException {

		Document dateDoc = getDoc(url);
		String dateLine = getElements(dateDoc, jobSite.date()).text();

		check(dateLine, "date line", url);
		return getDateByLine(dateLine);
	}

	@Override
	protected LocalDateTime getDateByLine(String dateLine) {
		dateLine = dateLine.replaceAll("\u00a0", "").trim();
		String[] dateParts = dateLine.split(jobSite.split());
		MonthsTools.removeZero(dateParts);

		int day = parseInt(dateParts[0]);
		int month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
		int year = getYear(month);

		return LocalDate.of(year, month, day).atTime(getTime());
	}

	@Override
	public String getCompany(Element job, String url) throws ParserException {
		String company = getElements(job, jobSite.company()).first().text();
		check(company, "company", url);
		return company;
	}
}
