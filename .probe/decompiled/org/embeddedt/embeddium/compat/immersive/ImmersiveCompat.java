package org.embeddedt.embeddium.compat.immersive;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.loading.FMLLoader;
import org.embeddedt.embeddium.api.ChunkMeshEvent;

@EventBusSubscriber(modid = "embeddium", value = { Dist.CLIENT }, bus = Bus.MOD)
public class ImmersiveCompat {

    private static final boolean immersiveLoaded = FMLLoader.getLoadingModList().getModFileById("immersiveengineering") != null;

    private static boolean hasRegisteredMeshAppender;

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
        if (immersiveLoaded) {
            event.registerReloadListener(new ImmersiveConnectionRenderer());
            if (!hasRegisteredMeshAppender) {
                hasRegisteredMeshAppender = true;
                ChunkMeshEvent.BUS.addListener(ImmersiveConnectionRenderer::meshAppendEvent);
            }
        }
    }
}