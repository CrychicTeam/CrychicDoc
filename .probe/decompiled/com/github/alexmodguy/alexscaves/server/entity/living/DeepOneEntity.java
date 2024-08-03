package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneBarterGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneDisappearGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneReactToPlayerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneTargetHostilePlayersGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DeepOneWanderGoal;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeepOneEntity extends DeepOneBaseEntity {

    public static final Animation ANIMATION_THROW = Animation.create(20);

    public static final Animation ANIMATION_BITE = Animation.create(8);

    public static final Animation ANIMATION_SCRATCH = Animation.create(22);

    public static final Animation ANIMATION_TRADE = Animation.create(55);

    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(0.99F, 0.99F, false);

    public static final ResourceLocation BARTER_LOOT = new ResourceLocation("alexscaves", "gameplay/deep_one_barter");

    public DeepOneEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new DeepOneAttackGoal(this));
        this.f_21345_.addGoal(1, new DeepOneBarterGoal(this));
        this.f_21345_.addGoal(2, new DeepOneReactToPlayerGoal(this));
        this.f_21345_.addGoal(3, new DeepOneDisappearGoal(this));
        this.f_21345_.addGoal(4, new DeepOneWanderGoal(this, 12, 1.0));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new DeepOneBaseEntity.HurtByHostileTargetGoal());
        this.f_21346_.addGoal(2, new DeepOneTargetHostilePlayersGoal(this));
    }

    @Override
    public EntityDimensions getSwimmingSize() {
        return SWIMMING_SIZE;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected ResourceLocation getBarterLootTable() {
        return BARTER_LOOT;
    }

    @Override
    public void startAttackBehavior(LivingEntity target) {
        double dist = (double) this.m_20270_(target);
        float f = this.m_20205_() + target.m_20205_();
        if (dist < (double) f + 1.0 && this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
            this.m_216990_(ACSoundRegistry.DEEP_ONE_ATTACK.get());
        }
        if (dist > (double) (f + 4.0F)) {
            this.m_21573_().moveTo(target, 1.3);
        }
        if (this.getAnimation() == ANIMATION_SCRATCH && (this.getAnimationTick() > 5 && this.getAnimationTick() < 9 || this.getAnimationTick() > 12 && this.getAnimationTick() < 16)) {
            this.checkAndDealMeleeDamage(target, 1.0F);
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() > 3 && this.getAnimationTick() <= 7) {
            this.checkAndDealMeleeDamage(target, 1.0F);
        }
    }

    @Override
    public Animation getTradingAnimation() {
        return ANIMATION_TRADE;
    }

    @Override
    public SoundEvent getAdmireSound() {
        return ACSoundRegistry.DEEP_ONE_ADMIRE.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.soundsAngry() ? ACSoundRegistry.DEEP_ONE_HOSTILE.get() : ACSoundRegistry.DEEP_ONE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.DEEP_ONE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.DEEP_ONE_DEATH.get();
    }

    @Override
    public boolean startDisappearBehavior(Player player) {
        this.m_21563_().setLookAt(player.m_20185_(), player.m_20188_(), player.m_20189_(), 20.0F, (float) this.m_8132_());
        if (this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(ACItemRegistry.INK_BOMB.get()));
        }
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_THROW);
        } else if (this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() > 10) {
            if (this.m_21120_(InteractionHand.MAIN_HAND).is(ACItemRegistry.INK_BOMB.get())) {
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            }
            return super.startDisappearBehavior(player);
        }
        return false;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_THROW, ANIMATION_BITE, ANIMATION_SCRATCH, ANIMATION_TRADE };
    }
}