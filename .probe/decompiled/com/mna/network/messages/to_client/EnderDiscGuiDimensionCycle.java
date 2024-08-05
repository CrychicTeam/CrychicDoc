package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class EnderDiscGuiDimensionCycle extends BaseMessage {

    private ResourceLocation dimID;

    public EnderDiscGuiDimensionCycle(ResourceLocation dimID) {
        this.dimID = dimID;
        this.messageIsValid = true;
    }

    public EnderDiscGuiDimensionCycle() {
        this.messageIsValid = false;
    }

    public ResourceLocation getDimensionID() {
        return this.dimID;
    }

    public static EnderDiscGuiDimensionCycle decode(FriendlyByteBuf buf) {
        EnderDiscGuiDimensionCycle msg = new EnderDiscGuiDimensionCycle();
        try {
            msg.dimID = buf.readResourceLocation();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading EnderDiscGuiDimensionCycle: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(EnderDiscGuiDimensionCycle msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.getDimensionID());
    }
}