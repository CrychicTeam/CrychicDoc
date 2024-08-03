package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ManaweaveWandSlotChangeMessage extends BaseMessage {

    private ResourceLocation selected;

    private boolean offhand;

    public ManaweaveWandSlotChangeMessage(ResourceLocation selected, boolean offhand) {
        this.selected = selected;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public ManaweaveWandSlotChangeMessage() {
        this.messageIsValid = false;
    }

    public ResourceLocation getSelected() {
        return this.selected;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static ManaweaveWandSlotChangeMessage decode(FriendlyByteBuf buf) {
        ManaweaveWandSlotChangeMessage msg = new ManaweaveWandSlotChangeMessage();
        try {
            if (buf.readBoolean()) {
                msg.selected = buf.readResourceLocation();
            }
            msg.offhand = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ManaweaveWandSlotChangeMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ManaweaveWandSlotChangeMessage msg, FriendlyByteBuf buf) {
        if (msg.getSelected() != null) {
            buf.writeBoolean(true);
            buf.writeResourceLocation(msg.getSelected());
        } else {
            buf.writeBoolean(false);
        }
        buf.writeBoolean(msg.isOffhand());
    }
}