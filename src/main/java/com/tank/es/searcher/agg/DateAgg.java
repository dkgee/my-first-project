package com.tank.es.searcher.agg;

/**
 * Created by zhongshu on 2016/10/3 13:51.
 */
public class DateAgg extends HawkeyeAgg {

    public static final String TIMEZONE = "+00:00";
    private String interval = "day";
    private String format;
    private String timeZone;

    public DateAgg(String aggField, int size) {
        super(aggField, size);
    }

    public DateAgg(String aggField , String interval , String format , int size ) {
        super(aggField, size);
        this.interval = interval;
        this.timeZone = TIMEZONE;
        this.format = format;
    }

    public DateAgg(String aggField, String interval, String timeZone, String format, int size){
        super(aggField, size);
        this.interval = interval;
        this.timeZone = timeZone;
        this.format = format;
    }

    public DateAgg(String aggField , String interval , int size ) {
        super(aggField, size);
        this.interval = interval;
        this.timeZone = TIMEZONE;
    }

    public DateAgg(String aggField) {
        super(aggField, null);
        this.timeZone = TIMEZONE;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
