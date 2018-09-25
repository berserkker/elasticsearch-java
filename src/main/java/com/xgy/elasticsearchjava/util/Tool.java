package com.xgy.elasticsearchjava.util;

import java.util.Date;

/**
 * 工具类
 * */
public interface Tool {
    /**
     * 字符串转日期
     *
     * @param date    日期
     * @param pattern 格式
     */
    Date stringToDate(String date, String pattern);
}
