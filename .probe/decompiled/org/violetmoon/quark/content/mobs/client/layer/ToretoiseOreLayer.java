package org.violetmoon.quark.content.mobs.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.client.model.ToretoiseModel;
import org.violetmoon.quark.content.mobs.entity.Toretoise;

public class ToretoiseOreLayer extends RenderLayer<Toretoise, ToretoiseModel> {

    private static final String ORE_BASE = "quark:textures/model/entity/toretoise/ore%d.png";

    public ToretoiseOreLayer(RenderLayerParent<Toretoise, ToretoiseModel> renderer) {
        super(renderer);
    }

    public void render(@NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light, Toretoise entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        int ore = entity.getOreType();
        if (ore != 0 && ore <= 5) {
            ResourceLocation res = new ResourceLocation(String.format("quark:textures/model/entity/toretoise/ore%d.png", ore));
            m_117376_(this.m_117386_(), res, matrix, buffer, light, entity, 1.0F, 1.0F, 1.0F);
        }
    }
}