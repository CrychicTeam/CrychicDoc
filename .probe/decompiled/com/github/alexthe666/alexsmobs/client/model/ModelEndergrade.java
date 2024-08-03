package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityEndergrade;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;

public class ModelEndergrade extends AdvancedEntityModel<EntityEndergrade> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox bodymain;

    private final AdvancedModelBox legbackL;

    private final AdvancedModelBox legbackR;

    private final AdvancedModelBox legmidL;

    private final AdvancedModelBox legmidR;

    private final AdvancedModelBox bodyfront;

    private final AdvancedModelBox head;

    private final AdvancedModelBox mouth;

    private final AdvancedModelBox legfrontL;

    private final AdvancedModelBox legfrontR;

    private final AdvancedModelBox tail;

    public ModelEndergrade() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.bodymain = new AdvancedModelBox(this, "bodymain");
        this.bodymain.setPos(0.0F, -9.0F, -1.0F);
        this.root.addChild(this.bodymain);
        this.bodymain.setTextureOffset(0, 0).addBox(-4.5F, -3.5F, 0.0F, 9.0F, 9.0F, 10.0F, 0.0F, false);
        this.legbackL = new AdvancedModelBox(this, "legbackL");
        this.legbackL.setPos(3.5F, 3.5F, 7.0F);
        this.bodymain.addChild(this.legbackL);
        this.legbackL.setTextureOffset(11, 45).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, false);
        this.legbackR = new AdvancedModelBox(this, "legbackR");
        this.legbackR.setPos(-3.5F, 3.5F, 7.0F);
        this.bodymain.addChild(this.legbackR);
        this.legbackR.setTextureOffset(11, 45).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, true);
        this.legmidL = new AdvancedModelBox(this, "legmidL");
        this.legmidL.setPos(3.5F, 3.5F, 1.0F);
        this.bodymain.addChild(this.legmidL);
        this.legmidL.setTextureOffset(39, 0).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, false);
        this.legmidR = new AdvancedModelBox(this, "legmidR");
        this.legmidR.setPos(-3.5F, 3.5F, 1.0F);
        this.bodymain.addChild(this.legmidR);
        this.legmidR.setTextureOffset(39, 0).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, true);
        this.bodyfront = new AdvancedModelBox(this, "bodyfront");
        this.bodyfront.setPos(0.0F, 0.5F, 0.0F);
        this.bodymain.addChild(this.bodyfront);
        this.bodyfront.setTextureOffset(25, 29).addBox(-4.0F, -3.5F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.5F, -8.0F);
        this.bodyfront.addChild(this.head);
        this.head.setTextureOffset(35, 16).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 6.0F, 4.0F, 0.0F, false);
        this.mouth = new AdvancedModelBox(this, "mouth");
        this.mouth.setPos(0.0F, 1.5F, -4.5F);
        this.head.addChild(this.mouth);
        this.mouth.setTextureOffset(26, 46).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        this.legfrontL = new AdvancedModelBox(this, "legfrontL");
        this.legfrontL.setPos(3.5F, 3.0F, -5.0F);
        this.bodyfront.addChild(this.legfrontL);
        this.legfrontL.setTextureOffset(0, 37).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, false);
        this.legfrontR = new AdvancedModelBox(this, "legfrontR");
        this.legfrontR.setPos(-3.5F, 3.0F, -5.0F);
        this.bodyfront.addChild(this.legfrontR);
        this.legfrontR.setTextureOffset(0, 37).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 7.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setPos(0.5F, -1.0F, 9.9F);
        this.bodymain.addChild(this.tail);
        this.setRotationAngle(this.tail, -0.1745F, 0.0F, 0.0F);
        this.tail.setTextureOffset(0, 20).addBox(-4.0F, -1.5F, -2.4F, 7.0F, 7.0F, 9.0F, 0.0F, false);
        this.updateDefaultPose();
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

    public void setupAnim(EntityEndergrade entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { this.bodyfront, this.bodymain, this.tail };
        AdvancedModelBox[] legPartsRight = new AdvancedModelBox[] { this.legfrontR, this.legmidR, this.legbackR };
        AdvancedModelBox[] legPartsLeft = new AdvancedModelBox[] { this.legfrontL, this.legmidL, this.legbackL };
        float walkSpeed = 1.7F;
        float walkDegree = 0.7F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float birdPitch = entityIn.prevTartigradePitch + (entityIn.tartigradePitch - entityIn.prevTartigradePitch) * partialTick;
        float biteProgress = entityIn.prevBiteProgress + (entityIn.biteProgress - entityIn.prevBiteProgress) * partialTick;
        this.mouth.setScale(1.0F, 1.0F, 1.0F + biteProgress * 0.4F);
        this.bodymain.rotateAngleX += birdPitch * (float) (Math.PI / 180.0);
        this.mouth.rotationPointZ = -3.0F - biteProgress * 0.2F;
        this.chainWave(bodyParts, walkSpeed, walkDegree * 0.3F, -1.0, limbSwing, limbSwingAmount);
        this.chainWave(legPartsRight, walkSpeed, walkDegree, -1.0, limbSwing, limbSwingAmount);
        this.chainWave(legPartsLeft, walkSpeed, walkDegree, -1.0, limbSwing, limbSwingAmount);
        this.chainFlap(legPartsRight, walkSpeed, walkDegree, 3.0, limbSwing, limbSwingAmount);
        this.chainFlap(legPartsLeft, walkSpeed, -walkDegree, 3.0, limbSwing, limbSwingAmount);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.bodymain, this.legbackL, this.legbackR, this.legfrontL, this.legfrontR, this.legmidL, this.legmidR, this.bodyfront, this.head, this.mouth, this.tail, new AdvancedModelBox[0]);
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}