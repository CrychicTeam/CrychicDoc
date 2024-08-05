package com.mna.items.renderers.bound;

import com.mna.items.renderers.models.ModelBoundAxe;
import com.mna.items.sorcery.bound.ItemBoundAxe;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BoundAxeItemRenderer extends GeoItemRenderer<ItemBoundAxe> {

    public BoundAxeItemRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelBoundAxe());
    }
}