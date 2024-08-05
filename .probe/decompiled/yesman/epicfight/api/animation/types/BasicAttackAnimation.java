package yesman.epicfight.api.animation.types;

import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.client.animation.property.JointMask;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class BasicAttackAnimation extends AttackAnimation {

    public BasicAttackAnimation(float convertTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        this(convertTime, antic, antic, contact, recovery, collider, colliderJoint, path, armature);
    }

    public BasicAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, preDelay, contact, recovery, collider, colliderJoint, path, armature);
        this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(true));
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(false));
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    public BasicAttackAnimation(float convertTime, float antic, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, Armature armature) {
        super(convertTime, antic, antic, contact, recovery, hand, collider, colliderJoint, path, armature);
        this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(true));
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(false));
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    public BasicAttackAnimation(float convertTime, String path, Armature armature, AttackAnimation.Phase... phases) {
        super(convertTime, path, armature, phases);
        this.addProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE, Boolean.valueOf(true));
        this.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, Boolean.valueOf(false));
        this.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER);
    }

    @Override
    protected void bindPhaseState(AttackAnimation.Phase phase) {
        float preDelay = phase.preDelay;
        if (preDelay == 0.0F) {
            preDelay += 0.01F;
        }
        this.stateSpectrumBlueprint.newTimePair(phase.start, preDelay).addState(EntityState.PHASE_LEVEL, 1).newTimePair(phase.start, phase.contact + 0.01F).addState(EntityState.CAN_SKILL_EXECUTION, false).newTimePair(phase.start, phase.recovery).addState(EntityState.MOVEMENT_LOCKED, true).addState(EntityState.UPDATE_LIVING_MOTION, false).addState(EntityState.CAN_BASIC_ATTACK, false).newTimePair(phase.start, phase.end).addState(EntityState.INACTION, true).newTimePair(preDelay, phase.contact + 0.01F).addState(EntityState.ATTACKING, true).addState(EntityState.PHASE_LEVEL, 2).newTimePair(phase.contact + 0.01F, phase.end).addState(EntityState.PHASE_LEVEL, 3).addState(EntityState.TURNING_LOCKED, true);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!this.properties.containsKey(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED)) {
            float basisSpeed = Float.parseFloat(String.format(Locale.US, "%.2f", 1.0F / this.totalTime));
            this.addProperty(AnimationProperty.AttackAnimationProperty.BASIS_ATTACK_SPEED, Float.valueOf(basisSpeed));
        }
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, DynamicAnimation nextAnimation, boolean isEnd) {
        super.end(entitypatch, nextAnimation, isEnd);
        boolean stiffAttack = entitypatch.getOriginal().m_9236_().getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS).get();
        if (!isEnd && !nextAnimation.isMainFrameAnimation() && entitypatch.isLogicalClient() && !stiffAttack) {
            float playbackSpeed = 0.05F * this.getPlaySpeed(entitypatch);
            entitypatch.getClientAnimator().baseLayer.copyLayerTo(entitypatch.getClientAnimator().baseLayer.getLayer(Layer.Priority.HIGHEST), playbackSpeed);
        }
    }

    @Override
    public TypeFlexibleHashMap<EntityState.StateFactor<?>> getStatesMap(LivingEntityPatch<?> entitypatch, float time) {
        TypeFlexibleHashMap<EntityState.StateFactor<?>> stateMap = super.getStatesMap(entitypatch, time);
        if (!entitypatch.getOriginal().m_9236_().getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS).get()) {
            stateMap.put(EntityState.MOVEMENT_LOCKED, false);
            stateMap.put(EntityState.UPDATE_LIVING_MOTION, true);
        }
        return stateMap;
    }

    @Override
    protected Vec3 getCoordVector(LivingEntityPatch<?> entitypatch, DynamicAnimation dynamicAnimation) {
        Vec3 vec3 = super.getCoordVector(entitypatch, dynamicAnimation);
        if (entitypatch.shouldBlockMoving() && (Boolean) this.getProperty(AnimationProperty.ActionAnimationProperty.CANCELABLE_MOVE).orElse(false)) {
            vec3 = vec3.scale(0.0);
        }
        return vec3;
    }

    @Override
    public boolean isJointEnabled(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        return layer == Layer.Priority.HIGHEST ? !JointMaskEntry.BASIC_ATTACK_MASK.isMasked(entitypatch.getCurrentLivingMotion(), joint) : super.isJointEnabled(entitypatch, layer, joint);
    }

    @Override
    public JointMask.BindModifier getBindModifier(LivingEntityPatch<?> entitypatch, Layer.Priority layer, String joint) {
        if (layer == Layer.Priority.HIGHEST) {
            List<JointMask> list = JointMaskEntry.BIPED_UPPER_JOINTS_WITH_ROOT;
            int position = list.indexOf(JointMask.of(joint));
            return position >= 0 ? ((JointMask) list.get(position)).getBindModifier() : null;
        } else {
            return super.getBindModifier(entitypatch, layer, joint);
        }
    }

    @Override
    public boolean isBasicAttackAnimation() {
        return true;
    }

    @Override
    public boolean shouldPlayerMove(LocalPlayerPatch playerpatch) {
        return !playerpatch.isLogicalClient() || playerpatch.getOriginal().m_9236_().getGameRules().getRule(EpicFightGamerules.STIFF_COMBO_ATTACKS).get() || playerpatch.getOriginal().input.forwardImpulse == 0.0F && playerpatch.getOriginal().input.leftImpulse == 0.0F;
    }
}