package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class RaycatModel extends AdvancedEntityModel<RaycatEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rarm;

    private final AdvancedModelBox larm;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tail2;

    public RaycatModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 17.5F, 0.0F);
        this.body.setTextureOffset(28, 44).addBox(-2.5F, -3.5F, -7.0F, 5.0F, 7.0F, 13.0F, 0.25F, false);
        this.body.setTextureOffset(0, 34).addBox(-2.5F, -3.5F, -7.0F, 5.0F, 7.0F, 13.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, -3.0F, -7.0F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(20, 19).addBox(-2.5F, -1.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.0F, false);
        this.head.setTextureOffset(40, 20).addBox(-2.5F, -0.5F, -5.0F, 5.0F, 2.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(38, 16).addBox(0.0F, -0.5F, -5.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(21, 0).addBox(-1.5F, 1.5F, -7.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(42, 16).addBox(-0.5F, 1.5F, -7.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(44, 54).addBox(-2.5F, -1.5F, -5.0F, 5.0F, 5.0F, 5.0F, 0.25F, false);
        this.head.setTextureOffset(44, 54).addBox(-2.75F, -2.75F, -1.75F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(44, 54).addBox(1.75F, -2.75F, -1.75F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        this.head.setTextureOffset(56, 60).addBox(-1.5F, 1.5F, -7.0F, 3.0F, 3.0F, 2.0F, 0.25F, false);
        this.rarm = new AdvancedModelBox(this);
        this.rarm.setRotationPoint(-2.0F, -2.0F, -5.0F);
        this.body.addChild(this.rarm);
        this.rarm.setTextureOffset(56, 51).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.25F, true);
        this.rarm.setTextureOffset(0, 34).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, true);
        this.larm = new AdvancedModelBox(this);
        this.larm.setRotationPoint(2.0F, -2.0F, -5.0F);
        this.body.addChild(this.larm);
        this.larm.setTextureOffset(56, 51).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.25F, false);
        this.larm.setTextureOffset(0, 34).addBox(-1.0F, -2.5F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-1.5F, 2.0F, 5.0F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(34, 0).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);
        this.rleg.setTextureOffset(56, 58).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.25F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(1.5F, 2.0F, 5.0F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(56, 58).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.25F, false);
        this.lleg.setTextureOffset(34, 0).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -2.5F, 6.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(48, 55).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        this.tail.setTextureOffset(22, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 0.0F, false);
        this.tail2 = new AdvancedModelBox(this);
        this.tail2.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.tail.addChild(this.tail2);
        this.tail2.setTextureOffset(48, 55).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F, 0.0F, false);
        this.tail2.setTextureOffset(42, 0).addBox(0.0F, -0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.tail2, this.head, this.lleg, this.rleg, this.rarm, this.larm);
    }

    public void setupAnim(RaycatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 0.7F;
        float walkDegree = 1.0F;
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float layProgress = entity.getLayProgress(partialTicks);
        float sitProgress = entity.getSitProgress(partialTicks) * (1.0F - layProgress);
        this.progressRotationPrev(this.body, sitProgress, (float) Math.toRadians(-35.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, sitProgress, (float) Math.toRadians(-45.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, sitProgress, (float) Math.toRadians(-45.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, sitProgress, 0.0F, 4.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.larm, sitProgress, 0.0F, 4.0F, -1.0F, 1.0F);
        this.progressRotationPrev(this.rarm, sitProgress, (float) Math.toRadians(35.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.larm, sitProgress, (float) Math.toRadians(35.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, sitProgress, (float) Math.toRadians(35.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, sitProgress, 0.0F, -1.0F, -2.0F, 1.0F);
        this.progressRotationPrev(this.head, sitProgress, (float) Math.toRadians(35.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, layProgress, 0.0F, 4.0F, -1.0F, 1.0F);
        this.progressRotationPrev(this.body, layProgress, (float) Math.toRadians(-30.0), 0.0F, (float) Math.toRadians(90.0), 1.0F);
        this.progressPositionPrev(this.head, layProgress, -2.0F, 2.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, layProgress, (float) Math.toRadians(-10.0), 0.0F, (float) Math.toRadians(-75.0), 1.0F);
        this.progressRotationPrev(this.rleg, layProgress, (float) Math.toRadians(-45.0), 0.0F, (float) Math.toRadians(-40.0), 1.0F);
        this.progressRotationPrev(this.lleg, layProgress, (float) Math.toRadians(25.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rarm, layProgress, -1.0F, 1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rarm, layProgress, (float) Math.toRadians(15.0), 0.0F, (float) Math.toRadians(-25.0), 1.0F);
        this.progressRotationPrev(this.larm, layProgress, (float) Math.toRadians(-15.0), 0.0F, 0.0F, 1.0F);
        this.walk(this.tail, 0.1F, 0.05F, true, 0.0F, 0.15F, ageInTicks, 1.0F);
        this.swing(this.tail, 0.1F, 0.2F, false, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, 0.1F, 0.3F, false, -2.5F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, 0.1F, 0.05F, true, 0.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.tail, walkSpeed, walkDegree * 0.1F, true, 0.0F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.tail2, walkSpeed, walkDegree * 0.2F, true, 0.0F, -0.2F, limbSwing, limbSwingAmount);
        this.walk(this.rleg, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.lleg, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.rarm, walkSpeed, walkDegree * 0.8F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.walk(this.larm, walkSpeed, walkDegree * 0.8F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}