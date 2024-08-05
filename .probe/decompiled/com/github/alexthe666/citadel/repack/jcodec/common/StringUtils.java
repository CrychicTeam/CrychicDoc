package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static final String[] zeroPad00 = new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09" };

    private static String[] splitWorker4(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            if (len == 0) {
                return new String[0];
            } else {
                List<String> list = new ArrayList();
                int sizePlus1 = 1;
                int i = 0;
                int start = 0;
                boolean match = false;
                boolean lastMatch = false;
                if (separatorChars == null) {
                    while (i < len) {
                        if (Character.isWhitespace(str.charAt(i))) {
                            if (match || preserveAllTokens) {
                                lastMatch = true;
                                if (sizePlus1++ == max) {
                                    i = len;
                                    lastMatch = false;
                                }
                                list.add(str.substring(start, i));
                                match = false;
                            }
                            start = ++i;
                        } else {
                            lastMatch = false;
                            match = true;
                            i++;
                        }
                    }
                } else if (separatorChars.length() == 1) {
                    char sep = separatorChars.charAt(0);
                    while (i < len) {
                        if (str.charAt(i) == sep) {
                            if (match || preserveAllTokens) {
                                lastMatch = true;
                                if (sizePlus1++ == max) {
                                    i = len;
                                    lastMatch = false;
                                }
                                list.add(str.substring(start, i));
                                match = false;
                            }
                            start = ++i;
                        } else {
                            lastMatch = false;
                            match = true;
                            i++;
                        }
                    }
                } else {
                    while (i < len) {
                        if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                            if (match || preserveAllTokens) {
                                lastMatch = true;
                                if (sizePlus1++ == max) {
                                    i = len;
                                    lastMatch = false;
                                }
                                list.add(str.substring(start, i));
                                match = false;
                            }
                            start = ++i;
                        } else {
                            lastMatch = false;
                            match = true;
                            i++;
                        }
                    }
                }
                if (match || preserveAllTokens && lastMatch) {
                    list.add(str.substring(start, i));
                }
                return (String[]) list.toArray(new String[list.size()]);
            }
        }
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        } else {
            int len = str.length();
            if (len == 0) {
                return new String[0];
            } else {
                List<String> list = new ArrayList();
                int i = 0;
                int start = 0;
                boolean match = false;
                boolean lastMatch = false;
                while (i < len) {
                    if (str.charAt(i) == separatorChar) {
                        if (match || preserveAllTokens) {
                            list.add(str.substring(start, i));
                            match = false;
                            lastMatch = true;
                        }
                        start = ++i;
                    } else {
                        lastMatch = false;
                        match = true;
                        i++;
                    }
                }
                if (match || preserveAllTokens && lastMatch) {
                    list.add(str.substring(start, i));
                }
                return (String[]) list.toArray(new String[list.size()]);
            }
        }
    }

    public static String[] split(String str) {
        return splitS3(str, null, -1);
    }

    public static String[] splitS(String str, String separatorChars) {
        return splitWorker4(str, separatorChars, -1, false);
    }

    public static String[] splitS3(String str, String separatorChars, int max) {
        return splitWorker4(str, separatorChars, max, false);
    }

    public static String[] splitC(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        } else {
            int i = 0;
            for (int isize = delimiters.length; i < isize; i++) {
                if (ch == delimiters[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String capitaliseAllWords(String str) {
        return capitalize(str);
    }

    public static String capitalize(String str) {
        return capitalizeD(str, null);
    }

    public static String capitalizeD(String str, char[] delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str != null && str.length() != 0 && delimLen != 0) {
            int strLen = str.length();
            StringBuilder buffer = new StringBuilder(strLen);
            boolean capitalizeNext = true;
            for (int i = 0; i < strLen; i++) {
                char ch = str.charAt(i);
                if (isDelimiter(ch, delimiters)) {
                    buffer.append(ch);
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer.append(Character.toTitleCase(ch));
                    capitalizeNext = false;
                } else {
                    buffer.append(ch);
                }
            }
            return buffer.toString();
        } else {
            return str;
        }
    }

    public static String join(Object[] array) {
        return joinS(array, null);
    }

    public static String join2(Object[] array, char separator) {
        return array == null ? null : join4(array, separator, 0, array.length);
    }

    public static String join4(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            int bufSize = endIndex - startIndex;
            if (bufSize <= 0) {
                return "";
            } else {
                bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1;
                StringBuilder buf = new StringBuilder(bufSize);
                for (int i = startIndex; i < endIndex; i++) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }
                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }
                return buf.toString();
            }
        }
    }

    public static String joinS(Object[] array, String separator) {
        return array == null ? null : joinS4(array, separator, 0, array.length);
    }

    public static String joinS4(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }
            int bufSize = endIndex - startIndex;
            if (bufSize <= 0) {
                return "";
            } else {
                bufSize *= (array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length();
                StringBuilder buf = new StringBuilder(bufSize);
                for (int i = startIndex; i < endIndex; i++) {
                    if (i > startIndex) {
                        buf.append(separator);
                    }
                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }
                return buf.toString();
            }
        }
    }

    public static String zeroPad2(int x) {
        return x >= 0 && x < 10 ? zeroPad00[x] : "" + x;
    }

    public static String zeroPad3(int n) {
        String s1 = zeroPad2(n);
        return s1.length() == 2 ? "0" + s1 : s1;
    }
}