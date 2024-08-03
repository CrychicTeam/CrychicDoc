package org.violetmoon.quark.content.mobs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.client.model.CrabModel;
import org.violetmoon.quark.content.mobs.entity.Crab;

public class CrabMoldLayer extends RenderLayer<Crab, CrabModel> {

    private static final ResourceLocation MOLD_LAYER = new ResourceLocation("quark", "textures/model/entity/crab/mold_layer.png");

    public CrabMoldLayer(RenderLayerParent<Crab, CrabModel> renderer) {
        super(renderer);
    }

    public void render(@NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light, Crab crab, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (crab.getVariant() >= 3) {
            m_117376_(this.m_117386_(), MOLD_LAYER, matrix, buffer, light, crab, 1.0F, 1.0F, 1.0F);
        }
    }
}