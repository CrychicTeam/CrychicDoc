package harmonised.pmmo.client.events;

import harmonised.pmmo.client.gui.VeinRenderer;
import harmonised.pmmo.client.utils.VeinTracker;
import harmonised.pmmo.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE, value = { Dist.CLIENT })
public class WorldRenderHandler {

    @SubscribeEvent
    public static void onWorldRender(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS)) {
            Minecraft mc = Minecraft.getInstance();
            if (Config.VEIN_ENABLED.get() && VeinTracker.isLookingAtVeinTarget(mc.hitResult) && !mc.player.m_9236_().getBlockState(((BlockHitResult) mc.hitResult).getBlockPos()).m_60795_()) {
                VeinTracker.updateVein(mc.player);
                VeinRenderer.drawBoxHighlights(event.getPoseStack(), VeinTracker.getVein());
            }
        }
    }
}