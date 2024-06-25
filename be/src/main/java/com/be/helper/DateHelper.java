package com.be.helper;

import java.sql.Timestamp;

public class DateHelper {
    public static Timestamp getTimestampNow() {
        return new Timestamp(System.currentTimeMillis());
    }
}
