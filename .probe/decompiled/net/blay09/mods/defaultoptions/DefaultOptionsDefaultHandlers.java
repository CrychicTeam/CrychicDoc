package net.blay09.mods.defaultoptions;

import java.io.File;
import net.blay09.mods.defaultoptions.api.DefaultOptionsAPI;
import net.blay09.mods.defaultoptions.api.DefaultOptionsCategory;
import net.blay09.mods.defaultoptions.api.DefaultOptionsPlugin;
import net.blay09.mods.defaultoptions.keys.KeyMappingDefaultsHandler;
import net.minecraft.client.Minecraft;

public class DefaultOptionsDefaultHandlers implements DefaultOptionsPlugin {

    @Override
    public void initialize() {
        DefaultOptionsAPI.registerOptionsFile(new File(DefaultOptions.getMinecraftDataDir(), "options.txt")).withLinePredicate(line -> !line.startsWith("key_")).withSaveHandler(() -> Minecraft.getInstance().options.save());
        DefaultOptionsAPI.registerOptionsFile(new File(DefaultOptions.getMinecraftDataDir(), "servers.dat")).withCategory(DefaultOptionsCategory.SERVERS);
        DefaultOptionsAPI.registerOptionsFile(new File(DefaultOptions.getMinecraftDataDir(), "optionsof.txt")).withSaveHandler(() -> Minecraft.getInstance().options.save());
        DefaultOptionsAPI.registerOptionsFile(new File(DefaultOptions.getMinecraftDataDir(), "optionsviveprofiles.txt"));
        DefaultOptionsAPI.registerOptionsHandler(new KeyMappingDefaultsHandler());
        DefaultOptionsAPI.registerOptionsHandler(new ExtraDefaultOptionsHandler());
    }
}