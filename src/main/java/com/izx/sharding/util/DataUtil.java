package com.izx.sharding.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class DataUtil {

    public static final String DATE_MONTH_FORMAT = "yyyyMM";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TABLE_FORMAT = "%s_%s";

    public static Date formatDate(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.parse(dateTime);
        } catch (ParseException e) {
            log.error("get exception when format date |{}|, exception is |{}|", dateTime, e);
            return null;
        }
    }

    public static String getTableByDate(String logicTable, Date date) {
        return getTableNameByDate( logicTable,  date,  true);
    }

    public static String getTableNameByDate(String logicTable, Date date, boolean isJudge) {
        // 避免查询超过
        Date now = new Date();
        if (isJudge && date.after(now)) {
            date = now;
        }
        return String.format(TABLE_FORMAT, logicTable,
                new SimpleDateFormat(DATE_MONTH_FORMAT).format(date));
    }

    public static Set<String> getTableSet(String logicTable, Date start, Date end) {
        Set<String> matchTables = new HashSet<>();
        if (start.after(end)) {
            matchTables.add(getTableByDate(logicTable, new Date()));
            return matchTables;
        }

        Calendar min = Calendar.getInstance(), max = Calendar.getInstance(),
                now = Calendar.getInstance();
        min.setTime(start);
        min.set(Calendar.DAY_OF_MONTH, 1);

        max.setTime(end);
        max.set(Calendar.DAY_OF_MONTH, 1);

        now.set(Calendar.DAY_OF_MONTH, 1);
        if (max.after(now)) {
            max = now;
        }

        while (min.compareTo(max) <= 0) {
            matchTables.add(getTableByDate(logicTable, min.getTime()));
            min.add(Calendar.MONTH, 1);
        }

        return matchTables;
    }

}
