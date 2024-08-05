package net.blay09.mods.defaultoptions.api;

import java.io.File;

public class DefaultOptionsAPI {

    public static InternalMethods __internalMethods;

    public static SimpleDefaultOptionsHandler registerOptionsFile(File file) {
        return __internalMethods.registerOptionsFile(file);
    }

    public static void registerOptionsHandler(DefaultOptionsHandler handler) {
        __internalMethods.registerOptionsHandler(handler);
    }

    public static File getDefaultOptionsFolder() {
        return __internalMethods.getDefaultOptionsFolder();
    }
}