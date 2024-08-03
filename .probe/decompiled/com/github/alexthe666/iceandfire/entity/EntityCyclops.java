package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateCyclops;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EntityCyclops extends Monster implements IAnimatedEntity, IBlacklistedFromStatues, IVillagerFear, IHumanoid, IHasCustomizableAttributes {

    private static final EntityDataAccessor<Boolean> BLINDED = SynchedEntityData.defineId(EntityCyclops.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityCyclops.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_STOMP;

    public static Animation ANIMATION_EATPLAYER;

    public static Animation ANIMATION_KICK;

    public static Animation ANIMATION_ROAR;

    public EntityCyclopsEye eyeEntity;

    private int animationTick;

    private Animation currentAnimation;

    public EntityCyclops(EntityType<EntityCyclops> type, Level worldIn) {
        super(type, worldIn);
        this.m_274367_(2.5F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, 0.0F);
        ANIMATION_STOMP = Animation.create(27);
        ANIMATION_EATPLAYER = Animation.create(40);
        ANIMATION_KICK = Animation.create(20);
        ANIMATION_ROAR = Animation.create(30);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.cyclopsMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, IafConfig.cyclopsAttackStrength).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 20.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.cyclopsMaxHealth);
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level worldIn) {
        return new PathNavigateCyclops(this, this.m_9236_());
    }

    @Override
    public int getExperienceReward() {
        return 40;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new RestrictSunGoal(this));
        this.f_21345_.addGoal(3, new FleeSunGoal(this, 1.0));
        this.f_21345_.addGoal(3, new CyclopsAIAttackMelee(this, 1.0, false));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity entity) {
                if (EntityGorgon.isStoneMob(entity)) {
                    return false;
                } else if (!DragonUtils.isAlive(entity)) {
                    return false;
                } else if (entity instanceof WaterAnimal) {
                    return false;
                } else {
                    if (entity instanceof Player playerEntity && (playerEntity.isCreative() || playerEntity.isSpectator())) {
                        return false;
                    }
                    if (entity instanceof EntityCyclops) {
                        return false;
                    } else {
                        return entity instanceof Animal && !(entity instanceof Wolf) && !(entity instanceof PolarBear) && !(entity instanceof EntityDragonBase) ? false : !ServerEvents.isSheep(entity);
                    }
                }
            }
        }));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, true, new Predicate<Player>() {

            public boolean apply(@Nullable Player entity) {
                return entity != null && !entity.isCreative() && !entity.isSpectator();
            }
        }));
        this.f_21346_.addGoal(3, new CyclopsAITargetSheepPlayers(this, Player.class, true));
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (!ServerEvents.isSheep(entityIn)) {
            entityIn.push(this);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        int attackDescision = this.m_217043_().nextInt(3);
        if (attackDescision == 0) {
            this.setAnimation(ANIMATION_STOMP);
            return true;
        } else if (attackDescision != 1) {
            this.setAnimation(ANIMATION_KICK);
            return true;
        } else {
            if (!entityIn.hasPassenger(this) && entityIn.getBbWidth() < 1.95F && !(entityIn instanceof EntityDragonBase) && !entityIn.getType().is(ForgeRegistries.ENTITY_TYPES.tags().createTagKey(IafTagRegistry.CYCLOPS_UNLIFTABLES))) {
                this.setAnimation(ANIMATION_EATPLAYER);
                entityIn.stopRiding();
                entityIn.startRiding(this, true);
            } else {
                this.setAnimation(ANIMATION_STOMP);
            }
            return true;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(BLINDED, Boolean.FALSE);
        this.f_19804_.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("Blind", this.isBlinded());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.m_7378_(compound);
        this.setBlinded(compound.getBoolean("Blind"));
        this.setVariant(compound.getInt("Variant"));
        this.setConfigurableAttributes();
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public boolean isBlinded() {
        return this.f_19804_.get(BLINDED);
    }

    public void setBlinded(boolean blind) {
        this.f_19804_.set(BLINDED, blind);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            passenger.setDeltaMovement(0.0, passenger.getDeltaMovement().y, 0.0);
            this.setAnimation(ANIMATION_EATPLAYER);
            double raiseUp = this.getAnimationTick() < 10 ? 0.0 : Math.min((double) (this.getAnimationTick() * 3 - 30) * 0.2, 5.2F);
            float pullIn = this.getAnimationTick() < 15 ? 0.0F : Math.min((float) (this.getAnimationTick() - 15) * 0.15F, 0.75F);
            this.f_20883_ = this.m_146908_();
            this.m_146922_(0.0F);
            float radius = -2.75F + pullIn;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_ + 3.15F;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + raiseUp, this.m_20189_() + extraZ);
            if (this.getAnimationTick() == 32) {
                passenger.hurt(this.m_9236_().damageSources().mobAttack(this), (float) IafConfig.cyclopsBiteStrength);
                passenger.stopRiding();
            }
        }
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.getAnimation() == ANIMATION_EATPLAYER) {
            super.m_7023_(vec.multiply(0.0, 0.0, 0.0));
        } else {
            super.m_7023_(vec);
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.eyeEntity == null) {
            this.eyeEntity = new EntityCyclopsEye(this, 0.2F, 0.0F, 7.4F, 1.2F, 0.6F, 1.0F);
            this.eyeEntity.m_20359_(this);
        }
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        if (this.isBlinded() && this.m_5448_() != null && this.m_20280_(this.m_5448_()) > 6.0) {
            this.m_6710_(null);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.m_5496_(IafSoundRegistry.CYCLOPS_BLINDED, 1.0F, 1.0F);
        }
        if (this.getAnimation() == ANIMATION_EATPLAYER && this.getAnimationTick() == 25) {
            this.m_5496_(IafSoundRegistry.CYCLOPS_BITE, 1.0F, 1.0F);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.m_5448_() != null && this.m_20280_(this.m_5448_()) < 12.0 && this.getAnimationTick() == 14) {
            this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_KICK && this.m_5448_() != null && this.m_20280_(this.m_5448_()) < 14.0 && this.getAnimationTick() == 12) {
            this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            if (this.m_5448_() != null) {
                this.m_5448_().knockback(2.0, this.m_20185_() - this.m_5448_().m_20185_(), this.m_20189_() - this.m_5448_().m_20189_());
            }
        }
        if (this.getAnimation() != ANIMATION_EATPLAYER && this.m_5448_() != null && !this.m_20197_().isEmpty() && this.m_20197_().contains(this.m_5448_())) {
            this.setAnimation(ANIMATION_EATPLAYER);
        }
        if (this.getAnimation() == NO_ANIMATION && this.m_5448_() != null && this.m_217043_().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 14) {
            for (int i1 = 0; i1 < 20; i1++) {
                double motionX = this.m_217043_().nextGaussian() * 0.07;
                double motionY = this.m_217043_().nextGaussian() * 0.07;
                double motionZ = this.m_217043_().nextGaussian() * 0.07;
                float radius = -1.5F;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1 * 1.0F;
                double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.8F;
                double extraZ = (double) (radius * Mth.cos(angle));
                BlockState BlockState = this.m_9236_().getBlockState(BlockPos.containing(this.m_20185_() + extraX, this.m_20186_() + extraY - 1.0, this.m_20189_() + extraZ));
                if (BlockState.m_60795_() && this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockState), this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.eyeEntity == null) {
            this.eyeEntity = new EntityCyclopsEye(this, 0.2F, 0.0F, 7.4F, 1.2F, 0.5F, 1.0F);
            this.eyeEntity.m_20359_(this);
        }
        EntityUtil.updatePart(this.eyeEntity, this);
        this.breakBlock();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.m_217043_().nextInt(4));
        return spawnDataIn;
    }

    public void breakBlock() {
        if (IafConfig.cyclopsGriefing) {
            for (int a = (int) Math.round(this.m_20191_().minX) - 1; a <= (int) Math.round(this.m_20191_().maxX) + 1; a++) {
                for (int b = (int) Math.round(this.m_20191_().minY) + 1; b <= (int) Math.round(this.m_20191_().maxY) + 2 && b <= 127; b++) {
                    for (int c = (int) Math.round(this.m_20191_().minZ) - 1; c <= (int) Math.round(this.m_20191_().maxZ) + 1; c++) {
                        BlockPos pos = new BlockPos(a, b, c);
                        BlockState state = this.m_9236_().getBlockState(pos);
                        Block block = state.m_60734_();
                        if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && !(block instanceof BushBlock) && block != Blocks.BEDROCK && (state.m_60734_() instanceof LeavesBlock || state.m_204336_(BlockTags.LOGS))) {
                            this.m_20184_().scale(0.6);
                            if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, (double) a, (double) b, (double) c)) && block != Blocks.AIR && !this.m_9236_().isClientSide) {
                                this.m_9236_().m_46961_(pos, true);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        if (this.eyeEntity != null) {
            this.eyeEntity.m_142687_(reason);
        }
        super.m_142687_(reason);
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { NO_ANIMATION, ANIMATION_STOMP, ANIMATION_EATPLAYER, ANIMATION_KICK, ANIMATION_ROAR };
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 40 && !this.isBlinded();
    }

    public void onHitEye(DamageSource source, float damage) {
        if (!this.isBlinded()) {
            this.setBlinded(true);
            this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue(6.0);
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);
            this.setAnimation(ANIMATION_ROAR);
            this.m_6469_(source, damage * 3.0F);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.CYCLOPS_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.CYCLOPS_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.CYCLOPS_DIE;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return !this.isBlinded();
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}