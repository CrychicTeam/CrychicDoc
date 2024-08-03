package com.simibubi.create.content.contraptions.sync;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;

public class ContraptionInteractionPacket extends SimplePacketBase {

    private InteractionHand interactionHand;

    private int target;

    private BlockPos localPos;

    private Direction face;

    public ContraptionInteractionPacket(AbstractContraptionEntity target, InteractionHand hand, BlockPos localPos, Direction side) {
        this.interactionHand = hand;
        this.localPos = localPos;
        this.target = target.m_19879_();
        this.face = side;
    }

    public ContraptionInteractionPacket(FriendlyByteBuf buffer) {
        this.target = buffer.readInt();
        int handId = buffer.readInt();
        this.interactionHand = handId == -1 ? null : InteractionHand.values()[handId];
        this.localPos = buffer.readBlockPos();
        this.face = Direction.from3DDataValue(buffer.readShort());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.target);
        buffer.writeInt(this.interactionHand == null ? -1 : this.interactionHand.ordinal());
        buffer.writeBlockPos(this.localPos);
        buffer.writeShort(this.face.get3DDataValue());
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                Entity entityByID = sender.m_9236_().getEntity(this.target);
                if (entityByID instanceof AbstractContraptionEntity contraptionEntity) {
                    AABB bb = contraptionEntity.m_20191_();
                    double boundsExtra = Math.max(bb.getXsize(), bb.getYsize());
                    double d = sender.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() + 10.0 + boundsExtra;
                    if (!sender.m_142582_(entityByID)) {
                        d -= 3.0;
                    }
                    d *= d;
                    if (!(sender.m_20280_(entityByID) > d)) {
                        if (contraptionEntity.handlePlayerInteraction(sender, this.localPos, this.face, this.interactionHand)) {
                            sender.m_21011_(this.interactionHand, true);
                        }
                    }
                }
            }
        });
        return true;
    }
}