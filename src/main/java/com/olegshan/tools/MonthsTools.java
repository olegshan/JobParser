package com.olegshan.tools;

import java.util.HashMap;
import java.util.Map;

public class MonthsTools {

    public static final Map<String, Integer> MONTHS = new HashMap<String, Integer>() {{

        put("січня", 1);
        put("лютого", 2);
        put("березня", 3);
        put("квітня", 4);
        put("травня", 5);
        put("червня", 6);
        put("липня", 7);
        put("серпня", 8);
        put("вересня", 9);
        put("жовтня", 10);
        put("листопада", 11);
        put("грудня", 12);

        put("января", 1);
        put("февраля", 2);
        put("марта", 3);
        put("апреля", 4);
        put("мая", 5);
        put("июня", 6);
        put("июля", 7);
        put("августа", 8);
        put("сентября", 9);
        put("октября", 10);
        put("ноября", 11);
        put("декабря", 12);

        put("янв", 1);
        put("фев", 2);
        put("мар", 3);
        put("апр", 4);
        put("май", 5);
        put("июн", 6);
        put("июл", 7);
        put("авг", 8);
        put("сен", 9);
        put("окт", 10);
        put("ноя", 11);
        put("дек", 12);

        put("january", 1);
        put("february", 2);
        put("march", 3);
        put("april", 4);
        put("may", 5);
        put("june", 6);
        put("july", 7);
        put("august", 8);
        put("september", 9);
        put("october", 10);
        put("november", 11);
        put("december", 12);
    }};

    //if day or month starts with '0'
    public static void removeZero(String[] dateParts) {
        for (int i = 0; i < dateParts.length; i++) {
            if (dateParts[i].startsWith("0")) {
                dateParts[i] = dateParts[i].substring(1);
            }
        }
    }
}
