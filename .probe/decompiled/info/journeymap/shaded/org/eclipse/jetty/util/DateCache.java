package info.journeymap.shaded.org.eclipse.jetty.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateCache {

    public static final String DEFAULT_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    private final String _formatString;

    private final String _tzFormatString;

    private final SimpleDateFormat _tzFormat;

    private final Locale _locale;

    private volatile DateCache.Tick _tick;

    public DateCache() {
        this("EEE MMM dd HH:mm:ss zzz yyyy");
    }

    public DateCache(String format) {
        this(format, null, TimeZone.getDefault());
    }

    public DateCache(String format, Locale l) {
        this(format, l, TimeZone.getDefault());
    }

    public DateCache(String format, Locale l, String tz) {
        this(format, l, TimeZone.getTimeZone(tz));
    }

    public DateCache(String format, Locale l, TimeZone tz) {
        this._formatString = format;
        this._locale = l;
        int zIndex = this._formatString.indexOf("ZZZ");
        if (zIndex >= 0) {
            String ss1 = this._formatString.substring(0, zIndex);
            String ss2 = this._formatString.substring(zIndex + 3);
            int tzOffset = tz.getRawOffset();
            StringBuilder sb = new StringBuilder(this._formatString.length() + 10);
            sb.append(ss1);
            sb.append("'");
            if (tzOffset >= 0) {
                sb.append('+');
            } else {
                tzOffset = -tzOffset;
                sb.append('-');
            }
            int raw = tzOffset / 60000;
            int hr = raw / 60;
            int min = raw % 60;
            if (hr < 10) {
                sb.append('0');
            }
            sb.append(hr);
            if (min < 10) {
                sb.append('0');
            }
            sb.append(min);
            sb.append('\'');
            sb.append(ss2);
            this._tzFormatString = sb.toString();
        } else {
            this._tzFormatString = this._formatString;
        }
        if (this._locale != null) {
            this._tzFormat = new SimpleDateFormat(this._tzFormatString, this._locale);
        } else {
            this._tzFormat = new SimpleDateFormat(this._tzFormatString);
        }
        this._tzFormat.setTimeZone(tz);
        this._tick = null;
    }

    public TimeZone getTimeZone() {
        return this._tzFormat.getTimeZone();
    }

    public String format(Date inDate) {
        long seconds = inDate.getTime() / 1000L;
        DateCache.Tick tick = this._tick;
        if (tick != null && seconds == tick._seconds) {
            return tick._string;
        } else {
            synchronized (this) {
                return this._tzFormat.format(inDate);
            }
        }
    }

    public String format(long inDate) {
        long seconds = inDate / 1000L;
        DateCache.Tick tick = this._tick;
        if (tick != null && seconds == tick._seconds) {
            return tick._string;
        } else {
            Date d = new Date(inDate);
            synchronized (this) {
                return this._tzFormat.format(d);
            }
        }
    }

    public String formatNow(long now) {
        long seconds = now / 1000L;
        DateCache.Tick tick = this._tick;
        return tick != null && tick._seconds == seconds ? tick._string : this.formatTick(now)._string;
    }

    public String now() {
        return this.formatNow(System.currentTimeMillis());
    }

    public DateCache.Tick tick() {
        return this.formatTick(System.currentTimeMillis());
    }

    protected DateCache.Tick formatTick(long now) {
        long seconds = now / 1000L;
        synchronized (this) {
            if (this._tick != null && this._tick._seconds == seconds) {
                return this._tick;
            } else {
                String s = this._tzFormat.format(new Date(now));
                return this._tick = new DateCache.Tick(seconds, s);
            }
        }
    }

    public String getFormatString() {
        return this._formatString;
    }

    public static class Tick {

        final long _seconds;

        final String _string;

        public Tick(long seconds, String string) {
            this._seconds = seconds;
            this._string = string;
        }
    }
}