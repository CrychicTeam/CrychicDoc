package com.mna.entities.renderers.living;

import com.mna.api.tools.RLoc;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

public class MagmooRenderer extends CowRenderer {

    private static final ResourceLocation MAGMOO_LOCATION = RLoc.create("textures/entity/magmoo.png");

    public MagmooRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(Cow pEntity) {
        return MAGMOO_LOCATION;
    }
}