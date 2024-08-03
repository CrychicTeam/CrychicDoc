package com.simibubi.create.content.equipment.extendoGrip;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;

public class ExtendoGripInteractionPacket extends SimplePacketBase {

    private InteractionHand interactionHand;

    private int target;

    private Vec3 specificPoint;

    public ExtendoGripInteractionPacket(Entity target) {
        this(target, null);
    }

    public ExtendoGripInteractionPacket(Entity target, InteractionHand hand) {
        this(target, hand, null);
    }

    public ExtendoGripInteractionPacket(Entity target, InteractionHand hand, Vec3 specificPoint) {
        this.interactionHand = hand;
        this.specificPoint = specificPoint;
        this.target = target.getId();
    }

    public ExtendoGripInteractionPacket(FriendlyByteBuf buffer) {
        this.target = buffer.readInt();
        int handId = buffer.readInt();
        this.interactionHand = handId == -1 ? null : InteractionHand.values()[handId];
        if (buffer.readBoolean()) {
            this.specificPoint = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.target);
        buffer.writeInt(this.interactionHand == null ? -1 : this.interactionHand.ordinal());
        buffer.writeBoolean(this.specificPoint != null);
        if (this.specificPoint != null) {
            buffer.writeDouble(this.specificPoint.x);
            buffer.writeDouble(this.specificPoint.y);
            buffer.writeDouble(this.specificPoint.z);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender != null) {
                Entity entityByID = sender.m_9236_().getEntity(this.target);
                if (entityByID != null && ExtendoGripItem.isHoldingExtendoGrip(sender)) {
                    double d = sender.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue();
                    if (!sender.m_142582_(entityByID)) {
                        d -= 3.0;
                    }
                    d *= d;
                    if (sender.m_20280_(entityByID) > d) {
                        return;
                    }
                    if (this.interactionHand == null) {
                        sender.attack(entityByID);
                    } else if (this.specificPoint == null) {
                        sender.m_36157_(entityByID, this.interactionHand);
                    } else {
                        entityByID.interactAt(sender, this.specificPoint, this.interactionHand);
                    }
                }
            }
        });
        return true;
    }
}