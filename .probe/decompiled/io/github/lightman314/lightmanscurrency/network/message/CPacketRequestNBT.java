package io.github.lightman314.lightmanscurrency.network.message;

import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class CPacketRequestNBT extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketRequestNBT> HANDLER = new CPacketRequestNBT.H();

    private final BlockPos pos;

    public CPacketRequestNBT(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
    }

    private static class H extends CustomPacket.Handler<CPacketRequestNBT> {

        @Nonnull
        public CPacketRequestNBT decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketRequestNBT(buffer.readBlockPos());
        }

        protected void handle(@Nonnull CPacketRequestNBT message, @Nullable ServerPlayer sender) {
            if (sender != null) {
                BlockEntity blockEntity = sender.m_9236_().getBlockEntity(message.pos);
                if (blockEntity != null) {
                    BlockEntityUtil.sendUpdatePacket(blockEntity);
                }
            }
        }
    }
}