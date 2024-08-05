package com.mna.events;

import com.mna.blocks.tileentities.models.FixedBookModel;
import com.mna.entities.models.AncientWizardModel;
import com.mna.entities.models.BubbleBoatModel;
import com.mna.entities.models.DemonStoneModel;
import com.mna.entities.models.MagicBroomModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD, value = { Dist.CLIENT })
public class RenderSetupEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MagicBroomModel.LAYER_LOCATION, MagicBroomModel::createBodyLayer);
        event.registerLayerDefinition(DemonStoneModel.LAYER_LOCATION, DemonStoneModel::createBodyLayer);
        event.registerLayerDefinition(AncientWizardModel.LAYER_LOCATION, AncientWizardModel::createBodyLayer);
        event.registerLayerDefinition(BubbleBoatModel.LAYER_LOCATION, BubbleBoatModel::createBodyLayer);
        event.registerLayerDefinition(FixedBookModel.LAYER_LOCATION, FixedBookModel::createBodyLayer);
    }
}