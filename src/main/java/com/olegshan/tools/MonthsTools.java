package com.olegshan.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegshan on 27.09.2016.
 */
public class MonthsTools {

    public static final Map<String, Integer> MONTHS = new HashMap<>();

    static {
        MONTHS.put("січня", 1);
        MONTHS.put("лютого", 2);
        MONTHS.put("березня", 3);
        MONTHS.put("квітня", 4);
        MONTHS.put("травня", 5);
        MONTHS.put("червня", 6);
        MONTHS.put("липня", 7);
        MONTHS.put("серпня", 8);
        MONTHS.put("вересня", 9);
        MONTHS.put("жовтня", 10);
        MONTHS.put("листопада", 11);
        MONTHS.put("грудня", 12);

        MONTHS.put("января", 1);
        MONTHS.put("февраля", 2);
        MONTHS.put("марта", 3);
        MONTHS.put("апреля", 4);
        MONTHS.put("мая", 5);
        MONTHS.put("июня", 6);
        MONTHS.put("июля", 7);
        MONTHS.put("августа", 8);
        MONTHS.put("сентября", 9);
        MONTHS.put("октября", 10);
        MONTHS.put("ноября", 11);
        MONTHS.put("декабря", 12);

        MONTHS.put("january", 1);
        MONTHS.put("february", 2);
        MONTHS.put("march", 3);
        MONTHS.put("april", 4);
        MONTHS.put("may", 5);
        MONTHS.put("june", 6);
        MONTHS.put("july", 7);
        MONTHS.put("august", 8);
        MONTHS.put("september", 9);
        MONTHS.put("october", 10);
        MONTHS.put("november", 11);
        MONTHS.put("december", 12);

    }

    //if day or month starts with '0'
    public static void removeZero(String[] dateParts) {
        for (int i = 0; i < dateParts.length; i++) {
            if (dateParts[i].startsWith("0")) {
                dateParts[i] = dateParts[i].substring(1);
            }
        }
    }

}
