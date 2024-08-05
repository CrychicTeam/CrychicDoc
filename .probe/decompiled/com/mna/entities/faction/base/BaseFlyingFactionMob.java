package com.mna.entities.faction.base;

import com.google.common.collect.Maps;
import com.mna.api.entities.IFactionEnemy;
import com.mna.effects.EffectInit;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.ai.RetaliateOnAttackGoal;
import com.mna.tools.SummonUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseFlyingFactionMob<T extends BaseFlyingFactionMob<?>> extends FlyingMob implements Enemy, IFactionEnemy<T>, GeoEntity, IAnimPacketSync<T>, IEntityAdditionalSpawnData {

    private static final EntityDataAccessor<OptionalInt> TARGET_ID = SynchedEntityData.defineId(BaseFlyingFactionMob.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(BaseFlyingFactionMob.class, EntityDataSerializers.INT);

    protected static final int RANDOM_FLY_ID = 999;

    protected Player raidTarget;

    protected int tier;

    protected HashMap<String, Integer> damageResists;

    protected AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    protected LivingEntity localTarget;

    protected int randomFlyCooldown = 0;

    private final Map<String, Tuple<MutableInt, Runnable>> timers = Maps.newHashMap();

    private final Map<Integer, Integer> cooldowns = Maps.newHashMap();

    protected BaseFlyingFactionMob(EntityType<T> mobType, Level world) {
        super(mobType, world);
        this.m_20242_(true);
        this.f_21342_ = new BaseFlyingFactionMob.MoveHelperController(this);
        this.damageResists = new HashMap();
    }

    protected void setTimer(String id, int delay, Runnable callback) {
        this.setTimer(id, delay, callback, true);
    }

    protected void setTimer(String id, int delay, Runnable callback, boolean runIfExists) {
        if (runIfExists && this.timers.containsKey(id)) {
            ((Runnable) ((Tuple) this.timers.get(id)).getB()).run();
        }
        this.timers.put(id, new Tuple<>(new MutableInt(delay), callback));
    }

    protected void setCooldown(int id, int ticks) {
        this.cooldowns.put(id, ticks);
    }

    protected boolean isOnCooldown(int id) {
        return (Integer) this.cooldowns.getOrDefault(id, 0) > 0;
    }

    protected int getStateFlag() {
        return this.f_19804_.get(DATA_STATE);
    }

    protected void setState(int... flags) {
        int finalFlag = 0;
        for (int i : flags) {
            finalFlag |= i;
        }
        this.f_19804_.set(DATA_STATE, finalFlag);
    }

    @Override
    public void tick() {
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            this.m_21563_().setLookAt(this.getTarget());
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.getTarget().m_146892_());
        } else if (this.m_20184_().length() > 0.1F) {
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.m_20182_().add(this.m_20184_().normalize().scale(30.0)));
        }
        if (this.m_21023_(EffectInit.GRAVITY_WELL.get())) {
            this.m_20256_(this.m_20184_().add(0.0, -0.2, 0.0));
        }
        this.cooldowns.keySet().forEach(c -> {
            int cd = (Integer) this.cooldowns.get(c);
            if (cd > 0) {
                this.cooldowns.put(c, cd - 1);
            }
        });
        List<String> timersToRemove = new ArrayList();
        this.timers.forEach((k, v) -> {
            ((MutableInt) ((Tuple) this.timers.get(k)).getA()).subtract(1);
            if (((MutableInt) ((Tuple) this.timers.get(k)).getA()).getValue() <= 0) {
                timersToRemove.add(k);
            }
        });
        timersToRemove.forEach(id -> {
            ((Runnable) ((Tuple) this.timers.get(id)).getB()).run();
            this.timers.remove(id);
        });
        if (this.randomFlyCooldown > 0 && this.isOnCooldown(999) && this.m_20184_().length() > 0.0) {
            Vec3 wanted = new Vec3(this.m_21566_().getWantedX(), this.m_21566_().getWantedY(), this.m_21566_().getWantedZ());
            if (this.m_20238_(wanted) < 1.0) {
                this.m_21566_().setWantedPosition(this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0);
                this.m_20256_(Vec3.ZERO);
            }
        }
        super.m_8119_();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(TARGET_ID, OptionalInt.empty());
        this.f_19804_.define(DATA_STATE, 0);
    }

    @Override
    public void setTarget(LivingEntity pLivingEntity) {
        super.m_6710_(pLivingEntity);
        if (!this.m_9236_().isClientSide()) {
            this.f_19804_.set(TARGET_ID, pLivingEntity != null ? OptionalInt.of(pLivingEntity.m_19879_()) : OptionalInt.empty());
        }
    }

    @Override
    public LivingEntity getTarget() {
        if (!this.m_9236_().isClientSide()) {
            return super.m_5448_();
        } else {
            OptionalInt targetId = this.f_19804_.get(TARGET_ID);
            if (!targetId.isPresent()) {
                return null;
            } else {
                if (this.localTarget == null || this.localTarget.m_19879_() != targetId.getAsInt()) {
                    Entity e = this.m_9236_().getEntity(targetId.getAsInt());
                    if (e instanceof LivingEntity) {
                        this.localTarget = (LivingEntity) e;
                    }
                }
                return this.localTarget;
            }
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(5, new BaseFlyingFactionMob.BaseFlyingFactionMobFlyGoal(this));
        this.f_21345_.addGoal(7, new BaseFlyingFactionMob.LookAroundGoal(this));
        this.f_21346_.addGoal(1, new RetaliateOnAttackGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, (Class<T>) Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, (Class<T>) Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, (Class<T>) Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction() && e.isAlive()));
    }

    public float getStepHeight() {
        return 1.2F;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        this.applyInitialSpawnTier(worldIn);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<T> controller = new AnimationController<>((T) this, state -> this.handleAnimState(state));
        this.addControllerListeners(controller);
        registrar.add(controller);
    }

    protected void addControllerListeners(AnimationController<T> controller) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void setRaidTarget(Player player) {
        this.raidTarget = player;
        this.setTarget(player);
    }

    @Override
    public Player getRaidTarget() {
        return this.raidTarget;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
        this.m_21051_(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("tier_health_bonus", (double) (tier + 1), AttributeModifier.Operation.MULTIPLY_BASE));
        this.m_21051_(Attributes.ARMOR).addPermanentModifier(new AttributeModifier("tier_armor_bonus", (double) (5 * tier), AttributeModifier.Operation.ADDITION));
        this.m_21153_(this.m_21233_());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        this.writeFactionData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.readFactionData(compound);
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return this.damageResists;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FALL)) {
            return false;
        } else {
            amount = this.applyDamageResists(source, amount);
            return super.m_6469_(source, amount);
        }
    }

    @Override
    public void checkDespawn() {
        super.m_6043_();
        this.raidTargetDespawn();
        if (this.m_9236_() != null && !this.m_9236_().isClientSide() && this.m_9236_().getServer() != null && this.m_9236_().getServer().getWorldData().getDifficulty() == Difficulty.PEACEFUL) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.m_6667_(cause);
        this.onKilled(cause);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.tier = additionalData.readInt();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.tier);
    }

    protected abstract PlayState handleAnimState(AnimationState<? extends BaseFlyingFactionMob<?>> var1);

    protected float keepDistanceFromAttackTarget() {
        return 10.0F;
    }

    protected float heightAboveAttackTarget() {
        return 3.0F;
    }

    protected float attackTargetRunawayThreshold() {
        return 0.0F;
    }

    protected float attackTargetRunawayDistance() {
        return 0.0F;
    }

    protected boolean canFlyInWater() {
        return false;
    }

    static class BaseFlyingFactionMobFlyGoal extends Goal {

        private final BaseFlyingFactionMob<?> parentEntity;

        public BaseFlyingFactionMobFlyGoal(BaseFlyingFactionMob<?> parent) {
            this.parentEntity = parent;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (!movementcontroller.hasWanted()) {
                return !this.parentEntity.isOnCooldown(999);
            } else if (this.parentEntity.getTarget() == null) {
                if (this.parentEntity.isOnCooldown(999)) {
                    return false;
                } else {
                    double d0 = movementcontroller.getWantedX() - this.parentEntity.m_20185_();
                    double d1 = movementcontroller.getWantedY() - this.parentEntity.m_20186_();
                    double d2 = movementcontroller.getWantedZ() - this.parentEntity.m_20189_();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    return d3 < 4.0 || d3 > 3600.0;
                }
            } else {
                double dist = this.parentEntity.getTarget().m_20280_(this.parentEntity);
                return !this.parentEntity.m_21574_().hasLineOfSight(this.parentEntity.getTarget()) || dist > 25.0 || dist < 4.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (this.parentEntity.getTarget() == null) {
                boolean setRandom = true;
                if (SummonUtils.isSummon(this.parentEntity)) {
                    LivingEntity summoner = SummonUtils.getSummoner(this.parentEntity);
                    if (summoner != null && summoner.m_20270_(this.parentEntity) > 16.0F) {
                        setRandom = false;
                        this.parentEntity.m_21566_().setWantedPosition(summoner.m_20185_(), summoner.m_20186_(), summoner.m_20189_(), this.parentEntity.m_21133_(Attributes.MOVEMENT_SPEED));
                    }
                }
                if (setRandom) {
                    RandomSource random = this.parentEntity.m_217043_();
                    double d0 = this.parentEntity.m_20185_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    double d1 = this.parentEntity.m_20186_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    double d2 = this.parentEntity.m_20189_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    int count = 0;
                    int y = (int) d1;
                    boolean ground = false;
                    while (count < 16) {
                        BlockPos bpTestPos = BlockPos.containing(d0, (double) y, d2);
                        if (this.parentEntity.m_9236_().m_46859_(bpTestPos)) {
                            count++;
                            y--;
                        } else {
                            if (!this.parentEntity.m_9236_().m_46801_(bpTestPos)) {
                                ground = true;
                                break;
                            }
                            count++;
                            if (!this.parentEntity.canFlyInWater()) {
                                y++;
                            }
                            ground = true;
                        }
                    }
                    if (!ground) {
                        d1 -= 16.0;
                    }
                    this.parentEntity.m_21566_().setWantedPosition(d0, d1, d2, 1.0);
                    if (this.parentEntity.randomFlyCooldown > 0) {
                        this.parentEntity.setCooldown(999, this.parentEntity.randomFlyCooldown);
                    }
                }
            } else {
                Vec3 direction = this.parentEntity.m_20182_().subtract(this.parentEntity.getTarget().m_20182_()).normalize();
                direction = direction.add(0.0, -direction.y, 0.0);
                Vec3 offset = direction.scale((double) this.parentEntity.keepDistanceFromAttackTarget());
                Vec3 target = this.parentEntity.getTarget().m_20182_().add(offset);
                if (this.parentEntity.getTarget().m_20096_()) {
                    target = target.add(0.0, (double) this.parentEntity.heightAboveAttackTarget(), 0.0);
                }
                if (new Vec3(target.x, 0.0, target.z).distanceTo(new Vec3(this.parentEntity.getTarget().m_20185_(), 0.0, this.parentEntity.getTarget().m_20189_())) < (double) this.parentEntity.attackTargetRunawayThreshold()) {
                    float dist = this.parentEntity.attackTargetRunawayDistance();
                    target = target.add(Math.random() * (double) dist / 2.0 - (double) dist, 0.0, Math.random() * (double) dist / 2.0 - (double) dist);
                }
                this.parentEntity.m_21566_().setWantedPosition(target.x, target.y, target.z, 1.0);
            }
        }
    }

    static class LookAroundGoal extends Goal {

        private final BaseFlyingFactionMob<?> parentEntity;

        public LookAroundGoal(BaseFlyingFactionMob<?> parent) {
            this.parentEntity = parent;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.parentEntity.getTarget() == null) {
                Vec3 vector3d = this.parentEntity.m_20184_();
                this.parentEntity.m_146926_(-((float) Mth.atan2(vector3d.x, vector3d.z)) * (180.0F / (float) Math.PI));
                this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
            } else {
                LivingEntity livingentity = this.parentEntity.getTarget();
                if (livingentity.m_20280_(this.parentEntity) < 4096.0) {
                    double d1 = livingentity.m_20185_() - this.parentEntity.m_20185_();
                    double d2 = livingentity.m_20189_() - this.parentEntity.m_20189_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(d1, d2)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }
    }

    static class MoveHelperController extends MoveControl {

        private final BaseFlyingFactionMob<?> parentEntity;

        private int courseChangeCooldown;

        public MoveHelperController(BaseFlyingFactionMob<?> parent) {
            super(parent);
            this.parentEntity = parent;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && this.courseChangeCooldown-- <= 0) {
                this.courseChangeCooldown = this.courseChangeCooldown + this.parentEntity.m_217043_().nextInt(5) + 2;
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                vector3d = vector3d.normalize();
                if (this.isCollided(vector3d, Mth.ceil(d0))) {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(0.1)));
                } else {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                }
            }
        }

        private boolean isCollided(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }
}