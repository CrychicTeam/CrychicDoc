package com.mrcrayfish.configured.util;

public class OptiFineHelper {

    private static Boolean loaded = null;

    public static boolean isLoaded() {
        if (loaded == null) {
            try {
                Class.forName("optifine.Installer");
                loaded = true;
            } catch (ClassNotFoundException var1) {
                loaded = false;
            }
        }
        return loaded;
    }
}