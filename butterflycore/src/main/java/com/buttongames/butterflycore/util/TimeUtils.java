package com.buttongames.butterflycore.util;

import java.time.*;

/**
 * Simple class with utility functions for dealing with time.
 * @author skogaby (skogabyskogaby@gmail.com)
 */
public class TimeUtils {

    /**
     * Convert an epoch timestamp to a LocalDateTime.
     * @param millis
     * @return
     */
    public static LocalDateTime timeFromEpoch(final long millis) {
        final Instant instant = Instant.ofEpochMilli(millis);

        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long getTime(){
        long unixTime = System.currentTimeMillis();
        return unixTime;
    }

    public static LocalDateTime getLocalDateTimeInUTC(){
        ZonedDateTime nowUTC = ZonedDateTime.now(ZoneOffset.UTC);

        return nowUTC.toLocalDateTime();
    }

    public static LocalDateTime getLocalDateTime(){
        return LocalDateTime.now();
    }
}
