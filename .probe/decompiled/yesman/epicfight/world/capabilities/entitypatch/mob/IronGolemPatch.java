package yesman.epicfight.world.capabilities.entitypatch.mob;

import java.util.Set;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class IronGolemPatch extends MobPatch<IronGolem> {

    private int deathTimerExt;

    public IronGolemPatch() {
        super(Faction.VILLAGER);
    }

    @Override
    protected void initAI() {
        super.initAI();
        this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, MobCombatBehaviors.IRON_GOLEM.build(this)));
        this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.original, 1.0, false));
    }

    @Override
    protected void selectGoalToRemove(Set<Goal> toRemove) {
        super.selectGoalToRemove(toRemove);
        for (WrappedGoal wrappedGoal : this.original.f_21345_.getAvailableGoals()) {
            Goal goal = wrappedGoal.getGoal();
            if (goal instanceof MoveTowardsTargetGoal) {
                toRemove.add(goal);
            }
        }
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue(4.0);
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue(6.0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.GOLEM_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.GOLEM_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.GOLEM_DEATH);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        super.commonMobUpdateMotion(considerInaction);
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (this.original.m_21223_() <= 0.0F) {
            this.original.m_146926_(0.0F);
            if (this.original.f_20919_ > 1 && this.deathTimerExt < 20) {
                this.deathTimerExt++;
                this.original.f_20919_--;
            }
        }
        super.tick(event);
    }

    @Override
    public SoundEvent getWeaponHitSound(InteractionHand hand) {
        return EpicFightSounds.BLUNT_HIT_HARD.get();
    }

    @Override
    public SoundEvent getSwingSound(InteractionHand hand) {
        return EpicFightSounds.WHOOSH_BIG.get();
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return null;
    }
}