package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.VerticalSwimmingMoveControl;
import com.github.alexmodguy.alexscaves.server.entity.item.MineGuardianAnchorEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.MineExplosion;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class MineGuardianEntity extends Monster {

    private float explodeProgress;

    private float prevExplodeProgress;

    private float scanProgress;

    private float prevScanProgress;

    private boolean clientSideTouchedGround;

    private int scanTime = 0;

    private int maxScanTime = 0;

    private int maxSleepTime = 200 + this.f_19796_.nextInt(100);

    private int lastScanTime = 0;

    private int timeSinceHadTarget = 0;

    private static final EntityDataAccessor<Optional<UUID>> ANCHOR_UUID = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> ANCHOR_ID = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MAX_CHAIN_LENGTH = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> EYE_CLOSED = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SCANNING = SynchedEntityData.defineId(MineGuardianEntity.class, EntityDataSerializers.BOOLEAN);

    public MineGuardianEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.f_21342_ = new VerticalSwimmingMoveControl(this, 0.7F, 30.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(ANCHOR_UUID, Optional.empty());
        this.f_19804_.define(ANCHOR_ID, -1);
        this.f_19804_.define(MAX_CHAIN_LENGTH, 8);
        this.f_19804_.define(EXPLODING, false);
        this.f_19804_.define(EYE_CLOSED, false);
        this.f_19804_.define(SCANNING, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new MineGuardianEntity.MeleeGoal());
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Nullable
    public UUID getAnchorUUID() {
        return (UUID) this.f_19804_.get(ANCHOR_UUID).orElse(null);
    }

    public void setAnchorUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(ANCHOR_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getAnchor() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getAnchorUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(ANCHOR_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_20072_() ? ACSoundRegistry.MINE_GUARDIAN_IDLE.get() : ACSoundRegistry.MINE_GUARDIAN_LAND_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.m_20072_() ? ACSoundRegistry.MINE_GUARDIAN_HURT.get() : ACSoundRegistry.MINE_GUARDIAN_LAND_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.m_20072_() ? ACSoundRegistry.MINE_GUARDIAN_DEATH.get() : ACSoundRegistry.MINE_GUARDIAN_LAND_DEATH.get();
    }

    public boolean isExploding() {
        return this.f_19804_.get(EXPLODING);
    }

    public void setExploding(boolean explode) {
        this.f_19804_.set(EXPLODING, explode);
    }

    public boolean isEyeClosed() {
        return this.f_19804_.get(EYE_CLOSED);
    }

    public void setEyeClosed(boolean eyeClosed) {
        this.f_19804_.set(EYE_CLOSED, eyeClosed);
    }

    public boolean isScanning() {
        return this.f_19804_.get(SCANNING);
    }

    public void setScanning(boolean scanning) {
        this.f_19804_.set(SCANNING, scanning);
    }

    public int getMaxChainLength() {
        return this.f_19804_.get(MAX_CHAIN_LENGTH);
    }

    public void setMaxChainLength(int length) {
        this.f_19804_.set(MAX_CHAIN_LENGTH, length);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.m_45784_(this);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevExplodeProgress = this.explodeProgress;
        this.prevScanProgress = this.scanProgress;
        if (this.isScanning() && this.scanProgress < 5.0F) {
            this.scanProgress++;
        }
        if (!this.isScanning() && this.scanProgress > 0.0F) {
            this.scanProgress--;
        }
        if (this.isExploding() && this.explodeProgress < 10.0F) {
            this.explodeProgress += 0.5F;
        }
        if (!this.isExploding() && this.explodeProgress > 0.0F) {
            this.explodeProgress -= 0.5F;
        }
        if (this.isExploding()) {
            if (this.explodeProgress >= 10.0F) {
                this.m_142687_(Entity.RemovalReason.KILLED);
                Explosion.BlockInteraction blockinteraction = ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) ? (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY) : Explosion.BlockInteraction.KEEP;
                MineExplosion explosion = new MineExplosion(this.m_9236_(), this, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 5.0F, this.m_20072_(), blockinteraction);
                explosion.explode();
                explosion.finalizeExplosion(true);
            }
            this.m_20256_(this.m_20184_().multiply(0.3F, 1.0, 0.3F));
        }
        if (!this.m_9236_().isClientSide) {
            Entity entity = this.getAnchor();
            if (entity == null) {
                this.setMaxChainLength(7 + this.m_217043_().nextInt(6));
                MineGuardianAnchorEntity created = new MineGuardianAnchorEntity(this);
                this.m_9236_().m_7967_(created);
                this.setAnchorUUID(created.m_20148_());
                this.f_19804_.set(ANCHOR_ID, created.m_19879_());
            } else if (entity instanceof MineGuardianAnchorEntity anchorEntity) {
                anchorEntity.linkWithGuardian(this);
            }
            if (this.m_20072_()) {
                this.m_20301_(300);
            } else if (this.m_20096_()) {
                this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.6F), 0.6F, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.6F)));
                this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
                this.m_6853_(false);
                this.m_216990_(ACSoundRegistry.MINE_GUARDIAN_FLOP.get());
                this.f_19812_ = true;
            }
            Entity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                this.timeSinceHadTarget = 0;
            } else {
                this.timeSinceHadTarget++;
            }
            if (this.isScanning()) {
                this.setEyeClosed(false);
                if (this.scanTime < this.maxScanTime) {
                    if (this.scanTime % 20 == 0) {
                        this.m_216990_(ACSoundRegistry.MINE_GUARDIAN_SCAN.get());
                    }
                    if (this.scanTime % 5 == 0 && this.scanProgress >= 5.0F) {
                        Entity found;
                        label193: {
                            found = null;
                            HitResult hitresult = this.m_9236_().m_45547_(new ClipContext(this.m_146892_(), this.m_146892_().add(this.m_20154_().scale(8.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                            if (hitresult instanceof EntityHitResult entityHitResult && this.isValidTarget(entityHitResult.getEntity())) {
                                Entity inSight = entityHitResult.getEntity();
                                if (!inSight.equals(this) && !inSight.isAlliedTo(this) && !this.m_7307_(inSight) && this.m_142582_(inSight)) {
                                    found = inSight;
                                }
                                break label193;
                            }
                            AABB around = new AABB(hitresult.getLocation().add(-0.5, -0.5, -0.5), hitresult.getLocation().add(0.5, 0.5, 0.5)).inflate(3.0);
                            for (Entity inSight : this.m_9236_().m_45976_(LivingEntity.class, around)) {
                                if (!inSight.equals(this) && !inSight.isAlliedTo(this) && !this.m_7307_(inSight) && this.m_142582_(inSight) && (found == null && this.isValidTarget(inSight) || found != null && this.isValidTarget(inSight) && inSight.distanceTo(this) < found.distanceTo(this))) {
                                    found = inSight;
                                }
                            }
                        }
                        if (found instanceof LivingEntity living) {
                            this.m_6710_(living);
                            this.setScanning(false);
                        }
                    }
                    this.scanTime++;
                } else {
                    this.scanTime = 0;
                    this.lastScanTime = this.f_19797_;
                    this.setScanning(false);
                }
            } else if (this.isEyeClosed()) {
                int j = this.f_19797_ - this.lastScanTime;
                if (this.timeSinceHadTarget == 0 || !this.m_20072_()) {
                    this.setEyeClosed(false);
                } else if (this.m_20072_() && (this.timeSinceHadTarget > this.maxSleepTime && j > 200 || this.f_20916_ > 0)) {
                    this.maxSleepTime = 200 + this.f_19796_.nextInt(100);
                    this.setScanning(true);
                    this.scanTime = 0;
                    this.maxScanTime = 100 + this.f_19796_.nextInt(100);
                }
            } else if (this.m_20072_() && this.timeSinceHadTarget > 100) {
                this.setEyeClosed(true);
            }
        } else {
            Vec3 vec3 = this.m_20184_();
            if (vec3.y > 0.0 && this.clientSideTouchedGround && !this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.getFlopSound(), this.m_5720_(), 1.0F, 1.0F, false);
            }
            this.clientSideTouchedGround = vec3.y < 0.0 && this.m_9236_().loadedAndEntityCanStandOn(this.m_20183_().below(), this);
        }
    }

    private boolean isValidTarget(Entity entity) {
        if (entity instanceof Player player && this.m_6779_(player)) {
            return true;
        }
        return false;
    }

    public static boolean checkMineGuardianSpawnRules(EntityType entityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (!level.m_6425_(blockPos).is(FluidTags.WATER)) {
            return false;
        } else {
            boolean flag = level.m_46791_() != Difficulty.PEACEFUL && m_219009_(level, blockPos, randomSource) && (mobSpawnType == MobSpawnType.SPAWNER || level.m_6425_(blockPos).is(FluidTags.WATER));
            if (randomSource.nextInt(10) == 0 && blockPos.m_123342_() < level.m_5736_() - 50 && flag) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(blockPos.m_123341_(), blockPos.m_123342_(), blockPos.m_123343_());
                while (!level.m_6425_(pos).isEmpty() && pos.m_123342_() < level.m_5736_()) {
                    pos.move(0, 1, 0);
                }
                int belowAirBy = pos.m_123342_() - blockPos.m_123342_();
                pos.set(blockPos);
                while (!level.m_6425_(pos).isEmpty() && pos.m_123342_() > level.m_141937_()) {
                    pos.move(0, -1, 0);
                }
                BlockState groundState = level.m_8055_(pos);
                return belowAirBy > 15 && (groundState.m_60713_(ACBlockRegistry.MUCK.get()) || groundState.m_60713_(ACBlockRegistry.ABYSSMARINE.get()) || groundState.m_60713_(Blocks.DEEPSLATE));
            } else {
                return false;
            }
        }
    }

    public float getScanProgress(float partialTick) {
        return (this.prevScanProgress + (this.scanProgress - this.prevScanProgress) * partialTick) * 0.2F;
    }

    public float getExplodeProgress(float partialTick) {
        return (this.prevExplodeProgress + (this.explodeProgress - this.prevExplodeProgress) * partialTick) * 0.1F;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.GUARDIAN_FLOP;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("AnchorUUID")) {
            this.setAnchorUUID(compound.getUUID("AnchorUUID"));
        }
        this.setMaxChainLength(compound.getInt("MaxChainLength"));
        this.scanTime = compound.getInt("ScanTime");
        this.setEyeClosed(compound.getBoolean("EyeClosed"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getAnchorUUID() != null) {
            compound.putUUID("AnchorUUID", this.getAnchorUUID());
        }
        compound.putInt("MaxChainLength", this.getMaxChainLength());
        compound.putBoolean("EyeClosed", this.isEyeClosed());
        compound.putInt("ScanTime", this.scanTime);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setEyeClosed(true);
        this.timeSinceHadTarget = 10;
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSource) {
        super.m_6668_(damageSource);
        if (!this.m_9236_().isClientSide && damageSource.getEntity() instanceof Player player) {
            ACWorldData worldData = ACWorldData.get(this.m_9236_());
            int relations = worldData.getDeepOneReputation(player.m_20148_());
            if (relations < 0) {
                worldData.setDeepOneReputation(player.m_20148_(), relations + this.f_19796_.nextInt(3) + 1);
            }
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    private class MeleeGoal extends Goal {

        private int timer = 0;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = MineGuardianEntity.this.m_5448_();
            return target != null;
        }

        @Override
        public void start() {
            this.timer = 0;
        }

        @Override
        public void tick() {
            LivingEntity target = MineGuardianEntity.this.m_5448_();
            if (target != null) {
                this.timer++;
                double dist = (double) MineGuardianEntity.this.m_20270_(target);
                MineGuardianEntity.this.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                if (dist > 2.0) {
                    if (MineGuardianEntity.this.m_20072_()) {
                        MineGuardianEntity.this.m_21573_().moveTo(target, 1.6);
                    }
                } else {
                    MineGuardianEntity.this.setExploding(true);
                }
                if (this.timer > 300) {
                    MineGuardianEntity.this.lastScanTime = MineGuardianEntity.this.f_19797_;
                    MineGuardianEntity.this.timeSinceHadTarget = 5;
                    MineGuardianEntity.this.setEyeClosed(true);
                    MineGuardianEntity.this.m_6710_(null);
                    MineGuardianEntity.this.m_6703_(null);
                }
            }
        }
    }
}