package com.mna.events.seasonal;

import java.time.ZonedDateTime;

public class SeasonalHelper {

    private static boolean MatchLocalDate(int day, int month, int beforeDaysTolerance, int afterDaysTolerance) {
        try {
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime min = ZonedDateTime.of(now.getYear(), month, day, 0, 0, 0, 0, now.getZone()).minusDays((long) beforeDaysTolerance);
            ZonedDateTime max = ZonedDateTime.of(now.getYear(), month, day, 23, 59, 59, 0, now.getZone()).plusDays((long) afterDaysTolerance);
            return now.isAfter(min) && now.isBefore(max);
        } catch (Exception var7) {
            return false;
        }
    }

    public static boolean isHalloween() {
        return MatchLocalDate(31, 10, 7, 1);
    }

    public static boolean isChristmas() {
        return MatchLocalDate(25, 12, 14, 3);
    }
}