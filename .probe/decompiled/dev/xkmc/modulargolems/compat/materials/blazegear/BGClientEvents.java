package dev.xkmc.modulargolems.compat.materials.blazegear;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class BGClientEvents {

    public static ModelLayerLocation BLAZE_ARMS_LAYER = new ModelLayerLocation(new ResourceLocation("mineraft:player"), "blazegear_blaze_arms");

    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BLAZE_ARMS_LAYER, DuplicatedBlazeArmsModel::createBodyLayer);
    }
}