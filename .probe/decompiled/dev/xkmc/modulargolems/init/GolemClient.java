package dev.xkmc.modulargolems.init;

import dev.xkmc.modulargolems.compat.curio.CurioCompatRegistry;
import dev.xkmc.modulargolems.compat.materials.common.CompatManager;
import dev.xkmc.modulargolems.compat.misc.MaidCompat;
import dev.xkmc.modulargolems.content.client.armor.GolemEquipmentModels;
import dev.xkmc.modulargolems.content.client.overlay.GolemStatusOverlay;
import dev.xkmc.modulargolems.content.entity.humanoid.skin.PlayerSkinRenderer;
import dev.xkmc.modulargolems.content.item.golem.GolemBEWLR;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "modulargolems", bus = Bus.MOD)
public class GolemClient {

    private static final boolean ENABLE_TLM = true;

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("touhou_little_maid")) {
            MinecraftForge.EVENT_BUS.register(MaidCompat.class);
        }
        event.enqueueWork(() -> {
            ClampedItemPropertyFunction func = (stack, level, entity, layer) -> entity != null && entity.isBlocking() && entity.getUseItem() == stack ? 1.0F : 0.0F;
            ItemProperties.register(Items.SHIELD, new ResourceLocation("blocking"), func);
            ClampedItemPropertyFunction arrow = (stack, level, entity, layer) -> stack.is(MGTagGen.BLUE_UPGRADES) ? 1.0F : (stack.is(MGTagGen.POTION_UPGRADES) ? 0.5F : 0.0F);
            for (UpgradeItem item : UpgradeItem.LIST) {
                ItemProperties.register(item, new ResourceLocation("modulargolems", "blue_arrow"), arrow);
            }
            CompatManager.dispatchClientSetup();
            GolemTabRegistry.register();
            CurioCompatRegistry.clientRegister();
        });
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "golem_stats", new GolemStatusOverlay());
    }

    @SubscribeEvent
    public static void registerArmorLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        GolemEquipmentModels.registerArmorLayer(event);
    }

    @SubscribeEvent
    public static void onResourceReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((PreparableReloadListener) GolemBEWLR.INSTANCE.get());
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        PlayerSkinRenderer.SLIM = new PlayerSkinRenderer(event.getContext(), true);
        PlayerSkinRenderer.REGULAR = new PlayerSkinRenderer(event.getContext(), false);
        if (ModList.get().isLoaded("touhou_little_maid")) {
            MaidCompat.addLayers(event);
        }
    }
}