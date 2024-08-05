package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityBananaSlug;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class ModelBananaSlug extends AdvancedEntityModel<EntityBananaSlug> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox goo;

    private final AdvancedModelBox leftAntenna;

    private final AdvancedModelBox rightAntenna;

    private final AdvancedModelBox tail;

    public ModelBananaSlug() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setRotationPoint(0.0F, -2.0F, -2.0F);
        this.root.addChild(this.body);
        this.body.setTextureOffset(18, 23).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 7.0F, 0.0F, false);
        this.goo = new AdvancedModelBox(this, "goo");
        this.goo.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.body.addChild(this.goo);
        this.goo.setTextureOffset(0, 0).addBox(-2.5F, -0.001F, 0.0F, 5.0F, 0.0F, 17.0F, 0.0F, false);
        this.leftAntenna = new AdvancedModelBox(this, "leftAntenna");
        this.leftAntenna.setRotationPoint(2.0F, -1.0F, -4.0F);
        this.body.addChild(this.leftAntenna);
        this.setRotationAngle(this.leftAntenna, 0.0F, 0.0F, -0.0873F);
        this.leftAntenna.setTextureOffset(0, 0).addBox(0.0F, -1.0F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);
        this.rightAntenna = new AdvancedModelBox(this, "rightAntenna");
        this.rightAntenna.setRotationPoint(-2.0F, -1.0F, -4.0F);
        this.body.addChild(this.rightAntenna);
        this.setRotationAngle(this.rightAntenna, 0.0F, 0.0F, 0.0873F);
        this.rightAntenna.setTextureOffset(0, 0).addBox(0.0F, -1.0F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 18).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 3.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    public void setupAnim(EntityBananaSlug entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.25F;
        float idleDegree = 0.25F;
        float walkSpeed = 1.0F;
        float walkDegree = 0.2F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        this.swing(this.leftAntenna, idleSpeed, idleDegree * 0.2F, true, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.rightAntenna, idleSpeed, idleDegree * 0.2F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.leftAntenna, idleSpeed, idleDegree * 0.5F, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.walk(this.rightAntenna, idleSpeed, idleDegree * 0.5F, true, 3.0F, 0.1F, ageInTicks, 1.0F);
        this.swing(this.tail, walkSpeed, walkDegree, true, 3.0F, 0.0F, limbSwing, limbSwingAmount);
        float antennaBack = -0.5F + (float) Math.sin((double) (ageInTicks * idleSpeed) + 3.0) * 0.2F;
        this.leftAntenna.rotationPointZ -= antennaBack;
        this.rightAntenna.rotationPointZ -= antennaBack;
        float stretch1 = (float) (Math.sin((double) (limbSwing * -walkSpeed)) * (double) limbSwingAmount) + limbSwingAmount;
        float stretch2 = (float) (Math.sin((double) (limbSwing * -walkSpeed + 1.0F)) * (double) limbSwingAmount) + limbSwingAmount;
        this.body.setScale(1.0F, 1.0F - stretch1 * 0.025F, 1.0F + stretch1 * 0.25F);
        this.tail.setScale(1.0F, 1.0F - stretch2 * 0.05F, 1.0F + stretch2 * 0.5F);
        this.body.setShouldScaleChildren(false);
        this.body.rotationPointZ -= stretch1 * 2.0F;
        this.leftAntenna.rotationPointZ -= stretch1 * 1.0F;
        this.rightAntenna.rotationPointZ -= stretch1 * 1.0F;
        this.leftAntenna.rotateAngleY += netHeadYaw * 0.6F * (float) (Math.PI / 180.0);
        this.leftAntenna.rotateAngleX += headPitch * 0.3F * (float) (Math.PI / 180.0);
        this.rightAntenna.rotateAngleY += netHeadYaw * 0.6F * (float) (Math.PI / 180.0);
        this.rightAntenna.rotateAngleX += headPitch * 0.3F * (float) (Math.PI / 180.0);
        float yaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float slimeYaw = Mth.wrapDegrees(entity.prevTrailYaw + (entity.trailYaw - entity.prevTrailYaw) * partialTick - yaw) * 0.65F;
        this.goo.rotationPointX = Mth.sin(limbSwing * -walkSpeed - 1.0F) * limbSwingAmount;
        this.goo.rotateAngleY = this.goo.rotateAngleY + Maths.rad((double) slimeYaw);
        this.tail.rotateAngleY = this.tail.rotateAngleY + Maths.rad((double) (slimeYaw * 0.8F));
        this.goo.setScale(1.0F, 0.0F, 1.0F + limbSwingAmount);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0, 0.8, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.goo, this.tail, this.leftAntenna, this.rightAntenna);
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}