package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityMudskipper;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class ModelMudskipper extends AdvancedEntityModel<EntityMudskipper> {

    private final AdvancedModelBox root;

    private final AdvancedModelBox head;

    private final AdvancedModelBox eyes;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox dorsalFin;

    private final AdvancedModelBox tailFin;

    private final AdvancedModelBox leftFin;

    private final AdvancedModelBox rightFin;

    public ModelMudskipper() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setRotationPoint(0.0F, -2.0F, -5.0F);
        this.root.addChild(this.head);
        this.head.setTextureOffset(0, 15).addBox(-3.5F, -3.0F, -4.0F, 7.0F, 5.0F, 6.0F, 0.0F, false);
        this.eyes = new AdvancedModelBox(this, "eyes");
        this.eyes.setRotationPoint(0.0F, -3.0F, -1.5F);
        this.head.addChild(this.eyes);
        this.eyes.setTextureOffset(19, 0).addBox(-2.5F, -2.0F, -1.5F, 5.0F, 2.0F, 3.0F, 0.0F, false);
        this.tail = new AdvancedModelBox(this, "tail");
        this.tail.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.head.addChild(this.tail);
        this.tail.setTextureOffset(0, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 10.0F, 0.0F, false);
        this.tail.setTextureOffset(23, 9).addBox(0.0F, -4.0F, 4.0F, 0.0F, 2.0F, 6.0F, 0.0F, false);
        this.dorsalFin = new AdvancedModelBox(this, "dorsalFin");
        this.dorsalFin.setRotationPoint(0.0F, -2.0F, -2.0F);
        this.tail.addChild(this.dorsalFin);
        this.dorsalFin.setTextureOffset(0, 27).addBox(0.0F, -5.0F, -1.0F, 0.0F, 5.0F, 7.0F, 0.0F, false);
        this.tailFin = new AdvancedModelBox(this, "tailFin");
        this.tailFin.setRotationPoint(0.0F, 0.0F, 9.0F);
        this.tail.addChild(this.tailFin);
        this.tailFin.setTextureOffset(20, 20).addBox(0.0F, -3.0F, -1.0F, 0.0F, 6.0F, 7.0F, 0.0F, false);
        this.leftFin = new AdvancedModelBox(this, "leftFin");
        this.leftFin.setRotationPoint(2.0F, 2.0F, -1.0F);
        this.tail.addChild(this.leftFin);
        this.leftFin.setTextureOffset(28, 18).addBox(0.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F, 0.0F, false);
        this.rightFin = new AdvancedModelBox(this, "rightFin");
        this.rightFin.setRotationPoint(-2.0F, 2.0F, -1.0F);
        this.tail.addChild(this.rightFin);
        this.rightFin.setTextureOffset(28, 18).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.head, this.eyes, this.tail, this.dorsalFin, this.tailFin, this.leftFin, this.rightFin);
    }

    public void setupAnim(EntityMudskipper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float blinkAmount = Math.max(0.0F, ((float) Math.sin((double) (ageInTicks * 0.1F)) - 0.5F) * 2.0F);
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float displayProgress = entity.prevDisplayProgress + (entity.displayProgress - entity.prevDisplayProgress) * partialTick;
        float swimProgress = entity.prevSwimProgress + (entity.swimProgress - entity.prevSwimProgress) * partialTick;
        float sitProgress = entity.prevSitProgress + (entity.sitProgress - entity.prevSitProgress) * partialTick;
        float walkSpeed = 1.0F;
        float walkDegree = 0.7F;
        float swimSpeed = 0.4F;
        float swimDegree = 0.5F;
        float displaySpeed = 0.3F;
        float displayDegree = 0.4F;
        this.progressPositionPrev(this.head, entity.prevMudProgress + (entity.mudProgress - entity.prevMudProgress) * partialTick, 0.0F, -2.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.eyes, blinkAmount, 0.0F, 1.5F, 0.0F, 1.0F);
        this.progressPositionPrev(this.dorsalFin, 5.0F - displayProgress, 0.0F, 2.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, displayProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, displayProgress, Maths.rad(20.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.dorsalFin, displayProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.rightFin, displayProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.leftFin, displayProgress, Maths.rad(-10.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.tailFin, displayProgress, Maths.rad(10.0), 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.head, displayProgress, 0.0F, -0.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.rightFin, displayProgress, 0.0F, -0.5F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftFin, displayProgress, 0.0F, -0.5F, 0.0F, 5.0F);
        this.progressRotationPrev(this.head, sitProgress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tail, sitProgress, 0.0F, Maths.rad(30.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.tailFin, sitProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.leftFin, sitProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.rightFin, sitProgress, 0.0F, Maths.rad(-20.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.rightFin, sitProgress, 0.5F, 0.0F, 0.0F, 5.0F);
        this.progressPositionPrev(this.leftFin, sitProgress, 0.0F, 0.0F, 1.0F, 5.0F);
        float walkSwingAmount = limbSwingAmount * (1.0F - 0.2F * swimProgress);
        float swimSwingAmount = limbSwingAmount * 0.2F * swimProgress;
        this.swing(this.head, swimSpeed, 0.4F * swimDegree, true, 1.0F, 0.0F, limbSwing, swimSwingAmount);
        this.swing(this.tail, swimSpeed, swimDegree, false, 0.0F, 0.0F, limbSwing, swimSwingAmount);
        this.swing(this.tailFin, swimSpeed, swimDegree, false, -1.0F, 0.0F, limbSwing, swimSwingAmount);
        this.flap(this.rightFin, swimSpeed, swimDegree, false, 2.0F, 0.1F, limbSwing, swimSwingAmount);
        this.flap(this.leftFin, swimSpeed, swimDegree, true, 2.0F, 0.1F, limbSwing, swimSwingAmount);
        this.bob(this.head, swimSpeed, swimDegree, false, limbSwing, swimSwingAmount);
        this.swing(this.head, displaySpeed, displayDegree * 0.3F, false, 0.0F, 0.0F, ageInTicks, displayProgress * 0.2F);
        this.swing(this.tail, displaySpeed, displayDegree, true, 0.0F, 0.0F, ageInTicks, displayProgress * 0.2F);
        this.swing(this.tailFin, displaySpeed, displayDegree, true, 0.0F, 0.0F, ageInTicks, displayProgress * 0.2F);
        this.flap(this.dorsalFin, displaySpeed, displayDegree, true, 0.0F, 0.0F, ageInTicks, displayProgress * 0.2F);
        float f1 = walkDegree * 0.15F;
        float headUp = 1.6F * Math.min(0.0F, (float) (Math.sin((double) (limbSwing * walkSpeed)) * (double) walkSwingAmount * (double) f1 * 9.0 - (double) (walkSwingAmount * f1) * 9.0));
        this.head.rotationPointY += headUp;
        this.head.rotationPointZ = this.head.rotationPointZ + (float) (Math.sin((double) (limbSwing * walkSpeed - 1.5F)) * (double) walkSwingAmount * (double) f1 * 9.0 - (double) (walkSwingAmount * f1) * 9.0);
        this.rightFin.rotationPointY += headUp;
        this.leftFin.rotationPointY += headUp;
        this.walk(this.tail, walkSpeed, walkDegree * 0.5F, true, 1.0F, 0.04F, limbSwing, walkSwingAmount);
        this.walk(this.tailFin, walkSpeed, walkDegree * 0.65F, false, 2.0F, -0.04F, limbSwing, walkSwingAmount);
        this.walk(this.head, walkSpeed, walkDegree * 0.5F, false, 0.0F, 0.04F, limbSwing, walkSwingAmount);
        this.flap(this.rightFin, walkSpeed, walkDegree, true, 3.0F, -0.3F, limbSwing, walkSwingAmount);
        this.flap(this.leftFin, walkSpeed, walkDegree, false, 3.0F, -0.3F, limbSwing, walkSwingAmount);
        this.swing(this.rightFin, walkSpeed, walkDegree, false, 2.0F, -0.3F, limbSwing, walkSwingAmount);
        this.swing(this.leftFin, walkSpeed, walkDegree, true, 2.0F, -0.3F, limbSwing, walkSwingAmount);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.45F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.4, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, buffer, packedLight, packedOverlay, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }
}