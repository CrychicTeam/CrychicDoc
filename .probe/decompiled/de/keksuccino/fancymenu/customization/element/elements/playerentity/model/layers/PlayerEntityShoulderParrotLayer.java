package de.keksuccino.fancymenu.customization.element.elements.playerentity.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.PlayerEntityElementRenderer;
import de.keksuccino.fancymenu.customization.element.elements.playerentity.model.PlayerEntityProperties;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Parrot;
import org.jetbrains.annotations.Nullable;

public class PlayerEntityShoulderParrotLayer extends PlayerEntityRenderLayer {

    private final ParrotModel model;

    public final PlayerEntityProperties properties;

    public PlayerEntityShoulderParrotLayer(PlayerEntityElementRenderer renderer, EntityModelSet modelSet, PlayerEntityProperties properties) {
        super(renderer);
        this.model = new ParrotModel(modelSet.bakeLayer(ModelLayers.PARROT));
        this.properties = properties;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, @Nullable Entity entity, float float3, float float4, float float5, float float6, float float7, float float8) {
        if (this.properties.hasParrotOnShoulder) {
            this.render(poseStack0, multiBufferSource1, int2, float3, float4, float7, float8, this.properties.parrotOnLeftShoulder);
        }
    }

    private void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, float float3, float float4, float float5, float float6, boolean onLeftShoulder) {
        poseStack0.pushPose();
        poseStack0.translate(onLeftShoulder ? 0.4F : -0.4F, this.properties.isCrouching() ? -1.3F : -1.5F, 0.0F);
        Parrot.Variant parrot$variant = Parrot.Variant.byId(this.properties.shoulderParrotVariant);
        VertexConsumer vertexconsumer = multiBufferSource1.getBuffer(this.model.m_103119_(ParrotRenderer.getVariantTexture(parrot$variant)));
        this.model.renderOnShoulder(poseStack0, vertexconsumer, int2, OverlayTexture.NO_OVERLAY, float3, float4, float5, float6, this.properties.tickCount);
        poseStack0.popPose();
    }
}