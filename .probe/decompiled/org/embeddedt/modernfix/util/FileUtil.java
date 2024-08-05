package org.embeddedt.modernfix.util;

import java.io.File;

public class FileUtil {

    public static File childFile(File file) {
        file.getParentFile().mkdirs();
        return file;
    }

    public static String normalize(String path) {
        char prevChar = 0;
        StringBuilder sb = null;
        for (int i = 0; i < path.length(); i++) {
            char thisChar = path.charAt(i);
            if (prevChar == '/' && thisChar == prevChar) {
                if (sb == null) {
                    sb = new StringBuilder(path.length());
                    sb.append(path, 0, i);
                }
            } else if (sb != null) {
                sb.append(thisChar);
            }
            prevChar = thisChar;
        }
        return sb == null ? path : sb.toString();
    }
}