package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class SpellBookSlotChangeMessage extends BaseMessage {

    private int slot;

    private boolean offhand;

    public SpellBookSlotChangeMessage(int slot, boolean offhand) {
        this.slot = slot;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public SpellBookSlotChangeMessage() {
        this.messageIsValid = false;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static SpellBookSlotChangeMessage decode(FriendlyByteBuf buf) {
        SpellBookSlotChangeMessage msg = new SpellBookSlotChangeMessage();
        try {
            msg.slot = buf.readInt();
            msg.offhand = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweavePatternDrawnMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SpellBookSlotChangeMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getSlot());
        buf.writeBoolean(msg.isOffhand());
    }
}