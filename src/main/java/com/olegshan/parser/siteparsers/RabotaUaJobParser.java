package com.olegshan.parser.siteparsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.olegshan.util.TimeUtil.localTimeZone;

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

    @Override
    public LocalDateTime getDate(Element job, String url) throws Exception {
        Document dateDoc = getDoc(url);

        Elements scriptElements = dateDoc.getElementsByTag("script");

        String varScript = null;

        for (Element scriptElement : scriptElements) {
            if (scriptElement.data().contains("var ruavars"))
                varScript = scriptElement.data();
        }

        if (StringUtils.isEmpty(varScript)) {
            LocalDateTime ldt = LocalDateTime.now(localTimeZone());
            log.warn("There was no date for job {}, return current date {}", url, ldt);
            return ldt;
        }

        String json = varScript.substring(varScript.indexOf("{"), varScript.lastIndexOf("}") + 1);
        JsonNode jsonNode = new ObjectMapper().readTree(json);
        String vacancyDate = jsonNode.get("vacancy_VacancyDate").toString().replaceAll("\\\"", "");

        return LocalDateTime.parse(vacancyDate);
    }

    private static final Logger log = LoggerFactory.getLogger(RabotaUaJobParser.class);
}
