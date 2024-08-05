package dev.xkmc.l2library.capability.player;

import dev.xkmc.l2library.init.L2Library;
import net.minecraft.server.level.ServerPlayer;

public class PlayerCapabilityNetworkHandler<T extends PlayerCapabilityTemplate<T>> {

    public final PlayerCapabilityHolder<T> holder;

    public PlayerCapabilityNetworkHandler(PlayerCapabilityHolder<T> holder) {
        this.holder = holder;
    }

    public void toClientSyncAll(ServerPlayer e) {
        L2Library.PACKET_HANDLER.toClientPlayer(new PlayerCapToClient(PlayerCapToClient.Action.ALL, this.holder, this.holder.get(e)), e);
    }

    public void toClientSyncClone(ServerPlayer e) {
        L2Library.PACKET_HANDLER.toClientPlayer(new PlayerCapToClient(PlayerCapToClient.Action.CLONE, this.holder, this.holder.get(e)), e);
    }

    public void toTracking(ServerPlayer e) {
        L2Library.PACKET_HANDLER.toTrackingPlayers(new PlayerCapToClient(PlayerCapToClient.Action.TRACK, this.holder, this.holder.get(e)), e);
    }

    public void startTracking(ServerPlayer tracker, ServerPlayer target) {
        L2Library.PACKET_HANDLER.toClientPlayer(new PlayerCapToClient(PlayerCapToClient.Action.TRACK, this.holder, this.holder.get(target)), tracker);
    }
}