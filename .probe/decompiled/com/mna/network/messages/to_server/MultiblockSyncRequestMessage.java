package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MultiblockSyncRequestMessage extends BaseMessage {

    private int entityID;

    private ResourceLocation recipeID;

    private MultiblockSyncRequestMessage() {
        this.messageIsValid = false;
    }

    public MultiblockSyncRequestMessage(int entityID, ResourceLocation recipe) {
        this();
        this.entityID = entityID;
        this.recipeID = recipe;
        this.messageIsValid = true;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public ResourceLocation getRecipe() {
        return this.recipeID;
    }

    public static MultiblockSyncRequestMessage decode(FriendlyByteBuf buf) {
        MultiblockSyncRequestMessage msg = new MultiblockSyncRequestMessage();
        try {
            msg.entityID = buf.readInt();
            msg.recipeID = buf.readResourceLocation();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MultiblockSyncRequestMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MultiblockSyncRequestMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeResourceLocation(msg.getRecipe());
    }
}