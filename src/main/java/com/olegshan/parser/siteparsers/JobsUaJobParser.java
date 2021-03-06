package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.sites.JobSite.Holder;
import com.olegshan.util.TimeUtil;
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
        removeAd(jobBlocks);

        return jobBlocks;
    }

    private void removeAd(Elements jobBlocks) {

        // ad block on jobs.ua has the same tags as the job blocks, so it should be removed
        for (int i = 0; i < jobBlocks.size(); i++) {

            String jobBlock = getElements(
                jobBlocks.get(i),
                Holder.of("class", "b-city__title b-city__companies-title"),
                true
            )
                .text();

            if (jobBlock.contains("VIP компании в Украине:"))
                jobBlocks.remove(i);
        }
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
        dateLine = dateLine.substring(dateLine.indexOf(NBSP) + 1, dateLine.lastIndexOf(NBSP)).trim();
        String[] dateParts = dateLine.split(jobSite.split());
        TimeUtil.removeZero(dateParts);

        int day = parseInt(dateParts[0]);
        int month = TimeUtil.MONTHS.get(dateParts[1].toLowerCase());
        int year = dateParts.length > 2 ? Integer.parseInt(dateParts[2]) : getYear(month);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        String company = removeNbsp(getElements(job, jobSite.company()).first().text());
        check(company, "company", url);
        return company;
    }
}
