package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.util.TimeZone;

class DatePolicy {

    private final TimeZone timeZone;

    private final boolean showFractionalSeconds;

    DatePolicy(TimeZone timeZone, boolean showFractionalSeconds) {
        this.timeZone = timeZone;
        this.showFractionalSeconds = showFractionalSeconds;
    }

    TimeZone getTimeZone() {
        return this.timeZone;
    }

    boolean isShowFractionalSeconds() {
        return this.showFractionalSeconds;
    }
}