package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.foundation.networking.BlockEntityDataPacket;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.tuple.Pair;

public class TunnelFlapPacket extends BlockEntityDataPacket<BeltTunnelBlockEntity> {

    private List<Pair<Direction, Boolean>> flaps;

    public TunnelFlapPacket(FriendlyByteBuf buffer) {
        super(buffer);
        byte size = buffer.readByte();
        this.flaps = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            Direction direction = Direction.from3DDataValue(buffer.readByte());
            boolean inwards = buffer.readBoolean();
            this.flaps.add(Pair.of(direction, inwards));
        }
    }

    public TunnelFlapPacket(BeltTunnelBlockEntity blockEntity, List<Pair<Direction, Boolean>> flaps) {
        super(blockEntity.m_58899_());
        this.flaps = new ArrayList(flaps);
    }

    @Override
    protected void writeData(FriendlyByteBuf buffer) {
        buffer.writeByte(this.flaps.size());
        for (Pair<Direction, Boolean> flap : this.flaps) {
            buffer.writeByte(((Direction) flap.getLeft()).get3DDataValue());
            buffer.writeBoolean((Boolean) flap.getRight());
        }
    }

    protected void handlePacket(BeltTunnelBlockEntity blockEntity) {
        for (Pair<Direction, Boolean> flap : this.flaps) {
            blockEntity.flap((Direction) flap.getLeft(), (Boolean) flap.getRight());
        }
    }
}