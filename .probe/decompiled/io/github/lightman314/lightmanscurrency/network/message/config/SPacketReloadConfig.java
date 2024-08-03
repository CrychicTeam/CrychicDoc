package io.github.lightman314.lightmanscurrency.network.message.config;

import io.github.lightman314.lightmanscurrency.api.config.ConfigFile;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketReloadConfig extends ServerToClientPacket.Simple {

    public static final SPacketReloadConfig INSTANCE = new SPacketReloadConfig();

    public static final CustomPacket.Handler<SPacketReloadConfig> HANDLER = new SPacketReloadConfig.H();

    private SPacketReloadConfig() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketReloadConfig> {

        protected H() {
            super(SPacketReloadConfig.INSTANCE);
        }

        protected void handle(@Nonnull SPacketReloadConfig message, @Nullable ServerPlayer sender) {
            ConfigFile.reloadClientFiles();
        }
    }
}