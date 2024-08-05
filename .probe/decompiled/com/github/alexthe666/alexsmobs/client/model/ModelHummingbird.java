package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityHummingbird;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelHummingbird extends AdvancedEntityModel<EntityHummingbird> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox wingL;

    private final AdvancedModelBox wingL_r1;

    private final AdvancedModelBox wingR;

    private final AdvancedModelBox wingR_r1;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox legL;

    private final AdvancedModelBox legR;

    public ModelHummingbird() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.body = new AdvancedModelBox(this, "body");
        this.body.setPos(0.0F, -0.5F, -0.5F);
        this.root.addChild(this.body);
        this.setRotationAngle(this.body, 0.2618F, 0.0F, 0.0F);
        this.body.setTextureOffset(0, 6).addBox(-1.5F, -4.7F, -1.4F, 3.0F, 5.0F, 3.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -4.4F, 0.6F);
        this.body.addChild(this.head);
        this.setRotationAngle(this.head, -0.2182F, 0.0F, 0.0F);
        this.head.setTextureOffset(10, 12).addBox(-1.5F, -3.1F, -2.1F, 3.0F, 3.0F, 3.0F, 0.1F, false);
        this.head.setTextureOffset(12, 0).addBox(-0.5F, -2.1F, -5.2F, 1.0F, 1.0F, 3.0F, 0.0F, false);
        this.wingL = new AdvancedModelBox(this, "wingL");
        this.wingL.setPos(1.5F, -4.5F, 0.5F);
        this.body.addChild(this.wingL);
        this.wingL_r1 = new AdvancedModelBox(this, "wingL_r1");
        this.wingL_r1.setPos(-0.3F, 0.0F, -1.0F);
        this.wingL.addChild(this.wingL_r1);
        this.setRotationAngle(this.wingL_r1, 0.0F, 0.0F, -0.0873F);
        this.wingL_r1.setTextureOffset(0, 15).addBox(0.0F, 0.0F, -0.1F, 1.0F, 5.0F, 2.0F, 0.0F, false);
        this.wingR = new AdvancedModelBox(this, "wingR");
        this.wingR.setPos(-1.5F, -4.5F, 0.5F);
        this.body.addChild(this.wingR);
        this.wingR_r1 = new AdvancedModelBox(this, "wingR_r1");
        this.wingR_r1.setPos(0.3F, 0.0F, -1.0F);
        this.wingR.addChild(this.wingR_r1);
        this.setRotationAngle(this.wingR_r1, 0.0F, 0.0F, 0.0873F);
        this.wingR_r1.setTextureOffset(0, 15).addBox(-1.0F, 0.0F, -0.1F, 1.0F, 5.0F, 2.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.0F, -0.1F, 1.5F);
        this.body.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.48F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 5.0F, 0.0F, false);
        this.legL = new AdvancedModelBox(this, "legL");
        this.legL.setPos(0.9F, -0.7F, -1.1F);
        this.body.addChild(this.legL);
        this.setRotationAngle(this.legL, -0.2618F, 0.0F, 0.0F);
        this.legL.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        this.legR = new AdvancedModelBox(this, "legR");
        this.legR.setPos(-0.9F, -0.7F, -1.1F);
        this.body.addChild(this.legR);
        this.setRotationAngle(this.legR, -0.2618F, 0.0F, 0.0F);
        this.legR.setTextureOffset(0, 0).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    public void setupAnim(EntityHummingbird entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float flySpeed = 1.0F;
        float flyDegree = 0.8F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float flyProgress = entityIn.prevFlyProgress + (entityIn.flyProgress - entityIn.prevFlyProgress) * partialTick;
        float zoomProgress = entityIn.prevMovingProgress + (entityIn.movingProgress - entityIn.prevMovingProgress) * partialTick;
        float sipProgress = entityIn.prevSipProgress + (entityIn.sipProgress - entityIn.prevSipProgress) * partialTick;
        this.progressRotationPrev(this.body, flyProgress, Maths.rad(30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, flyProgress, Maths.rad(-30.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.wingL, flyProgress, Maths.rad(15.0), Maths.rad(55.0), Maths.rad(-100.0), 5.0F);
        this.progressRotationPrev(this.wingR, flyProgress, Maths.rad(15.0), Maths.rad(-55.0), Maths.rad(100.0), 5.0F);
        this.progressRotationPrev(this.tail, flyProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legL, flyProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.legR, flyProgress, Maths.rad(-45.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.wingL, flyProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.wingR, flyProgress, 0.0F, 1.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legL, flyProgress, 0.0F, -0.4F, 0.0F, 5.0F);
        this.progressPositionPrev(this.legR, flyProgress, 0.0F, -0.4F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, flyProgress, 0.0F, -0.5F, -1.0F, 5.0F);
        if (flyProgress > 0.0F) {
            this.progressRotationPrev(this.body, zoomProgress, Maths.rad(25.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.head, zoomProgress, Maths.rad(-25.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.wingL, zoomProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
            this.progressRotationPrev(this.wingR, zoomProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        }
        this.progressPositionPrev(this.body, sipProgress, 0.0F, -1.0F, 3.0F, 5.0F);
        this.progressRotationPrev(this.head, sipProgress, Maths.rad(60.0), 0.0F, 0.0F, 5.0F);
        if (entityIn.isFlying()) {
            this.flap(this.wingL, flySpeed * 2.3F, flyDegree * 2.3F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.wingL, flySpeed * 2.3F, flyDegree, false, 0.0F, -0.4F, ageInTicks, 1.0F);
            this.flap(this.wingR, flySpeed * 2.3F, flyDegree * 2.3F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
            this.walk(this.wingR, flySpeed * 2.3F, flyDegree, false, 0.0F, -0.4F, ageInTicks, 1.0F);
            this.bob(this.legL, flySpeed * 0.3F, flyDegree * -0.2F, false, ageInTicks, 1.0F);
            this.bob(this.legR, flySpeed * 0.3F, flyDegree * -0.2F, false, ageInTicks, 1.0F);
            this.walk(this.body, flySpeed * 0.3F, flyDegree * 0.05F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
            this.walk(this.tail, flySpeed * 0.3F, flyDegree * 0.1F, false, 1.0F, 0.1F, ageInTicks, 1.0F);
            this.walk(this.head, flySpeed * 0.3F, flyDegree * 0.05F, true, 2.0F, 0.1F, ageInTicks, 1.0F);
            this.bob(this.body, flySpeed * 0.3F, flyDegree * 0.3F, true, ageInTicks, 1.0F);
        }
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.body, this.head, this.wingL, this.wingL_r1, this.wingR, this.wingR_r1, this.tail, this.legL, this.legR);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.75F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }
}