package io.github.lightman314.lightmanscurrency.network.message.data.trader;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketClearClientTraders extends ServerToClientPacket.Simple {

    public static final SPacketClearClientTraders INSTANCE = new SPacketClearClientTraders();

    public static final CustomPacket.Handler<SPacketClearClientTraders> HANDLER = new SPacketClearClientTraders.H();

    private SPacketClearClientTraders() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketClearClientTraders> {

        protected H() {
            super(SPacketClearClientTraders.INSTANCE);
        }

        protected void handle(@Nonnull SPacketClearClientTraders message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.clearClientTraders();
        }
    }
}