package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.server.entity.item.DinosaurSpiritEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class SubterranodonModel extends AdvancedEntityModel<SubterranodonEntity> {

    private final AdvancedModelBox body;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox head;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox lwing;

    private final AdvancedModelBox lhand;

    private final AdvancedModelBox lwingTip;

    private final AdvancedModelBox rwing;

    private final AdvancedModelBox rhand;

    private final AdvancedModelBox rwingTip;

    private final AdvancedModelBox lleg;

    private final AdvancedModelBox ltalon;

    private final AdvancedModelBox rleg;

    private final AdvancedModelBox rtalon;

    private final AdvancedModelBox tail;

    private final AdvancedModelBox tailTip;

    public SubterranodonModel() {
        this.texWidth = 256;
        this.texHeight = 256;
        this.body = new AdvancedModelBox(this);
        this.body.setRotationPoint(0.0F, 19.0F, -1.0F);
        this.body.setTextureOffset(0, 46).addBox(-5.0F, -5.0F, -7.0F, 10.0F, 10.0F, 14.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(0.0F, -4.0F, -6.5F);
        this.body.addChild(this.neck);
        this.neck.setTextureOffset(74, 27).addBox(-2.0F, -1.0F, -8.5F, 4.0F, 5.0F, 8.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(0.0F, 1.0F, -5.5F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(48, 46).addBox(0.0F, -15.0F, -13.0F, 0.0F, 12.0F, 19.0F, 0.0F, false);
        this.head.setTextureOffset(0, 79).addBox(-3.0F, -6.0F, -8.0F, 6.0F, 7.0F, 9.0F, 0.0F, false);
        this.head.setTextureOffset(74, 10).addBox(-1.0F, -3.0F, -22.0F, 2.0F, 3.0F, 14.0F, 0.0F, false);
        this.head.setTextureOffset(51, 78).addBox(-2.0F, -7.0F, -32.0F, 4.0F, 9.0F, 10.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, -1.0F, -8.0F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(0, 0).addBox(-1.5F, -3.0F, -23.5F, 3.0F, 8.0F, 9.0F, 0.0F, false);
        this.jaw.setTextureOffset(71, 62).addBox(-0.5F, -1.0F, -15.0F, 1.0F, 3.0F, 15.0F, 0.0F, false);
        this.lwing = new AdvancedModelBox(this);
        this.lwing.setRotationPoint(4.5F, -3.0F, -5.0F);
        this.body.addChild(this.lwing);
        this.lwing.setTextureOffset(12, 36).addBox(0.5F, 0.5F, 2.0F, 26.0F, 0.0F, 10.0F, 0.0F, false);
        this.lwing.setTextureOffset(72, 42).addBox(0.5F, -1.0F, -2.0F, 26.0F, 3.0F, 4.0F, 0.0F, false);
        this.lhand = new AdvancedModelBox(this);
        this.lhand.setRotationPoint(25.0F, 0.5F, -2.0F);
        this.lwing.addChild(this.lhand);
        this.lhand.setTextureOffset(30, 87).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        this.lwingTip = new AdvancedModelBox(this);
        this.lwingTip.setRotationPoint(26.5F, 0.5F, -1.0F);
        this.lwing.addChild(this.lwingTip);
        this.lwingTip.setTextureOffset(0, 0).addBox(0.0F, 0.0F, 1.0F, 26.0F, 0.0F, 24.0F, 0.0F, false);
        this.lwingTip.setTextureOffset(72, 49).addBox(0.0F, -1.0F, -1.0F, 26.0F, 2.0F, 2.0F, 0.0F, false);
        this.rwing = new AdvancedModelBox(this);
        this.rwing.setRotationPoint(-4.5F, -3.0F, -5.0F);
        this.body.addChild(this.rwing);
        this.rwing.setTextureOffset(12, 36).addBox(-26.5F, 0.5F, 2.0F, 26.0F, 0.0F, 10.0F, 0.0F, true);
        this.rwing.setTextureOffset(72, 42).addBox(-26.5F, -1.0F, -2.0F, 26.0F, 3.0F, 4.0F, 0.0F, true);
        this.rhand = new AdvancedModelBox(this);
        this.rhand.setRotationPoint(-25.0F, 2.0F, -2.0F);
        this.rwing.addChild(this.rhand);
        this.rhand.setTextureOffset(30, 87).addBox(-1.5F, -1.5F, -4.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
        this.rwingTip = new AdvancedModelBox(this);
        this.rwingTip.setRotationPoint(-26.5F, 0.5F, -1.0F);
        this.rwing.addChild(this.rwingTip);
        this.rwingTip.setTextureOffset(72, 49).addBox(-26.0F, -1.0F, -1.0F, 26.0F, 2.0F, 2.0F, 0.0F, true);
        this.rwingTip.setTextureOffset(0, 0).addBox(-26.0F, 0.0F, 1.0F, 26.0F, 0.0F, 24.0F, 0.0F, true);
        this.lleg = new AdvancedModelBox(this);
        this.lleg.setRotationPoint(2.0F, -2.5F, 6.5F);
        this.body.addChild(this.lleg);
        this.lleg.setTextureOffset(25, 70).addBox(-2.0F, 0.0F, -0.5F, 5.0F, 0.0F, 13.0F, 0.0F, false);
        this.ltalon = new AdvancedModelBox(this);
        this.ltalon.setRotationPoint(0.5F, 0.0F, 12.5F);
        this.lleg.addChild(this.ltalon);
        this.ltalon.setTextureOffset(98, 30).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F, 0.0F, false);
        this.rleg = new AdvancedModelBox(this);
        this.rleg.setRotationPoint(-2.0F, -2.5F, 6.5F);
        this.body.addChild(this.rleg);
        this.rleg.setTextureOffset(25, 70).addBox(-3.0F, 0.0F, -0.5F, 5.0F, 0.0F, 13.0F, 0.0F, true);
        this.rtalon = new AdvancedModelBox(this);
        this.rtalon.setRotationPoint(-0.5F, 0.0F, 12.5F);
        this.rleg.addChild(this.rtalon);
        this.rtalon.setTextureOffset(98, 30).addBox(-2.5F, 0.0F, 0.0F, 5.0F, 4.0F, 4.0F, 0.0F, true);
        this.tail = new AdvancedModelBox(this);
        this.tail.setRotationPoint(0.0F, -3.5F, 6.5F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(41, 46).addBox(-3.0F, 0.0F, -0.5F, 6.0F, 0.0F, 19.0F, 0.0F, false);
        this.tailTip = new AdvancedModelBox(this);
        this.tailTip.setRotationPoint(0.0F, 0.0F, 18.5F);
        this.tail.addChild(this.tailTip);
        this.tailTip.setTextureOffset(0, 51).addBox(0.0F, -5.0F, 0.0F, 0.0F, 9.0F, 19.0F, 0.0F, false);
        this.tailTip.setTextureOffset(29, 46).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 19.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.body);
    }

    public void setupAnim(SubterranodonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        float walkSpeed = 1.0F;
        float walkDegree = 1.0F;
        float partialTick = ageInTicks - (float) entity.f_19797_;
        float flyProgress = entity.getFlyProgress(partialTick);
        float buryEggsAmount = entity.getBuryEggsProgress(partialTick);
        float groundProgress = 1.0F - flyProgress;
        float flapAmount = flyProgress * entity.getFlapAmount(partialTick);
        float groundStill = groundProgress * (1.0F - limbSwingAmount);
        float groundMove = groundProgress * limbSwingAmount;
        float glide = flyProgress * (1.0F - flapAmount);
        float hoverProgress = flyProgress * entity.getHoverProgress(partialTick);
        float openMouthProgress = entity.getBiteProgress(partialTick);
        float sitProgress = entity.getSitProgress(partialTick) * groundProgress;
        float rollAmount = entity.getFlightRoll(partialTick) / (180.0F / (float) Math.PI) * flyProgress;
        float pitchAmount = entity.getFlightPitch(partialTick) / (180.0F / (float) Math.PI) * (flyProgress - hoverProgress);
        float yaw = entity.f_20884_ + (entity.f_20883_ - entity.f_20884_) * partialTick;
        float tailYaw = Mth.wrapDegrees(entity.getTailYaw(partialTick) - yaw) / (180.0F / (float) Math.PI);
        float danceAmount = entity.getDanceProgress(partialTick);
        float danceSpeed = 0.5F;
        this.progressPositionPrev(this.body, groundProgress, 0.0F, -8.0F, 2.0F, 1.0F);
        this.progressPositionPrev(this.rwing, groundProgress, 3.0F, -1.0F, 1.0F, 1.0F);
        this.progressPositionPrev(this.lwing, groundProgress, -3.0F, -1.0F, 1.0F, 1.0F);
        this.progressRotationPrev(this.lleg, groundProgress, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, groundProgress, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.ltalon, groundProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rtalon, groundProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lwing, groundProgress, 0.0F, (float) Math.toRadians(10.0), (float) Math.toRadians(35.0), 1.0F);
        this.progressRotationPrev(this.rwing, groundProgress, 0.0F, (float) Math.toRadians(-10.0), (float) Math.toRadians(-35.0), 1.0F);
        this.progressRotationPrev(this.lwingTip, groundProgress, 0.0F, (float) Math.toRadians(-10.0), (float) Math.toRadians(-130.0), 1.0F);
        this.progressRotationPrev(this.rwingTip, groundProgress, 0.0F, (float) Math.toRadians(10.0), (float) Math.toRadians(130.0), 1.0F);
        this.progressRotationPrev(this.lhand, groundProgress, 0.0F, (float) Math.toRadians(-10.0), (float) Math.toRadians(-35.0), 1.0F);
        this.progressRotationPrev(this.rhand, groundProgress, 0.0F, (float) Math.toRadians(10.0), (float) Math.toRadians(35.0), 1.0F);
        this.progressRotationPrev(this.tail, groundProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tailTip, groundProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, groundStill, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tailTip, groundStill, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.jaw, openMouthProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.body, sitProgress, 0.0F, -1.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.lleg, sitProgress, 0.0F, -2.0F, -3.0F, 1.0F);
        this.progressPositionPrev(this.rleg, sitProgress, 0.0F, -2.0F, -3.0F, 1.0F);
        this.progressPositionPrev(this.lwing, sitProgress, 1.0F, 1.0F, -1.0F, 1.0F);
        this.progressPositionPrev(this.rwing, sitProgress, -1.0F, 1.0F, -1.0F, 1.0F);
        this.progressRotationPrev(this.body, sitProgress, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck, sitProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, sitProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail, sitProgress, (float) Math.toRadians(55.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tailTip, sitProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, sitProgress, (float) Math.toRadians(15.0), (float) Math.toRadians(15.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, sitProgress, (float) Math.toRadians(15.0), (float) Math.toRadians(-15.0), 0.0F, 1.0F);
        this.progressRotationPrev(this.lwing, sitProgress, (float) Math.toRadians(40.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), 1.0F);
        this.progressRotationPrev(this.rwing, sitProgress, (float) Math.toRadians(40.0), (float) Math.toRadians(20.0), (float) Math.toRadians(-20.0), 1.0F);
        this.progressRotationPrev(this.lhand, sitProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rhand, sitProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.animateFlight(ageInTicks, flyProgress, hoverProgress, glide, flapAmount, entity.m_20160_(), true);
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            groundMove = buryEggsAmount * 0.5F;
            this.body.swing(0.25F, 0.4F, false, 0.0F, 0.0F, ageInTicks, buryEggsAmount);
            this.neck.swing(0.25F, 0.4F, true, -1.0F, 0.0F, ageInTicks, buryEggsAmount);
        }
        this.swing(this.tail, 0.1F, 0.2F, false, 2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tailTip, 0.1F, 0.2F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.lwing, walkSpeed, walkDegree, false, -1.0F, 0.2F, limbSwing, groundMove);
        this.swing(this.rwing, walkSpeed, walkDegree, false, -1.0F, -0.2F, limbSwing, groundMove);
        this.flap(this.lwingTip, walkSpeed, walkDegree * 1.0F, true, 1.0F, -0.2F, limbSwing, groundMove);
        this.flap(this.rwingTip, walkSpeed, walkDegree * 1.0F, true, 1.0F, 0.2F, limbSwing, groundMove);
        this.walk(this.lhand, walkSpeed, walkDegree, true, 0.0F, -0.2F, limbSwing, groundMove);
        this.walk(this.rhand, walkSpeed, walkDegree, false, 0.0F, -0.2F, limbSwing, groundMove);
        this.swing(this.neck, walkSpeed, walkDegree * 0.5F, false, 1.0F, 0.0F, limbSwing, groundMove);
        this.swing(this.head, walkSpeed, walkDegree * 0.5F, true, 0.5F, 0.0F, limbSwing, groundMove);
        this.walk(this.lleg, walkSpeed, walkDegree * 0.6F, false, -2.0F, 0.3F, limbSwing, groundMove);
        this.walk(this.rleg, walkSpeed, walkDegree * 0.6F, true, -2.0F, -0.3F, limbSwing, groundMove);
        this.walk(this.ltalon, walkSpeed, walkDegree * 0.6F, false, -1.0F, -0.3F, limbSwing, groundMove);
        this.walk(this.rtalon, walkSpeed, walkDegree * 0.6F, true, -1.0F, 0.3F, limbSwing, groundMove);
        this.swing(this.tail, walkSpeed, walkDegree * 0.5F, true, 0.5F, 0.0F, limbSwing, groundMove);
        this.bob(this.body, walkSpeed * 2.0F, walkDegree * 3.0F, false, limbSwing, groundMove);
        this.bob(this.neck, 0.1F, 0.5F, false, ageInTicks, 1.0F);
        this.faceTarget(netHeadYaw, headPitch, 1.0F, new AdvancedModelBox[] { this.head, this.neck });
        this.swing(this.neck, danceSpeed, 0.5F, true, 0.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.head, danceSpeed, 0.25F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.head, danceSpeed, 0.25F, true, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.jaw, danceSpeed, 0.25F, false, 2.0F, 0.2F, ageInTicks, danceAmount);
        this.swing(this.body, danceSpeed, 0.1F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.body.rotateAngleX += pitchAmount;
        this.body.rotateAngleZ += rollAmount;
        this.tail.rotateAngleY += tailYaw * 0.8F;
        this.tailTip.rotateAngleY += tailYaw * 0.2F;
        this.lleg.rotateAngleY += tailYaw * flyProgress * 0.4F;
        this.rleg.rotateAngleY += tailYaw * flyProgress * 0.4F;
    }

    private void animateFlight(float ageInTicks, float flyProgress, float hoverProgress, float glide, float flapAmount, boolean carrying, boolean bob) {
        this.progressPositionPrev(this.head, glide, 0.0F, 3.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neck, glide, 0.0F, 1.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.body, hoverProgress, (float) Math.toRadians(-50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck, hoverProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, hoverProgress, (float) Math.toRadians(30.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.lleg, hoverProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rleg, hoverProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.ltalon, hoverProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rtalon, hoverProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neck, glide, 0.0F, 1.0F, 0.0F, 1.0F);
        this.walk(this.lleg, 0.3F, 0.2F, false, 1.0F, -0.1F, ageInTicks, flyProgress);
        this.walk(this.rleg, 0.3F, 0.2F, false, 1.0F, -0.1F, ageInTicks, flyProgress);
        this.flap(this.rwing, 0.5F, 1.0F, false, 1.0F, -0.2F, ageInTicks, flapAmount);
        this.flap(this.lwing, 0.5F, 1.0F, true, 1.0F, -0.2F, ageInTicks, flapAmount);
        this.flap(this.rwingTip, 0.5F, 0.5F, false, 0.0F, -0.2F, ageInTicks, flapAmount);
        this.flap(this.lwingTip, 0.5F, 0.5F, true, 0.0F, -0.2F, ageInTicks, flapAmount);
        float bodyFlightBob = bob ? ACMath.walkValue(ageInTicks, flapAmount, 0.5F, 0.0F, 4.0F, false) : 0.0F;
        this.body.rotationPointY -= bodyFlightBob;
        this.bob(this.neck, 0.5F, -1.0F, false, ageInTicks, flapAmount);
        this.walk(this.neck, 0.5F, 0.1F, false, 1.0F, 0.0F, ageInTicks, flyProgress);
        if (carrying) {
            this.walk(this.lleg, 0.3F, 0.2F, true, 1.0F, -0.1F, ageInTicks, flyProgress);
            this.walk(this.rleg, 0.3F, 0.2F, true, 1.0F, -0.1F, ageInTicks, flyProgress);
            this.rleg.rotationPointY += bodyFlightBob * 0.25F;
            this.rleg.rotationPointZ += bodyFlightBob * 0.25F;
            this.lleg.rotationPointY += bodyFlightBob * 0.25F;
            this.lleg.rotationPointZ += bodyFlightBob * 0.25F;
        }
        this.walk(this.rwing, 2.0F, 0.05F, false, 2.0F, 0.1F, ageInTicks, glide);
        this.walk(this.lwing, 2.0F, 0.05F, false, 2.0F, 0.1F, ageInTicks, glide);
    }

    public Vec3 getLegPosition(boolean right, Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        this.body.translateAndRotate(translationStack);
        if (right) {
            this.rleg.translateAndRotate(translationStack);
        } else {
            this.lleg.translateAndRotate(translationStack);
        }
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) armOffsetVec.x(), (double) armOffsetVec.y(), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
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

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.body, this.tail, this.tailTip, this.lleg, this.rleg, this.ltalon, this.rtalon, this.neck, this.head, this.jaw, this.lwing, this.lwingTip, new AdvancedModelBox[] { this.lhand, this.rwing, this.rwingTip, this.rhand });
    }

    public void animateSpirit(DinosaurSpiritEntity entityIn, float partialTicks) {
        this.resetToDefaultPose();
        float ageInTicks = (float) entityIn.f_19797_ + partialTicks;
        float flyProgress = 1.0F;
        float hoverProgress = 1.0F;
        this.body.rotationPointY -= 16.0F;
        this.animateFlight(ageInTicks, flyProgress, hoverProgress, 0.0F, flyProgress, true, false);
    }
}