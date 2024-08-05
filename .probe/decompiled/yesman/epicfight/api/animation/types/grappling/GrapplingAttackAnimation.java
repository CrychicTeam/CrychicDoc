package yesman.epicfight.api.animation.types.grappling;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public class GrapplingAttackAnimation extends AttackAnimation {

    public GrapplingAttackAnimation(float contact, float recovery, String path, Armature armature) {
        this(contact, recovery, InteractionHand.MAIN_HAND, path, armature);
    }

    public GrapplingAttackAnimation(float contact, float recovery, InteractionHand hand, String path, Armature armature) {
        super(0.0F, 0.0F, contact, contact, recovery, hand, null, armature.rootJoint, path, armature);
        this.addProperty(AnimationProperty.AttackAnimationProperty.ATTACK_SPEED_FACTOR, Float.valueOf(0.0F));
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_ON_LINK, Boolean.valueOf(false));
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_GET, MoveCoordFunctions.ATTACHED);
    }

    @Override
    public void begin(LivingEntityPatch<?> entitypatch) {
        if (entitypatch.shouldMoveOnCurrentSide(this)) {
            Keyframe[] grapplingAnimCoord = entitypatch.getArmature().getActionAnimationCoord().getKeyframes();
            Vec3f translation = grapplingAnimCoord[grapplingAnimCoord.length - 1].transform().translation();
            entitypatch.getOriginal().m_20334_(0.0, 0.0, 0.0);
            entitypatch.getOriginal().m_146884_(translation.toDoubleVector());
        }
        super.begin(entitypatch);
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        entitypatch.setGrapplingTarget(null);
    }

    @Override
    protected void attackTick(LivingEntityPatch<?> entitypatch) {
        AnimationPlayer player = entitypatch.<Animator>getAnimator().getPlayerFor(this);
        float elapsedTime = player.getElapsedTime();
        float prevElapsedTime = player.getPrevElapsedTime();
        EntityState state = this.getState(entitypatch, elapsedTime);
        EntityState prevState = this.getState(entitypatch, prevElapsedTime);
        AttackAnimation.Phase phase = this.getPhaseByTime(elapsedTime);
        if (prevState.attacking() || state.attacking() || prevState.getLevel() < 2 && state.getLevel() > 2) {
            LivingEntity grapplingTarget = entitypatch.getGrapplingTarget();
            if (grapplingTarget != null) {
                EpicFightDamageSource source = this.getEpicFightDamageSource(entitypatch, grapplingTarget, phase);
                source.setInitialPosition(null);
                int prevInvulTime = grapplingTarget.f_19802_;
                grapplingTarget.f_19802_ = 0;
                entitypatch.attack(source, grapplingTarget, InteractionHand.MAIN_HAND);
                grapplingTarget.f_19802_ = prevInvulTime;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderDebugging(PoseStack poseStack, MultiBufferSource buffer, LivingEntityPatch<?> entitypatch, float playTime, float partialTicks) {
    }
}