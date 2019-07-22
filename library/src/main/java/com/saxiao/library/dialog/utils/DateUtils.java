package com.saxiao.library.dialog.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by yanghui on 2019/5/31.
 * 日期工具类
 *
 */

public class DateUtils {
    /**
     * 月份小于10加0
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期
     */
    @NonNull
    public static String dateAddZero(int year, int month, String day) {
        String newMonth = String.valueOf(month + 1);
        String date;
        if (month < 9) {
            newMonth = "0" + (month + 1);
        }
        if (Integer.valueOf(day) < 10) {
        	day = "0" + day;
        }
        if (TextUtils.isEmpty(day)) {
            date = year + "-" + newMonth;
        } else {
            date = year + "-" + newMonth + "-" + day;
        }
        return date;
    }
}
