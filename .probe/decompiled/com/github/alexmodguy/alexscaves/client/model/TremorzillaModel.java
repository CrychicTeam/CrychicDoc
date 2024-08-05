package com.github.alexmodguy.alexscaves.client.model;

import com.github.alexmodguy.alexscaves.client.model.misc.HideableModelBoxWithChildren;
import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.TremorzillaLegSolver;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ModelAnimator;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class TremorzillaModel extends AdvancedEntityModel<TremorzillaEntity> {

    private final AdvancedModelBox root;

    private final HideableModelBoxWithChildren torso;

    private final AdvancedModelBox torsoSpikes;

    private final AdvancedModelBox torsoSpike1;

    private final AdvancedModelBox torsoSpike2;

    private final AdvancedModelBox torsoSpike3;

    private final AdvancedModelBox rightLeg;

    private final AdvancedModelBox leftLeg;

    private final HideableModelBoxWithChildren tail1;

    private final HideableModelBoxWithChildren tail1Spikes;

    private final HideableModelBoxWithChildren tail1Spike1;

    private final HideableModelBoxWithChildren tail1Spike2;

    private final HideableModelBoxWithChildren tail2;

    private final HideableModelBoxWithChildren tail2Spikes;

    private final HideableModelBoxWithChildren tail3;

    private final HideableModelBoxWithChildren tail3Spikes;

    private final AdvancedModelBox tail4;

    private final AdvancedModelBox chest;

    private final AdvancedModelBox cube_r6;

    private final AdvancedModelBox neck;

    private final AdvancedModelBox neckSpikes;

    private final AdvancedModelBox head;

    private final AdvancedModelBox rightEar;

    private final AdvancedModelBox leftEar;

    private final AdvancedModelBox jaw;

    private final AdvancedModelBox leftArm;

    private final AdvancedModelBox leftThumb;

    private final AdvancedModelBox rightArm;

    private final AdvancedModelBox rightThumb;

    private final AdvancedModelBox chestSpikes;

    private final AdvancedModelBox chestSpike1;

    private final AdvancedModelBox chestSpike2;

    private final AdvancedModelBox chestSpike3;

    private final AdvancedModelBox neckSlope;

    private final ModelAnimator animator;

    public boolean straighten;

    public TremorzillaModel() {
        this.texWidth = 512;
        this.texHeight = 512;
        this.root = new AdvancedModelBox(this);
        this.root.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.torso = new HideableModelBoxWithChildren(this);
        this.torso.setRotationPoint(0.0F, -51.0F, 0.0F);
        this.root.addChild(this.torso);
        this.torso.setTextureOffset(0, 0).addBox(-26.0F, -31.0F, -28.0F, 52.0F, 62.0F, 56.0F, 0.0F, false);
        this.torsoSpikes = new AdvancedModelBox(this);
        this.torsoSpikes.setRotationPoint(0.0F, -28.0671F, 27.4673F);
        this.torso.addChild(this.torsoSpikes);
        this.torsoSpike1 = new AdvancedModelBox(this);
        this.torsoSpike1.setRotationPoint(0.0F, -15.9329F, -24.4673F);
        this.torsoSpikes.addChild(this.torsoSpike1);
        this.setRotateAngle(this.torsoSpike1, 0.2618F, 0.0F, 0.0F);
        this.torsoSpike1.setTextureOffset(284, 262).addBox(0.02F, -5.0F, 9.0F, 0.0F, 54.0F, 54.0F, 0.0F, false);
        this.torsoSpike2 = new AdvancedModelBox(this);
        this.torsoSpike2.setRotationPoint(0.0F, -15.9329F, -24.4673F);
        this.torsoSpikes.addChild(this.torsoSpike2);
        this.setRotateAngle(this.torsoSpike2, 0.3927F, -0.3054F, -0.3054F);
        this.torsoSpike2.setTextureOffset(128, 290).addBox(-10.0F, -5.0F, 9.0F, 0.0F, 54.0F, 54.0F, 0.0F, true);
        this.torsoSpike3 = new AdvancedModelBox(this);
        this.torsoSpike3.setRotationPoint(0.0F, -15.9329F, -24.4673F);
        this.torsoSpikes.addChild(this.torsoSpike3);
        this.setRotateAngle(this.torsoSpike3, 0.3927F, 0.3054F, 0.3054F);
        this.torsoSpike3.setTextureOffset(128, 290).addBox(10.0F, -5.0F, 9.0F, 0.0F, 54.0F, 54.0F, 0.0F, false);
        this.rightLeg = new AdvancedModelBox(this);
        this.rightLeg.setRotationPoint(-24.0F, -2.0F, -3.0F);
        this.torso.addChild(this.rightLeg);
        this.rightLeg.setTextureOffset(160, 24).addBox(-20.5F, 44.5F, -24.0F, 27.0F, 8.0F, 8.0F, 0.0F, true);
        this.rightLeg.setTextureOffset(139, 148).addBox(-20.5F, 44.5F, -24.5F, 27.0F, 8.0F, 8.0F, 0.5F, true);
        this.rightLeg.setTextureOffset(1, 344).addBox(-21.0F, -11.0F, -16.0F, 28.0F, 64.0F, 34.0F, 0.0F, true);
        this.leftLeg = new AdvancedModelBox(this);
        this.leftLeg.setRotationPoint(24.0F, -2.0F, -3.0F);
        this.torso.addChild(this.leftLeg);
        this.leftLeg.setTextureOffset(160, 24).addBox(-6.5F, 44.5F, -24.0F, 27.0F, 8.0F, 8.0F, 0.0F, false);
        this.leftLeg.setTextureOffset(139, 148).addBox(-6.5F, 44.5F, -24.5F, 27.0F, 8.0F, 8.0F, 0.5F, false);
        this.leftLeg.setTextureOffset(1, 344).addBox(-7.0F, -11.0F, -16.0F, 28.0F, 64.0F, 34.0F, 0.0F, false);
        this.tail1 = new HideableModelBoxWithChildren(this);
        this.tail1.setRotationPoint(-0.5F, 24.0F, 16.0F);
        this.torso.addChild(this.tail1);
        this.tail1.setTextureOffset(124, 164).addBox(-14.0F, -15.0F, -6.0F, 29.0F, 34.0F, 64.0F, 0.0F, false);
        this.tail1Spikes = new HideableModelBoxWithChildren(this);
        this.tail1Spikes.setRotationPoint(0.5F, -15.3829F, 26.0F);
        this.tail1.addChild(this.tail1Spikes);
        this.tail1Spikes.setTextureOffset(156, 238).addBox(0.0F, -39.6171F, -32.0F, 0.0F, 40.0F, 64.0F, 0.0F, false);
        this.tail1Spike1 = new HideableModelBoxWithChildren(this);
        this.tail1Spike1.setRotationPoint(6.0F, 0.3829F, 0.0F);
        this.tail1Spikes.addChild(this.tail1Spike1);
        this.setRotateAngle(this.tail1Spike1, 0.0F, 0.0F, 0.3054F);
        this.tail1Spike1.setTextureOffset(156, 198).addBox(0.0F, -40.0F, -32.0F, 0.0F, 40.0F, 64.0F, 0.0F, false);
        this.tail1Spike2 = new HideableModelBoxWithChildren(this);
        this.tail1Spike2.setRotationPoint(-6.0F, 0.3829F, 0.0F);
        this.tail1Spikes.addChild(this.tail1Spike2);
        this.setRotateAngle(this.tail1Spike2, 0.0F, 0.0F, -0.3054F);
        this.tail1Spike2.setTextureOffset(156, 198).addBox(0.0F, -40.0F, -32.0F, 0.0F, 40.0F, 64.0F, 0.0F, true);
        this.tail2 = new HideableModelBoxWithChildren(this);
        this.tail2.setRotationPoint(0.5F, -9.0F, 54.0F);
        this.tail1.addChild(this.tail2);
        this.tail2.setTextureOffset(140, 42).addBox(-9.0F, -4.01F, -7.99F, 18.0F, 28.0F, 76.0F, -0.01F, false);
        this.tail2Spikes = new HideableModelBoxWithChildren(this);
        this.tail2Spikes.setRotationPoint(-0.01F, -4.01F, 36.01F);
        this.tail2.addChild(this.tail2Spikes);
        this.tail2Spikes.setTextureOffset(0, 252).addBox(0.0F, -28.0F, -32.0F, 0.0F, 28.0F, 64.0F, -0.01F, false);
        this.tail3 = new HideableModelBoxWithChildren(this);
        this.tail3.setRotationPoint(0.0F, 10.0F, 66.0F);
        this.tail2.addChild(this.tail3);
        this.tail3.setTextureOffset(0, 228).addBox(-5.0F, -8.0F, -2.0F, 10.0F, 20.0F, 68.0F, 0.0F, false);
        this.tail3Spikes = new HideableModelBoxWithChildren(this);
        this.tail3Spikes.setRotationPoint(0.0F, -8.0F, 29.0F);
        this.tail3.addChild(this.tail3Spikes);
        this.tail3Spikes.setTextureOffset(261, 38).addBox(0.0F, -26.0F, -27.0F, 0.0F, 26.0F, 54.0F, 0.0F, true);
        this.tail4 = new AdvancedModelBox(this);
        this.tail4.setRotationPoint(0.0F, 2.0F, 66.0F);
        this.tail3.addChild(this.tail4);
        this.tail4.setTextureOffset(248, 146).addBox(-3.0F, -6.0F, -4.0F, 6.0F, 12.0F, 68.0F, 0.0F, false);
        this.chest = new AdvancedModelBox(this);
        this.chest.setRotationPoint(0.0F, -29.565F, 17.1508F);
        this.torso.addChild(this.chest);
        this.cube_r6 = new AdvancedModelBox(this);
        this.cube_r6.setRotationPoint(0.0F, -14.6696F, -14.0691F);
        this.chest.addChild(this.cube_r6);
        this.setRotateAngle(this.cube_r6, 0.3927F, 0.0F, 0.0F);
        this.cube_r6.setTextureOffset(0, 118).addBox(-20.0F, -29.0F, -43.0F, 40.0F, 56.0F, 54.0F, 0.0F, false);
        this.neck = new AdvancedModelBox(this);
        this.neck.setRotationPoint(1.5556F, -36.6696F, -20.1803F);
        this.chest.addChild(this.neck);
        this.neck.setTextureOffset(252, 0).addBox(-14.5556F, -50.0F, -33.8889F, 26.0F, 54.0F, 38.0F, 0.0F, false);
        this.neckSpikes = new AdvancedModelBox(this);
        this.neckSpikes.setRotationPoint(-1.5556F, -38.0F, -0.8889F);
        this.neck.addChild(this.neckSpikes);
        this.neckSpikes.setTextureOffset(0, 200).addBox(-0.02F, -22.0F, -7.0F, 0.0F, 48.0F, 28.0F, 0.0F, false);
        this.neckSpikes.setTextureOffset(248, 118).addBox(-7.0F, -20.0F, -9.0F, 0.0F, 48.0F, 28.0F, 0.0F, true);
        this.neckSpikes.setTextureOffset(248, 118).addBox(7.0F, -20.0F, -9.0F, 0.0F, 48.0F, 28.0F, 0.0F, false);
        this.head = new AdvancedModelBox(this);
        this.head.setRotationPoint(-1.5556F, -49.0F, -20.8889F);
        this.neck.addChild(this.head);
        this.head.setTextureOffset(335, 124).addBox(-13.5F, -4.0F, -36.0F, 27.0F, 14.0F, 36.0F, 0.0F, false);
        this.head.setTextureOffset(11, 443).addBox(-13.5F, 10.0F, -36.0F, 27.0F, 5.0F, 31.0F, 0.0F, false);
        this.head.setTextureOffset(4, 122).addBox(4.5F, -13.0F, -13.0F, 9.0F, 5.0F, 8.0F, 0.0F, false);
        this.head.setTextureOffset(4, 142).addBox(4.5F, -8.0F, -13.0F, 9.0F, 4.0F, 8.0F, 0.0F, false);
        this.head.setTextureOffset(4, 122).addBox(-13.5F, -13.0F, -13.0F, 9.0F, 5.0F, 8.0F, 0.0F, true);
        this.head.setTextureOffset(430, 52).addBox(-13.5F, 4.0F, -14.0F, 27.0F, 6.0F, 14.0F, 0.0F, false);
        this.head.setTextureOffset(4, 142).addBox(-13.5F, -8.0F, -13.0F, 9.0F, 4.0F, 8.0F, 0.0F, true);
        this.head.setTextureOffset(32, 14).addBox(-13.5F, -7.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, true);
        this.rightEar = new AdvancedModelBox(this);
        this.rightEar.setRotationPoint(-13.5F, -4.0F, 0.0F);
        this.head.addChild(this.rightEar);
        this.leftEar = new AdvancedModelBox(this);
        this.leftEar.setRotationPoint(13.5F, -4.0F, 0.0F);
        this.head.addChild(this.leftEar);
        this.leftEar.setTextureOffset(32, 14).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, false);
        this.jaw = new AdvancedModelBox(this);
        this.jaw.setRotationPoint(0.0F, 5.2738F, -2.0631F);
        this.head.addChild(this.jaw);
        this.jaw.setTextureOffset(383, 3).addBox(-15.0F, -1.2738F, -11.9369F, 30.0F, 20.0F, 15.0F, 0.0F, false);
        this.jaw.setTextureOffset(368, 346).addBox(-15.0F, -9.2738F, -35.9369F, 30.0F, 14.0F, 24.0F, 0.0F, false);
        this.jaw.setTextureOffset(372, 202).addBox(-15.0F, 4.7262F, -35.9369F, 30.0F, 14.0F, 24.0F, 0.0F, false);
        this.leftArm = new AdvancedModelBox(this);
        this.leftArm.setRotationPoint(17.5F, -19.6696F, -33.0691F);
        this.chest.addChild(this.leftArm);
        this.setRotateAngle(this.leftArm, 0.3927F, -0.3927F, 0.3927F);
        this.leftArm.setTextureOffset(312, 226).addBox(16.5F, -6.0F, -28.0F, 18.0F, 12.0F, 20.0F, 0.0F, false);
        this.leftArm.setTextureOffset(0, 0).addBox(16.5F, -6.0F, -38.0F, 18.0F, 12.0F, 10.0F, 0.0F, false);
        this.leftArm.setTextureOffset(160, 0).addBox(2.5F, -6.0F, -8.0F, 32.0F, 12.0F, 12.0F, 0.0F, false);
        this.leftThumb = new AdvancedModelBox(this);
        this.leftThumb.setRotationPoint(16.5F, 4.0F, -22.0F);
        this.leftArm.addChild(this.leftThumb);
        this.leftThumb.setTextureOffset(10, 34).addBox(-6.0F, -2.0F, -6.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        this.leftThumb.setTextureOffset(0, 46).addBox(-6.0F, -2.0F, -12.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        this.rightArm = new AdvancedModelBox(this);
        this.rightArm.setRotationPoint(-17.5F, -19.6696F, -33.0691F);
        this.chest.addChild(this.rightArm);
        this.setRotateAngle(this.rightArm, 0.3927F, 0.3927F, -0.3927F);
        this.rightArm.setTextureOffset(312, 226).addBox(-34.5F, -6.0F, -28.0F, 18.0F, 12.0F, 20.0F, 0.0F, true);
        this.rightArm.setTextureOffset(0, 0).addBox(-34.5F, -6.0F, -38.0F, 18.0F, 12.0F, 10.0F, 0.0F, true);
        this.rightArm.setTextureOffset(160, 0).addBox(-34.5F, -6.0F, -8.0F, 32.0F, 12.0F, 12.0F, 0.0F, true);
        this.rightThumb = new AdvancedModelBox(this);
        this.rightThumb.setRotationPoint(-16.5F, 4.0F, -22.0F);
        this.rightArm.addChild(this.rightThumb);
        this.rightThumb.setTextureOffset(10, 34).addBox(0.0F, -2.0F, -6.0F, 6.0F, 4.0F, 6.0F, 0.0F, true);
        this.rightThumb.setTextureOffset(0, 46).addBox(0.0F, -2.0F, -12.0F, 6.0F, 4.0F, 6.0F, 0.0F, true);
        this.chestSpikes = new AdvancedModelBox(this);
        this.chestSpikes.setRotationPoint(0.0F, -19.8909F, -9.5233F);
        this.chest.addChild(this.chestSpikes);
        this.chestSpike1 = new AdvancedModelBox(this);
        this.chestSpike1.setRotationPoint(0.0F, 5.4558F, -4.6274F);
        this.chestSpikes.addChild(this.chestSpike1);
        this.setRotateAngle(this.chestSpike1, 0.7854F, 0.0F, 0.0F);
        this.chestSpike1.setTextureOffset(128, 290).addBox(0.0F, -29.0F, 11.0F, 0.0F, 54.0F, 54.0F, 0.0F, false);
        this.chestSpike2 = new AdvancedModelBox(this);
        this.chestSpike2.setRotationPoint(10.0F, 0.0F, 0.0F);
        this.chestSpikes.addChild(this.chestSpike2);
        this.setRotateAngle(this.chestSpike2, 0.7854F, 0.2182F, 0.2618F);
        this.chestSpike2.setTextureOffset(236, 330).addBox(0.0F, -28.4142F, -0.1299F, 0.0F, 54.0F, 44.0F, 0.0F, false);
        this.chestSpike3 = new AdvancedModelBox(this);
        this.chestSpike3.setRotationPoint(-10.0F, 0.0F, 0.0F);
        this.chestSpikes.addChild(this.chestSpike3);
        this.setRotateAngle(this.chestSpike3, 0.7854F, -0.2182F, -0.2618F);
        this.chestSpike3.setTextureOffset(236, 330).addBox(0.0F, -28.4142F, -0.1299F, 0.0F, 54.0F, 44.0F, 0.0F, true);
        this.neckSlope = new AdvancedModelBox(this);
        this.neckSlope.setRotationPoint(-1.5556F, -50.0F, -24.8889F);
        this.neck.addChild(this.neckSlope);
        this.setRotateAngle(this.neckSlope, 1.0105F, 0.0F, 0.0F);
        this.neckSlope.setTextureOffset(426, 480).addBox(-13.0F, 0.0F, -17.0F, 26.0F, 15.0F, 17.0F, -0.01F, false);
        this.updateDefaultPose();
        this.animator = ModelAnimator.create();
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.f_102610_) {
            float f = 1.5F;
            this.head.setScale(f, f, f);
            this.head.setShouldScaleChildren(true);
            matrixStackIn.pushPose();
            matrixStackIn.scale(0.15F, 0.15F, 0.15F);
            matrixStackIn.translate(0.0, 8.55F, 0.0);
            this.parts().forEach(p_228292_8_ -> p_228292_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
            this.head.setScale(1.0F, 1.0F, 1.0F);
        } else {
            matrixStackIn.pushPose();
            this.parts().forEach(p_228290_8_ -> p_228290_8_.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
            matrixStackIn.popPose();
        }
    }

    public void animate(IAnimatedEntity entity) {
        this.animator.update(entity);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_SPEAK);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.neck, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(8);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(40.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_ROAR_1);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.move(this.neck, 0.0F, 10.0F, 0.0F);
        this.animator.move(this.head, 0.0F, 1.0F, 2.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(8);
        this.animator.move(this.neck, 0.0F, -5.0F, -5.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-60.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(50.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(30);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_ROAR_2);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(20.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(10.0), 0.0F, 0.0F);
        this.animator.move(this.head, 0.0F, -1.0F, -2.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.startKeyframe(13);
        this.animator.move(this.neck, 0.0F, 10.0F, -5.0F);
        this.animator.move(this.head, 0.0F, 1.0F, 5.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-80.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(60.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(25);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_RIGHT_SCRATCH);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, (float) Math.toRadians(-15.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.rightArm, (float) Math.toRadians(-85.0), (float) Math.toRadians(40.0), 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(10.0), (float) Math.toRadians(25.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.chest, -5.0F, -10.0F, 2.0F);
        this.animator.move(this.rightArm, 0.0F, 0.0F, -8.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(20.0), (float) Math.toRadians(-35.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), (float) Math.toRadians(35.0), 0.0F);
        this.animator.rotate(this.rightArm, (float) Math.toRadians(-35.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(-10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_LEFT_SCRATCH);
        this.animator.startKeyframe(10);
        this.animator.rotate(this.chest, (float) Math.toRadians(-15.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(15.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(-85.0), (float) Math.toRadians(-40.0), 0.0F);
        this.animator.rotate(this.rightArm, (float) Math.toRadians(10.0), (float) Math.toRadians(-25.0), 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.chest, 5.0F, -10.0F, 2.0F);
        this.animator.move(this.leftArm, 0.0F, 0.0F, -8.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(20.0), (float) Math.toRadians(35.0), 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-20.0), (float) Math.toRadians(-35.0), 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(-35.0), (float) Math.toRadians(40.0), (float) Math.toRadians(10.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_RIGHT_STOMP);
        this.animator.startKeyframe(10);
        this.animator.move(this.torso, 0.0F, -20.0F, 2.0F);
        this.animator.move(this.leftLeg, 0.0F, 20.0F, -2.0F);
        this.animator.move(this.rightLeg, 0.0F, -10.0F, -10.0F);
        this.animator.move(this.tail1, 0.0F, 10.0F, 2.0F);
        this.animator.move(this.rightArm, 0.0F, -8.0F, 2.0F);
        this.animator.move(this.leftArm, 0.0F, -8.0F, 2.0F);
        this.animator.rotate(this.torso, (float) Math.toRadians(-15.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(5.0), (float) Math.toRadians(5.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), (float) Math.toRadians(10.0), 0.0F);
        this.animator.rotate(this.leftLeg, (float) Math.toRadians(15.0), (float) Math.toRadians(15.0), (float) Math.toRadians(5.0));
        this.animator.rotate(this.rightLeg, (float) Math.toRadians(-15.0), 0.0F, (float) Math.toRadians(-5.0));
        this.animator.rotate(this.tail1, (float) Math.toRadians(10.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.torso, 0.0F, 0.0F, 10.0F);
        this.animator.move(this.rightLeg, 0.0F, -10.0F, -30.0F);
        this.animator.rotate(this.torso, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftLeg, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightLeg, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightArm, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_LEFT_STOMP);
        this.animator.startKeyframe(10);
        this.animator.move(this.torso, 0.0F, -20.0F, 2.0F);
        this.animator.move(this.rightLeg, 0.0F, 20.0F, -2.0F);
        this.animator.move(this.leftLeg, 0.0F, -10.0F, -10.0F);
        this.animator.move(this.tail1, 0.0F, 10.0F, 2.0F);
        this.animator.move(this.rightArm, 0.0F, -8.0F, 2.0F);
        this.animator.move(this.leftArm, 0.0F, -8.0F, 2.0F);
        this.animator.rotate(this.torso, (float) Math.toRadians(-15.0), (float) Math.toRadians(15.0), 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(5.0), (float) Math.toRadians(-5.0), 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(10.0), (float) Math.toRadians(-10.0), 0.0F);
        this.animator.rotate(this.rightLeg, (float) Math.toRadians(15.0), (float) Math.toRadians(-15.0), (float) Math.toRadians(-5.0));
        this.animator.rotate(this.leftLeg, (float) Math.toRadians(-15.0), 0.0F, (float) Math.toRadians(5.0));
        this.animator.rotate(this.tail1, (float) Math.toRadians(10.0), (float) Math.toRadians(-15.0), 0.0F);
        this.animator.rotate(this.tail2, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.startKeyframe(5);
        this.animator.move(this.torso, 0.0F, 0.0F, 10.0F);
        this.animator.move(this.leftLeg, 0.0F, -10.0F, -30.0F);
        this.animator.rotate(this.torso, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftLeg, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightLeg, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.rotate(this.rightArm, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(-35.0), 0.0F, 0.0F);
        this.animator.rotate(this.tail1, (float) Math.toRadians(-15.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(5);
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_BITE);
        this.animator.startKeyframe(5);
        this.animator.rotate(this.chest, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.chest, 0.0F, 0.0F, -5.0F);
        this.animator.move(this.neck, 0.0F, 2.0F, -5.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(25.0), 0.0F, (float) Math.toRadians(15.0));
        this.animator.rotate(this.head, (float) Math.toRadians(-55.0), 0.0F, (float) Math.toRadians(-15.0));
        this.animator.rotate(this.jaw, (float) Math.toRadians(55.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(5);
        this.animator.move(this.chest, 0.0F, 0.0F, -5.0F);
        this.animator.move(this.neck, 0.0F, 2.0F, -5.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(35.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(15.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-55.0), 0.0F, 0.0F);
        this.animator.rotate(this.jaw, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.resetKeyframe(10);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_PREPARE_BREATH);
        this.animator.startKeyframe(13);
        this.animator.move(this.neck, 0.0F, 10.0F, -5.0F);
        this.animator.move(this.head, 0.0F, 5.0F, 8.0F);
        this.animator.rotate(this.chest, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, (float) Math.toRadians(-30.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-70.0), 0.0F, 0.0F);
        this.animator.rotate(this.leftArm, (float) Math.toRadians(-35.0), (float) Math.toRadians(-40.0), (float) Math.toRadians(20.0));
        this.animator.rotate(this.rightArm, (float) Math.toRadians(-35.0), (float) Math.toRadians(40.0), (float) Math.toRadians(-20.0));
        this.animator.endKeyframe();
        this.animator.setStaticKeyframe(2);
        this.animator.resetKeyframe(5);
        this.animator.setAnimation(TremorzillaEntity.ANIMATION_CHEW);
        this.animator.startKeyframe(6);
        this.animator.rotate(this.jaw, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, 0.0F, 0.0F, (float) Math.toRadians(5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(6);
        this.animator.rotate(this.jaw, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(6);
        this.animator.rotate(this.jaw, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, 0.0F, 0.0F, (float) Math.toRadians(-5.0));
        this.animator.endKeyframe();
        this.animator.startKeyframe(6);
        this.animator.rotate(this.jaw, (float) Math.toRadians(-5.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(5.0), 0.0F, 0.0F);
        this.animator.endKeyframe();
        this.animator.startKeyframe(6);
        this.animator.rotate(this.jaw, (float) Math.toRadians(25.0), 0.0F, 0.0F);
        this.animator.rotate(this.head, (float) Math.toRadians(-10.0), 0.0F, 0.0F);
        this.animator.rotate(this.neck, 0.0F, 0.0F, (float) Math.toRadians(5.0));
        this.animator.endKeyframe();
        this.animator.resetKeyframe(5);
    }

    public void setupAnim(TremorzillaEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetToDefaultPose();
        this.animate(entity);
        if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            this.setupAnimForAnimation(entity, entity.getAnimation(), limbSwing, limbSwingAmount, ageInTicks);
        }
        float partialTicks = ageInTicks - (float) entity.f_19797_;
        float burnProgress = entity.getBeamProgress(partialTicks);
        float danceProgress = entity.getDanceProgress(partialTicks);
        float sitProgress = entity.getSitProgress(partialTicks) * (1.0F - danceProgress);
        float swimProgress = entity.m_20998_(partialTicks) * (1.0F - sitProgress);
        float groundProgress = 1.0F - swimProgress;
        float standProgress = 1.0F - sitProgress;
        float spikesDownProgress = entity.getClientSpikeDownAmount(partialTicks);
        float buryEggsAmount = entity.getBuryEggsProgress(partialTicks);
        float spikes1Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 0.0F);
        float spikes2Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 1.0F);
        float spikes3Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 2.0F);
        float spikes4Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 3.0F);
        float spikes5Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 4.0F);
        float spikes6Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 5.0F);
        float walkSpeed = 0.25F;
        float walkDegree = 2.0F;
        float swimSpeed = 0.25F;
        float swimDegree = 2.0F;
        float limbSwingAmountGround = limbSwingAmount * groundProgress;
        float limbSwingAmountSwim = limbSwingAmount * swimProgress;
        float headYawAmount = netHeadYaw / (180.0F / (float) Math.PI) * (1.0F - burnProgress) * standProgress;
        float headPitchAmount = headPitch / (180.0F / (float) Math.PI) * (1.0F - burnProgress) * standProgress;
        Vec3 burnPos = entity.getClientBeamEndPosition(partialTicks);
        this.articulateLegs(entity.legSolver, partialTicks, groundProgress * (1.0F - danceProgress) * standProgress);
        if (!this.straighten && !entity.isFakeEntity()) {
            this.positionTail(entity, partialTicks);
        }
        if (buryEggsAmount > 0.0F) {
            limbSwing = ageInTicks;
            limbSwingAmount = buryEggsAmount * 0.5F;
        }
        this.animateDancing(entity, danceProgress, ageInTicks);
        this.progressPositionPrev(this.torso, swimProgress, 0.0F, 20.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.head, swimProgress, 0.0F, 5.0F, 5.0F, 1.0F);
        this.progressPositionPrev(this.neck, swimProgress, 0.0F, 3.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.leftLeg, swimProgress, 2.0F, 15.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.rightLeg, swimProgress, -2.0F, 15.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neck, swimProgress, 0.0F, 3.0F, -5.0F, 1.0F);
        this.progressRotationPrev(this.torso, swimProgress, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail1, swimProgress, (float) Math.toRadians(-70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck, swimProgress, (float) Math.toRadians(-10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, swimProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rightArm, swimProgress, (float) Math.toRadians(-50.0), (float) Math.toRadians(-60.0), (float) Math.toRadians(-30.0), 1.0F);
        this.progressRotationPrev(this.leftArm, swimProgress, (float) Math.toRadians(-50.0), (float) Math.toRadians(60.0), (float) Math.toRadians(30.0), 1.0F);
        this.progressPositionPrev(this.neck, burnProgress, 0.0F, -10.0F, -8.0F, 1.0F);
        this.progressPositionPrev(this.head, burnProgress, 0.0F, 2.5F, 5.0F, 1.0F);
        this.progressPositionPrev(this.chest, burnProgress, 0.0F, -5.0F, -5.0F, 1.0F);
        this.progressRotationPrev(this.jaw, burnProgress, (float) Math.toRadians(70.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.head, burnProgress, (float) Math.toRadians(-110.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.neck, burnProgress, (float) Math.toRadians(50.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.chest, burnProgress, (float) Math.toRadians(20.0), 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.tail3Spikes, spikes1Down, 0.0F, 8.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.tail2Spikes, spikes2Down, 0.0F, 16.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.tail1Spikes, spikes3Down, 0.0F, 22.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.tail1Spike1, spikes3Down, -6.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.tail1Spike2, spikes3Down, 6.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.torsoSpikes, spikes4Down, 0.0F, 10.0F, -27.0F, 1.0F);
        this.progressPositionPrev(this.torsoSpike2, spikes4Down, 12.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.torsoSpike3, spikes4Down, -12.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.chestSpikes, spikes5Down, 0.0F, 20.0F, -22.0F, 1.0F);
        this.progressPositionPrev(this.chestSpike2, spikes5Down, -10.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.chestSpike3, spikes5Down, 10.0F, 0.0F, 0.0F, 1.0F);
        this.progressPositionPrev(this.neckSpikes, spikes6Down, 0.0F, 4.0F, -7.0F, 1.0F);
        this.progressPositionPrev(this.leftArm, limbSwingAmountGround, 0.0F, -3.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.rightArm, limbSwingAmountGround, 0.0F, -3.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.torso, sitProgress, 0.0F, 5.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.leftLeg, sitProgress, 0.0F, 8.0F, 10.0F, 1.0F);
        this.progressPositionPrev(this.rightLeg, sitProgress, 0.0F, 8.0F, 10.0F, 1.0F);
        this.progressPositionPrev(this.tail1, sitProgress, 0.0F, 0.0F, -20.0F, 1.0F);
        this.progressPositionPrev(this.neck, sitProgress, 0.0F, 5.0F, -5.0F, 1.0F);
        this.progressPositionPrev(this.head, sitProgress, 0.0F, 0.0F, 5.0F, 1.0F);
        this.progressPositionPrev(this.torsoSpikes, sitProgress, 0.0F, 9.0F, -3.0F, 1.0F);
        this.progressRotationPrev(this.torso, sitProgress, (float) Math.toRadians(80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.chest, sitProgress, (float) Math.toRadians(10.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.leftLeg, sitProgress, (float) Math.toRadians(-80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.rightLeg, sitProgress, (float) Math.toRadians(-80.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.tail1, sitProgress, (float) Math.toRadians(-90.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(-10.0), 1.0F);
        this.progressRotationPrev(this.neck, sitProgress, (float) Math.toRadians(-25.0), 0.0F, (float) Math.toRadians(30.0), 1.0F);
        this.progressRotationPrev(this.head, sitProgress, (float) Math.toRadians(-40.0), (float) Math.toRadians(-20.0), (float) Math.toRadians(20.0), 1.0F);
        this.progressRotationPrev(this.rightArm, sitProgress, (float) Math.toRadians(-90.0), (float) Math.toRadians(-50.0), (float) Math.toRadians(50.0), 1.0F);
        this.progressRotationPrev(this.leftArm, sitProgress, (float) Math.toRadians(-90.0), (float) Math.toRadians(50.0), (float) Math.toRadians(-20.0), 1.0F);
        this.progressRotationPrev(this.chestSpikes, sitProgress, (float) Math.toRadians(-20.0), 0.0F, 0.0F, 1.0F);
        this.progressRotationPrev(this.torsoSpikes, sitProgress, (float) Math.toRadians(-40.0), 0.0F, 0.0F, 1.0F);
        float bodyIdleBob = ACMath.walkValue(ageInTicks, 1.0F, 0.025F, -1.0F, 3.0F, false) * standProgress;
        this.walk(this.neck, 0.025F, 0.03F, false, 1.0F, 0.0F, ageInTicks, 1.0F);
        this.walk(this.head, 0.025F, 0.03F, true, 2.0F, -0.03F, ageInTicks, 1.0F);
        this.walk(this.jaw, 0.025F, 0.03F, true, 3.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail1, 0.025F, 0.05F, true, -1.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail2, 0.025F, 0.05F, true, -2.0F, 0.0F, ageInTicks, 1.0F);
        this.swing(this.tail3, 0.025F, 0.1F, true, -3.0F, 0.0F, ageInTicks, 1.0F);
        this.flap(this.leftArm, 0.025F, 0.1F, true, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.leftArm, 0.025F, 0.1F, true, 2.0F, -0.1F, ageInTicks, 1.0F);
        this.flap(this.rightArm, 0.025F, 0.1F, false, 1.0F, -0.1F, ageInTicks, 1.0F);
        this.swing(this.rightArm, 0.025F, 0.1F, false, 2.0F, -0.1F, ageInTicks, 1.0F);
        this.walk(this.chest, 0.025F, 0.03F, true, -1.0F, -0.02F, ageInTicks, 1.0F);
        float bodyWalkBob = -Math.abs(ACMath.walkValue(limbSwing, limbSwingAmountGround, walkSpeed, -1.5F, 6.0F, false));
        this.torso.rotationPointY += bodyIdleBob + bodyWalkBob;
        this.leftLeg.rotationPointY -= (bodyIdleBob + bodyWalkBob) * groundProgress;
        this.rightLeg.rotationPointY -= (bodyIdleBob + bodyWalkBob) * groundProgress;
        this.tail1.rotationPointY -= (bodyIdleBob + bodyWalkBob) * groundProgress;
        this.walk(this.torso, walkSpeed, walkDegree * 0.05F, true, 1.5F, -0.2F, limbSwing, limbSwingAmountGround);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 0.05F, false, 1.5F, -0.2F, limbSwing, limbSwingAmountGround);
        this.walk(this.rightLeg, walkSpeed, walkDegree * 0.05F, false, 1.5F, -0.2F, limbSwing, limbSwingAmountGround);
        this.walk(this.tail1, walkSpeed, walkDegree * 0.05F, false, 1.5F, -0.2F, limbSwing, limbSwingAmountGround);
        this.swing(this.tail1, walkSpeed, walkDegree * 0.05F, false, 2.0F, 0.0F, limbSwing, limbSwingAmountGround);
        this.swing(this.tail2, walkSpeed, walkDegree * 0.05F, false, 1.0F, 0.0F, limbSwing, limbSwingAmountGround);
        this.swing(this.tail3, walkSpeed, walkDegree * 0.05F, false, 0.0F, 0.0F, limbSwing, limbSwingAmountGround);
        this.swing(this.tail4, walkSpeed, walkDegree * 0.1F, false, -1.0F, 0.0F, limbSwing, limbSwingAmountGround);
        this.swing(this.leftArm, walkSpeed, walkDegree * 0.2F, false, -2.0F, 0.45F, limbSwing, limbSwingAmountGround);
        this.swing(this.rightArm, walkSpeed, walkDegree * 0.2F, false, -2.0F, -0.45F, limbSwing, limbSwingAmountGround);
        this.walk(this.neck, walkSpeed, walkDegree * 0.05F, true, 1.0F, -0.1F, limbSwing, limbSwingAmountGround);
        this.walk(this.head, walkSpeed, walkDegree * 0.05F, true, 2.5F, 0.2F, limbSwing, limbSwingAmountGround);
        this.walk(this.leftLeg, walkSpeed, walkDegree * 0.2F, false, 1.5F, 0.0F, limbSwing, limbSwingAmountGround);
        this.leftLeg.rotationPointY = this.leftLeg.rotationPointY + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmountGround, walkSpeed, -0.5F, 20.0F, true));
        this.leftLeg.rotationPointZ = this.leftLeg.rotationPointZ + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmountGround, walkSpeed, -1.0F, 5.0F, true));
        this.walk(this.rightLeg, walkSpeed, walkDegree * 0.2F, true, 1.5F, 0.0F, limbSwing, limbSwingAmountGround);
        this.rightLeg.rotationPointY = this.rightLeg.rotationPointY + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmountGround, walkSpeed, -0.5F, 20.0F, false));
        this.rightLeg.rotationPointZ = this.rightLeg.rotationPointZ + Math.min(0.0F, ACMath.walkValue(limbSwing, limbSwingAmountGround, walkSpeed, -1.0F, 5.0F, false));
        this.bob(this.root, swimSpeed, 4.0F, false, limbSwing, limbSwingAmountSwim);
        this.swing(this.tail2, swimSpeed, 0.1F, true, -2.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.swing(this.tail3, swimSpeed, 0.2F, true, -3.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.swing(this.rightArm, swimSpeed, 0.5F, true, -1.0F, -0.5F, limbSwing, limbSwingAmountSwim);
        this.flap(this.rightArm, swimSpeed, 0.2F, true, -2.0F, -0.4F, limbSwing, limbSwingAmountSwim);
        this.swing(this.leftArm, swimSpeed, 0.5F, true, -1.0F, 0.5F, limbSwing, limbSwingAmountSwim);
        this.flap(this.leftArm, swimSpeed, 0.2F, true, -2.0F, 0.4F, limbSwing, limbSwingAmountSwim);
        this.walk(this.leftLeg, swimSpeed, 0.3F, true, -4.0F, 0.1F, limbSwing, limbSwingAmountSwim);
        this.flap(this.leftLeg, swimSpeed, 0.1F, false, -4.0F, -0.1F, limbSwing, limbSwingAmountSwim);
        this.walk(this.rightLeg, swimSpeed, 0.3F, false, -4.0F, -0.1F, limbSwing, limbSwingAmountSwim);
        this.flap(this.rightLeg, swimSpeed, 0.1F, true, -4.0F, -0.1F, limbSwing, limbSwingAmountSwim);
        this.swing(this.torso, swimSpeed, 0.05F, true, -3.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.flap(this.chest, swimSpeed, 0.1F, true, -3.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.flap(this.neck, swimSpeed, 0.2F, true, -3.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.flap(this.head, swimSpeed, 0.2F, false, -3.0F, 0.0F, limbSwing, limbSwingAmountSwim);
        this.flap(this.chest, 1.0F, 0.03F, true, -1.0F, 0.0F, ageInTicks, burnProgress);
        this.flap(this.neck, 2.0F, 0.03F, true, -2.0F, 0.0F, ageInTicks, burnProgress);
        this.flap(this.head, 2.0F, 0.03F, true, -2.0F, 0.0F, ageInTicks, burnProgress);
        this.walk(this.jaw, 0.4F, 0.1F, true, -1.0F, 0.0F, ageInTicks, burnProgress);
        this.walk(this.head, 0.4F, 0.1F, false, 0.0F, 0.0F, ageInTicks, burnProgress);
        if (burnProgress > 0.0F && burnPos != null) {
            Vec3 vector3d1 = entity.getBeamShootFrom(partialTicks);
            Vec3 normalized = burnPos.subtract(vector3d1).normalize();
            double d0 = Mth.clamp(normalized.y, -1.0, 1.0) * Math.PI / 2.0;
            Vec3 vector3d2 = entity.getBodyRotViewVector(0.0F);
            vector3d2 = new Vec3(vector3d2.x, 0.0, vector3d2.z);
            Vec3 vector3d3 = new Vec3(burnPos.x - vector3d1.x, 0.0, burnPos.z - vector3d1.z).normalize().yRot((float) (Math.PI / 2));
            double d1 = vector3d2.dot(vector3d3);
            float xRotBy = (float) (d0 * (double) burnProgress);
            this.neck.rotateAngleX -= xRotBy * 0.5F;
            this.head.rotateAngleX -= xRotBy * 0.5F;
            this.neck.rotateAngleY = (float) ((double) this.neck.rotateAngleY + d1 * (double) burnProgress);
            if (xRotBy > 0.0F) {
                this.neck.rotationPointY += 5.0F * xRotBy;
                this.neck.rotationPointZ += 5.0F * xRotBy;
                this.head.rotationPointZ += 5.0F * xRotBy;
            }
        }
        if (!entity.isTremorzillaSwimming()) {
            this.neck.rotateAngleY += headYawAmount * 0.5F;
            this.head.rotateAngleY += headYawAmount * 0.5F;
        }
        this.neck.rotateAngleX += headPitchAmount * 0.5F;
        this.head.rotateAngleX += headPitchAmount * 0.5F;
    }

    public void showSpikesBasedOnProgress(float spikesDownProgress, float threshold) {
        boolean spikes1Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 0.0F) > threshold;
        boolean spikes2Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 1.0F) > threshold;
        boolean spikes3Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 2.0F) > threshold;
        boolean spikes4Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 3.0F) > threshold;
        boolean spikes5Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 4.0F) > threshold;
        boolean spikes6Down = TremorzillaEntity.calculateSpikesDownAmountAtIndex(spikesDownProgress, 6.0F, 5.0F) > threshold;
        this.tail3.showModel = spikes1Down;
        this.tail3Spikes.showModel = spikes1Down;
        this.tail2.showModel = spikes2Down;
        this.tail2Spikes.showModel = spikes2Down;
        this.tail1.showModel = spikes3Down;
        this.tail1Spikes.showModel = spikes3Down;
        this.tail1Spike1.showModel = spikes3Down;
        this.tail1Spike2.showModel = spikes3Down;
        this.torso.showModel = spikes4Down;
        this.torsoSpikes.showModel = spikes4Down;
        this.torsoSpike1.showModel = spikes4Down;
        this.torsoSpike2.showModel = spikes4Down;
        this.torsoSpike3.showModel = spikes4Down;
        this.chest.showModel = spikes5Down;
        this.neck.showModel = spikes6Down;
    }

    public void showAllSpikes() {
        this.tail3.showModel = true;
        this.tail3Spikes.showModel = true;
        this.tail2.showModel = true;
        this.tail2Spikes.showModel = true;
        this.tail1.showModel = true;
        this.tail1Spikes.showModel = true;
        this.tail1Spike1.showModel = true;
        this.tail1Spike2.showModel = true;
        this.torso.showModel = true;
        this.torsoSpikes.showModel = true;
        this.torsoSpike1.showModel = true;
        this.torsoSpike2.showModel = true;
        this.torsoSpike3.showModel = true;
        this.chest.showModel = true;
        this.neck.showModel = true;
    }

    private void positionTail(TremorzillaEntity entity, float partialTicks) {
        float tailPart1Pitch = (float) Math.toRadians((double) entity.tailPart1.calculateAnimationAngle(partialTicks, true));
        float tailPart2Pitch = (float) Math.toRadians((double) entity.tailPart2.calculateAnimationAngle(partialTicks, true));
        float f = entity.tailPart3.calculateAnimationAngle(partialTicks, true) + entity.tailPart4.calculateAnimationAngle(partialTicks, true);
        float tailPart3Pitch = (float) Math.toRadians((double) (Mth.wrapDegrees(f) * 0.25F));
        float f1 = entity.tailPart4.calculateAnimationAngle(partialTicks, true) + entity.tailPart5.calculateAnimationAngle(partialTicks, true);
        float tailPart4Pitch = (float) Math.toRadians((double) (Mth.wrapDegrees(f1) * 0.25F));
        float tailPart1Yaw = (float) Math.toRadians((double) entity.tailPart1.calculateAnimationAngle(partialTicks, false));
        float tailPart2Yaw = (float) Math.toRadians((double) entity.tailPart2.calculateAnimationAngle(partialTicks, false));
        float f2 = entity.tailPart3.calculateAnimationAngle(partialTicks, false) + entity.tailPart4.calculateAnimationAngle(partialTicks, false);
        float tailPart3Yaw = (float) Math.toRadians((double) (Mth.wrapDegrees(f2) * 0.25F));
        float f3 = entity.tailPart4.calculateAnimationAngle(partialTicks, false) + entity.tailPart5.calculateAnimationAngle(partialTicks, false);
        float tailPart4Yaw = (float) Math.toRadians((double) (Mth.wrapDegrees(f3) * 0.25F));
        this.tail1.rotateAngleY += tailPart1Yaw;
        this.tail1.rotateAngleX += tailPart1Pitch;
        this.tail2.rotateAngleY += tailPart2Yaw;
        this.tail2.rotateAngleX += tailPart2Pitch;
        this.tail3.rotateAngleY += tailPart3Yaw;
        this.tail3.rotateAngleX += tailPart3Pitch;
        this.tail4.rotateAngleY += tailPart4Yaw;
        this.tail4.rotateAngleX += tailPart4Pitch;
    }

    private void articulateLegs(TremorzillaLegSolver legs, float partialTick, float modifier) {
        float heightBackLeft = legs.legs[0].getHeight(partialTick);
        float heightBackRight = legs.legs[1].getHeight(partialTick);
        float max = (1.0F - ACMath.smin(1.0F - heightBackLeft, 1.0F - heightBackRight, 0.1F)) * 0.8F;
        this.root.rotationPointY += max * 16.0F * modifier;
        this.rightLeg.rotationPointY += (heightBackRight - max) * 16.0F * modifier;
        this.leftLeg.rotationPointY += (heightBackLeft - max) * 16.0F * modifier;
    }

    private void animateDancing(TremorzillaEntity entity, float danceAmount, float ageInTicks) {
        float danceSpeed = 0.2F;
        this.root.rotationPointY = this.root.rotationPointY - Math.abs(ACMath.walkValue(ageInTicks, danceAmount, danceSpeed, -1.0F, 50.0F, true));
        this.swing(this.root, danceSpeed, 0.2F, true, 0.5F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.neck, danceSpeed, 0.2F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.tail1, danceSpeed, 0.2F, false, 1.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.tail2, danceSpeed, 0.1F, false, 2.0F, 0.0F, ageInTicks, danceAmount);
        this.swing(this.tail4, danceSpeed, 0.1F, false, 3.0F, 0.0F, ageInTicks, danceAmount);
        this.walk(this.leftLeg, danceSpeed, 0.6F, false, 0.5F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.leftLeg, danceSpeed, 0.1F, false, 2.0F, 0.1F, ageInTicks, danceAmount);
        this.walk(this.rightLeg, danceSpeed, 0.6F, true, 1.5F, 0.0F, ageInTicks, danceAmount);
        this.flap(this.rightLeg, danceSpeed, 0.1F, true, 2.0F, 0.1F, ageInTicks, danceAmount);
        this.walk(this.rightArm, danceSpeed, 0.75F, false, 1.5F, -0.5F, ageInTicks, danceAmount);
        this.swing(this.rightArm, danceSpeed, 0.75F, false, 1.0F, -0.4F, ageInTicks, danceAmount);
        this.walk(this.leftArm, danceSpeed, 0.75F, true, 1.5F, 0.5F, ageInTicks, danceAmount);
        this.swing(this.leftArm, danceSpeed, 0.75F, false, 1.0F, 0.4F, ageInTicks, danceAmount);
    }

    private void setupAnimForAnimation(TremorzillaEntity entity, Animation animation, float limbSwing, float limbSwingAmount, float ageInTicks) {
        float partialTick = ageInTicks - (float) entity.f_19797_;
        if (entity.getAnimation() == TremorzillaEntity.ANIMATION_ROAR_1 || entity.getAnimation() == TremorzillaEntity.ANIMATION_ROAR_2) {
            float animationIntensity = 0.0F;
            float neckSide = 1.0F;
            if (entity.getAnimation() == TremorzillaEntity.ANIMATION_ROAR_1) {
                animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 10, 50);
            } else {
                neckSide = 0.2F;
                animationIntensity = ACMath.cullAnimationTick(entity.getAnimationTick(), 1.0F, animation, partialTick, 15, 50);
            }
            this.neck.swing(0.2F, neckSide * 0.7F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.neck.walk(0.2F, neckSide * 0.3F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.chest.swing(0.2F, 0.1F, false, 0.0F, 0.0F, ageInTicks, animationIntensity);
            this.chest.walk(0.2F, 0.05F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.head.swing(0.2F, neckSide * 0.3F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
            this.head.swing(1.0F, 0.1F, false, -1.0F, 0.0F, ageInTicks, animationIntensity);
            this.head.walk(0.2F, 0.3F, false, 2.0F, -0.2F, ageInTicks, animationIntensity);
            this.jaw.walk(2.0F, 0.1F, false, 1.0F, 0.0F, ageInTicks, animationIntensity);
        }
    }

    public Vec3 getMouthPosition(Vec3 offsetIn) {
        PoseStack translationStack = new PoseStack();
        translationStack.pushPose();
        this.root.translateAndRotate(translationStack);
        this.torso.translateAndRotate(translationStack);
        this.chest.translateAndRotate(translationStack);
        this.neck.translateAndRotate(translationStack);
        this.head.translateAndRotate(translationStack);
        Vector4f armOffsetVec = new Vector4f((float) offsetIn.x, (float) offsetIn.y, (float) offsetIn.z, 1.0F);
        armOffsetVec.mul(translationStack.last().pose());
        Vec3 vec3 = new Vec3((double) (-armOffsetVec.x()), (double) (-armOffsetVec.y()), (double) armOffsetVec.z());
        translationStack.popPose();
        return vec3.add(0.0, 1.25, 0.0);
    }

    public void translateToNeck(PoseStack translationStack) {
        this.root.translateAndRotate(translationStack);
        this.torso.translateAndRotate(translationStack);
        this.chest.translateAndRotate(translationStack);
        this.neck.translateAndRotate(translationStack);
        this.head.translateAndRotate(translationStack);
    }

    @Override
    public Iterable<BasicModelPart> parts() {
        return ImmutableList.of(this.root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(this.root, this.torso, this.torsoSpikes, this.rightLeg, this.leftLeg, this.chest, this.chestSpikes, this.neck, this.neckSlope, this.rightArm, this.leftArm, this.rightThumb, new AdvancedModelBox[] { this.leftThumb, this.head, this.jaw, this.rightEar, this.leftEar, this.neckSpikes, this.tail1, this.tail1Spikes, this.tail2, this.tail2Spikes, this.tail3, this.tail3Spikes, this.tail4, this.torsoSpike1, this.torsoSpike2, this.torsoSpike3, this.tail1Spike1, this.tail1Spike2, this.cube_r6, this.chestSpike1, this.chestSpike2, this.chestSpike3 });
    }
}