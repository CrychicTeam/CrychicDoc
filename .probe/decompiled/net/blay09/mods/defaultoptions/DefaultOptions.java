package net.blay09.mods.defaultoptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.defaultoptions.api.DefaultOptionsCategory;
import net.blay09.mods.defaultoptions.api.DefaultOptionsHandler;
import net.blay09.mods.defaultoptions.command.DefaultOptionsCommand;
import net.blay09.mods.defaultoptions.config.DefaultOptionsConfig;
import net.blay09.mods.defaultoptions.difficulty.DefaultDifficultyHandler;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultOptions {

    public static final String MOD_ID = "defaultoptions";

    public static final Logger logger = LogManager.getLogger("defaultoptions");

    private static final List<DefaultOptionsHandler> defaultOptionsHandlers = new ArrayList();

    public static void initialize() {
        DefaultOptionsConfig.initialize();
        Balm.getCommands().register(DefaultOptionsCommand::register);
        Balm.getEvents().onEvent(ClientStartedEvent.class, DefaultOptionsInitializer::postLoad);
        DefaultDifficultyHandler.initialize();
    }

    public static void saveDefaultOptions(DefaultOptionsCategory category) throws DefaultOptionsHandlerException {
        for (DefaultOptionsHandler handler : defaultOptionsHandlers) {
            if (handler.getCategory() == category) {
                handler.saveCurrentOptionsAsDefault();
            }
        }
    }

    public static File getDefaultOptionsFolder() {
        File defaultOptions = new File(getMinecraftDataDir(), "config/defaultoptions");
        if (!defaultOptions.exists() && !defaultOptions.mkdirs()) {
            throw new IllegalStateException("Could not create default options directory.");
        } else {
            return defaultOptions;
        }
    }

    public static File getMinecraftDataDir() {
        return Minecraft.getInstance().gameDirectory;
    }

    public static void addDefaultOptionsHandler(DefaultOptionsHandler handler) {
        defaultOptionsHandlers.add(handler);
    }

    public static List<DefaultOptionsHandler> getDefaultOptionsHandlers() {
        return defaultOptionsHandlers;
    }
}