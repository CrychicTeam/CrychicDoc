package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class WellspringSyncMessage extends BaseMessage {

    CompoundTag wellspringData;

    public WellspringSyncMessage(CompoundTag nbt) {
        this.wellspringData = nbt;
        this.messageIsValid = true;
    }

    private WellspringSyncMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.wellspringData;
    }

    public static WellspringSyncMessage decode(FriendlyByteBuf buf) {
        WellspringSyncMessage msg = new WellspringSyncMessage();
        try {
            msg.wellspringData = buf.readNbt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading WellspringSyncMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(WellspringSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getData());
    }
}