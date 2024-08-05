package fuzs.puzzleslib.api.capability.v2.data;

import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.capability.ClientboundSyncCapabilityMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface PlayerCapabilityKey<C extends CapabilityComponent> extends CapabilityKey<C> {

    void syncToRemote(ServerPlayer var1);

    static <C extends CapabilityComponent> void syncCapabilityToRemote(Entity holder, ServerPlayer receiver, SyncStrategy syncStrategy, C capability, ResourceLocation id, boolean force) {
        if (syncStrategy != SyncStrategy.MANUAL) {
            if (!(capability instanceof SyncedCapabilityComponent syncedCapability)) {
                if (!force) {
                    throw new IllegalStateException("Unable to sync capability component that is not of type %s".formatted(SyncedCapabilityComponent.class));
                }
                return;
            }
            if (force || syncedCapability.isDirty()) {
                syncStrategy.sendTo(new ClientboundSyncCapabilityMessage(id, holder, capability.toCompoundTag()), receiver);
                syncedCapability.markClean();
            }
        } else if (!force) {
            PuzzlesLib.LOGGER.warn("Attempting to sync capability {} that is set to manual syncing", id);
        }
    }
}