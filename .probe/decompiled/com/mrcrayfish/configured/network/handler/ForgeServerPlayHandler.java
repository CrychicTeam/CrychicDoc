package com.mrcrayfish.configured.network.handler;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.google.common.base.Joiner;
import com.mrcrayfish.configured.Constants;
import com.mrcrayfish.configured.network.ForgeNetwork;
import com.mrcrayfish.configured.network.ServerPlayHelper;
import com.mrcrayfish.configured.network.message.play.MessageSyncForgeConfig;
import com.mrcrayfish.configured.util.ForgeConfigHelper;
import java.io.ByteArrayInputStream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.PacketDistributor;

public class ForgeServerPlayHandler {

    private static final Joiner DOT_JOINER = Joiner.on(".");

    public static void handleSyncServerConfigMessage(ServerPlayer player, MessageSyncForgeConfig message) {
        if (ServerPlayHelper.canEditServerConfigs(player)) {
            Constants.LOG.debug("Received server config sync from player: {}", player.m_7755_().getString());
            ModConfig config = ForgeConfigHelper.getForgeConfig(message.fileName());
            if (config == null) {
                Constants.LOG.warn("{} tried to update a config that doesn't exist!", player.m_7755_().getString());
                player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.bad_config_packet"));
            } else if (config.getType() != Type.SERVER) {
                Constants.LOG.warn("{} tried to update a forge config that isn't a server type", player.m_7755_().getString());
                player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.bad_config_packet"));
            } else {
                ForgeConfigSpec spec = ForgeConfigHelper.findConfigSpec(config.getSpec());
                if (spec == null) {
                    Constants.LOG.warn("Unable to process server config update due to unknown spec for config: {}", message.fileName());
                    player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.bad_config_packet"));
                } else {
                    try {
                        CommentedConfig data = (CommentedConfig) TomlFormat.instance().createParser().parse(new ByteArrayInputStream(message.data()));
                        int result = spec.correct(data, (action, path, incorrectValue, correctedValue) -> Constants.LOG.warn("Incorrect key {} was corrected from {} to its default, {}. {}", new Object[] { DOT_JOINER.join(path), incorrectValue, correctedValue, incorrectValue == correctedValue ? "This seems to be an error." : "" }), (action, path, incorrectValue, correctedValue) -> Constants.LOG.debug("The comment on key {} does not match the spec. This may create a backup.", DOT_JOINER.join(path)));
                        if (result != 0) {
                            Constants.LOG.info("Config data sent from {} needed to be corrected", player.m_7755_().getString());
                        }
                        config.getConfigData().putAll(data);
                    } catch (ParsingException var6) {
                        Constants.LOG.warn("{} sent malformed config data to the server", player.m_7755_().getString());
                        player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.invalid_config_packet"));
                        ServerPlayHelper.sendMessageToOperators(Component.translatable("configured.chat.malformed_config_data", player.m_7755_(), Component.literal(config.getFileName()).withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.RED), player);
                        return;
                    } catch (Exception var7) {
                        var7.printStackTrace();
                        return;
                    }
                    Constants.LOG.debug("Successfully processed config update for '" + message.fileName() + "'");
                    ForgeNetwork.getPlay().send(PacketDistributor.ALL.noArg(), new MessageSyncForgeConfig(message.fileName(), message.data()));
                    ServerPlayHelper.sendMessageToOperators(Component.translatable("configured.chat.config_updated", player.m_7755_(), config.getFileName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), player);
                }
            }
        }
    }
}