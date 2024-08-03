package com.mna.items.renderers.bound;

import com.mna.items.renderers.models.ModelBoundSword;
import com.mna.items.sorcery.bound.ItemBoundSword;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BoundSwordItemRenderer extends GeoItemRenderer<ItemBoundSword> {

    public BoundSwordItemRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(new ModelBoundSword());
    }
}