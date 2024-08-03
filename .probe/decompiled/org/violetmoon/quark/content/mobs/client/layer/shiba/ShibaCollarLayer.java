package org.violetmoon.quark.content.mobs.client.layer.shiba;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.mobs.client.model.ShibaModel;
import org.violetmoon.quark.content.mobs.entity.Shiba;

public class ShibaCollarLayer extends RenderLayer<Shiba, ShibaModel> {

    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("quark", "textures/model/entity/shiba/collar.png");

    public ShibaCollarLayer(RenderLayerParent<Shiba, ShibaModel> renderer) {
        super(renderer);
    }

    public void render(@NotNull PoseStack matrix, @NotNull MultiBufferSource buffer, int light, Shiba foxhound, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if (foxhound.m_21824_() && !foxhound.m_20145_()) {
            float[] afloat = foxhound.getCollarColor().getTextureDiffuseColors();
            m_117376_(this.m_117386_(), WOLF_COLLAR, matrix, buffer, light, foxhound, afloat[0], afloat[1], afloat[2]);
        }
    }
}