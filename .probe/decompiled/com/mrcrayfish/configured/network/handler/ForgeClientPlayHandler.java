package com.mrcrayfish.configured.network.handler;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.mrcrayfish.configured.Constants;
import com.mrcrayfish.configured.network.message.play.MessageSyncForgeConfig;
import com.mrcrayfish.configured.util.ForgeConfigHelper;
import java.io.ByteArrayInputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class ForgeClientPlayHandler {

    public static void handleSyncServerConfigMessage(Connection connection, MessageSyncForgeConfig message) {
        if (!Minecraft.getInstance().isLocalServer()) {
            Constants.LOG.info("Received forge config sync from server");
            ModConfig config = ForgeConfigHelper.getForgeConfig(message.fileName());
            if (config == null) {
                Constants.LOG.error("Server sent data for a forge config that doesn't exist: {}", message.fileName());
                connection.disconnect(Component.translatable("configured.multiplayer.disconnect.process_config"));
            } else if (config.getType() != Type.SERVER) {
                Constants.LOG.error("Server sent data for a config that isn't a server type: {}", message.fileName());
                connection.disconnect(Component.translatable("configured.multiplayer.disconnect.process_config"));
            } else {
                try {
                    CommentedConfig data = (CommentedConfig) TomlFormat.instance().createParser().parse(new ByteArrayInputStream(message.data()));
                    if (!config.getSpec().isCorrect(data)) {
                        Constants.LOG.error("Server sent an incorrect config: {}", message.fileName());
                        connection.disconnect(Component.translatable("configured.multiplayer.disconnect.process_config"));
                    } else {
                        config.getSpec().acceptConfig(data);
                        ForgeConfigHelper.fireForgeConfigEvent(config, new ModConfigEvent.Reloading(config));
                    }
                } catch (ParsingException var4) {
                    throw new RuntimeException(var4);
                }
            }
        }
    }
}