package de.keksuccino.konkrete.json.minidev.asm;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeMap;

public class ConvertDate {

    static TreeMap<String, Integer> monthsTable = new TreeMap(new ConvertDate.StringCmpNS());

    static TreeMap<String, Integer> daysTable = new TreeMap(new ConvertDate.StringCmpNS());

    private static HashSet<String> voidData = new HashSet();

    public static TimeZone defaultTimeZone;

    static TreeMap<String, TimeZone> timeZoneMapping = new TreeMap();

    public static Integer getMonth(String month) {
        return (Integer) monthsTable.get(month);
    }

    private static Integer parseMonth(String s1) {
        if (Character.isDigit(s1.charAt(0))) {
            return Integer.parseInt(s1) - 1;
        } else {
            Integer month = (Integer) monthsTable.get(s1);
            if (month == null) {
                throw new NullPointerException("can not parse " + s1 + " as month");
            } else {
                return month;
            }
        }
    }

    private static GregorianCalendar newCalandar() {
        GregorianCalendar cal = new GregorianCalendar(2000, 0, 0, 0, 0, 0);
        if (defaultTimeZone != null) {
            cal.setTimeZone(defaultTimeZone);
        }
        TimeZone TZ = cal.getTimeZone();
        if (TZ == null) {
            TZ = TimeZone.getDefault();
        }
        cal.setTimeInMillis((long) (-TZ.getRawOffset()));
        return cal;
    }

    private static void fillMap(TreeMap<String, Integer> map, String key, Integer value) {
        map.put(key, value);
        key = key.replace("é", "e");
        key = key.replace("û", "u");
        map.put(key, value);
    }

    public static Date convertToDate(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof Number) {
            return new Date(((Number) obj).longValue());
        } else if (obj instanceof String) {
            obj = ((String) obj).replace("p.m.", "pm").replace("a.m.", "am");
            StringTokenizer st = new StringTokenizer((String) obj, " -/:,.+年月日曜時分秒");
            String s1 = "";
            if (!st.hasMoreTokens()) {
                return null;
            } else {
                s1 = st.nextToken();
                if (s1.length() == 4 && Character.isDigit(s1.charAt(0))) {
                    return getYYYYMMDD(st, s1);
                } else {
                    if (daysTable.containsKey(s1)) {
                        if (!st.hasMoreTokens()) {
                            return null;
                        }
                        s1 = st.nextToken();
                    }
                    if (monthsTable.containsKey(s1)) {
                        return getMMDDYYYY(st, s1);
                    } else {
                        return Character.isDigit(s1.charAt(0)) ? getDDMMYYYY(st, s1) : null;
                    }
                }
            }
        } else {
            throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
        }
    }

    private static Date getYYYYMMDD(StringTokenizer st, String s1) {
        GregorianCalendar cal = newCalandar();
        int year = Integer.parseInt(s1);
        cal.set(1, year);
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        } else {
            s1 = st.nextToken();
            cal.set(2, parseMonth(s1));
            if (!st.hasMoreTokens()) {
                return cal.getTime();
            } else {
                s1 = st.nextToken();
                if (Character.isDigit(s1.charAt(0))) {
                    if (s1.length() == 5 && s1.charAt(2) == 'T') {
                        int day = Integer.parseInt(s1.substring(0, 2));
                        cal.set(5, day);
                        return addHour(st, cal, s1.substring(3));
                    } else {
                        int day = Integer.parseInt(s1);
                        cal.set(5, day);
                        return addHour(st, cal, null);
                    }
                } else {
                    return cal.getTime();
                }
            }
        }
    }

    private static int getYear(String s1) {
        int year = Integer.parseInt(s1);
        if (year < 100) {
            if (year > 30) {
                year += 2000;
            } else {
                year += 1900;
            }
        }
        return year;
    }

    private static Date getMMDDYYYY(StringTokenizer st, String s1) {
        GregorianCalendar cal = newCalandar();
        Integer month = (Integer) monthsTable.get(s1);
        if (month == null) {
            throw new NullPointerException("can not parse " + s1 + " as month");
        } else {
            cal.set(2, month);
            if (!st.hasMoreTokens()) {
                return null;
            } else {
                s1 = st.nextToken();
                int day = Integer.parseInt(s1);
                cal.set(5, day);
                if (!st.hasMoreTokens()) {
                    return null;
                } else {
                    s1 = st.nextToken();
                    if (Character.isLetter(s1.charAt(0))) {
                        if (!st.hasMoreTokens()) {
                            return null;
                        }
                        s1 = st.nextToken();
                    }
                    if (s1.length() == 4) {
                        cal.set(1, getYear(s1));
                    } else if (s1.length() == 2) {
                        return addHour2(st, cal, s1);
                    }
                    return addHour(st, cal, null);
                }
            }
        }
    }

    private static Date getDDMMYYYY(StringTokenizer st, String s1) {
        GregorianCalendar cal = newCalandar();
        int day = Integer.parseInt(s1);
        cal.set(5, day);
        if (!st.hasMoreTokens()) {
            return null;
        } else {
            s1 = st.nextToken();
            cal.set(2, parseMonth(s1));
            if (!st.hasMoreTokens()) {
                return null;
            } else {
                s1 = st.nextToken();
                cal.set(1, getYear(s1));
                return addHour(st, cal, null);
            }
        }
    }

    private static Date addHour(StringTokenizer st, Calendar cal, String s1) {
        if (s1 == null) {
            if (!st.hasMoreTokens()) {
                return cal.getTime();
            }
            s1 = st.nextToken();
        }
        return addHour2(st, cal, s1);
    }

    private static Date addHour2(StringTokenizer st, Calendar cal, String s1) {
        s1 = trySkip(st, s1, cal);
        cal.set(11, Integer.parseInt(s1));
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        } else {
            s1 = st.nextToken();
            s1 = trySkip(st, s1, cal);
            if (s1 == null) {
                return cal.getTime();
            } else {
                cal.set(12, Integer.parseInt(s1));
                if (!st.hasMoreTokens()) {
                    return cal.getTime();
                } else {
                    s1 = st.nextToken();
                    s1 = trySkip(st, s1, cal);
                    if (s1 == null) {
                        return cal.getTime();
                    } else {
                        cal.set(13, Integer.parseInt(s1));
                        if (!st.hasMoreTokens()) {
                            return cal.getTime();
                        } else {
                            s1 = st.nextToken();
                            s1 = trySkip(st, s1, cal);
                            if (s1 == null) {
                                return cal.getTime();
                            } else {
                                s1 = trySkip(st, s1, cal);
                                if (s1.length() == 4 && Character.isDigit(s1.charAt(0))) {
                                    cal.set(1, getYear(s1));
                                }
                                return cal.getTime();
                            }
                        }
                    }
                }
            }
        }
    }

    private static String trySkip(StringTokenizer st, String s1, Calendar cal) {
        while (true) {
            TimeZone tz = (TimeZone) timeZoneMapping.get(s1);
            if (tz != null) {
                cal.setTimeZone(tz);
                if (!st.hasMoreTokens()) {
                    return null;
                }
                s1 = st.nextToken();
            } else {
                if (!voidData.contains(s1)) {
                    return s1;
                }
                if (s1.equalsIgnoreCase("pm")) {
                    cal.add(9, 1);
                }
                if (s1.equalsIgnoreCase("am")) {
                    cal.add(9, 0);
                }
                if (!st.hasMoreTokens()) {
                    return null;
                }
                s1 = st.nextToken();
            }
        }
    }

    static {
        voidData.add("à");
        voidData.add("at");
        voidData.add("MEZ");
        voidData.add("Uhr");
        voidData.add("h");
        voidData.add("pm");
        voidData.add("PM");
        voidData.add("am");
        voidData.add("AM");
        voidData.add("min");
        voidData.add("um");
        voidData.add("o'clock");
        for (String tz : TimeZone.getAvailableIDs()) {
            timeZoneMapping.put(tz, TimeZone.getTimeZone(tz));
        }
        for (Locale locale : DateFormatSymbols.getAvailableLocales()) {
            if (!"ja".equals(locale.getLanguage()) && !"ko".equals(locale.getLanguage()) && !"zh".equals(locale.getLanguage())) {
                DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
                String[] keys = dfs.getMonths();
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i].length() != 0) {
                        fillMap(monthsTable, keys[i], i);
                    }
                }
                keys = dfs.getShortMonths();
                for (int ix = 0; ix < keys.length; ix++) {
                    String s = keys[ix];
                    if (s.length() != 0 && !Character.isDigit(s.charAt(s.length() - 1))) {
                        fillMap(monthsTable, keys[ix], ix);
                        fillMap(monthsTable, keys[ix].replace(".", ""), ix);
                    }
                }
                keys = dfs.getWeekdays();
                for (int ixx = 0; ixx < keys.length; ixx++) {
                    String s = keys[ixx];
                    if (s.length() != 0) {
                        fillMap(daysTable, s, ixx);
                        fillMap(daysTable, s.replace(".", ""), ixx);
                    }
                }
                keys = dfs.getShortWeekdays();
                for (int ixxx = 0; ixxx < keys.length; ixxx++) {
                    String s = keys[ixxx];
                    if (s.length() != 0) {
                        fillMap(daysTable, s, ixxx);
                        fillMap(daysTable, s.replace(".", ""), ixxx);
                    }
                }
            }
        }
    }

    public static class StringCmpNS implements Comparator<String> {

        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    }
}