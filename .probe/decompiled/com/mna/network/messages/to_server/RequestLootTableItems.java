package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class RequestLootTableItems extends BaseMessage {

    ResourceLocation lootTableID;

    public RequestLootTableItems(ResourceLocation lootTableID) {
        this.lootTableID = lootTableID;
        this.messageIsValid = true;
    }

    public RequestLootTableItems() {
        this.messageIsValid = false;
    }

    public ResourceLocation getLootTableID() {
        return this.lootTableID;
    }

    public static RequestLootTableItems decode(FriendlyByteBuf buf) {
        RequestLootTableItems msg = new RequestLootTableItems();
        try {
            msg.lootTableID = buf.readResourceLocation();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading RequestLootTableItems: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(RequestLootTableItems msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.getLootTableID());
    }
}