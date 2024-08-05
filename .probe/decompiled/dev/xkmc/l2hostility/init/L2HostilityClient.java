package dev.xkmc.l2hostility.init;

import dev.xkmc.l2hostility.content.menu.tab.DifficultyOverlay;
import dev.xkmc.l2hostility.content.menu.tab.DifficultyTab;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2tabs.tabs.core.TabRegistry;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2hostility", bus = Bus.MOD)
public class L2HostilityClient {

    public static TabToken<DifficultyTab> TAB_DIFFICULTY;

    @SubscribeEvent
    public static void client(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            TAB_DIFFICULTY = TabRegistry.registerTab(5000, DifficultyTab::new, () -> Items.ZOMBIE_HEAD, LangData.INFO_TAB_TITLE.get());
            ItemProperties.register((Item) LHItems.RESTORATION.get(), new ResourceLocation("l2hostility", "filled"), (stack, level, entity, i) -> stack.getTagElement("UnsealRoot") == null ? 0.0F : 1.0F);
        });
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
    }

    @SubscribeEvent
    public static void registerOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "l2hostility", new DifficultyOverlay());
    }
}