package net.mehvahdjukaar.supplementaries.common.network;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TrappedPresentBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ServerBoundSetTrappedPresentPacket implements Message {

    private final BlockPos pos;

    private final boolean packed;

    public ServerBoundSetTrappedPresentPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.packed = buf.readBoolean();
    }

    public ServerBoundSetTrappedPresentPacket(BlockPos pos, boolean packed) {
        this.pos = pos;
        this.packed = packed;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(this.packed);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ServerPlayer player = (ServerPlayer) Objects.requireNonNull(context.getSender());
        Level level = player.m_9236_();
        if (level.m_46805_(this.pos) && level.getBlockEntity(this.pos) instanceof TrappedPresentBlockTile present) {
            present.updateState(this.packed);
            BlockState state = level.getBlockState(this.pos);
            present.m_6596_();
            level.sendBlockUpdated(this.pos, state, state, 3);
            if (this.packed) {
                player.doCloseContainer();
            }
        }
    }
}