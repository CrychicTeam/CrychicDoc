package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class WolfCollarLayer extends RenderLayer<Wolf, WolfModel<Wolf>> {

    private static final ResourceLocation WOLF_COLLAR_LOCATION = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public WolfCollarLayer(RenderLayerParent<Wolf, WolfModel<Wolf>> renderLayerParentWolfWolfModelWolf0) {
        super(renderLayerParentWolfWolfModelWolf0);
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, Wolf wolf3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (wolf3.m_21824_() && !wolf3.m_20145_()) {
            float[] $$10 = wolf3.getCollarColor().getTextureDiffuseColors();
            m_117376_(this.m_117386_(), WOLF_COLLAR_LOCATION, poseStack0, multiBufferSource1, int2, wolf3, $$10[0], $$10[1], $$10[2]);
        }
    }
}