package com.mna.entities.renderers.boss.attacks;

import com.mna.entities.boss.attacks.PumpkinKingEntangle;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PumpkinKingEntangleRenderer extends EntityRenderer<PumpkinKingEntangle> {

    public PumpkinKingEntangleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(PumpkinKingEntangle e, float partialTicks, float p_225623_3_, PoseStack stack, MultiBufferSource buffer, int light) {
        if (e.getOrigin() != null) {
            WorldRenderUtils.renderBeam(e.m_9236_(), partialTicks, stack, buffer, light, e.m_20182_(), e.getOrigin(), 1.0F, new int[] { 38, 91, 19 }, 0.1F, MARenderTypes.BOSS_VINE);
        }
    }

    public ResourceLocation getTextureLocation(PumpkinKingEntangle p_110775_1_) {
        return null;
    }
}