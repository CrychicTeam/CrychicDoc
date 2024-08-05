package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.FroststalkerAIFollowLeader;
import com.github.alexthe666.alexsmobs.entity.ai.FroststalkerAIMelee;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

public class EntityFroststalker extends Animal implements IAnimatedEntity, ISemiAquatic {

    public static final ResourceLocation SPIKED_LOOT = new ResourceLocation("alexsmobs", "entities/froststalker_spikes");

    public static final Animation ANIMATION_BITE = Animation.create(13);

    public static final Animation ANIMATION_SPEAK = Animation.create(11);

    public static final Animation ANIMATION_SLASH_L = Animation.create(12);

    public static final Animation ANIMATION_SLASH_R = Animation.create(12);

    public static final Animation ANIMATION_SHOVE = Animation.create(12);

    private static final EntityDataAccessor<Boolean> SPIKES = SynchedEntityData.defineId(EntityFroststalker.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TACKLING = SynchedEntityData.defineId(EntityFroststalker.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SPIKE_SHAKING = SynchedEntityData.defineId(EntityFroststalker.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BIPEDAL = SynchedEntityData.defineId(EntityFroststalker.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> TURN_ANGLE = SynchedEntityData.defineId(EntityFroststalker.class, EntityDataSerializers.FLOAT);

    public static final Predicate<Player> VALID_LEADER_PLAYERS = player -> player.getItemBySlot(EquipmentSlot.HEAD).is(AMItemRegistry.FROSTSTALKER_HELMET.get());

    public float bipedProgress;

    public float prevBipedProgress;

    public float tackleProgress;

    public float prevTackleProgress;

    public float spikeShakeProgress;

    public float prevSpikeShakeProgress;

    public float prevTurnAngle;

    private int animationTick;

    private Animation currentAnimation;

    private int standingTime = 400 - this.f_19796_.nextInt(700);

    private int currentSpeedMode = -1;

    private LivingEntity leader;

    private int packSize = 1;

    private int shakeTime = 0;

    private boolean hasSpikedArmor = false;

    private int fleeFireFlag;

    private int resetLeaderCooldown = 100;

    protected EntityFroststalker(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.LAVA, -1.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.FROSTSTALKER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.FROSTSTALKER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.FROSTSTALKER_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.froststalkerSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canFroststalkerSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8 && (worldIn.m_8055_(pos.below()).m_60713_(Blocks.ICE) || worldIn.m_8055_(pos.below()).m_280296_() || worldIn.m_8055_(pos.below()).m_60713_(Blocks.SNOW_BLOCK));
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.hasSpikes() ? SPIKED_LOOT : super.m_7582_();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ARMOR, 2.0).add(Attributes.ATTACK_DAMAGE, 4.5).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount *= 2.0F;
        }
        boolean prev = super.hurt(source, amount);
        if (prev && this.hasSpikes() && !this.isSpikeShaking() && source.getEntity() != null && source.getEntity().distanceTo(this) < 10.0F) {
            this.setSpikeShaking(true);
            this.shakeTime = 20 + this.f_19796_.nextInt(60);
            this.standFor(this.shakeTime + 10);
        }
        return prev;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this) {

            @Override
            public void tick() {
                if (EntityFroststalker.this.m_217043_().nextFloat() < 0.8F) {
                    if (EntityFroststalker.this.hasSpikes()) {
                        EntityFroststalker.this.jumpUnderwater();
                    } else {
                        EntityFroststalker.this.m_21569_().jump();
                    }
                }
            }
        });
        this.f_21345_.addGoal(1, new EntityFroststalker.AIAvoidFire());
        this.f_21345_.addGoal(2, new FroststalkerAIMelee(this));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(5, new FroststalkerAIFollowLeader(this));
        this.f_21345_.addGoal(6, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(7, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(8, new AnimalAIWanderRanged(this, 90, 1.0, 7, 7));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 15.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, EntityFroststalker.class).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, LivingEntity.class, 40, false, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.FROSTSTALKER_TARGETS)));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 80, false, true, livingEntity -> !livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(AMItemRegistry.FROSTSTALKER_HELMET.get())));
    }

    private void jumpUnderwater() {
        BlockPos pos = this.m_20097_();
        if (this.m_9236_().m_46801_(pos) && !this.m_9236_().m_46801_(pos.above())) {
            this.m_6034_(this.m_20185_(), this.m_20186_() + 1.0, this.m_20189_());
            this.m_9236_().setBlockAndUpdate(pos, Blocks.FROSTED_ICE.defaultBlockState());
            this.m_9236_().m_186460_(pos, Blocks.FROSTED_ICE, Mth.nextInt(this.m_217043_(), 60, 120));
        }
        double d0 = 0.2F;
        Vec3 vec3 = this.m_20184_();
        this.m_20334_(vec3.x, d0, vec3.z);
    }

    @Override
    public void setInLove(@Nullable Player player) {
        if (player != null && this.isValidLeader(player)) {
            super.setInLove(player);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(TURN_ANGLE, 0.0F);
        this.f_19804_.define(SPIKES, true);
        this.f_19804_.define(BIPEDAL, false);
        this.f_19804_.define(SPIKE_SHAKING, false);
        this.f_19804_.define(TACKLING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Spiked", this.hasSpikes());
        compound.putBoolean("Bipedal", this.isBipedal());
        compound.putBoolean("SpikeShaking", this.isSpikeShaking());
        compound.putInt("StandingTime", this.standingTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSpiked(compound.getBoolean("Spiked"));
        this.setBipedal(compound.getBoolean("Bipedal"));
        this.setSpikeShaking(compound.getBoolean("SpikeShaking"));
        this.standingTime = compound.getInt("StandingTime");
    }

    @Override
    public BlockPos getRestrictCenter() {
        return this.leader == null ? super.m_21534_() : this.leader.m_20097_();
    }

    @Override
    public boolean hasRestriction() {
        return this.isFollower();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.PORKCHOP) || stack.is(Items.COOKED_PORKCHOP);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevBipedProgress = this.bipedProgress;
        this.prevTackleProgress = this.tackleProgress;
        this.prevSpikeShakeProgress = this.spikeShakeProgress;
        this.prevTurnAngle = this.getTurnAngle();
        if (this.isBipedal()) {
            if (this.bipedProgress < 5.0F) {
                this.bipedProgress++;
            }
        } else if (this.bipedProgress > 0.0F) {
            this.bipedProgress--;
        }
        if (this.isTackling()) {
            if (this.tackleProgress < 5.0F) {
                this.tackleProgress++;
            }
        } else if (this.tackleProgress > 0.0F) {
            this.tackleProgress--;
        }
        if (this.isSpikeShaking()) {
            if (this.spikeShakeProgress < 5.0F) {
                this.spikeShakeProgress++;
            }
            if (this.currentSpeedMode != 2) {
                this.currentSpeedMode = 2;
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.1F);
            }
        } else {
            if (this.spikeShakeProgress > 0.0F) {
                this.spikeShakeProgress--;
            }
            if (this.isBipedal()) {
                if (this.currentSpeedMode != 0) {
                    this.currentSpeedMode = 0;
                    this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35F);
                }
            } else if (this.currentSpeedMode != 1) {
                this.currentSpeedMode = 1;
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
            }
        }
        if (this.hasSpikes()) {
            if (!this.hasSpikedArmor) {
                this.hasSpikedArmor = true;
                this.m_21051_(Attributes.ARMOR).setBaseValue(12.0);
            }
        } else if (this.hasSpikedArmor) {
            this.hasSpikedArmor = false;
            this.m_21051_(Attributes.ARMOR).setBaseValue(0.0);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ % 200 == 0) {
                if (this.m_20071_() && !this.hasSpikes()) {
                    this.setSpiked(true);
                }
                if (this.isHotBiome() && !this.m_20071_()) {
                    this.m_7292_(new MobEffectInstance(MobEffects.WEAKNESS, 400));
                    if (this.f_19796_.nextInt(2) == 0 && !this.m_20071_()) {
                        this.setSpiked(false);
                    }
                }
            }
            float threshold = 1.0F;
            boolean flag = false;
            if (this.isBipedal() && this.f_19859_ - this.m_146908_() > threshold) {
                this.setTurnAngle(this.getTurnAngle() + 5.0F);
                flag = true;
            }
            if (this.isBipedal() && this.f_19859_ - this.m_146908_() < -threshold) {
                this.setTurnAngle(this.getTurnAngle() - 5.0F);
                flag = true;
            }
            if (!flag) {
                if (this.getTurnAngle() > 0.0F) {
                    this.setTurnAngle(Math.max(this.getTurnAngle() - 10.0F, 0.0F));
                }
                if (this.getTurnAngle() < 0.0F) {
                    this.setTurnAngle(Math.min(this.getTurnAngle() + 10.0F, 0.0F));
                }
            }
            this.setTurnAngle(Mth.clamp(this.getTurnAngle(), -60.0F, 60.0F));
            if (this.standingTime > 0) {
                this.standingTime--;
            }
            if (this.standingTime < 0) {
                this.standingTime++;
            }
            if (this.standingTime <= 0 && this.isBipedal()) {
                this.standingTime = -200 - this.f_19796_.nextInt(400);
                this.setBipedal(false);
            }
            if (this.standingTime == 0 && !this.isBipedal() && this.m_20184_().lengthSqr() >= 0.03) {
                this.standingTime = 200 + this.f_19796_.nextInt(600);
                this.setBipedal(true);
            }
            if (this.shakeTime > 0) {
                if (this.shakeTime % 5 == 0) {
                    int spikeCount = 2 + this.f_19796_.nextInt(4);
                    for (int i = 0; i < spikeCount; i++) {
                        float f = (float) (i + 1) / (float) spikeCount * 360.0F;
                        EntityIceShard shard = new EntityIceShard(this.m_9236_(), this);
                        shard.shootFromRotation(this, this.m_146909_() - (float) this.f_19796_.nextInt(40), f, 0.0F, 0.15F + this.f_19796_.nextFloat() * 0.2F, 1.0F);
                        this.m_9236_().m_7967_(shard);
                    }
                }
                this.shakeTime--;
            }
            if (this.shakeTime == 0 && this.isSpikeShaking()) {
                this.setSpikeShaking(false);
                if (this.f_19796_.nextInt(2) == 0) {
                    this.setSpiked(false);
                }
            }
            if (this.m_5448_() != null && this.isValidLeader(this.m_5448_())) {
                this.m_6710_(null);
            }
            if (this.m_5448_() != null && !this.isValidLeader(this.m_5448_()) && this.m_5448_().isAlive() && (this.m_21188_() == null || !this.m_21188_().isAlive())) {
                this.m_6703_(this.m_5448_());
            }
            LivingEntity playerTarget = null;
            if (this.leader instanceof Player) {
                playerTarget = this.leader.getLastHurtMob();
                if (playerTarget == null || !playerTarget.isAlive() || playerTarget instanceof EntityFroststalker) {
                    playerTarget = this.leader.getLastHurtByMob();
                }
            }
            if (playerTarget != null && playerTarget.isAlive() && !(playerTarget instanceof EntityFroststalker)) {
                this.m_6710_(playerTarget);
            }
            boolean attackAnim = this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5 || this.getAnimation() == ANIMATION_SHOVE && this.getAnimationTick() == 8 || this.getAnimation() == ANIMATION_SLASH_L && this.getAnimationTick() == 7 || this.getAnimation() == ANIMATION_SLASH_R && this.getAnimationTick() == 7;
            if (this.m_5448_() != null && attackAnim) {
                this.m_5448_().knockback(0.2F, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
            }
        }
        if (this.fleeFireFlag > 0) {
            this.fleeFireFlag--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.resetLeaderCooldown > 0) {
                this.resetLeaderCooldown--;
            } else {
                this.resetLeaderCooldown = 200 + this.m_217043_().nextInt(200);
                this.lookForPlayerLeader();
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void lookForPlayerLeader() {
        if (!(this.leader instanceof Player)) {
            float range = 10.0F;
            List<Player> playerList = this.m_9236_().m_6443_(Player.class, this.m_20191_().inflate((double) range, (double) range, (double) range), VALID_LEADER_PLAYERS);
            Player closestPlayer = null;
            for (Player player : playerList) {
                if (closestPlayer == null || player.m_20270_(this) < closestPlayer.m_20270_(this)) {
                    closestPlayer = player;
                }
            }
            if (closestPlayer != null) {
                this.stopFollowing();
                this.startFollowing(closestPlayer);
            }
        }
    }

    public boolean isFleeingFire() {
        return this.fleeFireFlag > 0;
    }

    public boolean isHotBiome() {
        if (this.m_21525_()) {
            return false;
        } else if (this.m_9236_().dimension() == Level.NETHER) {
            return true;
        } else {
            int i = Mth.floor(this.m_20185_());
            int k = Mth.floor(this.m_20189_());
            return this.m_9236_().m_204166_(new BlockPos(i, 0, k)).is(BiomeTags.SNOW_GOLEM_MELTS);
        }
    }

    public void standFor(int time) {
        this.setBipedal(true);
        this.standingTime = time;
    }

    @Override
    protected float getJumpPower() {
        return 0.52F * this.m_20098_();
    }

    @Override
    protected void jumpFromGround() {
        double d0 = (double) this.getJumpPower() + (double) this.m_285755_();
        Vec3 vec3 = this.m_20184_();
        this.m_20334_(vec3.x, d0, vec3.z);
        float f = this.m_146908_() * (float) (Math.PI / 180.0);
        this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f) * 0.2F), 0.0, (double) (Mth.cos(f) * 0.2F)));
        this.f_19812_ = true;
        ForgeHooks.onLivingJump(this);
    }

    public void frostJump() {
        this.jumpFromGround();
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_BITE, ANIMATION_SPEAK, ANIMATION_SLASH_L, ANIMATION_SLASH_R, ANIMATION_SHOVE };
    }

    public float getTurnAngle() {
        return this.f_19804_.get(TURN_ANGLE);
    }

    public void setTurnAngle(float progress) {
        this.f_19804_.set(TURN_ANGLE, progress);
    }

    public boolean hasSpikes() {
        return this.f_19804_.get(SPIKES);
    }

    public void setSpiked(boolean bar) {
        this.f_19804_.set(SPIKES, bar);
    }

    public boolean isTackling() {
        return this.f_19804_.get(TACKLING);
    }

    public void setTackling(boolean bar) {
        this.f_19804_.set(TACKLING, bar);
    }

    public boolean isBipedal() {
        return this.f_19804_.get(BIPEDAL);
    }

    public void setBipedal(boolean bar) {
        this.f_19804_.set(BIPEDAL, bar);
    }

    public boolean isSpikeShaking() {
        return this.f_19804_.get(SPIKE_SHAKING);
    }

    public void setSpikeShaking(boolean bar) {
        this.f_19804_.set(SPIKE_SHAKING, bar);
    }

    public boolean isFollower() {
        return this.leader != null && this.isValidLeader(this.leader);
    }

    public boolean isValidLeader(LivingEntity leader) {
        if (leader instanceof Player) {
            return this.m_21188_() != null && this.m_21188_().equals(leader) ? false : leader.getItemBySlot(EquipmentSlot.HEAD).is(AMItemRegistry.FROSTSTALKER_HELMET.get());
        } else {
            return leader.isAlive() && leader instanceof EntityFroststalker;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            int anim = this.f_19796_.nextInt(4);
            switch(anim) {
                case 0:
                    this.setAnimation(ANIMATION_SHOVE);
                    break;
                case 1:
                    this.setAnimation(ANIMATION_BITE);
                    break;
                case 2:
                    this.setAnimation(ANIMATION_SLASH_L);
                    break;
                case 3:
                    this.setAnimation(ANIMATION_SLASH_R);
            }
        }
        return true;
    }

    public LivingEntity startFollowing(LivingEntity leader) {
        this.leader = leader;
        if (leader instanceof EntityFroststalker) {
            ((EntityFroststalker) leader).addFollower();
        }
        return leader;
    }

    public void stopFollowing() {
        if (this.leader instanceof EntityFroststalker) {
            ((EntityFroststalker) this.leader).removeFollower();
        }
        this.leader = null;
    }

    private void addFollower() {
        this.packSize++;
    }

    private void removeFollower() {
        this.packSize--;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.packSize < this.getMaxPackSize() && this.isValidLeader(this);
    }

    public boolean hasFollowers() {
        return this.packSize > 1;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    public int getMaxPackSize() {
        return this.getMaxSpawnClusterSize();
    }

    public void addFollowers(Stream<EntityFroststalker> streamEntityFroststalker0) {
        streamEntityFroststalker0.limit((long) (this.getMaxPackSize() - this.packSize)).filter(p_27538_ -> p_27538_ != this).forEach(p_27536_ -> p_27536_.startFollowing(this));
    }

    public boolean inRangeOfLeader() {
        return (double) this.m_20270_(this.leader) <= 60.0;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            double speed = 1.0;
            if (this.leader instanceof Player) {
                speed = 1.3;
                if (this.m_20270_(this.leader) > 24.0F) {
                    speed = 1.4F;
                    this.standFor(20);
                }
            }
            if (this.m_20270_(this.leader) > 6.0F && this.m_21573_().isDone()) {
                this.m_21573_().moveTo(this.leader, speed);
            }
        }
    }

    @Override
    protected void onChangedBlock(BlockPos pos) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, this);
        if (i > 0 || this.hasSpikes()) {
            FrostWalkerEnchantment.onEntityMoved(this, this.m_9236_(), pos, i == 0 ? -1 : i);
        }
        if (this.m_6757_(this.m_20075_())) {
            this.m_21185_();
        }
        this.m_21186_();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        this.m_21051_(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.f_19796_.nextGaussian() * 0.05, AttributeModifier.Operation.MULTIPLY_BASE));
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new EntityFroststalker.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((EntityFroststalker.SchoolSpawnGroupData) spawnGroupData3).leader);
        }
        return spawnGroupData3;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return AMEntityRegistry.FROSTSTALKER.get().create(serverLevel0);
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.hasSpikes() && (this.m_5448_() == null || !this.m_5448_().isAlive());
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.hasSpikes() || this.m_5448_() != null && this.m_5448_().isAlive();
    }

    @Override
    public boolean shouldStopMoving() {
        return false;
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    private class AIAvoidFire extends Goal {

        private final int searchLength;

        private final int verticalSearchRange;

        protected BlockPos destinationBlock;

        protected int runDelay = 20;

        private Vec3 fleeTarget;

        private AIAvoidFire() {
            this.searchLength = 20;
            this.verticalSearchRange = 1;
        }

        @Override
        public boolean canContinueToUse() {
            return this.destinationBlock != null && this.isFire(EntityFroststalker.this.m_9236_(), this.destinationBlock.mutable()) && this.isCloseToFire(16.0);
        }

        public boolean isCloseToFire(double dist) {
            return this.destinationBlock == null || EntityFroststalker.this.m_20238_(Vec3.atCenterOf(this.destinationBlock)) < dist * dist;
        }

        @Override
        public boolean canUse() {
            if (this.runDelay > 0) {
                this.runDelay--;
                return false;
            } else {
                this.runDelay = 30 + EntityFroststalker.this.f_19796_.nextInt(100);
                return this.searchForDestination();
            }
        }

        @Override
        public void start() {
            EntityFroststalker.this.fleeFireFlag = 200;
            Vec3 vec = LandRandomPos.getPosAway(EntityFroststalker.this, 15, 5, Vec3.atCenterOf(this.destinationBlock));
            if (vec != null) {
                EntityFroststalker.this.standFor(100 + EntityFroststalker.this.f_19796_.nextInt(100));
                this.fleeTarget = vec;
                EntityFroststalker.this.m_21573_().moveTo(vec.x, vec.y, vec.z, 1.2F);
            }
        }

        @Override
        public void tick() {
            if (this.isCloseToFire(16.0)) {
                EntityFroststalker.this.fleeFireFlag = 200;
                if (this.fleeTarget == null || EntityFroststalker.this.m_20238_(this.fleeTarget) < 2.0) {
                    Vec3 vec = LandRandomPos.getPosAway(EntityFroststalker.this, 15, 5, Vec3.atCenterOf(this.destinationBlock));
                    if (vec != null) {
                        this.fleeTarget = vec;
                    }
                }
                if (this.fleeTarget != null) {
                    EntityFroststalker.this.m_21573_().moveTo(this.fleeTarget.x, this.fleeTarget.y, this.fleeTarget.z, 1.0);
                }
            }
        }

        @Override
        public void stop() {
            this.fleeTarget = null;
        }

        protected boolean searchForDestination() {
            int lvt_1_1_ = this.searchLength;
            int lvt_2_1_ = this.verticalSearchRange;
            BlockPos lvt_3_1_ = EntityFroststalker.this.m_20183_();
            BlockPos.MutableBlockPos lvt_4_1_ = new BlockPos.MutableBlockPos();
            for (int lvt_5_1_ = -8; lvt_5_1_ <= 2; lvt_5_1_++) {
                for (int lvt_6_1_ = 0; lvt_6_1_ < lvt_1_1_; lvt_6_1_++) {
                    for (int lvt_7_1_ = 0; lvt_7_1_ <= lvt_6_1_; lvt_7_1_ = lvt_7_1_ > 0 ? -lvt_7_1_ : 1 - lvt_7_1_) {
                        for (int lvt_8_1_ = lvt_7_1_ < lvt_6_1_ && lvt_7_1_ > -lvt_6_1_ ? lvt_6_1_ : 0; lvt_8_1_ <= lvt_6_1_; lvt_8_1_ = lvt_8_1_ > 0 ? -lvt_8_1_ : 1 - lvt_8_1_) {
                            lvt_4_1_.setWithOffset(lvt_3_1_, lvt_7_1_, lvt_5_1_ - 1, lvt_8_1_);
                            if (this.isFire(EntityFroststalker.this.m_9236_(), lvt_4_1_)) {
                                this.destinationBlock = lvt_4_1_;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean isFire(Level world, BlockPos.MutableBlockPos lvt_4_1_) {
            return world.getBlockState(lvt_4_1_).m_204336_(AMTagRegistry.FROSTSTALKER_FEARS);
        }
    }

    public static class SchoolSpawnGroupData implements SpawnGroupData {

        public final EntityFroststalker leader;

        public SchoolSpawnGroupData(EntityFroststalker entityFroststalker0) {
            this.leader = entityFroststalker0;
        }
    }
}