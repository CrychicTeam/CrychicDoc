package noppes.npcs.client.model.animation;

import java.util.HashMap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;
import noppes.npcs.entity.EntityNPCInterface;

public class AnimationHandler {

    private static final HashMap<Integer, AnimationBase> ANIMATIONS = new HashMap();

    public static void animateBipedPre(ModelData data, HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityNPCInterface npc = (EntityNPCInterface) livingEntity;
        bipedModel.body.x = bipedModel.body.y = bipedModel.body.z = 0.0F;
        bipedModel.body.xRot = bipedModel.body.yRot = bipedModel.body.zRot = 0.0F;
        bipedModel.hat.xRot = bipedModel.head.xRot = 0.0F;
        bipedModel.hat.zRot = bipedModel.head.zRot = 0.0F;
        bipedModel.hat.x = bipedModel.head.x = 0.0F;
        bipedModel.hat.y = bipedModel.head.y = 0.0F;
        bipedModel.hat.z = bipedModel.head.z = 0.0F;
        bipedModel.leftLeg.xRot = 0.0F;
        bipedModel.leftLeg.yRot = 0.0F;
        bipedModel.leftLeg.zRot = 0.0F;
        bipedModel.rightLeg.xRot = 0.0F;
        bipedModel.rightLeg.yRot = 0.0F;
        bipedModel.rightLeg.zRot = 0.0F;
        bipedModel.leftArm.x = 0.0F;
        bipedModel.leftArm.y = 2.0F;
        bipedModel.leftArm.z = 0.0F;
        bipedModel.rightArm.x = 0.0F;
        bipedModel.rightArm.y = 2.0F;
        bipedModel.rightArm.z = 0.0F;
        AnimationBase animation = getAnimationFor(npc.currentAnimation);
        if (animation != null) {
            animation.animatePre(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, livingEntity, bipedModel, npc.animationStart);
        }
    }

    public static void animateBipedPost(ModelData data, HumanoidModel bipedModel, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityNPCInterface npc = (EntityNPCInterface) livingEntity;
        if (livingEntity.isSleeping() && bipedModel.head.xRot < 0.0F) {
            bipedModel.head.xRot = 90.0F;
            bipedModel.hat.xRot = 90.0F;
        }
        AnimationBase animation = getAnimationFor(npc.currentAnimation);
        if (animation != null) {
            animation.animatePost(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, livingEntity, bipedModel, npc.animationStart);
        }
        if (bipedModel.crouching) {
            bipedModel.body.xRot = 0.5F / data.getPartConfig(EnumParts.BODY).scaleY;
        }
        if (bipedModel instanceof PlayerModel playerModel) {
            playerModel.leftPants.copyFrom(playerModel.f_102814_);
            playerModel.rightPants.copyFrom(playerModel.f_102813_);
            playerModel.leftSleeve.copyFrom(playerModel.f_102812_);
            playerModel.rightSleeve.copyFrom(playerModel.f_102811_);
            playerModel.jacket.copyFrom(playerModel.f_102810_);
        }
        bipedModel.hat.copyFrom(bipedModel.head);
    }

    public static void addAnimation(int enumAnimation, AnimationBase animationBase) {
        ANIMATIONS.put(enumAnimation, animationBase);
    }

    public static HashMap<Integer, AnimationBase> getAllAnimations() {
        return ANIMATIONS;
    }

    public static AnimationBase getAnimationFor(int animation) {
        return (AnimationBase) ANIMATIONS.get(animation);
    }

    static {
        addAnimation(0, new AniBlank());
        addAnimation(2, new AniBlank());
        addAnimation(7, new AniCrawling());
        addAnimation(3, new AniHug());
        addAnimation(5, new AniDancing());
        addAnimation(10, new AniWaving());
        addAnimation(11, new AniBow());
        addAnimation(13, new AniYes());
        addAnimation(12, new AniNo());
        addAnimation(8, new AniPoint());
        addAnimation(14, new AniBlank());
        addAnimation(6, new AniAim());
        addAnimation(9, new AnimationBase() {

            @Override
            public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
            }

            @Override
            public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
                model.hat.xRot = model.head.xRot = 0.7F;
            }
        });
        addAnimation(1, new AnimationBase() {

            @Override
            public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
                model.f_102609_ = true;
            }

            @Override
            public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
                model.f_102609_ = false;
            }
        });
        addAnimation(4, new AnimationBase() {

            @Override
            public void animatePre(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
                model.crouching = true;
            }

            @Override
            public void animatePost(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, Entity entity, HumanoidModel model, int animationStart) {
                model.crouching = false;
            }
        });
    }
}