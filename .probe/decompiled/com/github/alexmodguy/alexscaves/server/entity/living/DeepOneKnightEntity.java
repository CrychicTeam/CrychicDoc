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
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class DeepOneKnightEntity extends DeepOneBaseEntity {

    public static final Animation ANIMATION_THROW = Animation.create(20);

    public static final Animation ANIMATION_BITE = Animation.create(8);

    public static final Animation ANIMATION_SCRATCH = Animation.create(22);

    public static final Animation ANIMATION_TRADE = Animation.create(55);

    private UUID lastThrownTrident = null;

    private boolean melee = this.f_19796_.nextBoolean();

    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(1.2F, 1.3F, false);

    public static final ResourceLocation BARTER_LOOT = new ResourceLocation("alexscaves", "gameplay/deep_one_knight_barter");

    public DeepOneKnightEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new DeepOneAttackGoal(this));
        this.f_21345_.addGoal(1, new DeepOneBarterGoal(this));
        this.f_21345_.addGoal(2, new DeepOneReactToPlayerGoal(this) {

            @Override
            public boolean canUse() {
                return super.canUse() && DeepOneKnightEntity.this.lastThrownTrident == null;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && DeepOneKnightEntity.this.lastThrownTrident == null;
            }
        });
        this.f_21345_.addGoal(3, new DeepOneDisappearGoal(this));
        this.f_21345_.addGoal(4, new DeepOneWanderGoal(this, 12, 1.0));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new DeepOneBaseEntity.HurtByHostileTargetGoal());
        this.f_21346_.addGoal(2, new DeepOneTargetHostilePlayersGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    public EntityDimensions getSwimmingSize() {
        return SWIMMING_SIZE;
    }

    public boolean isNoon() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && (s.toLowerCase().contains("noon") || s.toLowerCase().contains("stinkyfish"));
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.m_5448_();
        if ((target == null || !target.isAlive()) && !this.m_9236_().isClientSide && this.lastThrownTrident != null) {
            this.pickUpTrident();
        }
    }

    @Override
    protected ResourceLocation getBarterLootTable() {
        return BARTER_LOOT;
    }

    @Override
    public boolean startDisappearBehavior(Player player) {
        this.m_21563_().setLookAt(player.m_20185_(), player.m_20188_(), player.m_20189_(), 20.0F, (float) this.m_8132_());
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.swapItemsForAnimation(new ItemStack(ACItemRegistry.INK_BOMB.get()));
        }
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_THROW);
        } else if (this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() > 10) {
            if (this.m_21120_(InteractionHand.MAIN_HAND).is(ACItemRegistry.INK_BOMB.get())) {
                this.restoreSwappedItem();
            }
            return super.startDisappearBehavior(player);
        }
        return false;
    }

    protected boolean pickUpTrident() {
        if (this.lastThrownTrident != null && !this.m_9236_().isClientSide && ((ServerLevel) this.m_9236_()).getEntity(this.lastThrownTrident) instanceof ThrownTrident trident) {
            if ((double) this.m_20270_(trident) < 2.0) {
                if (this.getAnimation() == NO_ANIMATION) {
                    this.setAnimation(ANIMATION_SCRATCH);
                }
                this.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.TRIDENT));
                trident.m_142687_(Entity.RemovalReason.DISCARDED);
                this.m_21573_().stop();
                this.lastThrownTrident = null;
            } else {
                this.m_21573_().moveTo(trident, 1.5);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void startAttackBehavior(LivingEntity target) {
        double distance = (double) this.m_20270_(target);
        float f = this.m_20205_() + target.m_20205_();
        boolean meleeOnly = this.m_21120_(InteractionHand.MAIN_HAND).is(ACItemRegistry.ORTHOLANCE.get());
        if ((distance > 15.0 || !this.melee) && !meleeOnly) {
            this.melee = false;
            if (!this.pickUpTrident() && this.m_21120_(InteractionHand.MAIN_HAND).is(Items.TRIDENT)) {
                if (this.m_142582_(target) && distance < 35.0) {
                    this.m_21573_().stop();
                    if (this.getAnimation() == NO_ANIMATION) {
                        this.setAnimation(ANIMATION_THROW);
                    } else if (this.getAnimation() == ANIMATION_THROW && this.getAnimationTick() > 8) {
                        this.melee = true;
                        this.throwTrident(target);
                        this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    }
                } else {
                    this.m_21573_().moveTo(target, 1.2);
                }
            }
        }
        if (this.melee || meleeOnly) {
            if (distance < (double) (f + 1.0F)) {
                if (this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_SCRATCH : ANIMATION_BITE);
                    this.m_216990_(ACSoundRegistry.DEEP_ONE_KNIGHT_ATTACK.get());
                }
            } else {
                this.m_21573_().moveTo(target, 1.2);
            }
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

    public void throwTrident(LivingEntity target) {
        ThrownTrident throwntrident = new ThrownTrident(this.m_9236_(), this, new ItemStack(Items.TRIDENT));
        double d0 = target.m_20185_() - this.m_20185_();
        double d1 = target.m_20227_(0.3333333333333333) - throwntrident.m_20186_();
        double d2 = target.m_20189_() - this.m_20189_();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        throwntrident.m_6686_(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
        this.m_5496_(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_(throwntrident);
        this.lastThrownTrident = throwntrident.m_20148_();
    }

    @Override
    protected void checkAndDealMeleeDamage(LivingEntity target, float multiplier) {
        super.checkAndDealMeleeDamage(target, multiplier);
        this.melee = this.f_19796_.nextBoolean();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.lastThrownTrident != null) {
            compound.putUUID("LastTridentUUID", this.lastThrownTrident);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("LastTridentUUID")) {
            this.lastThrownTrident = compound.getUUID("LastTridentUUID");
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        spawnGroupData = super.m_6518_(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.m_8061_(EquipmentSlot.MAINHAND, (double) this.f_19796_.nextFloat() > 0.7 ? new ItemStack(ACItemRegistry.ORTHOLANCE.get()) : new ItemStack(Items.TRIDENT));
        return spawnGroupData;
    }

    @Override
    public SoundEvent getAdmireSound() {
        return ACSoundRegistry.DEEP_ONE_KNIGHT_ADMIRE.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.soundsAngry() ? ACSoundRegistry.DEEP_ONE_KNIGHT_HOSTILE.get() : ACSoundRegistry.DEEP_ONE_KNIGHT_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.DEEP_ONE_KNIGHT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.DEEP_ONE_KNIGHT_DEATH.get();
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_THROW, ANIMATION_BITE, ANIMATION_SCRATCH, ANIMATION_TRADE };
    }
}