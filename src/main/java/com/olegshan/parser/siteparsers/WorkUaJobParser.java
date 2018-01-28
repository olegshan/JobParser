package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class WorkUaJobParser extends JobParser {

	public WorkUaJobParser(JobSite jobSite) {
		super(jobSite);
	}

	@Override
	public Elements getJobBlocks(Document doc) throws ParserException {
		Elements jobBlocks = getElements(doc, jobSite.jobBox(), true);
		check(jobBlocks, "job blocks");
		return jobBlocks;
	}

	@Override
	public Elements getTitleBlock(Element job) {
		return job.getElementsByTag("a");
	}

	@Override
	public LocalDateTime getDate(Element job, String url) throws ParserException {
		String title = getTitleBlock(job).attr("title");
		String[] dateParts = title.substring(title.indexOf("вакансия от ") + "вакансия от ".length()).split(jobSite.split());
		check(dateParts, "date parts", url);

		int year = parseInt(dateParts[2]);
		int month = MonthsTools.MONTHS.get(dateParts[1]);
		int day = parseInt(dateParts[0]);

		return LocalDate.of(year, month, day).atTime(getTime());
	}

	@Override
	public String getCompany(Element job, String url) throws ParserException {
		Document jobDoc = getDoc(url);
		Elements companyBlock = getElements(jobDoc, jobSite.company());
		check(companyBlock, "company block", url);
		return removeNbsp(companyBlock.get(0).getElementsByTag("a").text());
	}
}
