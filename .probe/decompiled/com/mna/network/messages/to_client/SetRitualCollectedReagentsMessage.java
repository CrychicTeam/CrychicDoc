package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class SetRitualCollectedReagentsMessage extends BaseMessage {

    CompoundTag reagentData;

    int entityID;

    public SetRitualCollectedReagentsMessage(CompoundTag data, int entityID) {
        this.reagentData = data;
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public SetRitualCollectedReagentsMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.reagentData;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static SetRitualCollectedReagentsMessage decode(FriendlyByteBuf buf) {
        SetRitualCollectedReagentsMessage msg = new SetRitualCollectedReagentsMessage();
        try {
            msg.entityID = buf.readInt();
            msg.reagentData = buf.readNbt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SetRitualCollectedReagentsMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityID);
        buf.writeNbt(message.getData());
    }
}