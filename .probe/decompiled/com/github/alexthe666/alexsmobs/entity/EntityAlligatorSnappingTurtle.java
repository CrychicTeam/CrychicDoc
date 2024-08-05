package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.BottomFeederAIWander;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;

public class EntityAlligatorSnappingTurtle extends Animal implements ISemiAquatic, Shearable, IForgeShearable {

    public static final Predicate<LivingEntity> TARGET_PRED = animal -> !(animal instanceof EntityAlligatorSnappingTurtle) && !(animal instanceof ArmorStand) && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(animal) && animal.isAlive();

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> MOSS = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> WAITING = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ATTACK_TARGET_FLAG = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LUNGE_FLAG = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> TURTLE_SCALE = SynchedEntityData.defineId(EntityAlligatorSnappingTurtle.class, EntityDataSerializers.FLOAT);

    public float openMouthProgress;

    public float prevOpenMouthProgress;

    public float attackProgress;

    public float prevAttackProgress;

    public int chaseTime = 0;

    private int biteTick = 0;

    private int waitTime = 0;

    private int timeUntilWait = 0;

    private int mossTime = 0;

    protected EntityAlligatorSnappingTurtle(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.m_274367_(1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.ALLIGATOR_SNAPPING_TURTLE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ALLIGATOR_SNAPPING_TURTLE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ALLIGATOR_SNAPPING_TURTLE_HURT.get();
    }

    public static boolean canTurtleSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.ALLIGATOR_SNAPPING_TURTLE_SPAWNS);
        return spawnBlock && pos.m_123342_() < worldIn.m_5736_() + 4;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.alligatorSnappingTurtleSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 18.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.7).add(Attributes.ARMOR, 8.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.3F : 1.0F;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.3, false));
        this.f_21345_.addGoal(2, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(2, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(3, new BottomFeederAIWander(this, 1.0, 120, 150, 10));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this) {

            @Override
            public boolean canContinueToUse() {
                return EntityAlligatorSnappingTurtle.this.chaseTime >= 0 && super.m_8045_();
            }
        });
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, LivingEntity.class, 2, false, true, TARGET_PRED) {

            @Override
            protected AABB getTargetSearchArea(double targetDistance) {
                return this.f_26135_.m_20191_().inflate(0.5, 2.0, 0.5);
            }
        });
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.COD;
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CLIMBING, (byte) 0);
        this.f_19804_.define(MOSS, 0);
        this.f_19804_.define(TURTLE_SCALE, 1.0F);
        this.f_19804_.define(WAITING, false);
        this.f_19804_.define(ATTACK_TARGET_FLAG, false);
        this.f_19804_.define(LUNGE_FLAG, false);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOpenMouthProgress = this.openMouthProgress;
        this.prevAttackProgress = this.attackProgress;
        boolean attack = this.f_19804_.get(LUNGE_FLAG);
        boolean open = this.isWaiting() || this.f_19804_.get(ATTACK_TARGET_FLAG) && !attack;
        if (attack) {
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        if (open) {
            if (this.openMouthProgress < 5.0F) {
                this.openMouthProgress++;
            }
        } else if (this.openMouthProgress > 0.0F) {
            this.openMouthProgress--;
        }
        if (this.attackProgress == 4.0F && this.getTarget() != null && this.m_6084_() && this.m_142582_(this.getTarget()) && this.m_20270_(this.getTarget()) < 2.3F) {
            float dmg = this.m_6162_() ? 1.0F : (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
            this.getTarget().hurt(this.m_269291_().mobAttack(this), dmg);
        }
        if (this.attackProgress > 4.0F) {
            this.biteTick = 5;
        }
        if (this.biteTick > 0) {
            this.biteTick--;
        }
        if (this.chaseTime < 0) {
            this.chaseTime++;
        }
        if (!this.m_9236_().isClientSide) {
            this.setBesideClimbableBlock(this.f_19862_ && this.m_20069_());
            if (this.isWaiting()) {
                this.waitTime++;
                this.timeUntilWait = 1500;
                if (this.waitTime > 1500 || this.getTarget() != null) {
                    this.setWaiting(false);
                }
            } else {
                this.timeUntilWait--;
                this.waitTime = 0;
            }
            if ((this.getTarget() == null || !this.getTarget().isAlive()) && this.timeUntilWait <= 0 && this.m_20069_()) {
                this.setWaiting(true);
            }
            if (this.getTarget() != null && this.biteTick == 0) {
                this.setWaiting(false);
                this.chaseTime++;
                this.f_19804_.set(ATTACK_TARGET_FLAG, true);
                this.m_21391_(this.getTarget(), 360.0F, 40.0F);
                this.f_20883_ = this.m_146908_();
                if (this.openMouthProgress > 4.0F && this.m_142582_(this.getTarget()) && this.m_20270_(this.getTarget()) < 2.3F) {
                    this.f_19804_.set(LUNGE_FLAG, true);
                }
                if (this.chaseTime > 40 && this.m_20270_(this.getTarget()) > (float) (this.getTarget() instanceof Player ? 5 : 10)) {
                    this.chaseTime = -50;
                    this.setTarget(null);
                    this.setLastHurtByMob(null);
                    this.m_21335_(null);
                    this.f_20888_ = null;
                }
            } else {
                this.f_19804_.set(ATTACK_TARGET_FLAG, false);
                this.f_19804_.set(LUNGE_FLAG, false);
            }
            this.mossTime++;
            if (this.m_20069_() && this.mossTime > 12000) {
                this.mossTime = 0;
                this.setMoss(Math.min(10, this.getMoss() + 1));
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getTarget() {
        return this.chaseTime < 0 ? null : super.m_5448_();
    }

    @Override
    public void setTarget(@Nullable LivingEntity entitylivingbaseIn) {
        if (this.chaseTime >= 0) {
            super.m_6710_(entitylivingbaseIn);
        } else {
            super.m_6710_(null);
        }
    }

    @Nullable
    @Override
    public LivingEntity getLastHurtByMob() {
        return this.chaseTime < 0 ? null : super.m_21188_();
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity entitylivingbaseIn) {
        if (this.chaseTime >= 0) {
            super.m_6703_(entitylivingbaseIn);
        } else {
            super.m_6703_(null);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setMoss(this.f_19796_.nextInt(6));
        this.setTurtleScale(0.8F + this.f_19796_.nextFloat() * 0.2F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public float getTurtleScale() {
        return this.f_19804_.get(TURTLE_SCALE);
    }

    public void setTurtleScale(float scale) {
        this.f_19804_.set(TURTLE_SCALE, scale);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return this.f_26495_.getBlockState(pos).m_60819_().isEmpty();
            }
        };
    }

    public boolean isWaiting() {
        return this.f_19804_.get(WAITING);
    }

    public void setWaiting(boolean sit) {
        this.f_19804_.set(WAITING, sit);
    }

    public int getMoss() {
        return this.f_19804_.get(MOSS);
    }

    public void setMoss(int moss) {
        this.f_19804_.set(MOSS, moss);
    }

    protected void updateAir(int air) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Waiting", this.isWaiting());
        compound.putInt("MossLevel", this.getMoss());
        compound.putFloat("TurtleScale", this.getTurtleScale());
        compound.putInt("MossTime", this.mossTime);
        compound.putInt("WaitTime", this.waitTime);
        compound.putInt("WaitTime2", this.timeUntilWait);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setWaiting(compound.getBoolean("Waiting"));
        this.setMoss(compound.getInt("MossLevel"));
        this.setTurtleScale(compound.getFloat("TurtleScale"));
        this.mossTime = compound.getInt("MossTime");
        this.waitTime = compound.getInt("WaitTime");
        this.timeUntilWait = compound.getInt("WaitTime2");
    }

    @Override
    public boolean shouldEnterWater() {
        return true;
    }

    @Override
    public boolean shouldLeaveWater() {
        return false;
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isWaiting();
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_6425_(pos.below()).isEmpty() && worldIn.m_6425_(pos).is(FluidTags.WATER) ? 10.0F : super.getWalkTargetValue(pos, worldIn);
    }

    public boolean isBesideClimbableBlock() {
        return (this.f_19804_.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.f_19804_.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.f_19804_.set(CLIMBING, b0);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (this.f_20899_) {
                this.m_20256_(this.m_20184_().scale(1.0));
                this.m_20256_(this.m_20184_().add(0.0, 0.72, 0.0));
            } else {
                this.m_20256_(this.m_20184_().scale(0.4));
                this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.m_6084_() && this.getMoss() > 0;
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, Level world, BlockPos pos) {
        return this.readyForShearing();
    }

    @Override
    public void shear(SoundSource category) {
        this.m_9236_().playSound(null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        if (!this.m_9236_().isClientSide()) {
            if (this.f_19796_.nextFloat() < (float) this.getMoss() * 0.05F) {
                this.m_19998_(AMItemRegistry.SPIKED_SCUTE.get());
            } else {
                this.m_19998_(Items.SEAGRASS);
            }
            this.setMoss(0);
        }
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.m_146850_(GameEvent.ENTITY_INTERACT);
        if (!world.isClientSide()) {
            if (this.f_19796_.nextFloat() < (float) this.getMoss() * 0.05F) {
                this.setMoss(0);
                return Collections.singletonList(new ItemStack(AMItemRegistry.SPIKED_SCUTE.get()));
            } else {
                this.setMoss(0);
                return Collections.singletonList(new ItemStack(Items.SEAGRASS));
            }
        } else {
            return Collections.emptyList();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get().create(p_241840_1_);
    }
}