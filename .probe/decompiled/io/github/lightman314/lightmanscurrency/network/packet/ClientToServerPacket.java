package io.github.lightman314.lightmanscurrency.network.packet;

import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;

public abstract class ClientToServerPacket extends CustomPacket {

    public final void send() {
        LightmansCurrencyPacketHandler.instance.sendToServer(this);
    }

    public static class Simple extends ClientToServerPacket {

        @Override
        public void encode(@Nonnull FriendlyByteBuf buffer) {
        }
    }
}