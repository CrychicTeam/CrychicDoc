package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class RequestWellspringPowerNetworkSyncMessage extends BaseMessage {

    private ResourceKey<Level> forDimension;

    private boolean nearbyNodes;

    public RequestWellspringPowerNetworkSyncMessage(ResourceKey<Level> dimension, boolean nearbyNodes) {
        this.forDimension = dimension;
        this.nearbyNodes = nearbyNodes;
        this.messageIsValid = true;
    }

    public RequestWellspringPowerNetworkSyncMessage() {
        this.messageIsValid = false;
    }

    public static final RequestWellspringPowerNetworkSyncMessage decode(FriendlyByteBuf buf) {
        RequestWellspringPowerNetworkSyncMessage msg = new RequestWellspringPowerNetworkSyncMessage();
        try {
            ResourceLocation location = buf.readResourceLocation();
            msg.forDimension = ResourceKey.create(Registries.DIMENSION, location);
            msg.nearbyNodes = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading RequestWellspringPowerNetworkSyncMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public ResourceKey<Level> getDimension() {
        return this.forDimension;
    }

    public boolean getNearbyNodes() {
        return this.nearbyNodes;
    }

    public static final void encode(RequestWellspringPowerNetworkSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeResourceLocation(msg.forDimension.location());
        buf.writeBoolean(msg.getNearbyNodes());
    }
}