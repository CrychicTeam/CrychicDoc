package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.network.messages.BaseMessage;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class WellspringPowerNetworkSyncMessage extends BaseMessage {

    CompoundTag wellspringData;

    private ResourceKey<Level> forDimension;

    public WellspringPowerNetworkSyncMessage(CompoundTag nbt, ResourceKey<Level> dimension) {
        this.wellspringData = nbt;
        this.messageIsValid = true;
        this.forDimension = dimension;
    }

    private WellspringPowerNetworkSyncMessage() {
        this.messageIsValid = false;
    }

    public CompoundTag getData() {
        return this.wellspringData;
    }

    public ResourceKey<Level> getDimension() {
        return this.forDimension;
    }

    public static WellspringPowerNetworkSyncMessage decode(FriendlyByteBuf buf) {
        WellspringPowerNetworkSyncMessage msg = new WellspringPowerNetworkSyncMessage();
        try {
            msg.wellspringData = buf.readNbt();
            ResourceLocation location = buf.readResourceLocation();
            msg.forDimension = ResourceKey.create(Registries.DIMENSION, location);
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading WellspringPowerNetworkSyncMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(WellspringPowerNetworkSyncMessage msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.getData());
        buf.writeResourceLocation(msg.forDimension.location());
    }

    public static WellspringPowerNetworkSyncMessage from(ServerLevel world, ServerPlayer player, boolean fullSync) {
        CompoundTag nbt = new CompoundTag();
        world.getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().serializeNetworkStrength(player, nbt, fullSync));
        return new WellspringPowerNetworkSyncMessage(nbt, world.m_46472_());
    }
}