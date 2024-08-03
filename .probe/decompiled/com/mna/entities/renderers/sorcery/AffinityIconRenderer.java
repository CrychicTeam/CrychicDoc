package com.mna.entities.renderers.sorcery;

import com.mna.api.affinity.Affinity;
import com.mna.entities.sorcery.AffinityIcon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.HashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class AffinityIconRenderer extends EntityRenderer<AffinityIcon> {

    private static HashMap<Affinity, ResourceLocation> affinityIconMap = new HashMap();

    public AffinityIconRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(AffinityIcon entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        float scaleFactor = 0.5F;
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RenderType.entityTranslucent((ResourceLocation) affinityIconMap.get(entityIn.getAffinity())));
        matrixStackIn.pushPose();
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(portalRotation);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.translate(0.0F, -0.25F, 0.0F);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = 1.0F;
        float nrmH = 1.0F;
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV);
        matrixStackIn.popPose();
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(255, 255, 255, 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }

    public ResourceLocation getTextureLocation(AffinityIcon entity) {
        return null;
    }

    static {
        affinityIconMap.put(Affinity.UNKNOWN, new ResourceLocation("mna", "textures/gui/affinity/arcane.png"));
        affinityIconMap.put(Affinity.ARCANE, new ResourceLocation("mna", "textures/gui/affinity/arcane.png"));
        affinityIconMap.put(Affinity.EARTH, new ResourceLocation("mna", "textures/gui/affinity/earth.png"));
        affinityIconMap.put(Affinity.ENDER, new ResourceLocation("mna", "textures/gui/affinity/ender.png"));
        affinityIconMap.put(Affinity.FIRE, new ResourceLocation("mna", "textures/gui/affinity/fire.png"));
        affinityIconMap.put(Affinity.LIGHTNING, new ResourceLocation("mna", "textures/gui/affinity/lightning.png"));
        affinityIconMap.put(Affinity.HELLFIRE, new ResourceLocation("mna", "textures/gui/affinity/fire.png"));
        affinityIconMap.put(Affinity.WATER, new ResourceLocation("mna", "textures/gui/affinity/water.png"));
        affinityIconMap.put(Affinity.ICE, new ResourceLocation("mna", "textures/gui/affinity/ice.png"));
        affinityIconMap.put(Affinity.WIND, new ResourceLocation("mna", "textures/gui/affinity/air.png"));
    }
}