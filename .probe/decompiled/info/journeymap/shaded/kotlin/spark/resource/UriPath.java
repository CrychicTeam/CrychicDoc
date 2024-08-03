package info.journeymap.shaded.kotlin.spark.resource;

public class UriPath {

    public static String canonical(String path) {
        if (path != null && path.length() != 0) {
            int end = path.length();
            int start;
            label183: for (start = path.lastIndexOf(47, end); end > 0; start = path.lastIndexOf(47, start - 1)) {
                switch(end - start) {
                    case 2:
                        if (path.charAt(start + 1) == '.') {
                            break label183;
                        }
                        break;
                    case 3:
                        if (path.charAt(start + 1) == '.' && path.charAt(start + 2) == '.') {
                            break label183;
                        }
                }
                end = start;
            }
            if (start >= end) {
                return path;
            } else {
                StringBuilder buf = new StringBuilder(path);
                int delStart = -1;
                int delEnd = -1;
                int skip = 0;
                while (end > 0) {
                    switch(end - start) {
                        case 2:
                            if (buf.charAt(start + 1) != '.') {
                                if (skip > 0) {
                                    if (--skip == 0) {
                                        delStart = start >= 0 ? start : 0;
                                        if (delStart > 0 && delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                                            delStart++;
                                        }
                                    }
                                }
                            } else {
                                if (start < 0 && buf.length() > 2 && buf.charAt(1) == '/' && buf.charAt(2) == '/') {
                                    break;
                                }
                                if (delEnd < 0) {
                                    delEnd = end;
                                }
                                delStart = start;
                                if (start >= 0 && (start != 0 || buf.charAt(start) != '/')) {
                                    if (end == buf.length()) {
                                        delStart = start + 1;
                                    }
                                    end = start--;
                                    while (start >= 0 && buf.charAt(start) != '/') {
                                        start--;
                                    }
                                    continue;
                                }
                                delStart = start + 1;
                                if (delEnd < buf.length() && buf.charAt(delEnd) == '/') {
                                    delEnd++;
                                }
                            }
                            break;
                        case 3:
                            if (buf.charAt(start + 1) == '.' && buf.charAt(start + 2) == '.') {
                                delStart = start;
                                if (delEnd < 0) {
                                    delEnd = end;
                                }
                                skip++;
                                end = start--;
                                while (start >= 0 && buf.charAt(start) != '/') {
                                    start--;
                                }
                                continue;
                            }
                            if (skip > 0) {
                                if (--skip == 0) {
                                    delStart = start >= 0 ? start : 0;
                                    if (delStart > 0 && delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                                        delStart++;
                                    }
                                }
                            }
                            break;
                        default:
                            if (skip > 0) {
                                if (--skip == 0) {
                                    delStart = start >= 0 ? start : 0;
                                    if (delEnd == buf.length() && buf.charAt(delEnd - 1) == '.') {
                                        delStart++;
                                    }
                                }
                            }
                    }
                    if (skip <= 0 && delStart >= 0 && delEnd >= delStart) {
                        buf.delete(delStart, delEnd);
                        delEnd = -1;
                        delStart = -1;
                        if (skip > 0) {
                            delEnd = end;
                        }
                    }
                    end = start--;
                    while (start >= 0 && buf.charAt(start) != '/') {
                        start--;
                    }
                }
                if (skip > 0) {
                    return null;
                } else {
                    if (delEnd >= 0) {
                        buf.delete(delStart, delEnd);
                    }
                    return buf.toString();
                }
            }
        } else {
            return path;
        }
    }
}