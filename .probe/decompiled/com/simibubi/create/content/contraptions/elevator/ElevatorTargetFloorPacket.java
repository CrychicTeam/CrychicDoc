package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

public class ElevatorTargetFloorPacket extends SimplePacketBase {

    private int entityId;

    private int targetY;

    public ElevatorTargetFloorPacket(AbstractContraptionEntity entity, int targetY) {
        this.targetY = targetY;
        this.entityId = entity.m_19879_();
    }

    public ElevatorTargetFloorPacket(FriendlyByteBuf buffer) {
        this.entityId = buffer.readInt();
        this.targetY = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeInt(this.targetY);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender.serverLevel().getEntity(this.entityId) instanceof AbstractContraptionEntity ace) {
                if (ace.getContraption() instanceof ElevatorContraption ec) {
                    if (!(ace.m_20280_(sender) > 2500.0)) {
                        Level level = sender.m_9236_();
                        ElevatorColumn elevatorColumn = ElevatorColumn.get(level, ec.getGlobalColumn());
                        if (elevatorColumn.contacts.contains(this.targetY)) {
                            if (!ec.isTargetUnreachable(this.targetY)) {
                                BlockPos pos = elevatorColumn.contactAt(this.targetY);
                                BlockState blockState = level.getBlockState(pos);
                                if (blockState.m_60734_() instanceof ElevatorContactBlock ecb) {
                                    ecb.callToContactAndUpdate(elevatorColumn, blockState, level, pos, false);
                                }
                            }
                        }
                    }
                }
            }
        });
        return true;
    }
}