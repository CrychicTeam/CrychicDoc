package fuzs.puzzleslib.api.capability.v2.data;

import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.puzzleslib.impl.PuzzlesLibMod;
import fuzs.puzzleslib.impl.capability.SyncStrategyImpl;
import net.minecraft.server.level.ServerPlayer;

public interface SyncStrategy {

    SyncStrategy MANUAL = new SyncStrategyImpl((message, player) -> {
    });

    SyncStrategy SELF = new SyncStrategyImpl((message, player) -> PuzzlesLibMod.NETWORK.sendTo(player, message));

    SyncStrategy SELF_AND_TRACKING = new SyncStrategyImpl((message, entity) -> PuzzlesLibMod.NETWORK.sendToAllTracking(entity, message, true));

    <T extends Record & ClientboundMessage<T>> void sendTo(T var1, ServerPlayer var2);
}