package malte0811.ferritecore;

import malte0811.ferritecore.impl.Deduplicator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "ferritecore", bus = Bus.MOD)
public class ModClientForge {

    @SubscribeEvent
    public static void registerReloadListener(RenderLevelStageEvent.RegisterStageEvent ev) {
        Deduplicator.registerReloadListener();
    }
}