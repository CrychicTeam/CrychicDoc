package net.minecraft.world.entity.monster;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractSkeleton extends Monster implements RangedAttackMob {

    private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);

    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {

        @Override
        public void stop() {
            super.stop();
            AbstractSkeleton.this.m_21561_(false);
        }

        @Override
        public void start() {
            super.start();
            AbstractSkeleton.this.m_21561_(true);
        }
    };

    protected AbstractSkeleton(EntityType<? extends AbstractSkeleton> entityTypeExtendsAbstractSkeleton0, Level level1) {
        super(entityTypeExtendsAbstractSkeleton0, level1);
        this.reassessWeaponGoal();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(2, new RestrictSunGoal(this));
        this.f_21345_.addGoal(3, new FleeSunGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Wolf.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(this.getStepSound(), 0.15F, 1.0F);
    }

    abstract SoundEvent getStepSound();

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void aiStep() {
        boolean $$0 = this.m_21527_();
        if ($$0) {
            ItemStack $$1 = this.m_6844_(EquipmentSlot.HEAD);
            if (!$$1.isEmpty()) {
                if ($$1.isDamageableItem()) {
                    $$1.setDamageValue($$1.getDamageValue() + this.f_19796_.nextInt(2));
                    if ($$1.getDamageValue() >= $$1.getMaxDamage()) {
                        this.m_21166_(EquipmentSlot.HEAD);
                        this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                    }
                }
                $$0 = false;
            }
            if ($$0) {
                this.m_20254_(8);
            }
        }
        super.aiStep();
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        if (this.m_275832_() instanceof PathfinderMob $$0) {
            this.f_20883_ = $$0.f_20883_;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource0, DifficultyInstance difficultyInstance1) {
        super.m_213945_(randomSource0, difficultyInstance1);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        spawnGroupData3 = super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        this.populateDefaultEquipmentSlots($$5, difficultyInstance1);
        this.m_213946_($$5, difficultyInstance1);
        this.reassessWeaponGoal();
        this.m_21553_($$5.nextFloat() < 0.55F * difficultyInstance1.getSpecialMultiplier());
        if (this.m_6844_(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate $$6 = LocalDate.now();
            int $$7 = $$6.get(ChronoField.DAY_OF_MONTH);
            int $$8 = $$6.get(ChronoField.MONTH_OF_YEAR);
            if ($$8 == 10 && $$7 == 31 && $$5.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack($$5.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.f_21348_[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }
        return spawnGroupData3;
    }

    public void reassessWeaponGoal() {
        if (this.m_9236_() != null && !this.m_9236_().isClientSide) {
            this.f_21345_.removeGoal(this.meleeGoal);
            this.f_21345_.removeGoal(this.bowGoal);
            ItemStack $$0 = this.m_21120_(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
            if ($$0.is(Items.BOW)) {
                int $$1 = 20;
                if (this.m_9236_().m_46791_() != Difficulty.HARD) {
                    $$1 = 40;
                }
                this.bowGoal.setMinAttackInterval($$1);
                this.f_21345_.addGoal(4, this.bowGoal);
            } else {
                this.f_21345_.addGoal(4, this.meleeGoal);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity0, float float1) {
        ItemStack $$2 = this.m_6298_(this.m_21120_(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow $$3 = this.getArrow($$2, float1);
        double $$4 = livingEntity0.m_20185_() - this.m_20185_();
        double $$5 = livingEntity0.m_20227_(0.3333333333333333) - $$3.m_20186_();
        double $$6 = livingEntity0.m_20189_() - this.m_20189_();
        double $$7 = Math.sqrt($$4 * $$4 + $$6 * $$6);
        $$3.shoot($$4, $$5 + $$7 * 0.2F, $$6, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
        this.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_($$3);
    }

    protected AbstractArrow getArrow(ItemStack itemStack0, float float1) {
        return ProjectileUtil.getMobArrow(this, itemStack0, float1);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem0) {
        return projectileWeaponItem0 == Items.BOW;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        this.reassessWeaponGoal();
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
        super.m_8061_(equipmentSlot0, itemStack1);
        if (!this.m_9236_().isClientSide) {
            this.reassessWeaponGoal();
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.6;
    }

    public boolean isShaking() {
        return this.m_146890_();
    }
}