package io.redspace.ironsspellbooks.entity.spells.devour_jaw;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DevourJawRenderer extends EntityRenderer<DevourJaw> {

    private final DevourJawRenderer.DevourJawModel model;

    public DevourJawRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new DevourJawRenderer.DevourJawModel(pContext.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    public void render(DevourJaw entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (entity.f_19797_ >= 5) {
            float f = (float) entity.f_19797_ + partialTicks;
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-entity.m_146908_()));
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            poseStack.scale(1.85F, 1.85F, 1.85F);
            this.model.setupAnim(entity, f, 0.0F, 0.0F, entity.m_146908_(), entity.m_146909_());
            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
            this.model.m_7695_(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
            super.render(entity, yaw, partialTicks, poseStack, multiBufferSource, light);
        }
    }

    public ResourceLocation getTextureLocation(DevourJaw pEntity) {
        return IronsSpellbooks.id("textures/entity/devour_jaw.png");
    }

    static class DevourJawModel extends EvokerFangsModel<DevourJaw> {

        private final ModelPart root;

        private final ModelPart base;

        private final ModelPart upperJaw;

        private final ModelPart lowerJaw;

        public DevourJawModel(ModelPart pRoot) {
            super(pRoot);
            this.root = pRoot;
            this.base = pRoot.getChild("base");
            this.upperJaw = pRoot.getChild("upper_jaw");
            this.lowerJaw = pRoot.getChild("lower_jaw");
        }

        public void setupAnim(DevourJaw entity, float time, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            time -= 5.0F;
            float interval = (float) (13 - 5);
            float f = Mth.clamp(time / interval, 0.0F, 1.0F);
            f = 1.0F - f * f * f * f;
            this.upperJaw.zRot = (float) Math.PI - f * 0.35F * (float) Math.PI;
            this.lowerJaw.zRot = (float) Math.PI + f * 0.35F * (float) Math.PI;
            float f2 = time / interval;
            f2 = 0.5F * Mth.cos((float) (Math.PI / 2) * (f2 - 1.0F)) + 0.5F;
            f2 *= f2;
            this.upperJaw.y = -18.0F * f2 + 16.0F;
            this.lowerJaw.y = this.upperJaw.y;
            this.base.y = this.upperJaw.y;
        }
    }
}