package org.embeddedt.embeddium.render.frapi;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.client.resources.model.BakedModel;

public class FRAPIModelUtils {

    public static boolean isFRAPIModel(BakedModel model) {
        return !FRAPIRenderHandler.INDIGO_PRESENT ? false : !((FabricBakedModel) model).isVanillaAdapter();
    }
}