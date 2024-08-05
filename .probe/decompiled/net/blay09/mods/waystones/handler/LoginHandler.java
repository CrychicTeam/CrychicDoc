package net.blay09.mods.waystones.handler;

import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.minecraft.server.level.ServerPlayer;

public class LoginHandler {

    public static void onPlayerLogin(PlayerLoginEvent event) {
        ServerPlayer player = event.getPlayer();
        for (IWaystone waystone : WaystoneManager.get(player.server).getGlobalWaystones()) {
            if (!PlayerWaystoneManager.isWaystoneActivated(player, waystone)) {
                PlayerWaystoneManager.activateWaystone(player, waystone);
            }
        }
        WaystoneSyncManager.sendActivatedWaystones(player);
        WaystoneSyncManager.sendWarpPlates(player);
        WaystoneSyncManager.sendWaystoneCooldowns(player);
    }
}