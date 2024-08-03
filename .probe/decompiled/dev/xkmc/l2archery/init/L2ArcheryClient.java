package dev.xkmc.l2archery.init;

import dev.xkmc.l2archery.content.client.ArrowDisplayOverlay;
import dev.xkmc.l2archery.content.client.BowFluxBarRenderer;
import dev.xkmc.l2archery.content.client.BowInfoOverlay;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2archery", bus = Bus.MOD)
public class L2ArcheryClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(L2ArcheryClient::registerItemProperties);
    }

    public static void registerItemProperties() {
        for (GenericBowItem bow : ArcheryItems.BOW_LIKE) {
            ItemProperties.register(bow, new ResourceLocation("pull"), (stack, level, entity, i) -> entity != null && entity.getUseItem() == stack ? bow.getPullForTime(entity, (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks())) : 0.0F);
            ItemProperties.register(bow, new ResourceLocation("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        }
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        BowFluxBarRenderer deco = new BowFluxBarRenderer();
        for (GenericBowItem bow : ArcheryItems.BOW_LIKE) {
            event.register(bow, deco);
        }
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "arrow", new ArrowDisplayOverlay());
        event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), "info", new BowInfoOverlay());
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
    }
}