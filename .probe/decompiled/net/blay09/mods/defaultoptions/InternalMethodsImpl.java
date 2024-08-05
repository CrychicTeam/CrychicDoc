package net.blay09.mods.defaultoptions;

import java.io.File;
import net.blay09.mods.defaultoptions.api.DefaultOptionsHandler;
import net.blay09.mods.defaultoptions.api.InternalMethods;
import net.blay09.mods.defaultoptions.api.SimpleDefaultOptionsHandler;

public class InternalMethodsImpl implements InternalMethods {

    @Override
    public SimpleDefaultOptionsHandler registerOptionsFile(File file) {
        SimpleDefaultOptionsFileHandler handler = new SimpleDefaultOptionsFileHandler(file);
        DefaultOptions.addDefaultOptionsHandler(handler);
        return handler;
    }

    @Override
    public void registerOptionsHandler(DefaultOptionsHandler handler) {
        DefaultOptions.addDefaultOptionsHandler(handler);
    }

    @Override
    public File getDefaultOptionsFolder() {
        return DefaultOptions.getDefaultOptionsFolder();
    }
}