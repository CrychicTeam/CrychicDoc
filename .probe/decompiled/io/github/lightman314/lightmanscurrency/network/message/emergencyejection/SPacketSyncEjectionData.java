package io.github.lightman314.lightmanscurrency.network.message.emergencyejection;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SPacketSyncEjectionData extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketSyncEjectionData> HANDLER = new SPacketSyncEjectionData.H();

    final CompoundTag data;

    public SPacketSyncEjectionData(CompoundTag data) {
        this.data = data;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.data);
    }

    private static class H extends CustomPacket.Handler<SPacketSyncEjectionData> {

        @Nonnull
        public SPacketSyncEjectionData decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketSyncEjectionData(buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull SPacketSyncEjectionData message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.receiveEmergencyEjectionData(message.data);
        }
    }
}