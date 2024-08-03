package io.github.lightman314.lightmanscurrency.network.message.data.bank;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.network.packet.ServerToClientPacket;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class SPacketUpdateClientBank extends ServerToClientPacket {

    public static final CustomPacket.Handler<SPacketUpdateClientBank> HANDLER = new SPacketUpdateClientBank.H();

    UUID player;

    CompoundTag bankData;

    public SPacketUpdateClientBank(@Nonnull UUID player, @Nonnull CompoundTag bankData) {
        this.player = player;
        this.bankData = bankData;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeUUID(this.player);
        buffer.writeNbt(this.bankData);
    }

    private static class H extends CustomPacket.Handler<SPacketUpdateClientBank> {

        @Nonnull
        public SPacketUpdateClientBank decode(@Nonnull FriendlyByteBuf buffer) {
            return new SPacketUpdateClientBank(buffer.readUUID(), buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull SPacketUpdateClientBank message, @Nullable ServerPlayer sender) {
            LightmansCurrency.PROXY.updateBankAccount(message.player, message.bankData);
        }
    }
}