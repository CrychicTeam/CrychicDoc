package fr.frinn.custommachinery.forge.client;

import fr.frinn.custommachinery.client.render.BoxCreatorRenderer;
import fr.frinn.custommachinery.client.render.StructureCreatorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "custommachinery", bus = Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            BoxCreatorRenderer.renderSelectedBlocks(event.getPoseStack());
            StructureCreatorRenderer.renderSelectedBlocks(event.getPoseStack());
        }
    }
}