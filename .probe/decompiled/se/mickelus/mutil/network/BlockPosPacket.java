package se.mickelus.mutil.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public abstract class BlockPosPacket extends AbstractPacket {

    protected BlockPos pos;

    public BlockPosPacket() {
    }

    public BlockPosPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.pos.m_123341_());
        buffer.writeInt(this.pos.m_123342_());
        buffer.writeInt(this.pos.m_123343_());
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        this.pos = new BlockPos(x, y, z);
    }
}