package io.github.lightman314.lightmanscurrency.network.message.tax;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketRemoveTax extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketRemoveTax> HANDLER = new SPacketRemoveTax.H();

    private final long id;

    public SPacketRemoveTax(long id) {
        this.id = id;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.id);
    }

    private static class H extends CustomPacket.Handler<SPacketRemoveTax> {

        @Nonnull
        public SPacketRemoveTax decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketRemoveTax(buffer.readLong());
        }

        protected void handle(@Nonnull SPacketRemoveTax message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.removeTaxEntry(message.id);
        }
    }
}