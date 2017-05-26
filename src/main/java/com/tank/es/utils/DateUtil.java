package com.tank.es.utils;

import java.util.Date;

/**
 * Description:
 * User： JinHuaTao
 * Date：2017/4/11
 * Time: 17:34
 */

public class DateUtil {

    public static synchronized Date fromLong(long time) {
        return new Date(time);
    }

}
