package yesman.epicfight.world.capabilities.entitypatch;

import com.google.common.collect.Sets;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSetAttackTarget;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public abstract class MobPatch<T extends Mob> extends LivingEntityPatch<T> {

    protected final Faction mobFaction;

    public MobPatch() {
        this.mobFaction = Faction.NEUTRAL;
    }

    public MobPatch(Faction faction) {
        this.mobFaction = faction;
    }

    public void onJoinWorld(T entityIn, EntityJoinLevelEvent event) {
        super.onJoinWorld(entityIn, event);
        if (!entityIn.m_9236_().isClientSide() && !this.original.isNoAi()) {
            this.initAI();
        }
    }

    protected void initAI() {
        Set<Goal> toRemove = Sets.newHashSet();
        this.selectGoalToRemove(toRemove);
        toRemove.forEach(this.original.goalSelector::m_25363_);
    }

    protected void selectGoalToRemove(Set<Goal> toRemove) {
        for (WrappedGoal wrappedGoal : this.original.goalSelector.getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();
            if (goal instanceof MeleeAttackGoal || goal instanceof AnimatedAttackGoal || goal instanceof RangedAttackGoal || goal instanceof TargetChasingGoal) {
                toRemove.add(goal);
            }
        }
    }

    protected final void commonMobUpdateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.INACTION;
        } else if (this.original.m_20202_() != null) {
            this.currentLivingMotion = LivingMotions.MOUNT;
        } else if (!(this.original.m_20184_().y < -0.55F) && !this.isAirborneState()) {
            if (this.original.f_267362_.speed() > 0.01F) {
                this.currentLivingMotion = LivingMotions.WALK;
            } else {
                this.currentLivingMotion = LivingMotions.IDLE;
            }
        } else {
            this.currentLivingMotion = LivingMotions.FALL;
        }
        this.currentCompositeMotion = this.currentLivingMotion;
    }

    protected final void commonAggressiveMobUpdateMotion(boolean considerInaction) {
        if (this.original.m_21223_() <= 0.0F) {
            this.currentLivingMotion = LivingMotions.DEATH;
        } else if (this.state.inaction() && considerInaction) {
            this.currentLivingMotion = LivingMotions.IDLE;
        } else if (this.original.m_20202_() != null) {
            this.currentLivingMotion = LivingMotions.MOUNT;
        } else if (!(this.original.m_20184_().y < -0.55F) && !this.isAirborneState()) {
            if (this.original.f_267362_.speed() > 0.08F) {
                if (this.original.isAggressive()) {
                    this.currentLivingMotion = LivingMotions.CHASE;
                } else {
                    this.currentLivingMotion = LivingMotions.WALK;
                }
            } else {
                this.currentLivingMotion = LivingMotions.IDLE;
            }
        } else {
            this.currentLivingMotion = LivingMotions.FALL;
        }
        this.currentCompositeMotion = this.currentLivingMotion;
    }

    protected final void commonAggressiveRangedMobUpdateMotion(boolean considerInaction) {
        this.commonAggressiveMobUpdateMotion(considerInaction);
        UseAnim useAction = this.original.m_21120_(this.original.m_7655_()).getUseAnimation();
        if (this.original.m_6117_()) {
            if (useAction == UseAnim.CROSSBOW) {
                this.currentCompositeMotion = LivingMotions.RELOAD;
            } else {
                this.currentCompositeMotion = LivingMotions.AIM;
            }
        } else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation()) {
            this.currentCompositeMotion = LivingMotions.NONE;
        }
        if (CrossbowItem.isCharged(this.original.m_21205_())) {
            this.currentCompositeMotion = LivingMotions.AIM;
        } else if (this.getClientAnimator().isAiming() && this.currentCompositeMotion != LivingMotions.AIM) {
            this.playReboundAnimation();
        }
    }

    @Override
    public void updateArmor(CapabilityItem fromCap, CapabilityItem toCap, EquipmentSlot slotType) {
        if (this.original.m_21204_().hasAttribute(EpicFightAttributes.STUN_ARMOR.get())) {
            if (fromCap != null) {
                this.original.m_21204_().removeAttributeModifiers(fromCap.getAttributeModifiers(slotType, this));
            }
            if (toCap != null) {
                this.original.m_21204_().addTransientAttributeModifiers(toCap.getAttributeModifiers(slotType, this));
            }
        }
    }

    @Override
    public boolean isTeammate(Entity entityIn) {
        EntityPatch<?> cap = EpicFightCapabilities.getEntityPatch(entityIn, EntityPatch.class);
        if (cap != null && cap instanceof MobPatch && ((MobPatch) cap).mobFaction.equals(this.mobFaction)) {
            Optional<LivingEntity> opt = Optional.ofNullable(this.getTarget());
            return (Boolean) opt.map(attackTarget -> !attackTarget.m_7306_(entityIn)).orElse(true);
        } else {
            return super.isTeammate(entityIn);
        }
    }

    @Override
    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        boolean shouldSwap = hand == InteractionHand.OFF_HAND;
        this.epicFightDamageSource = damageSource;
        this.setOffhandDamage(shouldSwap);
        this.original.doHurtTarget(target);
        this.recoverMainhandDamage(shouldSwap);
        this.epicFightDamageSource = null;
        return super.attack(damageSource, target, hand);
    }

    @Override
    public LivingEntity getTarget() {
        return this.original.getTarget();
    }

    public void setAttakTargetSync(LivingEntity entityIn) {
        if (!this.original.m_9236_().isClientSide()) {
            this.original.setTarget(entityIn);
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(new SPSetAttackTarget(this.original.m_19879_(), entityIn != null ? entityIn.m_19879_() : -1), this.original);
        }
    }

    @Override
    public float getAttackDirectionPitch() {
        Entity attackTarget = this.getTarget();
        if (attackTarget != null) {
            float partialTicks = EpicFightMod.isPhysicalClient() ? Minecraft.getInstance().getFrameTime() : 1.0F;
            Vec3 target = attackTarget.getEyePosition(partialTicks);
            Vec3 vector3d = this.original.m_20299_(partialTicks);
            double d0 = target.x - vector3d.x;
            double d1 = target.y - vector3d.y;
            double d2 = target.z - vector3d.z;
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            return Mth.clamp(Mth.wrapDegrees((float) (Mth.atan2(d1, d3) * 180.0F / (float) Math.PI)), -30.0F, 30.0F);
        } else {
            return super.getAttackDirectionPitch();
        }
    }
}