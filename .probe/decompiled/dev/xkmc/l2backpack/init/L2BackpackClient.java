package dev.xkmc.l2backpack.init;

import dev.xkmc.l2backpack.content.bag.AbstractBag;
import dev.xkmc.l2backpack.content.common.InvClientTooltip;
import dev.xkmc.l2backpack.content.common.InvTooltip;
import dev.xkmc.l2backpack.content.quickswap.common.QuickSwapOverlay;
import dev.xkmc.l2backpack.content.quickswap.quiver.Quiver;
import dev.xkmc.l2backpack.content.render.BackpackModel;
import dev.xkmc.l2backpack.content.render.BagCountDeco;
import dev.xkmc.l2backpack.content.render.DrawerCountDeco;
import dev.xkmc.l2backpack.content.render.EnderPreviewOverlay;
import dev.xkmc.l2backpack.content.render.RenderEvents;
import dev.xkmc.l2backpack.init.data.BackpackKeys;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2backpack", bus = Bus.MOD)
public class L2BackpackClient {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register((Item) BackpackItems.QUIVER.get(), new ResourceLocation("l2backpack", "arrow"), (stack, level, entity, i) -> Quiver.displayArrow(stack));
            ClampedItemPropertyFunction func = (stack, level, entity, i) -> AbstractBag.isFilled(stack) ? 1.0F : 0.0F;
            ItemProperties.register((Item) BackpackItems.ARMOR_BAG.get(), new ResourceLocation("l2backpack", "fill"), func);
            ItemProperties.register((Item) BackpackItems.BOOK_BAG.get(), new ResourceLocation("l2backpack", "fill"), func);
        });
    }

    @SubscribeEvent
    public static void registerClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(InvTooltip.class, InvClientTooltip::new);
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "arrow_bag", new QuickSwapOverlay());
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "ender_drawer", new EnderPreviewOverlay());
    }

    @SubscribeEvent
    public static void registerDeco(RegisterItemDecorationsEvent event) {
        DrawerCountDeco deco = new DrawerCountDeco();
        event.register((ItemLike) BackpackItems.DRAWER.get(), deco);
        event.register((ItemLike) BackpackItems.ENDER_DRAWER.get(), deco);
        BagCountDeco decox = new BagCountDeco();
        event.register((ItemLike) BackpackItems.ARMOR_BAG.get(), decox);
        event.register((ItemLike) BackpackItems.BOOK_BAG.get(), decox);
    }

    @SubscribeEvent
    public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) resourceManager -> RenderEvents.registerBackpackLayer());
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RenderEvents.BACKPACK_LAYER, BackpackModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        for (BackpackKeys e : BackpackKeys.values()) {
            event.register(e.map);
        }
    }
}