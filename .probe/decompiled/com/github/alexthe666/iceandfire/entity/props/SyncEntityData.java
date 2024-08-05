package com.github.alexthe666.iceandfire.entity.props;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public record SyncEntityData(int entityId, CompoundTag tag) {

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeNbt(this.tag);
    }

    public static SyncEntityData decode(FriendlyByteBuf buffer) {
        return new SyncEntityData(buffer.readInt(), buffer.readNbt());
    }

    public static void handle(SyncEntityData message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
        if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            context.enqueueWork(() -> {
                Player localPlayer = CapabilityHandler.getLocalPlayer();
                if (localPlayer != null) {
                    EntityDataProvider.getCapability(localPlayer.m_9236_().getEntity(message.entityId)).ifPresent(data -> data.deserialize(message.tag));
                }
            });
        }
        context.setPacketHandled(true);
    }
}