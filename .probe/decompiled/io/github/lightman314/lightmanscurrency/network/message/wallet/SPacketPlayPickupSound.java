package io.github.lightman314.lightmanscurrency.network.message.wallet;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketPlayPickupSound extends ServerToClientPacket.Simple {

    public static final SPacketPlayPickupSound INSTANCE = new SPacketPlayPickupSound();

    public static final CustomPacket.Handler<SPacketPlayPickupSound> HANDLER = new SPacketPlayPickupSound.H();

    private SPacketPlayPickupSound() {
    }

    private static class H extends CustomPacket.SimpleHandler<SPacketPlayPickupSound> {

        protected H() {
            super(SPacketPlayPickupSound.INSTANCE);
        }

        protected void handle(@Nonnull SPacketPlayPickupSound message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.playCoinSound();
        }
    }
}