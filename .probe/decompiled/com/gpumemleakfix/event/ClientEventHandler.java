package com.gpumemleakfix.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.core.Vec3i;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {

    public static ConcurrentLinkedQueue<Vec3i> queue = new ConcurrentLinkedQueue();

    @SubscribeEvent
    public static void onCLientTick(TickEvent.ClientTickEvent event) {
        int counter = 0;
        while (!queue.isEmpty() && counter++ < 20) {
            Vec3i ids = (Vec3i) queue.poll();
            if (ids != null) {
                GlStateManager._bindTexture(0);
                GlStateManager._glBindFramebuffer(36160, 0);
                if (ids.getX() > -1) {
                    TextureUtil.releaseTextureId(ids.getX());
                }
                if (ids.getY() > -1) {
                    TextureUtil.releaseTextureId(ids.getY());
                }
                if (ids.getZ() > -1) {
                    GlStateManager._glBindFramebuffer(36160, 0);
                    GlStateManager._glDeleteFramebuffers(ids.getZ());
                }
            }
        }
    }
}