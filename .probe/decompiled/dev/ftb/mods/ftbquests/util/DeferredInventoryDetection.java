package dev.ftb.mods.ftbquests.util;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class DeferredInventoryDetection {

    private static final long MILLIS_IN_TICK = 50L;

    private static final Object2LongOpenHashMap<UUID> playerMap = new Object2LongOpenHashMap();

    public static void tick(MinecraftServer server) {
        if (!playerMap.isEmpty()) {
            Set<UUID> toRemove = new HashSet();
            long now = System.currentTimeMillis();
            ObjectIterator var4 = playerMap.keySet().iterator();
            while (var4.hasNext()) {
                UUID id = (UUID) var4.next();
                if (now >= playerMap.getLong(id)) {
                    ServerPlayer sp = server.getPlayerList().getPlayer(id);
                    if (sp != null) {
                        FTBQuestsInventoryListener.detect(sp, ItemStack.EMPTY, 0L);
                    }
                    toRemove.add(id);
                }
            }
            toRemove.forEach(playerMap::removeLong);
        }
    }

    static void scheduleInventoryCheck(ServerPlayer sp, int delay) {
        playerMap.putIfAbsent(sp.m_20148_(), System.currentTimeMillis() + (long) delay * 50L);
    }
}