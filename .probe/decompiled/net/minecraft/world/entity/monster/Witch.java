package net.minecraft.world.entity.monster;

import java.util.List;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestHealableRaiderTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Witch extends Raider implements RangedAttackMob {

    private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");

    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25, AttributeModifier.Operation.ADDITION);

    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);

    private int usingTime;

    private NearestHealableRaiderTargetGoal<Raider> healRaidersGoal;

    private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;

    public Witch(EntityType<? extends Witch> entityTypeExtendsWitch0, Level level1) {
        super(entityTypeExtendsWitch0, level1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.healRaidersGoal = new NearestHealableRaiderTargetGoal<>(this, Raider.class, true, p_289462_ -> p_289462_ != null && this.m_37886_() && p_289462_.m_6095_() != EntityType.WITCH);
        this.attackPlayersGoal = new NearestAttackableWitchTargetGoal<>(this, Player.class, 10, true, false, null);
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new RangedAttackGoal(this, 1.0, 60, 10.0F));
        this.f_21345_.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, Raider.class));
        this.f_21346_.addGoal(2, this.healRaidersGoal);
        this.f_21346_.addGoal(3, this.attackPlayersGoal);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(DATA_USING_ITEM, false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.WITCH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    public void setUsingItem(boolean boolean0) {
        this.m_20088_().set(DATA_USING_ITEM, boolean0);
    }

    public boolean isDrinkingPotion() {
        return this.m_20088_().get(DATA_USING_ITEM);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 26.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public void aiStep() {
        if (!this.m_9236_().isClientSide && this.m_6084_()) {
            this.healRaidersGoal.decrementCooldown();
            if (this.healRaidersGoal.getCooldown() <= 0) {
                this.attackPlayersGoal.setCanAttack(true);
            } else {
                this.attackPlayersGoal.setCanAttack(false);
            }
            if (this.isDrinkingPotion()) {
                if (this.usingTime-- <= 0) {
                    this.setUsingItem(false);
                    ItemStack $$0 = this.m_21205_();
                    this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    if ($$0.is(Items.POTION)) {
                        List<MobEffectInstance> $$1 = PotionUtils.getMobEffects($$0);
                        if ($$1 != null) {
                            for (MobEffectInstance $$2 : $$1) {
                                this.m_7292_(new MobEffectInstance($$2));
                            }
                        }
                    }
                    this.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
                }
            } else {
                Potion $$3 = null;
                if (this.f_19796_.nextFloat() < 0.15F && this.m_204029_(FluidTags.WATER) && !this.m_21023_(MobEffects.WATER_BREATHING)) {
                    $$3 = Potions.WATER_BREATHING;
                } else if (this.f_19796_.nextFloat() < 0.15F && (this.m_6060_() || this.m_21225_() != null && this.m_21225_().is(DamageTypeTags.IS_FIRE)) && !this.m_21023_(MobEffects.FIRE_RESISTANCE)) {
                    $$3 = Potions.FIRE_RESISTANCE;
                } else if (this.f_19796_.nextFloat() < 0.05F && this.m_21223_() < this.m_21233_()) {
                    $$3 = Potions.HEALING;
                } else if (this.f_19796_.nextFloat() < 0.5F && this.m_5448_() != null && !this.m_21023_(MobEffects.MOVEMENT_SPEED) && this.m_5448_().m_20280_(this) > 121.0) {
                    $$3 = Potions.SWIFTNESS;
                }
                if ($$3 != null) {
                    this.m_8061_(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), $$3));
                    this.usingTime = this.m_21205_().getUseDuration();
                    this.setUsingItem(true);
                    if (!this.m_20067_()) {
                        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_DRINK, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
                    }
                    AttributeInstance $$4 = this.m_21051_(Attributes.MOVEMENT_SPEED);
                    $$4.removeModifier(SPEED_MODIFIER_DRINKING);
                    $$4.addTransientModifier(SPEED_MODIFIER_DRINKING);
                }
            }
            if (this.f_19796_.nextFloat() < 7.5E-4F) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 15);
            }
        }
        super.aiStep();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.WITCH_CELEBRATE;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 15) {
            for (int $$1 = 0; $$1 < this.f_19796_.nextInt(35) + 10; $$1++) {
                this.m_9236_().addParticle(ParticleTypes.WITCH, this.m_20185_() + this.f_19796_.nextGaussian() * 0.13F, this.m_20191_().maxY + 0.5 + this.f_19796_.nextGaussian() * 0.13F, this.m_20189_() + this.f_19796_.nextGaussian() * 0.13F, 0.0, 0.0, 0.0);
            }
        } else {
            super.m_7822_(byte0);
        }
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource damageSource0, float float1) {
        float1 = super.m_6515_(damageSource0, float1);
        if (damageSource0.getEntity() == this) {
            float1 = 0.0F;
        }
        if (damageSource0.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            float1 *= 0.15F;
        }
        return float1;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        if (!this.isDrinkingPotion()) {
            Vec3 $$2 = livingEntity0.m_20184_();
            double $$3 = livingEntity0.m_20185_() + $$2.x - this.m_20185_();
            double $$4 = livingEntity0.m_20188_() - 1.1F - this.m_20186_();
            double $$5 = livingEntity0.m_20189_() + $$2.z - this.m_20189_();
            double $$6 = Math.sqrt($$3 * $$3 + $$5 * $$5);
            Potion $$7 = Potions.HARMING;
            if (livingEntity0 instanceof Raider) {
                if (livingEntity0.getHealth() <= 4.0F) {
                    $$7 = Potions.HEALING;
                } else {
                    $$7 = Potions.REGENERATION;
                }
                this.m_6710_(null);
            } else if ($$6 >= 8.0 && !livingEntity0.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                $$7 = Potions.SLOWNESS;
            } else if (livingEntity0.getHealth() >= 8.0F && !livingEntity0.hasEffect(MobEffects.POISON)) {
                $$7 = Potions.POISON;
            } else if ($$6 <= 3.0 && !livingEntity0.hasEffect(MobEffects.WEAKNESS) && this.f_19796_.nextFloat() < 0.25F) {
                $$7 = Potions.WEAKNESS;
            }
            ThrownPotion $$8 = new ThrownPotion(this.m_9236_(), this);
            $$8.m_37446_(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), $$7));
            $$8.m_146926_($$8.m_146909_() - -20.0F);
            $$8.m_6686_($$3, $$4 + $$6 * 0.2, $$5, 0.75F, 8.0F);
            if (!this.m_20067_()) {
                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_THROW, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
            }
            this.m_9236_().m_7967_($$8);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 1.62F;
    }

    @Override
    public void applyRaidBuffs(int int0, boolean boolean1) {
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }
}