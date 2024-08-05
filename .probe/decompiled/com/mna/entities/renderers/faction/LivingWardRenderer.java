package com.mna.entities.renderers.faction;

import com.mna.api.affinity.Affinity;
import com.mna.entities.faction.LivingWard;
import com.mna.entities.models.faction.LivingWardModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LivingWardRenderer extends MAGeckoRenderer<LivingWard> {

    public LivingWardRenderer(EntityRendererProvider.Context context) {
        super(context, new LivingWardModel());
    }

    public void render(LivingWard entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.translate(0.0, 0.25, 0.0);
        WorldRenderUtils.renderFlatRadiant(entity, stack, bufferIn, Affinity.ARCANE.getSecondaryColor(), Affinity.ARCANE.getColor(), 128, 0.1F);
        stack.popPose();
        stack.scale(0.5F, 0.5F, 0.5F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, 15728880);
    }
}