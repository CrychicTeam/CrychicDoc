package io.github.lightman314.lightmanscurrency.network.message.tax;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncClientTax extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncClientTax> HANDLER = new SPacketSyncClientTax.H();

    private final CompoundTag updateTag;

    public SPacketSyncClientTax(CompoundTag updateTag) {
        this.updateTag = updateTag;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.updateTag);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncClientTax> {

        @Nonnull
        public SPacketSyncClientTax decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncClientTax(buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull SPacketSyncClientTax message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.updateTaxEntries(message.updateTag);
        }
    }
}