package yesman.epicfight.world.capabilities.entitypatch;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.data.reloader.MobPatchReloadListener;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.brain.BrainRecomposer;
import yesman.epicfight.world.entity.ai.brain.task.AnimatedCombatBehavior;
import yesman.epicfight.world.entity.ai.brain.task.BackUpIfTooCloseStopInaction;
import yesman.epicfight.world.entity.ai.brain.task.MoveToTargetSinkStopInaction;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class CustomHumanoidMobPatch<T extends PathfinderMob> extends HumanoidMobPatch<T> {

    private final MobPatchReloadListener.CustomHumanoidMobPatchProvider provider;

    public CustomHumanoidMobPatch(Faction faction, MobPatchReloadListener.CustomHumanoidMobPatchProvider provider) {
        super(faction);
        this.provider = provider;
        this.weaponLivingMotions = this.provider.getHumanoidWeaponMotions();
        this.weaponAttackMotions = this.provider.getHumanoidCombatBehaviors();
    }

    @Override
    public void setAIAsInfantry(boolean holdingRanedWeapon) {
        boolean isUsingBrain = this.getOriginal().m_6274_().availableBehaviorsByPriority.size() > 0;
        if (isUsingBrain) {
            if (!holdingRanedWeapon) {
                CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
                if (builder != null) {
                    BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.FIGHT, MeleeAttack.class, new AnimatedCombatBehavior<>(this, builder.build(this)));
                }
                BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.FIGHT, OneShot.class, BehaviorBuilder.triggerIf(entity -> entity.m_21093_(is -> is.getItem() instanceof CrossbowItem), BackUpIfTooCloseStopInaction.create(5, 0.75F)));
                BrainRecomposer.replaceBehaviors(this.original.m_6274_(), Activity.CORE, MoveToTargetSink.class, new MoveToTargetSinkStopInaction());
            }
        } else if (!holdingRanedWeapon) {
            CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
            if (builder != null) {
                this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, builder.build(this)));
                this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), this.provider.getChasingSpeed(), true));
            }
        }
    }

    @Override
    protected void setWeaponMotions() {
        if (this.weaponAttackMotions == null) {
            super.setWeaponMotions();
        }
    }

    @Override
    protected void initAttributes() {
        this.original.m_21051_(EpicFightAttributes.WEIGHT.get()).setBaseValue(this.original.m_21051_(Attributes.MAX_HEALTH).getBaseValue() * 2.0);
        this.original.m_21051_(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.MAX_STRIKES.get()));
        this.original.m_21051_(EpicFightAttributes.ARMOR_NEGATION.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.ARMOR_NEGATION.get()));
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.IMPACT.get()));
        if (this.provider.getAttributeValues().containsKey(Attributes.ATTACK_DAMAGE)) {
            this.original.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((Double) this.provider.getAttributeValues().get(Attributes.ATTACK_DAMAGE));
        }
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        for (Pair<LivingMotion, StaticAnimation> pair : this.provider.getDefaultAnimations()) {
            clientAnimator.addLivingAnimation((LivingMotion) pair.getFirst(), (StaticAnimation) pair.getSecond());
        }
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        super.commonAggressiveMobUpdateMotion(considerInaction);
        if (this.original.m_6117_()) {
            CapabilityItem activeItem = this.getHoldingItemCapability(this.original.m_7655_());
            UseAnim useAnim = this.original.m_21120_(this.original.m_7655_()).getUseAnimation();
            UseAnim secondUseAnim = activeItem.getUseAnimation(this);
            if (useAnim == UseAnim.BLOCK || secondUseAnim == UseAnim.BLOCK) {
                if (activeItem.getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD) {
                    this.currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
                } else {
                    this.currentCompositeMotion = LivingMotions.BLOCK;
                }
            } else if (useAnim == UseAnim.BOW || useAnim == UseAnim.SPEAR) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (useAnim == UseAnim.CROSSBOW) {
                this.currentCompositeMotion = LivingMotions.RELOAD;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
        } else {
            if (CrossbowItem.isCharged(this.original.m_21205_())) {
                this.currentCompositeMotion = LivingMotions.AIM;
            } else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().isReboundAnimation()) {
                this.currentCompositeMotion = LivingMotions.NONE;
            } else if (this.original.f_20911_ && this.original.m_21257_().isEmpty()) {
                this.currentCompositeMotion = LivingMotions.DIGGING;
            } else {
                this.currentCompositeMotion = this.currentLivingMotion;
            }
            if (this.getClientAnimator().isAiming() && this.currentCompositeMotion != LivingMotions.AIM) {
                this.playReboundAnimation();
            }
        }
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return (StaticAnimation) this.provider.getStunAnimations().get(stunType);
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        float scale = this.provider.getScale();
        return super.getModelMatrix(partialTicks).scale(scale, scale, scale);
    }
}