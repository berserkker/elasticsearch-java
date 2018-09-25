package com.xgy.elasticsearchjava.util;

import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ToolImpl implements Tool{

    /**
     * 字符串转日期
     *
     * @param date    日期
     * @param pattern 格式
     */
    public Date stringToDate(String date, String pattern) {
        Date result = null;
        result = DateUtils.parseDate(date, new String[]{pattern});
        return result;
    }
}
