package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.ai.MobTarget3DGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.WatcherAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.PossessesCamera;
import com.github.alexmodguy.alexscaves.server.entity.util.WatcherPossessionAccessor;
import com.github.alexmodguy.alexscaves.server.message.PossessionKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class WatcherEntity extends Monster implements IAnimatedEntity, PossessesCamera {

    private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(WatcherEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SHADE_MODE = SynchedEntityData.defineId(WatcherEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<UUID>> POSSESSED_ENTITY_UUID = SynchedEntityData.defineId(WatcherEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> POSSESSED_ENTITY_ID = SynchedEntityData.defineId(WatcherEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> POSSESSION_STRENGTH = SynchedEntityData.defineId(WatcherEntity.class, EntityDataSerializers.FLOAT);

    public static final Animation ANIMATION_ATTACK_0 = Animation.create(15);

    public static final Animation ANIMATION_ATTACK_1 = Animation.create(15);

    private Animation currentAnimation;

    private int animationTick;

    private float runProgress;

    private float prevRunProgress;

    private float shadeProgress;

    private float prevShadeProgress;

    private boolean isLandNavigator;

    private final PathNavigation groundNavigator;

    private final PathNavigation airNavigator;

    private BlockPos lastPossessionSite = null;

    private int lastPossessionTimestamp;

    private Entity prevPossessedEntity;

    private boolean isPossessionBreakable;

    private int possessedTimeout = 0;

    private static final String LAST_POSSESSED_TIME_IDENTIFIER = "alexscaves_last_possessed_time";

    private UUID previousPossessionUUID;

    public WatcherEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.groundNavigator = this.createNavigation(level);
        this.airNavigator = this.createShadeNavigation(level);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.FOLLOW_RANGE, 256.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigatorNoSpin(this, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new WatcherAttackGoal(this));
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0, 100));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, UnderzealotEntity.class, WatcherEntity.class, ForsakenEntity.class).setAlertOthers());
        this.f_21346_.addGoal(2, new MobTarget3DGoal(this, Player.class, false, 10, this::canPossessTargetEntity));
    }

    protected PathNavigation createShadeNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(BlockPos pos) {
                return !this.f_26495_.getBlockState(pos.below()).m_60795_();
            }
        };
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.m_7008_(false);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.groundNavigator;
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new WatcherEntity.MoveController();
            this.f_21344_ = this.airNavigator;
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(RUNNING, false);
        this.f_19804_.define(SHADE_MODE, false);
        this.f_19804_.define(POSSESSED_ENTITY_UUID, Optional.empty());
        this.f_19804_.define(POSSESSED_ENTITY_ID, -1);
        this.f_19804_.define(POSSESSION_STRENGTH, 0.0F);
    }

    public boolean isRunning() {
        return this.f_19804_.get(RUNNING);
    }

    public void setRunning(boolean bool) {
        this.f_19804_.set(RUNNING, bool);
    }

    public boolean isShadeMode() {
        return this.f_19804_.get(SHADE_MODE);
    }

    public void setShadeMode(boolean bool) {
        this.f_19804_.set(SHADE_MODE, bool);
    }

    @Override
    public float getPossessionStrength(float partialTicks) {
        return this.f_19804_.get(POSSESSION_STRENGTH);
    }

    @Override
    public boolean instant() {
        return false;
    }

    public void setPossessionStrength(float possessionStrength) {
        this.f_19804_.set(POSSESSION_STRENGTH, possessionStrength);
    }

    @Nullable
    public UUID getPossessedEntityUUID() {
        return (UUID) this.f_19804_.get(POSSESSED_ENTITY_UUID).orElse(null);
    }

    public void setPossessedEntityUUID(@Nullable UUID hologram) {
        this.f_19804_.set(POSSESSED_ENTITY_UUID, Optional.ofNullable(hologram));
    }

    @Override
    public boolean isPossessionBreakable() {
        return this.isPossessionBreakable;
    }

    public Entity getPossessedEntity() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getPossessedEntityUUID();
            return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            int id = this.f_19804_.get(POSSESSED_ENTITY_ID);
            return id == -1 ? null : this.m_9236_().getEntity(id);
        }
    }

    public boolean canReach(LivingEntity target, boolean flying) {
        Path path = (flying ? this.airNavigator : this.groundNavigator).createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.m_146903_();
                int j = node.y - target.m_146904_();
                int k = node.z - target.m_146907_();
                return (double) (i * i + j * j + k * k) <= 3.0;
            }
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevRunProgress = this.runProgress;
        this.prevShadeProgress = this.shadeProgress;
        Entity possessedEntity = this.getPossessedEntity();
        if (this.isRunning() && this.runProgress < 5.0F) {
            this.runProgress++;
        }
        if (!this.isRunning() && this.runProgress > 0.0F) {
            this.runProgress--;
        }
        if (this.isShadeMode() && this.shadeProgress < 5.0F) {
            this.shadeProgress++;
        }
        if (!this.isShadeMode() && this.shadeProgress > 0.0F) {
            this.shadeProgress--;
        }
        if (this.isShadeMode()) {
            this.f_19789_ = 0.0F;
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (possessedEntity instanceof Player) {
            this.f_20885_ = Mth.approachDegrees(this.f_20886_, this.f_20885_, 3.0F);
        }
        if (!this.m_9236_().isClientSide) {
            if (possessedEntity != null && possessedEntity.isAlive()) {
                double dist = (double) possessedEntity.distanceTo(this);
                if (this.f_19804_.get(POSSESSED_ENTITY_ID) != possessedEntity.getId()) {
                    this.f_19804_.set(POSSESSED_ENTITY_ID, possessedEntity.getId());
                    this.setPossessionStrength(1.0F);
                }
                this.lastPossessionTimestamp = this.f_19797_;
                if (this.possessedTimeout++ > 140) {
                    this.setPossessionStrength(Math.max(0.0F, this.getPossessionStrength(1.0F) + 0.1F));
                }
                if (!(dist < 1.0) && !this.stopPossession(possessedEntity) && this.m_6084_()) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 77);
                    if (possessedEntity instanceof LivingEntity living) {
                        living.zza = 0.0F;
                        living.yya = 0.0F;
                        living.xxa = 0.0F;
                    }
                } else {
                    if (possessedEntity instanceof WatcherPossessionAccessor possessionAccessor) {
                        possessionAccessor.setPossessedByWatcher(false);
                    }
                    if (possessedEntity instanceof Player player) {
                        setLastPossessedTimeFor(player);
                    }
                    this.m_9236_().broadcastEntityEvent(this, (byte) 78);
                    this.setPossessedEntityUUID(null);
                    this.f_19804_.set(POSSESSED_ENTITY_ID, -1);
                }
            } else {
                if (possessedEntity != null || this.f_19804_.get(POSSESSED_ENTITY_ID) != -1) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 78);
                }
                this.possessedTimeout = 0;
                this.f_19804_.set(POSSESSED_ENTITY_ID, -1);
            }
        } else if (possessedEntity instanceof LivingEntity living) {
            living.zza = 0.0F;
            living.yya = 0.0F;
            living.xxa = 0.0F;
            if (this.getPossessionStrength(1.0F) == 0.0F) {
                this.isPossessionBreakable = true;
            }
            if (living instanceof Player player && this.isPossessionBreakable) {
                player.f_20899_ = false;
                Player clientSidePlayer = AlexsCaves.PROXY.getClientSidePlayer();
                if (AlexsCaves.PROXY.isKeyDown(-1) && player == clientSidePlayer) {
                    AlexsCaves.sendMSGToServer(new PossessionKeyMessage(this.m_19879_(), player.m_19879_(), 0));
                }
            }
            if (this.prevPossessedEntity != living) {
                this.isPossessionBreakable = false;
            }
            this.prevPossessedEntity = living;
        } else {
            this.prevPossessedEntity = null;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private boolean stopPossession(Entity possessed) {
        float possessionStrength = this.getPossessionStrength(1.0F);
        this.setPossessionStrength(Math.max(0.0F, possessionStrength - 0.05F));
        return possessionStrength >= 1.0F && this.possessedTimeout > 40;
    }

    public boolean canPossessTargetEntity(Entity entity) {
        if (entity instanceof Player player) {
            CompoundTag playerData = player.getPersistentData();
            CompoundTag data = playerData.getCompound("PlayerPersisted");
            if (data != null) {
                long timeElapsed = this.m_9236_().getGameTime() - data.getLong("alexscaves_last_possessed_time");
                return timeElapsed >= (long) AlexsCaves.COMMON_CONFIG.watcherPossessionCooldown.get().intValue();
            }
        }
        return true;
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b != 77 && b != 78) {
            super.m_7822_(b);
        } else {
            Entity possessedEntity = this.getPossessedEntity();
            if (possessedEntity == null && this.getPossessedEntityUUID() != null) {
                possessedEntity = this.m_9236_().m_46003_(this.getPossessedEntityUUID());
            }
            if (possessedEntity instanceof Player player && player == AlexsCaves.PROXY.getClientSidePlayer()) {
                if (b == 77) {
                    if (AlexsCaves.COMMON_CONFIG.watcherPossession.get()) {
                        AlexsCaves.PROXY.setRenderViewEntity(player, this);
                    }
                } else {
                    this.m_9236_().addParticle(ACParticleRegistry.WATCHER_APPEARANCE.get(), player.m_20185_(), player.m_20188_(), player.m_20189_(), 0.0, 0.0, 0.0);
                    player.m_9236_().playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), ACSoundRegistry.WATCHER_SCARE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
                    if (AlexsCaves.COMMON_CONFIG.watcherPossession.get()) {
                        AlexsCaves.PROXY.resetRenderViewEntity(player);
                    }
                }
            }
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getShadeAmount(float partialTick) {
        return (this.prevShadeProgress + (this.shadeProgress - this.prevShadeProgress) * partialTick) * 0.2F;
    }

    public float getRunAmount(float partialTick) {
        return (this.prevRunProgress + (this.runProgress - this.prevRunProgress) * partialTick) * 0.2F;
    }

    public boolean attemptPossession(LivingEntity living) {
        if (this.f_19797_ - this.lastPossessionTimestamp > 100 && (this.lastPossessionSite == null || this.lastPossessionSite.m_123331_(this.m_20183_()) > 10.0) && this.canPossessTargetEntity(living)) {
            this.lastPossessionSite = this.m_20183_();
            this.lastPossessionTimestamp = this.f_19797_;
            if (living instanceof Player player) {
                setLastPossessedTimeFor(player);
                ((WatcherPossessionAccessor) player).setPossessedByWatcher(true);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void setLastPossessedTimeFor(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag data = playerData.getCompound("PlayerPersisted");
        if (data != null) {
            data.putLong("alexscaves_last_possessed_time", player.m_9236_().getGameTime());
            playerData.put("PlayerPersisted", data);
        }
    }

    @Override
    public void onPossessionKeyPacket(Entity keyPresser, int type) {
        Entity possessed = this.getPossessedEntity();
        if (possessed.equals(keyPresser)) {
            this.setPossessionStrength(this.getPossessionStrength(1.0F) + 0.07F);
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
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK_0, ANIMATION_ATTACK_1 };
    }

    public static boolean checkWatcherSpawnRules(EntityType<? extends Monster> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return m_219013_(entityType, levelAccessor, mobSpawnType, blockPos, randomSource) && randomSource.nextInt(20) == 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.WATCHER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.WATCHER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.WATCHER_DEATH.get();
    }

    class MoveController extends MoveControl {

        private final Mob parentEntity = WatcherEntity.this;

        public MoveController() {
            super(WatcherEntity.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 ed = this.f_24974_.getNavigation().getTargetPos().getCenter();
                double d1 = this.f_24976_ - this.f_24974_.m_20186_();
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                double width = this.parentEntity.m_20191_().getSize();
                LivingEntity attackTarget = this.parentEntity.getTarget();
                Vec3 vector3d1 = vector3d.scale(this.f_24978_ * 0.025 / d0).add(0.0, 0.08 + d1 / d0 * 0.1, 0.0);
                this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d1));
                if (d0 < width * 0.8F) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                } else if (d0 >= width && attackTarget == null) {
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    if (WatcherEntity.this.m_5448_() != null) {
                        this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                    }
                }
            }
        }
    }
}