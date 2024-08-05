package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.client.model.MilkmadeHatModel;
import com.sihenzhang.crockpot.client.model.VoltGoatModel;
import com.sihenzhang.crockpot.client.model.geom.CrockPotModelLayers;
import com.sihenzhang.crockpot.client.renderer.entity.EmptyRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.VoltGoatRenderer;
import com.sihenzhang.crockpot.client.renderer.entity.layers.MilkmadeHatLayer;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.inventory.CrockPotMenuTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "crockpot", bus = Bus.MOD)
public class ClientRegistry {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(CrockPotMenuTypes.CROCK_POT_MENU_TYPE.get(), CrockPotScreen::new));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CrockPotEntities.BIRDCAGE.get(), EmptyRenderer::new);
        event.registerEntityRenderer(CrockPotEntities.PARROT_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(CrockPotEntities.VOLT_GOAT.get(), VoltGoatRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrockPotModelLayers.MILKMADE_HAT, MilkmadeHatModel::createLayer);
        event.registerLayerDefinition(CrockPotModelLayers.VOLT_GOAT, VoltGoatModel::createBodyLayer);
        event.registerLayerDefinition(CrockPotModelLayers.VOLT_GOAT_ARMOR, VoltGoatModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(name -> {
            if (event.getSkin(name) instanceof PlayerRenderer playerRenderer) {
                playerRenderer.m_115326_(new MilkmadeHatLayer<>(playerRenderer, event.getEntityModels()));
            }
        });
        Minecraft.getInstance().getEntityRenderDispatcher().renderers.values().forEach(renderer -> {
            if (renderer instanceof ArmorStandRenderer armorStandRenderer) {
                armorStandRenderer.m_115326_(new MilkmadeHatLayer<>(armorStandRenderer, event.getEntityModels()));
            }
            if (renderer instanceof HumanoidMobRenderer<Mob, HumanoidModel<Mob>> humanoidMobRenderer) {
                humanoidMobRenderer.m_115326_(new MilkmadeHatLayer<>(humanoidMobRenderer, event.getEntityModels()));
            }
        });
    }
}