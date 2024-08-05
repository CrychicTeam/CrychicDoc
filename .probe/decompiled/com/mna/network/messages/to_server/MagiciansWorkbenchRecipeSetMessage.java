package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class MagiciansWorkbenchRecipeSetMessage extends BaseMessage {

    private int recipeIndex;

    public MagiciansWorkbenchRecipeSetMessage(int recipeIndex) {
        this.recipeIndex = recipeIndex;
        this.messageIsValid = true;
    }

    public MagiciansWorkbenchRecipeSetMessage() {
        this.messageIsValid = false;
    }

    public int getIndex() {
        return this.recipeIndex;
    }

    public static MagiciansWorkbenchRecipeSetMessage decode(FriendlyByteBuf buf) {
        MagiciansWorkbenchRecipeSetMessage msg = new MagiciansWorkbenchRecipeSetMessage();
        try {
            msg.recipeIndex = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading EnderDiscIndexSetMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MagiciansWorkbenchRecipeSetMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getIndex());
    }
}