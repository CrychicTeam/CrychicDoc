package info.journeymap.shaded.kotlin.spark.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class StringUtils {

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
            StringBuilder sb = new StringBuilder();
            int pos = 0;
            int index = inString.indexOf(oldPattern);
            for (int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sb.append(inString.substring(pos, index));
                sb.append(newPattern);
                pos = index + patLen;
            }
            sb.append(inString.substring(pos));
            return sb.toString();
        } else {
            return inString;
        }
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (hasLength(inString) && hasLength(charsToDelete)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inString.length(); i++) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return inString;
        }
    }

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf("/");
            return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
        }
    }

    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith("/")) {
                newPath = newPath + "/";
            }
            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    public static String cleanPath(String path) {
        if (path == null) {
            return null;
        } else {
            String pathToUse = replace(path, "\\", "/");
            int prefixIndex = pathToUse.indexOf(":");
            String prefix = "";
            if (prefixIndex != -1) {
                prefix = pathToUse.substring(0, prefixIndex + 1);
                pathToUse = pathToUse.substring(prefixIndex + 1);
            }
            if (pathToUse.startsWith("/")) {
                prefix = prefix + "/";
                pathToUse = pathToUse.substring(1);
            }
            String[] pathArray = delimitedListToStringArray(pathToUse, "/");
            List<String> pathElements = new LinkedList();
            int tops = 0;
            for (int i = pathArray.length - 1; i >= 0; i--) {
                String element = pathArray[i];
                if (!".".equals(element)) {
                    if ("..".equals(element)) {
                        tops++;
                    } else if (tops > 0) {
                        tops--;
                    } else {
                        pathElements.add(0, element);
                    }
                }
            }
            for (int ix = 0; ix < tops; ix++) {
                pathElements.add(0, "..");
            }
            return prefix + collectionToDelimitedString(pathElements, "/");
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[]) collection.toArray(new String[collection.size()]);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        } else if (delimiter == null) {
            return new String[] { str };
        } else {
            List<String> result = new ArrayList();
            if ("".equals(delimiter)) {
                for (int i = 0; i < str.length(); i++) {
                    result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
                }
            } else {
                int pos = 0;
                int delPos;
                while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                    result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                    pos = delPos + delimiter.length();
                }
                if (str.length() > 0 && pos <= str.length()) {
                    result.add(deleteAny(str.substring(pos), charsToDelete));
                }
            }
            return toStringArray(result);
        }
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator<?> it = coll.iterator();
            while (it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }
            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String toString(byte[] bytes, String encoding) {
        String str;
        if (encoding != null && Charset.isSupported(encoding)) {
            try {
                str = new String(bytes, encoding);
            } catch (UnsupportedEncodingException var4) {
                str = new String(bytes);
            }
        } else {
            str = new String(bytes);
        }
        return str;
    }

    public static String removeLeadingAndTrailingSlashesFrom(String string) {
        String trimmed = string;
        if (string.endsWith("/") || string.endsWith("\\")) {
            trimmed = string.substring(0, string.length() - 1);
        }
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        return trimmed;
    }
}