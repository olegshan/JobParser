package com.olegshan.parser.job;

import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.lang.Integer.parseInt;

/**
 * @author Taras Zubrei
 */
public class HeadHunterUaJobParser extends JobParser {
    public HeadHunterUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);
        return LocalDate.of(LocalDate.now(ZoneId.of("Europe/Athens")).getYear(),
                MonthsTools.MONTHS.get(dateParts[1].toLowerCase()),
                parseInt(dateParts[0])).atTime(getTime());
    }
}
