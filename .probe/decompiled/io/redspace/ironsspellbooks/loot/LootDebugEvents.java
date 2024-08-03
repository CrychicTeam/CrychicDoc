package io.redspace.ironsspellbooks.loot;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class LootDebugEvents {

    private static final boolean debugLootTables = false;

    @SubscribeEvent
    public static void alertLootTable(PlayerInteractEvent.RightClickBlock event) {
    }
}