package com.mna.entities.renderers.sorcery;

import com.mna.api.tools.RLoc;
import com.mna.entities.models.GreaterAnimusModel;
import com.mna.entities.renderers.player.HandParticleLayer;
import com.mna.entities.summon.GreaterAnimus;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GreaterAnimusRenderer extends HumanoidMobRenderer<GreaterAnimus, GreaterAnimusModel> {

    private static final ResourceLocation GREATER_ANIMUS_LOC = RLoc.create("textures/entity/greater_animus.png");

    public GreaterAnimusRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new GreaterAnimusModel(ctx.bakeLayer(ModelLayers.ZOMBIE)), 0.0F);
        this.m_115326_(new HandParticleLayer<>(this));
    }

    public ResourceLocation getTextureLocation(GreaterAnimus pEntity) {
        return GREATER_ANIMUS_LOC;
    }
}