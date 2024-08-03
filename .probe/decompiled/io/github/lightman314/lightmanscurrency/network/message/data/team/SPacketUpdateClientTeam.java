package io.github.lightman314.lightmanscurrency.network.message.data.team;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketUpdateClientTeam extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketUpdateClientTeam> HANDLER = new SPacketUpdateClientTeam.H();

    CompoundTag traderData;

    public SPacketUpdateClientTeam(CompoundTag traderData) {
        this.traderData = traderData;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.traderData);
    }

    private static class H extends CustomPacket.Handler<SPacketUpdateClientTeam> {

        @Nonnull
        public SPacketUpdateClientTeam decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketUpdateClientTeam(buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull SPacketUpdateClientTeam message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.updateTeam(message.traderData);
        }
    }
}