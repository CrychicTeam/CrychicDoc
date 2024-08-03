package dev.latvian.mods.rhino;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

final class NativeDate extends IdScriptableObject {

    private static final Object DATE_TAG = "Date";

    private static final String js_NaN_date_str = "Invalid Date";

    private static final double HalfTimeDomain = 8.64E15;

    private static final double HoursPerDay = 24.0;

    private static final double MinutesPerHour = 60.0;

    private static final double SecondsPerMinute = 60.0;

    private static final double msPerSecond = 1000.0;

    private static final double MinutesPerDay = 1440.0;

    private static final double SecondsPerDay = 86400.0;

    private static final double msPerDay = 8.64E7;

    private static final double SecondsPerHour = 3600.0;

    private static final double msPerHour = 3600000.0;

    private static final double msPerMinute = 60000.0;

    private static final int MAXARGS = 7;

    private static final int ConstructorId_now = -3;

    private static final int ConstructorId_parse = -2;

    private static final int ConstructorId_UTC = -1;

    private static final int Id_constructor = 1;

    private static final int Id_toString = 2;

    private static final int Id_toTimeString = 3;

    private static final int Id_toDateString = 4;

    private static final int Id_toLocaleString = 5;

    private static final int Id_toLocaleTimeString = 6;

    private static final int Id_toLocaleDateString = 7;

    private static final int Id_toUTCString = 8;

    private static final int Id_toSource = 9;

    private static final int Id_valueOf = 10;

    private static final int Id_getTime = 11;

    private static final int Id_getYear = 12;

    private static final int Id_getFullYear = 13;

    private static final int Id_getUTCFullYear = 14;

    private static final int Id_getMonth = 15;

    private static final int Id_getUTCMonth = 16;

    private static final int Id_getDate = 17;

    private static final int Id_getUTCDate = 18;

    private static final int Id_getDay = 19;

    private static final int Id_getUTCDay = 20;

    private static final int Id_getHours = 21;

    private static final int Id_getUTCHours = 22;

    private static final int Id_getMinutes = 23;

    private static final int Id_getUTCMinutes = 24;

    private static final int Id_getSeconds = 25;

    private static final int Id_getUTCSeconds = 26;

    private static final int Id_getMilliseconds = 27;

    private static final int Id_getUTCMilliseconds = 28;

    private static final int Id_getTimezoneOffset = 29;

    private static final int Id_setTime = 30;

    private static final int Id_setMilliseconds = 31;

    private static final int Id_setUTCMilliseconds = 32;

    private static final int Id_setSeconds = 33;

    private static final int Id_setUTCSeconds = 34;

    private static final int Id_setMinutes = 35;

    private static final int Id_setUTCMinutes = 36;

    private static final int Id_setHours = 37;

    private static final int Id_setUTCHours = 38;

    private static final int Id_setDate = 39;

    private static final int Id_setUTCDate = 40;

    private static final int Id_setMonth = 41;

    private static final int Id_setUTCMonth = 42;

    private static final int Id_setFullYear = 43;

    private static final int Id_setUTCFullYear = 44;

    private static final int Id_setYear = 45;

    private static final int Id_toISOString = 46;

    private static final int Id_toJSON = 47;

    private static final int MAX_PROTOTYPE_ID = 47;

    private static final int Id_toGMTString = 8;

    private static final TimeZone thisTimeZone = TimeZone.getDefault();

    private static final double LocalTZA = (double) thisTimeZone.getRawOffset();

    private static final DateFormat timeZoneFormatter = new SimpleDateFormat("zzz");

    private static final DateFormat localeDateTimeFormatter = new SimpleDateFormat("MMMM d, yyyy h:mm:ss a z");

    private static final DateFormat localeDateFormatter = new SimpleDateFormat("MMMM d, yyyy");

    private static final DateFormat localeTimeFormatter = new SimpleDateFormat("h:mm:ss a z");

    private double date;

    static void init(Scriptable scope, boolean sealed, Context cx) {
        NativeDate obj = new NativeDate();
        obj.date = Double.NaN;
        obj.exportAsJSClass(47, scope, sealed, cx);
    }

    private static double Day(double t) {
        return Math.floor(t / 8.64E7);
    }

    private static double TimeWithinDay(double t) {
        double result = t % 8.64E7;
        if (result < 0.0) {
            result += 8.64E7;
        }
        return result;
    }

    private static boolean IsLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static double DayFromYear(double y) {
        return 365.0 * (y - 1970.0) + Math.floor((y - 1969.0) / 4.0) - Math.floor((y - 1901.0) / 100.0) + Math.floor((y - 1601.0) / 400.0);
    }

    private static double TimeFromYear(double y) {
        return DayFromYear(y) * 8.64E7;
    }

    private static int YearFromTime(double t) {
        if (!Double.isInfinite(t) && !Double.isNaN(t)) {
            double y = Math.floor(t / 3.1556952E10) + 1970.0;
            double t2 = TimeFromYear(y);
            if (t2 > t) {
                y--;
            } else if (t2 + 8.64E7 * DaysInYear(y) <= t) {
                y++;
            }
            return (int) y;
        } else {
            return 0;
        }
    }

    private static double DayFromMonth(int m, int year) {
        int day = m * 30;
        if (m >= 7) {
            day += m / 2 - 1;
        } else if (m >= 2) {
            day += (m - 1) / 2 - 1;
        } else {
            day += m;
        }
        if (m >= 2 && IsLeapYear(year)) {
            day++;
        }
        return (double) day;
    }

    private static double DaysInYear(double year) {
        if (!Double.isInfinite(year) && !Double.isNaN(year)) {
            return IsLeapYear((int) year) ? 366.0 : 365.0;
        } else {
            return Double.NaN;
        }
    }

    private static int DaysInMonth(int year, int month) {
        if (month == 2) {
            return IsLeapYear(year) ? 29 : 28;
        } else {
            return month >= 8 ? 31 - (month & 1) : 30 + (month & 1);
        }
    }

    private static int MonthFromTime(double t) {
        int year = YearFromTime(t);
        int d = (int) (Day(t) - DayFromYear((double) year));
        d -= 59;
        if (d < 0) {
            return d < -28 ? 0 : 1;
        } else {
            if (IsLeapYear(year)) {
                if (d == 0) {
                    return 1;
                }
                d--;
            }
            int estimate = d / 30;
            int mstart;
            switch(estimate) {
                case 0:
                    return 2;
                case 1:
                    mstart = 31;
                    break;
                case 2:
                    mstart = 61;
                    break;
                case 3:
                    mstart = 92;
                    break;
                case 4:
                    mstart = 122;
                    break;
                case 5:
                    mstart = 153;
                    break;
                case 6:
                    mstart = 184;
                    break;
                case 7:
                    mstart = 214;
                    break;
                case 8:
                    mstart = 245;
                    break;
                case 9:
                    mstart = 275;
                    break;
                case 10:
                    return 11;
                default:
                    throw Kit.codeBug();
            }
            return d >= mstart ? estimate + 2 : estimate + 1;
        }
    }

    private static int DateFromTime(double t) {
        int year = YearFromTime(t);
        int d = (int) (Day(t) - DayFromYear((double) year));
        d -= 59;
        if (d < 0) {
            return d < -28 ? d + 31 + 28 + 1 : d + 28 + 1;
        } else {
            if (IsLeapYear(year)) {
                if (d == 0) {
                    return 29;
                }
                d--;
            }
            int mdays;
            int mstart;
            switch(d / 30) {
                case 0:
                    return d + 1;
                case 1:
                    mdays = 31;
                    mstart = 31;
                    break;
                case 2:
                    mdays = 30;
                    mstart = 61;
                    break;
                case 3:
                    mdays = 31;
                    mstart = 92;
                    break;
                case 4:
                    mdays = 30;
                    mstart = 122;
                    break;
                case 5:
                    mdays = 31;
                    mstart = 153;
                    break;
                case 6:
                    mdays = 31;
                    mstart = 184;
                    break;
                case 7:
                    mdays = 30;
                    mstart = 214;
                    break;
                case 8:
                    mdays = 31;
                    mstart = 245;
                    break;
                case 9:
                    mdays = 30;
                    mstart = 275;
                    break;
                case 10:
                    return d - 275 + 1;
                default:
                    throw Kit.codeBug();
            }
            d -= mstart;
            if (d < 0) {
                d += mdays;
            }
            return d + 1;
        }
    }

    private static int WeekDay(double t) {
        double result = Day(t) + 4.0;
        result %= 7.0;
        if (result < 0.0) {
            result += 7.0;
        }
        return (int) result;
    }

    private static double now() {
        return (double) System.currentTimeMillis();
    }

    private static double DaylightSavingTA(double t) {
        if (t < 0.0) {
            int year = EquivalentYear(YearFromTime(t));
            double day = MakeDay((double) year, (double) MonthFromTime(t), (double) DateFromTime(t));
            t = MakeDate(day, TimeWithinDay(t));
        }
        Date date = new Date((long) t);
        return thisTimeZone.inDaylightTime(date) ? 3600000.0 : 0.0;
    }

    private static int EquivalentYear(int year) {
        int day = (int) DayFromYear((double) year) + 4;
        day %= 7;
        if (day < 0) {
            day += 7;
        }
        if (IsLeapYear(year)) {
            switch(day) {
                case 0:
                    return 1984;
                case 1:
                    return 1996;
                case 2:
                    return 1980;
                case 3:
                    return 1992;
                case 4:
                    return 1976;
                case 5:
                    return 1988;
                case 6:
                    return 1972;
            }
        } else {
            switch(day) {
                case 0:
                    return 1978;
                case 1:
                    return 1973;
                case 2:
                    return 1985;
                case 3:
                    return 1986;
                case 4:
                    return 1981;
                case 5:
                    return 1971;
                case 6:
                    return 1977;
            }
        }
        throw Kit.codeBug();
    }

    private static double LocalTime(double t) {
        return t + LocalTZA + DaylightSavingTA(t);
    }

    private static double internalUTC(double t) {
        return t - LocalTZA - DaylightSavingTA(t - LocalTZA);
    }

    private static int HourFromTime(double t) {
        double result = Math.floor(t / 3600000.0) % 24.0;
        if (result < 0.0) {
            result += 24.0;
        }
        return (int) result;
    }

    private static int MinFromTime(double t) {
        double result = Math.floor(t / 60000.0) % 60.0;
        if (result < 0.0) {
            result += 60.0;
        }
        return (int) result;
    }

    private static int SecFromTime(double t) {
        double result = Math.floor(t / 1000.0) % 60.0;
        if (result < 0.0) {
            result += 60.0;
        }
        return (int) result;
    }

    private static int msFromTime(double t) {
        double result = t % 1000.0;
        if (result < 0.0) {
            result += 1000.0;
        }
        return (int) result;
    }

    private static double MakeTime(double hour, double min, double sec, double ms) {
        return ((hour * 60.0 + min) * 60.0 + sec) * 1000.0 + ms;
    }

    private static double MakeDay(double year, double month, double date) {
        year += Math.floor(month / 12.0);
        month %= 12.0;
        if (month < 0.0) {
            month += 12.0;
        }
        double yearday = Math.floor(TimeFromYear(year) / 8.64E7);
        double monthday = DayFromMonth((int) month, (int) year);
        return yearday + monthday + date - 1.0;
    }

    private static double MakeDate(double day, double time) {
        return day * 8.64E7 + time;
    }

    private static double TimeClip(double d) {
        if (Double.isNaN(d) || d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY || Math.abs(d) > 8.64E15) {
            return Double.NaN;
        } else {
            return d > 0.0 ? Math.floor(d + 0.0) : Math.ceil(d + 0.0);
        }
    }

    private static double date_msecFromDate(double year, double mon, double mday, double hour, double min, double sec, double msec) {
        double day = MakeDay(year, mon, mday);
        double time = MakeTime(hour, min, sec, msec);
        return MakeDate(day, time);
    }

    private static double date_msecFromArgs(Object[] args, Context cx) {
        double[] array = new double[7];
        for (int loop = 0; loop < 7; loop++) {
            if (loop < args.length) {
                double d = ScriptRuntime.toNumber(cx, args[loop]);
                if (Double.isNaN(d) || Double.isInfinite(d)) {
                    return Double.NaN;
                }
                array[loop] = ScriptRuntime.toInteger(cx, args[loop]);
            } else if (loop == 2) {
                array[loop] = 1.0;
            } else {
                array[loop] = 0.0;
            }
        }
        if (array[0] >= 0.0 && array[0] <= 99.0) {
            array[0] += 1900.0;
        }
        return date_msecFromDate(array[0], array[1], array[2], array[3], array[4], array[5], array[6]);
    }

    private static double jsStaticFunction_UTC(Context cx, Object[] args) {
        return args.length == 0 ? Double.NaN : TimeClip(date_msecFromArgs(args, cx));
    }

    private static double parseISOString(String s) {
        int ERROR = -1;
        int YEAR = 0;
        int MONTH = 1;
        int DAY = 2;
        int HOUR = 3;
        int MIN = 4;
        int SEC = 5;
        int MSEC = 6;
        int TZHOUR = 7;
        int TZMIN = 8;
        int state = 0;
        int[] values = new int[] { 1970, 1, 1, 0, 0, 0, 0, -1, -1 };
        int yearlen = 4;
        int yearmod = 1;
        int tzmod = 1;
        int i = 0;
        int len = s.length();
        if (len != 0) {
            char c = s.charAt(0);
            if (c == '+' || c == '-') {
                i++;
                yearlen = 6;
                yearmod = c == '-' ? -1 : 1;
            } else if (c == 'T') {
                i++;
                state = 3;
            }
        }
        label194: while (state != -1) {
            int m = i + (state == 0 ? yearlen : (state == 6 ? 3 : 2));
            if (m > len) {
                state = -1;
                break;
            }
            int value;
            for (value = 0; i < m; i++) {
                char c = s.charAt(i);
                if (c < '0' || c > '9') {
                    state = -1;
                    break label194;
                }
                value = 10 * value + (c - '0');
            }
            values[state] = value;
            if (i == len) {
                state = switch(state) {
                    case 3, 7 ->
                        -1;
                    default ->
                        state;
                };
                break;
            }
            char c = s.charAt(i++);
            if (c == 'Z') {
                values[7] = 0;
                values[8] = 0;
                switch(state) {
                    case 4:
                    case 5:
                    case 6:
                        break label194;
                    default:
                        state = -1;
                        break label194;
                }
            }
            switch(state) {
                case 0:
                case 1:
                    state = c == '-' ? state + 1 : (c == 'T' ? 3 : -1);
                    break;
                case 2:
                    state = c == 'T' ? 3 : -1;
                    break;
                case 3:
                    state = c == ':' ? 4 : -1;
                    break;
                case 4:
                    state = c == ':' ? 5 : (c != '+' && c != '-' ? -1 : 7);
                    break;
                case 5:
                    state = c == '.' ? 6 : (c != '+' && c != '-' ? -1 : 7);
                    break;
                case 6:
                    state = c != '+' && c != '-' ? -1 : 7;
                    break;
                case 7:
                    if (c != ':') {
                        i--;
                    }
                    state = 8;
                    break;
                case 8:
                    state = -1;
            }
            if (state == 7) {
                tzmod = c == '-' ? -1 : 1;
            }
        }
        if (state != -1 && i == len) {
            int year = values[0];
            int month = values[1];
            int day = values[2];
            int hour = values[3];
            int min = values[4];
            int sec = values[5];
            int msec = values[6];
            int tzhour = values[7];
            int tzmin = values[8];
            if (year <= 275943 && month >= 1 && month <= 12 && day >= 1 && day <= DaysInMonth(year, month) && hour <= 24 && (hour != 24 || min <= 0 && sec <= 0 && msec <= 0) && min <= 59 && sec <= 59 && tzhour <= 23 && tzmin <= 59) {
                double date = date_msecFromDate((double) (year * yearmod), (double) (month - 1), (double) day, (double) hour, (double) min, (double) sec, (double) msec);
                if (tzhour != -1) {
                    date -= (double) (tzhour * 60 + tzmin) * 60000.0 * (double) tzmod;
                }
                if (!(date < -8.64E15) && !(date > 8.64E15)) {
                    return date;
                }
            }
        }
        return Double.NaN;
    }

    private static double date_parseString(String s) {
        double d = parseISOString(s);
        if (!Double.isNaN(d)) {
            return d;
        } else {
            int year = -1;
            int mon = -1;
            int mday = -1;
            int hour = -1;
            int min = -1;
            int sec = -1;
            char c = '\u0000';
            char si = '\u0000';
            int i = 0;
            int n = -1;
            double tzoffset = -1.0;
            char prevc = 0;
            int limit = 0;
            boolean seenplusminus = false;
            limit = s.length();
            label319: while (i < limit) {
                c = s.charAt(i);
                i++;
                if (c > ' ' && c != ',' && c != '-') {
                    if (c == '(') {
                        int depth = 1;
                        while (i < limit) {
                            c = s.charAt(i);
                            i++;
                            if (c == '(') {
                                depth++;
                            } else if (c == ')') {
                                if (--depth <= 0) {
                                    break;
                                }
                            }
                        }
                    } else if ('0' <= c && c <= '9') {
                        for (n = c - '0'; i < limit && '0' <= (c = s.charAt(i)) && c <= '9'; i++) {
                            n = n * 10 + c - 48;
                        }
                        if (prevc != '+' && prevc != '-') {
                            if (n < 70 && (prevc != '/' || mon < 0 || mday < 0 || year >= 0)) {
                                if (c == ':') {
                                    if (hour < 0) {
                                        hour = n;
                                    } else {
                                        if (min >= 0) {
                                            return Double.NaN;
                                        }
                                        min = n;
                                    }
                                } else if (c == '/') {
                                    if (mon < 0) {
                                        mon = n - 1;
                                    } else {
                                        if (mday >= 0) {
                                            return Double.NaN;
                                        }
                                        mday = n;
                                    }
                                } else {
                                    if (i < limit && c != ',' && c > ' ' && c != '-') {
                                        return Double.NaN;
                                    }
                                    if (seenplusminus && n < 60) {
                                        if (tzoffset < 0.0) {
                                            tzoffset -= (double) n;
                                        } else {
                                            tzoffset += (double) n;
                                        }
                                    } else if (hour >= 0 && min < 0) {
                                        min = n;
                                    } else if (min >= 0 && sec < 0) {
                                        sec = n;
                                    } else {
                                        if (mday >= 0) {
                                            return Double.NaN;
                                        }
                                        mday = n;
                                    }
                                }
                            } else {
                                if (year >= 0) {
                                    return Double.NaN;
                                }
                                if (c > ' ' && c != ',' && c != '/' && i < limit) {
                                    return Double.NaN;
                                }
                                year = n < 100 ? n + 1900 : n;
                            }
                        } else {
                            seenplusminus = true;
                            if (n < 24) {
                                n *= 60;
                            } else {
                                n = n % 100 + n / 100 * 60;
                            }
                            if (prevc == '+') {
                                n = -n;
                            }
                            if (tzoffset != 0.0 && tzoffset != -1.0) {
                                return Double.NaN;
                            }
                            tzoffset = (double) n;
                        }
                        prevc = 0;
                    } else if (c != '/' && c != ':' && c != '+' && c != '-') {
                        int st;
                        for (st = i - 1; i < limit; i++) {
                            c = s.charAt(i);
                            if (('A' > c || c > 'Z') && ('a' > c || c > 'z')) {
                                break;
                            }
                        }
                        int letterCount = i - st;
                        if (letterCount < 2) {
                            return Double.NaN;
                        }
                        String wtb = "am;pm;monday;tuesday;wednesday;thursday;friday;saturday;sunday;january;february;march;april;may;june;july;august;september;october;november;december;gmt;ut;utc;est;edt;cst;cdt;mst;mdt;pst;pdt;";
                        int index = 0;
                        int wtbOffset = 0;
                        while (true) {
                            int wtbNext = wtb.indexOf(59, wtbOffset);
                            if (wtbNext < 0) {
                                return Double.NaN;
                            }
                            if (wtb.regionMatches(true, wtbOffset, s, st, letterCount)) {
                                if (index < 2) {
                                    if (hour > 12 || hour < 0) {
                                        return Double.NaN;
                                    }
                                    if (index == 0) {
                                        if (hour == 12) {
                                            hour = 0;
                                        }
                                    } else if (hour != 12) {
                                        hour += 12;
                                    }
                                } else {
                                    index -= 2;
                                    if (index >= 7) {
                                        index -= 7;
                                        if (index < 12) {
                                            if (mon >= 0) {
                                                return Double.NaN;
                                            }
                                            mon = index;
                                        } else {
                                            index -= 12;
                                            switch(index) {
                                                case 0:
                                                    tzoffset = 0.0;
                                                    continue label319;
                                                case 1:
                                                    tzoffset = 0.0;
                                                    continue label319;
                                                case 2:
                                                    tzoffset = 0.0;
                                                    continue label319;
                                                case 3:
                                                    tzoffset = 300.0;
                                                    continue label319;
                                                case 4:
                                                    tzoffset = 240.0;
                                                    continue label319;
                                                case 5:
                                                    tzoffset = 360.0;
                                                    continue label319;
                                                case 6:
                                                    tzoffset = 300.0;
                                                    continue label319;
                                                case 7:
                                                    tzoffset = 420.0;
                                                    continue label319;
                                                case 8:
                                                    tzoffset = 360.0;
                                                    continue label319;
                                                case 9:
                                                    tzoffset = 480.0;
                                                    continue label319;
                                                case 10:
                                                    tzoffset = 420.0;
                                                    continue label319;
                                                default:
                                                    Kit.codeBug();
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                            wtbOffset = wtbNext + 1;
                            index++;
                        }
                    } else {
                        prevc = c;
                    }
                } else if (i < limit) {
                    si = s.charAt(i);
                    if (c == '-' && '0' <= si && si <= '9') {
                        prevc = c;
                    }
                }
            }
            if (year >= 0 && mon >= 0 && mday >= 0) {
                if (sec < 0) {
                    sec = 0;
                }
                if (min < 0) {
                    min = 0;
                }
                if (hour < 0) {
                    hour = 0;
                }
                double msec = date_msecFromDate((double) year, (double) mon, (double) mday, (double) hour, (double) min, (double) sec, 0.0);
                return tzoffset == -1.0 ? internalUTC(msec) : msec + tzoffset * 60000.0;
            } else {
                return Double.NaN;
            }
        }
    }

    private static String date_format(double t, int methodId) {
        StringBuilder result = new StringBuilder(60);
        double local = LocalTime(t);
        if (methodId != 3) {
            appendWeekDayName(result, WeekDay(local));
            result.append(' ');
            appendMonthName(result, MonthFromTime(local));
            result.append(' ');
            append0PaddedUint(result, DateFromTime(local), 2);
            result.append(' ');
            int year = YearFromTime(local);
            if (year < 0) {
                result.append('-');
                year = -year;
            }
            append0PaddedUint(result, year, 4);
            if (methodId != 4) {
                result.append(' ');
            }
        }
        if (methodId != 4) {
            append0PaddedUint(result, HourFromTime(local), 2);
            result.append(':');
            append0PaddedUint(result, MinFromTime(local), 2);
            result.append(':');
            append0PaddedUint(result, SecFromTime(local), 2);
            int minutes = (int) Math.floor((LocalTZA + DaylightSavingTA(t)) / 60000.0);
            int offset = minutes / 60 * 100 + minutes % 60;
            if (offset > 0) {
                result.append(" GMT+");
            } else {
                result.append(" GMT-");
                offset = -offset;
            }
            append0PaddedUint(result, offset, 4);
            if (t < 0.0) {
                int equiv = EquivalentYear(YearFromTime(local));
                double day = MakeDay((double) equiv, (double) MonthFromTime(t), (double) DateFromTime(t));
                t = MakeDate(day, TimeWithinDay(t));
            }
            result.append(" (");
            Date date = new Date((long) t);
            synchronized (timeZoneFormatter) {
                result.append(timeZoneFormatter.format(date));
            }
            result.append(')');
        }
        return result.toString();
    }

    private static Object jsConstructor(Object[] args, Context cx) {
        NativeDate obj = new NativeDate();
        if (args.length == 0) {
            obj.date = now();
            return obj;
        } else if (args.length == 1) {
            Object arg0 = args[0];
            if (arg0 instanceof NativeDate) {
                obj.date = ((NativeDate) arg0).date;
                return obj;
            } else {
                if (arg0 instanceof Scriptable) {
                    arg0 = ((Scriptable) arg0).getDefaultValue(cx, null);
                }
                double date;
                if (arg0 instanceof CharSequence) {
                    date = date_parseString(arg0.toString());
                } else {
                    date = ScriptRuntime.toNumber(cx, arg0);
                }
                obj.date = TimeClip(date);
                return obj;
            }
        } else {
            double time = date_msecFromArgs(args, cx);
            if (!Double.isNaN(time) && !Double.isInfinite(time)) {
                time = TimeClip(internalUTC(time));
            }
            obj.date = time;
            return obj;
        }
    }

    private static String toLocale_helper(double t, int methodId) {
        DateFormat formatter = switch(methodId) {
            case 5 ->
                localeDateTimeFormatter;
            case 6 ->
                localeTimeFormatter;
            case 7 ->
                localeDateFormatter;
            default ->
                throw new AssertionError();
        };
        synchronized (formatter) {
            return formatter.format(new Date((long) t));
        }
    }

    private static String js_toUTCString(double date) {
        StringBuilder result = new StringBuilder(60);
        appendWeekDayName(result, WeekDay(date));
        result.append(", ");
        append0PaddedUint(result, DateFromTime(date), 2);
        result.append(' ');
        appendMonthName(result, MonthFromTime(date));
        result.append(' ');
        int year = YearFromTime(date);
        if (year < 0) {
            result.append('-');
            year = -year;
        }
        append0PaddedUint(result, year, 4);
        result.append(' ');
        append0PaddedUint(result, HourFromTime(date), 2);
        result.append(':');
        append0PaddedUint(result, MinFromTime(date), 2);
        result.append(':');
        append0PaddedUint(result, SecFromTime(date), 2);
        result.append(" GMT");
        return result.toString();
    }

    private static String js_toISOString(double t) {
        StringBuilder result = new StringBuilder(27);
        int year = YearFromTime(t);
        if (year < 0) {
            result.append('-');
            append0PaddedUint(result, -year, 6);
        } else if (year > 9999) {
            append0PaddedUint(result, year, 6);
        } else {
            append0PaddedUint(result, year, 4);
        }
        result.append('-');
        append0PaddedUint(result, MonthFromTime(t) + 1, 2);
        result.append('-');
        append0PaddedUint(result, DateFromTime(t), 2);
        result.append('T');
        append0PaddedUint(result, HourFromTime(t), 2);
        result.append(':');
        append0PaddedUint(result, MinFromTime(t), 2);
        result.append(':');
        append0PaddedUint(result, SecFromTime(t), 2);
        result.append('.');
        append0PaddedUint(result, msFromTime(t), 3);
        result.append('Z');
        return result.toString();
    }

    private static void append0PaddedUint(StringBuilder sb, int i, int minWidth) {
        if (i < 0) {
            Kit.codeBug();
        }
        int scale = 1;
        minWidth--;
        if (i >= 10) {
            if (i < 1000000000) {
                while (true) {
                    int newScale = scale * 10;
                    if (i < newScale) {
                        break;
                    }
                    minWidth--;
                    scale = newScale;
                }
            } else {
                minWidth -= 9;
                scale = 1000000000;
            }
        }
        while (minWidth > 0) {
            sb.append('0');
            minWidth--;
        }
        while (scale != 1) {
            sb.append((char) (48 + i / scale));
            i %= scale;
            scale /= 10;
        }
        sb.append((char) (48 + i));
    }

    private static void appendMonthName(StringBuilder sb, int index) {
        String months = "JanFebMarAprMayJunJulAugSepOctNovDec";
        index *= 3;
        for (int i = 0; i != 3; i++) {
            sb.append(months.charAt(index + i));
        }
    }

    private static void appendWeekDayName(StringBuilder sb, int index) {
        String days = "SunMonTueWedThuFriSat";
        index *= 3;
        for (int i = 0; i != 3; i++) {
            sb.append(days.charAt(index + i));
        }
    }

    private static double makeTime(double date, Object[] args, int methodId, Context cx) {
        if (args.length == 0) {
            return Double.NaN;
        } else {
            boolean local = true;
            int maxargs;
            switch(methodId) {
                case 32:
                    local = false;
                case 31:
                    maxargs = 1;
                    break;
                case 34:
                    local = false;
                case 33:
                    maxargs = 2;
                    break;
                case 36:
                    local = false;
                case 35:
                    maxargs = 3;
                    break;
                case 38:
                    local = false;
                case 37:
                    maxargs = 4;
                    break;
                default:
                    throw Kit.codeBug();
            }
            boolean hasNaN = false;
            int numNums = args.length < maxargs ? args.length : maxargs;
            assert numNums <= 4;
            double[] nums = new double[4];
            for (int i = 0; i < numNums; i++) {
                double d = ScriptRuntime.toNumber(cx, args[i]);
                if (!Double.isNaN(d) && !Double.isInfinite(d)) {
                    nums[i] = ScriptRuntime.toInteger(d);
                } else {
                    hasNaN = true;
                }
            }
            if (!hasNaN && !Double.isNaN(date)) {
                int ix = 0;
                double lorutime;
                if (local) {
                    lorutime = LocalTime(date);
                } else {
                    lorutime = date;
                }
                double hour;
                if (maxargs >= 4 && ix < numNums) {
                    hour = nums[ix++];
                } else {
                    hour = (double) HourFromTime(lorutime);
                }
                double min;
                if (maxargs >= 3 && ix < numNums) {
                    min = nums[ix++];
                } else {
                    min = (double) MinFromTime(lorutime);
                }
                double sec;
                if (maxargs >= 2 && ix < numNums) {
                    sec = nums[ix++];
                } else {
                    sec = (double) SecFromTime(lorutime);
                }
                double msec;
                if (maxargs >= 1 && ix < numNums) {
                    msec = nums[ix++];
                } else {
                    msec = (double) msFromTime(lorutime);
                }
                double time = MakeTime(hour, min, sec, msec);
                double result = MakeDate(Day(lorutime), time);
                if (local) {
                    result = internalUTC(result);
                }
                return TimeClip(result);
            } else {
                return Double.NaN;
            }
        }
    }

    private static double makeDate(double date, Object[] args, int methodId, Context cx) {
        if (args.length == 0) {
            return Double.NaN;
        } else {
            boolean local = true;
            int maxargs;
            switch(methodId) {
                case 40:
                    local = false;
                case 39:
                    maxargs = 1;
                    break;
                case 42:
                    local = false;
                case 41:
                    maxargs = 2;
                    break;
                case 44:
                    local = false;
                case 43:
                    maxargs = 3;
                    break;
                default:
                    throw Kit.codeBug();
            }
            boolean hasNaN = false;
            int numNums = args.length < maxargs ? args.length : maxargs;
            assert 1 <= numNums && numNums <= 3;
            double[] nums = new double[3];
            for (int i = 0; i < numNums; i++) {
                double d = ScriptRuntime.toNumber(cx, args[i]);
                if (!Double.isNaN(d) && !Double.isInfinite(d)) {
                    nums[i] = ScriptRuntime.toInteger(d);
                } else {
                    hasNaN = true;
                }
            }
            if (hasNaN) {
                return Double.NaN;
            } else {
                int ix = 0;
                double lorutime;
                if (Double.isNaN(date)) {
                    if (maxargs < 3) {
                        return Double.NaN;
                    }
                    lorutime = 0.0;
                } else if (local) {
                    lorutime = LocalTime(date);
                } else {
                    lorutime = date;
                }
                double year;
                if (maxargs >= 3 && ix < numNums) {
                    year = nums[ix++];
                } else {
                    year = (double) YearFromTime(lorutime);
                }
                double month;
                if (maxargs >= 2 && ix < numNums) {
                    month = nums[ix++];
                } else {
                    month = (double) MonthFromTime(lorutime);
                }
                double day;
                if (maxargs >= 1 && ix < numNums) {
                    day = nums[ix++];
                } else {
                    day = (double) DateFromTime(lorutime);
                }
                day = MakeDay(year, month, day);
                double result = MakeDate(day, TimeWithinDay(lorutime));
                if (local) {
                    result = internalUTC(result);
                }
                return TimeClip(result);
            }
        }
    }

    private NativeDate() {
    }

    @Override
    public String getClassName() {
        return "Date";
    }

    @Override
    public Object getDefaultValue(Context cx, Class<?> typeHint) {
        if (typeHint == null) {
            typeHint = ScriptRuntime.StringClass;
        }
        return super.getDefaultValue(cx, typeHint);
    }

    double getJSTimeValue() {
        return this.date;
    }

    @Override
    protected void fillConstructorProperties(IdFunctionObject ctor, Context cx) {
        this.addIdFunctionProperty(ctor, DATE_TAG, -3, "now", 0, cx);
        this.addIdFunctionProperty(ctor, DATE_TAG, -2, "parse", 1, cx);
        this.addIdFunctionProperty(ctor, DATE_TAG, -1, "UTC", 7, cx);
        super.fillConstructorProperties(ctor, cx);
    }

    @Override
    protected void initPrototypeId(int id, Context cx) {
        String s;
        int arity;
        switch(id) {
            case 1:
                arity = 7;
                s = "constructor";
                break;
            case 2:
                arity = 0;
                s = "toString";
                break;
            case 3:
                arity = 0;
                s = "toTimeString";
                break;
            case 4:
                arity = 0;
                s = "toDateString";
                break;
            case 5:
                arity = 0;
                s = "toLocaleString";
                break;
            case 6:
                arity = 0;
                s = "toLocaleTimeString";
                break;
            case 7:
                arity = 0;
                s = "toLocaleDateString";
                break;
            case 8:
                arity = 0;
                s = "toUTCString";
                break;
            case 9:
                arity = 0;
                s = "toSource";
                break;
            case 10:
                arity = 0;
                s = "valueOf";
                break;
            case 11:
                arity = 0;
                s = "getTime";
                break;
            case 12:
                arity = 0;
                s = "getYear";
                break;
            case 13:
                arity = 0;
                s = "getFullYear";
                break;
            case 14:
                arity = 0;
                s = "getUTCFullYear";
                break;
            case 15:
                arity = 0;
                s = "getMonth";
                break;
            case 16:
                arity = 0;
                s = "getUTCMonth";
                break;
            case 17:
                arity = 0;
                s = "getDate";
                break;
            case 18:
                arity = 0;
                s = "getUTCDate";
                break;
            case 19:
                arity = 0;
                s = "getDay";
                break;
            case 20:
                arity = 0;
                s = "getUTCDay";
                break;
            case 21:
                arity = 0;
                s = "getHours";
                break;
            case 22:
                arity = 0;
                s = "getUTCHours";
                break;
            case 23:
                arity = 0;
                s = "getMinutes";
                break;
            case 24:
                arity = 0;
                s = "getUTCMinutes";
                break;
            case 25:
                arity = 0;
                s = "getSeconds";
                break;
            case 26:
                arity = 0;
                s = "getUTCSeconds";
                break;
            case 27:
                arity = 0;
                s = "getMilliseconds";
                break;
            case 28:
                arity = 0;
                s = "getUTCMilliseconds";
                break;
            case 29:
                arity = 0;
                s = "getTimezoneOffset";
                break;
            case 30:
                arity = 1;
                s = "setTime";
                break;
            case 31:
                arity = 1;
                s = "setMilliseconds";
                break;
            case 32:
                arity = 1;
                s = "setUTCMilliseconds";
                break;
            case 33:
                arity = 2;
                s = "setSeconds";
                break;
            case 34:
                arity = 2;
                s = "setUTCSeconds";
                break;
            case 35:
                arity = 3;
                s = "setMinutes";
                break;
            case 36:
                arity = 3;
                s = "setUTCMinutes";
                break;
            case 37:
                arity = 4;
                s = "setHours";
                break;
            case 38:
                arity = 4;
                s = "setUTCHours";
                break;
            case 39:
                arity = 1;
                s = "setDate";
                break;
            case 40:
                arity = 1;
                s = "setUTCDate";
                break;
            case 41:
                arity = 2;
                s = "setMonth";
                break;
            case 42:
                arity = 2;
                s = "setUTCMonth";
                break;
            case 43:
                arity = 3;
                s = "setFullYear";
                break;
            case 44:
                arity = 3;
                s = "setUTCFullYear";
                break;
            case 45:
                arity = 1;
                s = "setYear";
                break;
            case 46:
                arity = 0;
                s = "toISOString";
                break;
            case 47:
                arity = 1;
                s = "toJSON";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(id));
        }
        this.initPrototypeMethod(DATE_TAG, id, s, arity, cx);
    }

    @Override
    public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (!f.hasTag(DATE_TAG)) {
            return super.execIdCall(f, cx, scope, thisObj, args);
        } else {
            int id = f.methodId();
            switch(id) {
                case -3:
                    return ScriptRuntime.wrapNumber(now());
                case -2:
                    String dataStr = ScriptRuntime.toString(cx, args, 0);
                    return ScriptRuntime.wrapNumber(date_parseString(dataStr));
                case -1:
                    return ScriptRuntime.wrapNumber(jsStaticFunction_UTC(cx, args));
                case 1:
                    if (thisObj != null) {
                        return date_format(now(), 2);
                    }
                    return jsConstructor(args, cx);
                case 47:
                    String toISOString = "toISOString";
                    Scriptable o = ScriptRuntime.toObject(cx, scope, thisObj);
                    Object tv = ScriptRuntime.toPrimitive(cx, o, ScriptRuntime.NumberClass);
                    if (tv instanceof Number) {
                        double d = ((Number) tv).doubleValue();
                        if (Double.isNaN(d) || Double.isInfinite(d)) {
                            return null;
                        }
                    }
                    Object toISO = getProperty(o, "toISOString", cx);
                    if (toISO == NOT_FOUND) {
                        throw ScriptRuntime.typeError2(cx, "msg.function.not.found.in", "toISOString", ScriptRuntime.toString(cx, o));
                    } else if (!(toISO instanceof Callable)) {
                        throw ScriptRuntime.typeError3(cx, "msg.isnt.function.in", "toISOString", ScriptRuntime.toString(cx, o), ScriptRuntime.toString(cx, toISO));
                    } else {
                        Object result = ((Callable) toISO).call(cx, scope, o, ScriptRuntime.EMPTY_OBJECTS);
                        if (!ScriptRuntime.isPrimitive(result)) {
                            throw ScriptRuntime.typeError1(cx, "msg.toisostring.must.return.primitive", ScriptRuntime.toString(cx, result));
                        }
                        return result;
                    }
                default:
                    if (thisObj instanceof NativeDate realThis) {
                        double t = realThis.date;
                        switch(id) {
                            case 2:
                            case 3:
                            case 4:
                                if (!Double.isNaN(t)) {
                                    return date_format(t, id);
                                }
                                return "Invalid Date";
                            case 5:
                            case 6:
                            case 7:
                                if (!Double.isNaN(t)) {
                                    return toLocale_helper(t, id);
                                }
                                return "Invalid Date";
                            case 8:
                                if (!Double.isNaN(t)) {
                                    return js_toUTCString(t);
                                }
                                return "Invalid Date";
                            case 9:
                                return "not_supported";
                            case 10:
                            case 11:
                                return ScriptRuntime.wrapNumber(t);
                            case 12:
                            case 13:
                            case 14:
                                if (!Double.isNaN(t)) {
                                    if (id != 14) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) YearFromTime(t);
                                    if (id == 12) {
                                        t -= 1900.0;
                                    }
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 15:
                            case 16:
                                if (!Double.isNaN(t)) {
                                    if (id == 15) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) MonthFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 17:
                            case 18:
                                if (!Double.isNaN(t)) {
                                    if (id == 17) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) DateFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 19:
                            case 20:
                                if (!Double.isNaN(t)) {
                                    if (id == 19) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) WeekDay(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 21:
                            case 22:
                                if (!Double.isNaN(t)) {
                                    if (id == 21) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) HourFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 23:
                            case 24:
                                if (!Double.isNaN(t)) {
                                    if (id == 23) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) MinFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 25:
                            case 26:
                                if (!Double.isNaN(t)) {
                                    if (id == 25) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) SecFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 27:
                            case 28:
                                if (!Double.isNaN(t)) {
                                    if (id == 27) {
                                        t = LocalTime(t);
                                    }
                                    t = (double) msFromTime(t);
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 29:
                                if (!Double.isNaN(t)) {
                                    t = (t - LocalTime(t)) / 60000.0;
                                }
                                return ScriptRuntime.wrapNumber(t);
                            case 30:
                                t = TimeClip(ScriptRuntime.toNumber(cx, args, 0));
                                realThis.date = t;
                                return ScriptRuntime.wrapNumber(t);
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                                t = makeTime(t, args, id, cx);
                                realThis.date = t;
                                return ScriptRuntime.wrapNumber(t);
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                                t = makeDate(t, args, id, cx);
                                realThis.date = t;
                                return ScriptRuntime.wrapNumber(t);
                            case 45:
                                double year = ScriptRuntime.toNumber(cx, args, 0);
                                if (!Double.isNaN(year) && !Double.isInfinite(year)) {
                                    if (Double.isNaN(t)) {
                                        t = 0.0;
                                    } else {
                                        t = LocalTime(t);
                                    }
                                    if (year >= 0.0 && year <= 99.0) {
                                        year += 1900.0;
                                    }
                                    double day = MakeDay(year, (double) MonthFromTime(t), (double) DateFromTime(t));
                                    t = MakeDate(day, TimeWithinDay(t));
                                    t = internalUTC(t);
                                    t = TimeClip(t);
                                } else {
                                    t = Double.NaN;
                                }
                                realThis.date = t;
                                return ScriptRuntime.wrapNumber(t);
                            case 46:
                                if (!Double.isNaN(t)) {
                                    return js_toISOString(t);
                                }
                                String msg = ScriptRuntime.getMessage0("msg.invalid.date");
                                throw ScriptRuntime.rangeError(cx, msg);
                            default:
                                throw new IllegalArgumentException(String.valueOf(id));
                        }
                    } else {
                        throw incompatibleCallError(f, cx);
                    }
            }
        }
    }

    @Override
    protected int findPrototypeId(String s) {
        return switch(s) {
            case "constructor" ->
                1;
            case "toString" ->
                2;
            case "toTimeString" ->
                3;
            case "toDateString" ->
                4;
            case "toLocaleString" ->
                5;
            case "toLocaleTimeString" ->
                6;
            case "toLocaleDateString" ->
                7;
            case "toUTCString" ->
                8;
            case "toSource" ->
                9;
            case "valueOf" ->
                10;
            case "getTime" ->
                11;
            case "getYear" ->
                12;
            case "getFullYear" ->
                13;
            case "getUTCFullYear" ->
                14;
            case "getMonth" ->
                15;
            case "getUTCMonth" ->
                16;
            case "getDate" ->
                17;
            case "getUTCDate" ->
                18;
            case "getDay" ->
                19;
            case "getUTCDay" ->
                20;
            case "getHours" ->
                21;
            case "getUTCHours" ->
                22;
            case "getMinutes" ->
                23;
            case "getUTCMinutes" ->
                24;
            case "getSeconds" ->
                25;
            case "getUTCSeconds" ->
                26;
            case "getMilliseconds" ->
                27;
            case "getUTCMilliseconds" ->
                28;
            case "getTimezoneOffset" ->
                29;
            case "setTime" ->
                30;
            case "setMilliseconds" ->
                31;
            case "setUTCMilliseconds" ->
                32;
            case "setSeconds" ->
                33;
            case "setUTCSeconds" ->
                34;
            case "setMinutes" ->
                35;
            case "setUTCMinutes" ->
                36;
            case "setHours" ->
                37;
            case "setUTCHours" ->
                38;
            case "setDate" ->
                39;
            case "setUTCDate" ->
                40;
            case "setMonth" ->
                41;
            case "setUTCMonth" ->
                42;
            case "setFullYear" ->
                43;
            case "setUTCFullYear" ->
                44;
            case "setYear" ->
                45;
            case "toISOString" ->
                46;
            case "toJSON" ->
                47;
            default ->
                0;
        };
    }
}