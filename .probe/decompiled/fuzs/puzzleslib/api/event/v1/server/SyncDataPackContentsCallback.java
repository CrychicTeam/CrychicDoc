package fuzs.puzzleslib.api.event.v1.server;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface SyncDataPackContentsCallback {

    EventInvoker<SyncDataPackContentsCallback> EVENT = EventInvoker.lookup(SyncDataPackContentsCallback.class);

    void onSyncDataPackContents(ServerPlayer var1, boolean var2);
}