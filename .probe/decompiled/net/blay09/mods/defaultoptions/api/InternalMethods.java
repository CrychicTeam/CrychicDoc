package net.blay09.mods.defaultoptions.api;

import java.io.File;

public interface InternalMethods {

    SimpleDefaultOptionsHandler registerOptionsFile(File var1);

    void registerOptionsHandler(DefaultOptionsHandler var1);

    File getDefaultOptionsFolder();
}