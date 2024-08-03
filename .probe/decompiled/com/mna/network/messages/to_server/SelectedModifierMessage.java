package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SelectedModifierMessage extends BaseMessage {

    private ResourceLocation modifierRLoc;

    private boolean offhand;

    public SelectedModifierMessage(ResourceLocation modifierRLoc, boolean offhand) {
        this.modifierRLoc = modifierRLoc;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public SelectedModifierMessage() {
        this.messageIsValid = false;
    }

    public ResourceLocation getModifierRLoc() {
        return this.modifierRLoc;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static SelectedModifierMessage decode(FriendlyByteBuf buf) {
        SelectedModifierMessage msg = new SelectedModifierMessage();
        try {
            msg.modifierRLoc = buf.readResourceLocation();
            msg.offhand = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading SelectedModifierMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SelectedModifierMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.modifierRLoc);
        buf.writeBoolean(msg.isOffhand());
    }
}