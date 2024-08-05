package io.redspace.ironsspellbooks.setup;

import net.minecraft.server.ReloadableServerResources;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "irons_spellbooks", bus = Bus.FORGE)
public class DataHandling {

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        ReloadableServerResources reloadableServerResources = event.getServerResources();
    }

    @SubscribeEvent
    public static void onChunkDataEvent(ChunkDataEvent event) {
        event.getData();
    }
}