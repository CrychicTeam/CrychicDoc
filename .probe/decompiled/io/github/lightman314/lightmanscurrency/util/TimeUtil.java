package io.github.lightman314.lightmanscurrency.util;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCConfig;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.text.TimeUnitTextEntry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.MutableComponent;

public class TimeUtil {

    public static final long DURATION_SECOND = 1000L;

    public static final long DURATION_MINUTE = 60000L;

    public static final long DURATION_HOUR = 3600000L;

    public static final long DURATION_DAY = 86400000L;

    public static long getCurrentTime() {
        return System.currentTimeMillis() + LightmansCurrency.PROXY.getTimeDesync();
    }

    public static String formatTime(long timeStamp) {
        return new SimpleDateFormat(LCConfig.CLIENT.timeFormat.get()).format(new Date(timeStamp - LightmansCurrency.PROXY.getTimeDesync()));
    }

    public static boolean compareTime(long duration, long compareTime) {
        long ignoreTime = getCurrentTime() - duration;
        return compareTime >= ignoreTime;
    }

    public static long getDuration(long days, long hours, long minutes, long seconds) {
        days = Math.max(days, 0L);
        hours = Math.max(hours, 0L);
        minutes = Math.max(minutes, 0L);
        seconds = Math.max(seconds, 0L);
        hours += 24L * days;
        minutes += 60L * hours;
        seconds += 60L * minutes;
        return seconds * 1000L;
    }

    @Deprecated(forRemoval = true)
    public static TimeUtil.TimeData separateDuration(long duration) {
        return new TimeUtil.TimeData(duration);
    }

    public static class TimeData {

        public final long days;

        public final long hours;

        public final long minutes;

        public final long seconds;

        public final long miliseconds;

        public TimeData(long days, long hours, long minutes, long seconds) {
            this(TimeUtil.getDuration(days, hours, minutes, seconds));
        }

        public TimeData(long milliseconds) {
            this.miliseconds = Math.max(milliseconds, 0L);
            long seconds = this.miliseconds / 1000L;
            long minutes = seconds / 60L;
            seconds %= 60L;
            long hours = minutes / 60L;
            minutes %= 60L;
            long days = hours / 24L;
            hours %= 24L;
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        private long getUnitValue(TimeUtil.TimeUnit unit) {
            return switch(unit) {
                case DAY ->
                    this.days;
                case HOUR ->
                    this.hours;
                case MINUTE ->
                    this.minutes;
                case SECOND ->
                    this.seconds;
            };
        }

        public String getUnitString(TimeUtil.TimeUnit unit, boolean shortText) {
            return this.getUnitString(unit, shortText, true);
        }

        private String getUnitString(TimeUtil.TimeUnit unit, boolean shortText, boolean force) {
            StringBuilder text = new StringBuilder();
            long count = this.getUnitValue(unit);
            if (count > 0L || force) {
                text.append(count).append(shortText ? unit.getShortText().getString() : (count != 1L ? unit.getPluralText().getString() : unit.getText().getString()));
            }
            return text.toString();
        }

        public String getString() {
            return this.getString(false, Integer.MAX_VALUE);
        }

        public String getString(int maxCount) {
            return this.getString(false, maxCount);
        }

        public String getShortString() {
            return this.getString(true, Integer.MAX_VALUE);
        }

        public String getShortString(int maxCount) {
            return this.getString(true, maxCount);
        }

        private String getString(boolean shortText, int maxCount) {
            StringBuilder text = new StringBuilder();
            int count = 0;
            for (int i = 0; i < TimeUtil.TimeUnit.UNITS_LARGE_TO_SMALL.size() && count < maxCount; i++) {
                TimeUtil.TimeUnit unit = (TimeUtil.TimeUnit) TimeUtil.TimeUnit.UNITS_LARGE_TO_SMALL.get(i);
                String unitText = this.getUnitString(unit, shortText, false);
                if (!unitText.isEmpty()) {
                    if (!text.isEmpty()) {
                        text.append(" ");
                    }
                    text.append(unitText);
                    count++;
                }
            }
            return text.toString();
        }
    }

    public static enum TimeUnit {

        SECOND(LCText.TIME_UNIT_SECOND), MINUTE(LCText.TIME_UNIT_MINUTE), HOUR(LCText.TIME_UNIT_HOUR), DAY(LCText.TIME_UNIT_DAY);

        public final TimeUnitTextEntry text;

        public static final List<TimeUtil.TimeUnit> UNITS_SMALL_TO_LARGE = ImmutableList.of(SECOND, MINUTE, HOUR, DAY);

        public static final List<TimeUtil.TimeUnit> UNITS_LARGE_TO_SMALL = ImmutableList.of(DAY, HOUR, MINUTE, SECOND);

        private TimeUnit(@Nonnull TimeUnitTextEntry text) {
            this.text = text;
        }

        public MutableComponent getText() {
            return this.text.fullText.get();
        }

        public MutableComponent getPluralText() {
            return this.text.pluralText.get();
        }

        public MutableComponent getShortText() {
            return this.text.shortText.get();
        }
    }
}