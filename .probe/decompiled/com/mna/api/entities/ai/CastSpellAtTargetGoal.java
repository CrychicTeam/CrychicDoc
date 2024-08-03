package com.mna.api.entities.ai;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;

public class CastSpellAtTargetGoal<T extends PathfinderMob> extends Goal {

    protected final T entity;

    protected final double moveSpeedAmp;

    protected int attackCooldown;

    protected final float maxAttackDistance;

    protected int attackTime = -1;

    protected int seeTime;

    protected boolean strafingClockwise;

    protected boolean strafingBackwards;

    protected int strafingTime = -1;

    protected ItemStack spell;

    protected boolean setItems;

    protected boolean hasCast = false;

    protected Consumer<T> startCallback;

    protected Consumer<T> stopCallback;

    protected Consumer<T> resetCallback;

    protected Function<T, Boolean> precastCallback;

    protected LivingEntity target;

    public CastSpellAtTargetGoal(T mob, ItemStack spell, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        this(mob, spell, moveSpeedAmpIn, attackCooldownIn, maxAttackDistanceIn, true);
    }

    public CastSpellAtTargetGoal(T mob, ItemStack spell, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn, boolean setItems) {
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.spell = spell;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.setItems = setItems;
    }

    public CastSpellAtTargetGoal<T> setAttackCooldown(int attackCooldownIn) {
        this.attackCooldown = attackCooldownIn;
        return this;
    }

    public CastSpellAtTargetGoal<T> setSpell(ItemStack spell) {
        this.spell = spell.copy();
        return this;
    }

    public CastSpellAtTargetGoal<T> setStartCallback(Consumer<T> callback) {
        this.startCallback = callback;
        return this;
    }

    public CastSpellAtTargetGoal<T> setStopCallback(Consumer<T> callback) {
        this.stopCallback = callback;
        return this;
    }

    public CastSpellAtTargetGoal<T> setResetCallback(Consumer<T> callback) {
        this.resetCallback = callback;
        return this;
    }

    public CastSpellAtTargetGoal<T> setPrecastCallback(Function<T, Boolean> callback) {
        this.precastCallback = callback;
        return this;
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && ManaAndArtificeMod.getSpellHelper().containsSpell(this.spell);
    }

    @Override
    public boolean canContinueToUse() {
        return this.hasCast ? false : this.canUse() || !this.entity.m_21573_().isDone();
    }

    @Override
    public void start() {
        super.start();
        this.hasCast = false;
        this.entity.m_21561_(true);
        this.target = this.entity.m_5448_();
        if (this.startCallback != null) {
            this.startCallback.accept(this.entity);
        }
        if (this.setItems) {
            this.entity.m_21008_(InteractionHand.MAIN_HAND, this.spell);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.m_21561_(false);
        this.seeTime = 0;
        this.attackTime = -1;
        if (this.setItems) {
            this.entity.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        this.entity.m_5810_();
        if (this.stopCallback != null) {
            this.stopCallback.accept(this.entity);
        }
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.target;
        if (livingentity != null) {
            double d0 = this.entity.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            boolean canSee = this.entity.m_21574_().hasLineOfSight(livingentity);
            boolean positiveSeeTime = this.seeTime > 0;
            if (canSee != positiveSeeTime) {
                this.seeTime = 0;
            }
            if (canSee) {
                this.seeTime++;
            } else {
                this.seeTime--;
            }
            if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
                this.entity.m_21573_().stop();
                this.strafingTime++;
            } else {
                this.entity.m_21573_().moveTo(livingentity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double) this.entity.m_217043_().nextFloat() < 0.3) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.entity.m_217043_().nextFloat() < 0.3) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.entity.m_21566_().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.m_21391_(livingentity, 30.0F, 30.0F);
            } else {
                this.entity.m_21563_().setLookAt(livingentity, 30.0F, 30.0F);
            }
            if (--this.attackTime <= 0 && this.seeTime >= -60) {
                ISpellDefinition recipe = ManaAndArtificeMod.getSpellHelper().parseSpellDefinition(this.spell);
                if (!canSee && this.seeTime < -60) {
                    this.attackTime = this.attackCooldown == -1 ? recipe.getCooldown(null) : this.attackCooldown;
                    if (this.resetCallback != null) {
                        this.resetCallback.accept(this.entity);
                    }
                } else if (canSee) {
                    if (this.precastCallback != null && !(Boolean) this.precastCallback.apply(this.entity)) {
                        return;
                    }
                    this.entity.m_5810_();
                    SpellTarget targetHint = new SpellTarget(this.target);
                    ManaAndArtificeMod.getSpellHelper().affect(this.spell, recipe, this.entity.m_9236_(), new SpellSource(this.entity, InteractionHand.MAIN_HAND), targetHint);
                    this.onSpellCast(recipe);
                    this.attackTime = this.attackCooldown == -1 ? recipe.getCooldown(null) : this.attackCooldown;
                    this.hasCast = true;
                }
            }
        }
    }

    protected void onSpellCast(ISpellDefinition spell) {
    }

    protected T getEntity() {
        return this.entity;
    }
}