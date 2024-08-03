package com.mrcrayfish.configured.api.util;

import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.screen.ConfigScreen;
import com.mrcrayfish.configured.client.screen.ModConfigSelectionScreen;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigScreenHelper {

    public static Screen createSelectionScreen(Screen parent, Component title, Map<ConfigType, Set<IModConfig>> configs, ResourceLocation background) {
        return new ModConfigSelectionScreen(parent, title, background, configs);
    }

    public static Screen createSelectionScreen(Component title, IModConfig config, ResourceLocation background) {
        return createSelectionScreen(Minecraft.getInstance().screen, title, config, background);
    }

    public static Screen createSelectionScreen(Screen parent, Component title, IModConfig config, ResourceLocation background) {
        return new ConfigScreen(parent, title, config, background);
    }
}