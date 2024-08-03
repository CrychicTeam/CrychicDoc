package com.github.alexthe666.alexsmobs.client.model;

import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public class ModelCockroach extends AdvancedEntityModel<EntityCockroach> {

    public final AdvancedModelBox root;

    public final AdvancedModelBox abdomen;

    public final AdvancedModelBox left_leg_front;

    public final AdvancedModelBox right_leg_front;

    public final AdvancedModelBox left_leg_back;

    public final AdvancedModelBox right_leg_back;

    public final AdvancedModelBox left_leg_mid;

    public final AdvancedModelBox right_leg_mid;

    public final AdvancedModelBox left_wing;

    public final AdvancedModelBox right_wing;

    public final AdvancedModelBox neck;

    public final AdvancedModelBox head;

    public final AdvancedModelBox left_antenna;

    public final AdvancedModelBox right_antenna;

    public ModelCockroach() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.root = new AdvancedModelBox(this, "root");
        this.root.setPos(0.0F, 24.0F, 0.0F);
        this.abdomen = new AdvancedModelBox(this, "abdomen");
        this.abdomen.setPos(0.0F, -1.6F, -1.0F);
        this.root.addChild(this.abdomen);
        this.abdomen.setTextureOffset(0, 12).addBox(-2.0F, -0.9F, -2.0F, 4.0F, 2.0F, 9.0F, 0.0F, false);
        this.left_leg_front = new AdvancedModelBox(this, "left_leg_front");
        this.left_leg_front.setPos(1.5F, 0.6F, -2.0F);
        this.abdomen.addChild(this.left_leg_front);
        this.setRotationAngle(this.left_leg_front, 0.0F, 0.0F, 0.1309F);
        this.left_leg_front.setTextureOffset(0, 24).addBox(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 3.0F, 0.0F, false);
        this.right_leg_front = new AdvancedModelBox(this, "right_leg_front");
        this.right_leg_front.setPos(-1.5F, 0.6F, -2.0F);
        this.abdomen.addChild(this.right_leg_front);
        this.setRotationAngle(this.right_leg_front, 0.0F, 0.0F, -0.1309F);
        this.right_leg_front.setTextureOffset(0, 24).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 3.0F, 0.0F, true);
        this.left_leg_back = new AdvancedModelBox(this, "left_leg_back");
        this.left_leg_back.setPos(1.5F, 0.6F, 3.0F);
        this.abdomen.addChild(this.left_leg_back);
        this.setRotationAngle(this.left_leg_back, -0.0436F, -0.5236F, 0.1745F);
        this.left_leg_back.setTextureOffset(18, 12).addBox(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 5.0F, 0.0F, false);
        this.right_leg_back = new AdvancedModelBox(this, "right_leg_back");
        this.right_leg_back.setPos(-1.5F, 0.6F, 3.0F);
        this.abdomen.addChild(this.right_leg_back);
        this.setRotationAngle(this.right_leg_back, -0.0436F, 0.5236F, -0.1745F);
        this.right_leg_back.setTextureOffset(18, 12).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 5.0F, 0.0F, true);
        this.left_leg_mid = new AdvancedModelBox(this, "left_leg_mid");
        this.left_leg_mid.setPos(1.5F, 0.6F, 0.0F);
        this.abdomen.addChild(this.left_leg_mid);
        this.setRotationAngle(this.left_leg_mid, -0.0436F, -0.2182F, 0.1309F);
        this.left_leg_mid.setTextureOffset(23, 20).addBox(0.0F, 0.0F, 0.0F, 7.0F, 0.0F, 4.0F, 0.0F, false);
        this.right_leg_mid = new AdvancedModelBox(this, "right_leg_mid");
        this.right_leg_mid.setPos(-1.5F, 0.6F, 0.0F);
        this.abdomen.addChild(this.right_leg_mid);
        this.setRotationAngle(this.right_leg_mid, -0.0436F, 0.2182F, -0.1309F);
        this.right_leg_mid.setTextureOffset(23, 20).addBox(-7.0F, 0.0F, 0.0F, 7.0F, 0.0F, 4.0F, 0.0F, true);
        this.left_wing = new AdvancedModelBox(this, "left_wing");
        this.left_wing.setPos(0.0F, -1.4F, -2.0F);
        this.abdomen.addChild(this.left_wing);
        this.left_wing.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 0.0F, 3.0F, 1.0F, 10.0F, 0.0F, false);
        this.right_wing = new AdvancedModelBox(this, "right_wing");
        this.right_wing.setPos(0.0F, -1.4F, -2.0F);
        this.abdomen.addChild(this.right_wing);
        this.right_wing.setTextureOffset(0, 0).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 1.0F, 10.0F, 0.0F, true);
        this.neck = new AdvancedModelBox(this, "neck");
        this.neck.setPos(0.0F, 0.0F, -2.0F);
        this.abdomen.addChild(this.neck);
        this.neck.setTextureOffset(21, 25).addBox(-2.5F, -1.6F, -2.0F, 5.0F, 3.0F, 2.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this, "head");
        this.head.setPos(0.0F, -0.1F, -2.0F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(0, 28).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);
        this.left_antenna = new AdvancedModelBox(this, "left_antenna");
        this.left_antenna.setPos(0.1F, -1.0F, -2.0F);
        this.head.addChild(this.left_antenna);
        this.setRotationAngle(this.left_antenna, -0.2182F, -0.2618F, 0.1309F);
        this.left_antenna.setTextureOffset(17, 0).addBox(0.0F, 0.0F, -8.0F, 5.0F, 0.0F, 8.0F, 0.0F, false);
        this.right_antenna = new AdvancedModelBox(this, "right_antenna");
        this.right_antenna.setPos(-0.1F, -1.0F, -2.0F);
        this.head.addChild(this.right_antenna);
        this.setRotationAngle(this.right_antenna, -0.2182F, 0.2618F, -0.1309F);
        this.right_antenna.setTextureOffset(17, 0).addBox(-5.0F, 0.0F, -8.0F, 5.0F, 0.0F, 8.0F, 0.0F, true);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.abdomen, this.neck, this.head, this.left_antenna, this.right_antenna, this.left_leg_front, this.right_leg_front, this.left_leg_mid, this.right_leg_mid, this.left_leg_back, this.right_leg_back, new AdvancedModelBox[] { this.left_wing, this.right_wing });
    }

    public void setupAnim(EntityCockroach entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float idleSpeed = 0.25F;
        float idleDegree = 0.25F;
        float flySpeed = 0.5F;
        float flyDegree = 0.5F;
        float walkSpeed = 1.25F;
        float walkDegree = 0.5F;
        float partialTick = Minecraft.getInstance().getFrameTime();
        float danceProgress = entity.prevDanceProgress + (entity.danceProgress - entity.prevDanceProgress) * partialTick;
        this.progressRotationPrev(this.abdomen, danceProgress, Maths.rad(-70.0), 0.0F, 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg_front, danceProgress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg_front, danceProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.left_leg_mid, danceProgress, 0.0F, Maths.rad(-10.0), 0.0F, 5.0F);
        this.progressRotationPrev(this.right_leg_mid, danceProgress, 0.0F, Maths.rad(10.0), 0.0F, 5.0F);
        this.progressPositionPrev(this.abdomen, danceProgress, 0.0F, -15.0F, 2.0F, 5.0F);
        if (danceProgress > 0.0F) {
            this.walk(this.left_antenna, 0.5F, 0.5F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
            this.walk(this.right_antenna, 0.5F, 0.5F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
            if (entity.hasMaracas()) {
                this.swing(this.abdomen, 0.5F, 0.15F, false, 0.0F, 0.0F, ageInTicks, 1.0F);
                this.flap(this.abdomen, 0.5F, 0.15F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
                this.bob(this.abdomen, 0.25F, 10.0F, true, ageInTicks, 1.0F);
                this.swing(this.right_leg_front, 0.5F, 0.5F, false, 0.0F, -0.05F, ageInTicks, 1.0F);
                this.swing(this.left_leg_front, 0.5F, 0.5F, false, 0.0F, -0.05F, ageInTicks, 1.0F);
                this.swing(this.right_leg_mid, 0.5F, 0.5F, false, 2.0F, -0.05F, ageInTicks, 1.0F);
                this.swing(this.left_leg_mid, 0.5F, 0.5F, false, 2.0F, -0.05F, ageInTicks, 1.0F);
            } else {
                float spinDegree = Mth.wrapDegrees(ageInTicks * 15.0F);
                this.abdomen.rotateAngleY = (float) (Math.toRadians((double) spinDegree) * (double) danceProgress * 0.2F);
                this.bob(this.abdomen, 0.25F, 10.0F, true, ageInTicks, 1.0F);
            }
        }
        this.swing(this.left_antenna, idleSpeed, idleDegree, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.right_antenna, idleSpeed, idleDegree, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.left_antenna, idleSpeed, idleDegree * 0.25F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.right_antenna, idleSpeed, idleDegree * 0.25F, false, -1.0F, -0.05F, ageInTicks, 1.0F);
        this.walk(this.head, idleSpeed * 0.5F, idleDegree * 0.5F, false, 0.0F, 0.1F, ageInTicks, 1.0F);
        if (entity.randomWingFlapTick > 0) {
            this.swing(this.left_wing, flySpeed * 3.3F, flyDegree * 0.6F, true, 0.0F, -0.2F, ageInTicks, 1.0F);
            this.swing(this.right_wing, flySpeed * 3.3F, flyDegree * 0.6F, false, 0.0F, -0.2F, ageInTicks, 1.0F);
        }
        this.swing(this.right_leg_front, walkSpeed, walkDegree, false, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.right_leg_back, walkSpeed, walkDegree, false, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.left_leg_mid, walkSpeed, walkDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.bob(this.abdomen, walkSpeed, walkDegree * 2.5F, true, limbSwing, limbSwingAmount);
        this.swing(this.left_leg_front, walkSpeed, walkDegree, true, 1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.left_leg_back, walkSpeed, walkDegree, true, -1.0F, 0.0F, limbSwing, limbSwingAmount);
        this.swing(this.right_leg_mid, walkSpeed, walkDegree, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head });
        if (entity.isHeadless()) {
            this.head.showModel = false;
            this.left_antenna.showModel = false;
            this.right_antenna.showModel = false;
        } else {
            this.head.showModel = true;
            this.left_antenna.showModel = true;
            this.right_antenna.showModel = true;
        }
        if (entity.m_6162_()) {
            this.left_wing.showModel = false;
            this.right_wing.showModel = false;
        } else {
            this.left_wing.showModel = true;
            this.right_wing.showModel = true;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            this.head.setScale(1.5F, 1.5F, 1.5F);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.65F, 0.65F, 0.65F);
            matrixStackIn.translate(0.0, 0.815, 0.125);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        } else {
            this.head.setScale(1.0F, 1.0F, 1.0F);
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void setRotationAngle(AdvancedModelBox advancedModelBox, float x, float y, float z) {
        advancedModelBox.rotateAngleX = x;
        advancedModelBox.rotateAngleY = y;
        advancedModelBox.rotateAngleZ = z;
    }
}