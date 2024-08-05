package org.embeddedt.modernfix.util;

import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

public class CommonModUtil {

    public static void runWithoutCrash(CommonModUtil.SafeRunnable r, String errorMsg) {
        try {
            r.run();
        } catch (Throwable var3) {
            ModernFixMixinPlugin.instance.logger.error(errorMsg, var3);
        }
    }

    @FunctionalInterface
    public interface SafeRunnable {

        void run() throws Throwable;
    }
}