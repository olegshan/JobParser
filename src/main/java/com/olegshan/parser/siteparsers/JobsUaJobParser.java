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

public class JobsUaJobParser extends JobParser {

    public JobsUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.substring(0, 10).split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);
        return LocalDate.of(parseInt(dateParts[2]), parseInt(dateParts[1]), parseInt(dateParts[0])).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        String[] companyData = jobSite.getCompanyData();
        Document jobDoc = getDoc(url);
        Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);

        String company = companyBlock.get(0).getElementsByTag("a").first().text();
        check(company, "company", url);
        return company;
    }
}
