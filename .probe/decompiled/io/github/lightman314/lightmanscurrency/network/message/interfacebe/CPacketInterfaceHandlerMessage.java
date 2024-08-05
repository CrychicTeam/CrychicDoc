package io.github.lightman314.lightmanscurrency.network.message.interfacebe;

import io.github.lightman314.lightmanscurrency.api.trader_interface.blockentity.TraderInterfaceBlockEntity;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketInterfaceHandlerMessage extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketInterfaceHandlerMessage> HANDLER = new CPacketInterfaceHandlerMessage.H();

    private static final int MAX_TYPE_LENGTH = 100;

    BlockPos pos;

    ResourceLocation type;

    CompoundTag updateInfo;

    public CPacketInterfaceHandlerMessage(BlockPos pos, ResourceLocation type, CompoundTag updateInfo) {
        this.pos = pos;
        this.type = type;
        this.updateInfo = updateInfo;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(this.type.toString(), 100);
        buffer.writeNbt(this.updateInfo);
    }

    private static class H extends CustomPacket.Handler<CPacketInterfaceHandlerMessage> {

        @Nonnull
        public CPacketInterfaceHandlerMessage decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketInterfaceHandlerMessage(buffer.readBlockPos(), new ResourceLocation(buffer.readUtf(100)), buffer.readAnySizeNbt());
        }

        protected void handle(@Nonnull CPacketInterfaceHandlerMessage message, @Nullable ServerPlayer sender) {
            if (sender != null && sender.m_9236_().getBlockEntity(message.pos) instanceof TraderInterfaceBlockEntity interfaceBE) {
                interfaceBE.receiveHandlerMessage(message.type, sender, message.updateInfo);
            }
        }
    }
}