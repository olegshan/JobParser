package com.olegshan.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by olegshan on 27.09.2016.
 */
public class MonthsTools {

    public static final Map<String, Integer> MONTHS = new HashMap<>();

    static {
        MONTHS.put("січня", 0);
        MONTHS.put("лютого", 1);
        MONTHS.put("березня", 2);
        MONTHS.put("квітня", 3);
        MONTHS.put("травня", 4);
        MONTHS.put("червня", 5);
        MONTHS.put("липня", 6);
        MONTHS.put("серпня", 7);
        MONTHS.put("вересня", 8);
        MONTHS.put("жовтня", 9);
        MONTHS.put("листопада", 10);
        MONTHS.put("грудня", 11);

        MONTHS.put("января", 0);
        MONTHS.put("февраля", 1);
        MONTHS.put("марта", 2);
        MONTHS.put("апреля", 3);
        MONTHS.put("мая", 4);
        MONTHS.put("июня", 5);
        MONTHS.put("июля", 6);
        MONTHS.put("августа", 7);
        MONTHS.put("сентября", 8);
        MONTHS.put("октября", 9);
        MONTHS.put("ноября", 10);
        MONTHS.put("декабря", 11);

        MONTHS.put("january", 0);
        MONTHS.put("february", 1);
        MONTHS.put("march", 2);
        MONTHS.put("april", 3);
        MONTHS.put("may", 4);
        MONTHS.put("june", 5);
        MONTHS.put("july", 6);
        MONTHS.put("august", 7);
        MONTHS.put("september", 8);
        MONTHS.put("october", 9);
        MONTHS.put("november", 10);
        MONTHS.put("december", 11);

    }

    //if day or month start with '0'
    public static void removeZero(String[] dateParts) {
        for (int i = 0; i < dateParts.length; i++) {
            if (dateParts[i].startsWith("0")) {
                dateParts[i] = dateParts[i].substring(1);
            }
        }
    }

}
