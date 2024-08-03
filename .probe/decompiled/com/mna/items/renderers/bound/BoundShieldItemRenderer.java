package com.mna.items.renderers.bound;

import com.mna.items.renderers.models.ModelBoundShield;
import com.mna.items.sorcery.bound.ItemBoundShield;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BoundShieldItemRenderer extends GeoItemRenderer<ItemBoundShield> {

    public BoundShieldItemRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelBoundShield());
    }
}