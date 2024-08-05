package com.mrcrayfish.configured.util;

import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.api.ModContext;
import com.mrcrayfish.configured.api.util.ConfigScreenHelper;
import com.mrcrayfish.configured.client.ClientHandler;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModContainer;

public class ForgeConfigScreenHelper {

    public static Screen createForgeConfigSelectionScreen(Component title, ModContainer mod, ResourceLocation background) {
        return createForgeConfigSelectionScreen(Minecraft.getInstance().screen, title, mod, background);
    }

    public static Screen createForgeConfigSelectionScreen(Screen parent, Component title, ModContainer mod, ResourceLocation background) {
        Map<ConfigType, Set<IModConfig>> configs = ClientHandler.createConfigMap(new ModContext(mod.getModId()));
        return ConfigScreenHelper.createSelectionScreen(parent, title, configs, background);
    }
}