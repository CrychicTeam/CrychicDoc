package io.github.lightman314.lightmanscurrency.network.message.data.team;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketClearClientTeams extends ServerToClientPacket.Simple {

    public static final SPacketClearClientTeams INSTANCE = new SPacketClearClientTeams();

    public static final CustomPacket.Handler<SPacketClearClientTeams> HANDLER = new SPacketClearClientTeams.H();

    private SPacketClearClientTeams() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketClearClientTeams> {

        protected H() {
            super(SPacketClearClientTeams.INSTANCE);
        }

        protected void handle(@Nonnull SPacketClearClientTeams message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.clearTeams();
        }
    }
}