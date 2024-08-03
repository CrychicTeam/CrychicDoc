package com.mrcrayfish.configured.impl.framework.handler;

import com.mrcrayfish.configured.api.ConfigType;
import com.mrcrayfish.configured.api.IModConfig;
import com.mrcrayfish.configured.client.SessionData;
import com.mrcrayfish.configured.client.screen.RequestScreen;
import com.mrcrayfish.configured.impl.framework.FrameworkModConfig;
import com.mrcrayfish.configured.impl.framework.message.MessageFramework;
import com.mrcrayfish.configured.util.ConfigHelper;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class FrameworkClientHandler {

    public static void handleResponse(MessageFramework.Response message, Consumer<Component> disconnect) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof RequestScreen requestScreen) {
            Player player = minecraft.player;
            if (ConfigHelper.isOperator(player) && SessionData.isDeveloper(player)) {
                IModConfig pendingConfig = requestScreen.getActiveConfig();
                if (pendingConfig instanceof FrameworkModConfig frameworkConfig) {
                    if (!pendingConfig.getType().isServer() || pendingConfig.getType().isSync() || pendingConfig.getType() == ConfigType.DEDICATED_SERVER) {
                        requestScreen.handleResponse(null, Component.translatable("configured.gui.request.invalid_config_type"));
                    } else if (!frameworkConfig.loadDataFromResponse(message)) {
                        requestScreen.handleResponse(null, Component.translatable("configured.gui.request.process_error"));
                    } else {
                        requestScreen.handleResponse(frameworkConfig, null);
                    }
                } else {
                    requestScreen.handleResponse(null, Component.translatable("configured.gui.request.invalid_config"));
                }
            } else {
                requestScreen.handleResponse(null, Component.translatable("configured.gui.no_permission"));
            }
        }
    }
}