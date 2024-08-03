package dev.xkmc.l2complements.init;

import dev.xkmc.l2complements.content.client.EnchStackDeco;
import dev.xkmc.l2complements.content.client.RangeDiggingOverlay;
import dev.xkmc.l2complements.content.item.misc.LCBEWLR;
import dev.xkmc.l2complements.init.data.LCKeys;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2complements.init.registrate.LCParticle;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2complements", bus = Bus.MOD)
public class L2ComplementsClient {

    @SubscribeEvent
    public static void onParticleRegistryEvent(RegisterParticleProvidersEvent event) {
        LCParticle.registerClient();
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register((Item) LCItems.SONIC_SHOOTER.get(), new ResourceLocation("l2complements", "shoot"), (stack, level, user, index) -> user != null && user.isUsingItem() ? 1.0F - 1.0F * (float) user.getUseItemRemainingTicks() / (float) stack.getUseDuration() : 0.0F));
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "range_digging", new RangeDiggingOverlay());
    }

    @SubscribeEvent
    public static void registerItemDecoration(RegisterItemDecorationsEvent event) {
        event.register(Items.ENCHANTED_BOOK, new EnchStackDeco());
    }

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((PreparableReloadListener) LCBEWLR.INSTANCE.get());
    }

    @SubscribeEvent
    public static void registerKeyMaps(RegisterKeyMappingsEvent event) {
        for (LCKeys e : LCKeys.values()) {
            event.register(e.map);
        }
    }
}