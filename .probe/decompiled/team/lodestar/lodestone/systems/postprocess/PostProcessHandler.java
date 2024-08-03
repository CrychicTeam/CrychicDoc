package team.lodestar.lodestone.systems.postprocess;

import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.FORGE)
public class PostProcessHandler {

    private static final List<PostProcessor> instances = new ArrayList();

    private static boolean didCopyDepth = false;

    public static void addInstance(PostProcessor instance) {
        instances.add(instance);
    }

    public static void copyDepthBuffer() {
        if (!didCopyDepth) {
            instances.forEach(PostProcessor::copyDepthBuffer);
            didCopyDepth = true;
        }
    }

    public static void resize(int width, int height) {
        instances.forEach(i -> i.resize(width, height));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onWorldRenderLast(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)) {
            PostProcessor.viewModelStack = event.getPoseStack();
        }
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_LEVEL)) {
            copyDepthBuffer();
            instances.forEach(PostProcessor::applyPostProcess);
            didCopyDepth = false;
        }
    }
}