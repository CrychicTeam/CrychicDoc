package net.mehvahdjukaar.moonlight.core.network;

import net.mehvahdjukaar.moonlight.api.block.IPistonMotionReact;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientBoundOnPistonMovedBlockPacket implements Message {

    public final BlockPos pos;

    private final Direction dir;

    private final BlockState movedState;

    private final boolean extending;

    public ClientBoundOnPistonMovedBlockPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.dir = Direction.from3DDataValue(buffer.readVarInt());
        this.movedState = buffer.readById(Block.BLOCK_STATE_REGISTRY);
        this.extending = buffer.readBoolean();
    }

    public ClientBoundOnPistonMovedBlockPacket(BlockPos pos, BlockState movedState, Direction direction, boolean extending) {
        this.pos = pos;
        this.movedState = movedState;
        this.dir = direction;
        this.extending = extending;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeVarInt(this.dir.get3DDataValue());
        buffer.writeId(Block.BLOCK_STATE_REGISTRY, this.movedState);
        buffer.writeBoolean(this.extending);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        handlePacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handlePacket(ClientBoundOnPistonMovedBlockPacket message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            level.m_7731_(message.pos, message.movedState, 0);
            if (message.movedState.m_60734_() instanceof IPistonMotionReact p) {
                p.onMoved(level, message.pos, message.movedState, message.dir, message.extending);
            }
        }
    }
}