package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.entities.IAnimPacketSync;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class EntityStateMessage extends BaseMessage {

    private CompoundTag data;

    private int entityID;

    private EntityStateMessage(int entityID, CompoundTag nbt) {
        this.data = nbt;
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    private EntityStateMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.data;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static EntityStateMessage decode(FriendlyByteBuf buf) {
        EntityStateMessage msg = new EntityStateMessage();
        try {
            msg.entityID = buf.readInt();
            msg.data = buf.readNbt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading EntityStateMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(EntityStateMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeNbt(msg.getData());
    }

    public static EntityStateMessage fromEntity(IAnimPacketSync<? extends Entity> entity) {
        return new EntityStateMessage(((Entity) entity).getId(), entity.getPacketData());
    }
}