package com.mna.items.renderers.bound;

import com.mna.items.renderers.models.ModelBoundBow;
import com.mna.items.sorcery.bound.ItemBoundBow;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BoundBowItemRenderer extends GeoItemRenderer<ItemBoundBow> {

    public BoundBowItemRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelBoundBow());
    }
}