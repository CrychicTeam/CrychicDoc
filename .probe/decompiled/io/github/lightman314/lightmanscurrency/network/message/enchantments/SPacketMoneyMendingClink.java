package io.github.lightman314.lightmanscurrency.network.message.enchantments;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketMoneyMendingClink extends ServerToClientPacket.Simple {

    public static final CustomPacket.Handler<SPacketMoneyMendingClink> HANDLER = new SPacketMoneyMendingClink.H();

    public static final SPacketMoneyMendingClink INSTANCE = new SPacketMoneyMendingClink();

    private SPacketMoneyMendingClink() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketMoneyMendingClink> {

        protected H() {
            super(SPacketMoneyMendingClink.INSTANCE);
        }

        protected void handle(@Nonnull SPacketMoneyMendingClink message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.playCoinSound();
        }
    }
}