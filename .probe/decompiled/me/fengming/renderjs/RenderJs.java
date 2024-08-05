package me.fengming.renderjs;

import me.fengming.renderjs.events.RenderJsEvents;
import me.fengming.renderjs.events.level.RenderEntityEventJS;
import me.fengming.renderjs.events.level.RenderLevelEventJS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("renderjs")
public class RenderJs {

    public static final String MOD_ID = "renderjs";

    @EventBusSubscriber(modid = "renderjs", value = { Dist.CLIENT })
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onRenderEntityBefore(RenderLivingEvent.Pre<?, ?> e) {
            RenderJsEvents.BEFORE_RENDER_ENTITY.post(new RenderEntityEventJS.Before(e.getEntity(), e.getRenderer(), e.getPartialTick(), e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight()));
        }

        @SubscribeEvent
        public static void onRenderEntityAfter(RenderLivingEvent.Post<?, ?> e) {
            RenderJsEvents.AFTER_RENDER_ENTITY.post(new RenderEntityEventJS.After(e.getEntity(), e.getRenderer(), e.getPartialTick(), e.getPoseStack(), e.getMultiBufferSource(), e.getPackedLight()));
        }

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent e) {
            if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS) {
                RenderJsEvents.AFTER_RENDER_SOLID_BLOCK.post(new RenderLevelEventJS.After(e.getLevelRenderer(), e.getPoseStack(), e.getProjectionMatrix(), e.getRenderTick(), e.getPartialTick(), e.getCamera(), e.getFrustum()));
            }
        }
    }

    @EventBusSubscriber(modid = "renderjs", value = { Dist.CLIENT }, bus = Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onSetup(FMLClientSetupEvent event) {
        }
    }
}