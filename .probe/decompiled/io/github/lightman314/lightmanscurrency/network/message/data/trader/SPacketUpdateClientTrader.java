package io.github.lightman314.lightmanscurrency.network.message.data.trader;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketUpdateClientTrader extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketUpdateClientTrader> HANDLER = new SPacketUpdateClientTrader.H();

    CompoundTag traderData;

    public SPacketUpdateClientTrader(CompoundTag traderData) {
        this.traderData = traderData;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.traderData);
    }

    private static class H extends CustomPacket.Handler<SPacketUpdateClientTrader> {

        @Nonnull
        public SPacketUpdateClientTrader decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketUpdateClientTrader(buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull SPacketUpdateClientTrader message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.updateTrader(message.traderData);
        }
    }
}