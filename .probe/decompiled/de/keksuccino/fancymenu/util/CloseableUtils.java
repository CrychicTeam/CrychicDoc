package de.keksuccino.fancymenu.util;

import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

public class CloseableUtils {

    public static void closeQuietly(@Nullable AutoCloseable closeable) {
        if (closeable != null) {
            if (closeable instanceof Closeable c) {
                IOUtils.closeQuietly(c);
            } else {
                try {
                    closeable.close();
                } catch (Exception var2) {
                }
            }
        }
    }
}