package com.mna.entities.boss;

import com.google.common.collect.Maps;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.tools.CollectionUtils;
import com.mna.effects.EffectInit;
import com.mna.entities.ai.ThreatTable;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.StructureUtils;
import com.mna.tools.SummonUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BossMonster<T extends BossMonster<?>> extends Monster implements GeoEntity, AnimationController.CustomKeyframeHandler<BossMonster<?>>, AnimationController.SoundKeyframeHandler<BossMonster<?>> {

    protected AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> DATA_ID_TARGET = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_ID_INV = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_SHOW_BOSS_BAR = SynchedEntityData.defineId(BossMonster.class, EntityDataSerializers.BOOLEAN);

    protected static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = entity -> entity.isAlive() && entity.attackable();

    protected static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forCombat().range(20.0).selector(LIVING_ENTITY_SELECTOR);

    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();

    protected static final int ACTION_ATTACK = 1;

    private final Map<Integer, Integer> cooldowns = Maps.newHashMap();

    private final Map<String, Tuple<MutableInt, Runnable>> timers = Maps.newHashMap();

    private BoundingBox arenaBB;

    protected ThreatTable threat;

    protected LivingEntity target;

    protected boolean killedByPlayer = false;

    protected BossMonster(EntityType<? extends Monster> p_i48553_1_, Level p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        this.threat = new ThreatTable(e -> e == this ? false : !(e instanceof Player) || !((Player) e).isCreative() && !((Player) e).isSpectator());
    }

    @Override
    public void tick() {
        super.m_8119_();
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
        if (this.arenaBB != null && !this.m_9236_().isClientSide() && this.m_9236_().getGameTime() % 100L == 0L) {
            if (!this.arenaBB.isInside(this.m_20183_())) {
                BlockPos newPos = this.arenaBB.getCenter();
                this.m_146884_(new Vec3((double) newPos.m_123341_(), (double) newPos.m_123342_(), (double) newPos.m_123343_()));
            }
            this.threat.forEach((id, threat) -> {
                Entity e = this.m_9236_().getEntity(id);
                if (e != null && e instanceof LivingEntity && e.isAlive() && !this.arenaBB.isInside(e.blockPosition())) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            });
        }
        if (this.m_9236_().isClientSide()) {
            this.spawnParticles();
        } else if (this.m_9236_().getGameTime() % 300L == 0L) {
            this.m_9236_().getEntities(this, this.m_20191_().inflate(32.0), e -> e.isAlive() && e instanceof LivingEntity && !this.threat.isOn((LivingEntity) e) && this.m_21574_().hasLineOfSight(e) && !SummonUtils.isTargetFriendly(e, this)).stream().map(e -> (LivingEntity) e).forEach(e -> this.threat.addThreat(e, 1.0F, this.getTarget()));
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return !Arrays.asList(EffectInit.REDUCE.get(), EffectInit.ENLARGE.get(), EffectInit.CHOOSING_WELLSPRING.get(), EffectInit.LIFT.get(), EffectInit.SILENCE.get(), EffectInit.CIRCLE_OF_POWER.get(), EffectInit.COLD_DARK.get(), EffectInit.MIND_CONTROL.get(), EffectInit.POSSESSION.get()).contains(pPotioneffect.getEffect());
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void setCustomName(@Nullable Component p_200203_1_) {
        super.m_6593_(p_200203_1_);
        this.getBossEvent().setName(this.m_5446_());
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        if (this.doesRegenerate() && this.f_19797_ % this.getPassiveHealRate() == 0) {
            this.m_5634_(this.getPassiveHealAmount());
        }
        this.getBossEvent().setProgress(this.m_21223_() / this.m_21233_());
    }

    @Override
    public void makeStuckInBlock(BlockState p_213295_1_, Vec3 p_213295_2_) {
    }

    @Override
    public void startSeenByPlayer(ServerPlayer p_184178_1_) {
        super.m_6457_(p_184178_1_);
        if (this.f_19804_.get(DATA_SHOW_BOSS_BAR)) {
            this.getBossEvent().addPlayer(p_184178_1_);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer p_184203_1_) {
        super.m_6452_(p_184203_1_);
        this.getBossEvent().removePlayer(p_184203_1_);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_ID_INV, 0);
        this.f_19804_.define(DATA_STATE, this.defaultState());
        this.f_19804_.define(DATA_SHOW_BOSS_BAR, true);
        this.f_19804_.define(DATA_ID_TARGET, -1);
    }

    @Override
    public LivingEntity getTarget() {
        if (this.target == null) {
            int targetID = this.f_19804_.get(DATA_ID_TARGET);
            if (targetID != -1) {
                Entity e = this.m_9236_().getEntity(targetID);
                if (e instanceof LivingEntity) {
                    this.target = (LivingEntity) e;
                }
            }
        }
        return this.target;
    }

    @Override
    public void setTarget(LivingEntity pLivingEntity) {
        if (pLivingEntity == null) {
            this.f_19804_.set(DATA_ID_TARGET, -1);
        } else {
            this.f_19804_.set(DATA_ID_TARGET, pLivingEntity.m_19879_());
        }
        this.target = pLivingEntity;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.m_7380_(nbt);
        nbt.putInt("Invul", this.getInvulnerableTicks());
        nbt.putInt("state", this.f_19804_.get(DATA_STATE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.m_7378_(nbt);
        this.setInvulnerableTicks(nbt.getInt("Invul"));
        if (this.m_8077_()) {
            this.getBossEvent().setName(this.m_5446_());
        }
        this.f_19804_.set(DATA_STATE, nbt.getInt("state"));
    }

    @Override
    public boolean hurt(DamageSource type, float amount) {
        if (this.getInvulnerableTicks() > 0 && !type.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return false;
        } else if (!this.isDamageCheaty(type) && !this.isSourceTooFar(type)) {
            boolean success = super.m_6469_(type, amount);
            if (success && type.getEntity() != null && type.getEntity() instanceof LivingEntity && type.getEntity().getId() != this.m_19879_()) {
                this.threat.addThreat((LivingEntity) type.getEntity(), amount, this.getTarget());
            }
            return success;
        } else {
            this.teleportToTarget(type);
            return false;
        }
    }

    @Override
    public void die(DamageSource pCause) {
        super.m_6667_(pCause);
        if (pCause.getEntity() != null && pCause.getEntity() instanceof LivingEntity) {
            boolean validKill = pCause.getEntity() instanceof Player;
            if (SummonUtils.isSummon(pCause.getEntity()) && SummonUtils.getSummoner((LivingEntity) pCause.getEntity()) instanceof Player) {
                validKill = true;
            }
            if (validKill) {
                this.threat.players(this.m_9236_()).forEach(p -> {
                    if (p instanceof ServerPlayer) {
                        ResourceLocation typeName = ForgeRegistries.ENTITY_TYPES.getKey(this.m_6095_());
                        ResourceLocation killStat = new ResourceLocation(typeName.toString() + "_kills");
                        p.awardStat(killStat, 1);
                        int totalKills = ((ServerPlayer) p).getStats().m_13015_(Stats.CUSTOM.get(killStat));
                        CustomAdvancementTriggers.DEFEAT_BOSS.trigger((ServerPlayer) p, typeName, totalKills);
                    }
                });
                this.killedByPlayer = true;
            }
        }
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.m_9236_() instanceof ServerLevel && this.getArenaStructureID() != null) {
            this.arenaBB = StructureUtils.getStructureBoundingBoxAt((ServerLevel) this.m_9236_(), this.m_20183_(), this.getArenaStructureID(), this.getArenaStructureSegment());
        }
    }

    protected int defaultState() {
        return 0;
    }

    protected boolean isDamageCheaty(DamageSource type) {
        return type.is(DamageTypes.CRAMMING) || type.is(DamageTypes.LAVA) || type.is(DamageTypes.IN_WALL) || type.is(DamageTypes.FALLING_ANVIL);
    }

    protected boolean isSourceTooFar(DamageSource type) {
        Entity e = type.getEntity();
        return e == null ? false : this.m_20182_().distanceToSqr(e.position()) > 1024.0;
    }

    protected abstract ServerBossEvent getBossEvent();

    protected void setCooldown(int id, int ticks) {
        this.cooldowns.put(id, ticks);
    }

    protected boolean isOnCooldown(int id) {
        return (Integer) this.cooldowns.getOrDefault(id, 0) > 0;
    }

    protected float getPassiveHealAmount() {
        return 1.0F;
    }

    protected int getPassiveHealRate() {
        return 40;
    }

    protected boolean doesRegenerate() {
        return this.getInvulnerableTicks() > 0 ? false : this.getPassiveHealRate() > 0 && this.getPassiveHealAmount() > 0.0F;
    }

    protected void setState(int... flags) {
        int finalFlag = 0;
        for (int i : flags) {
            finalFlag |= i;
        }
        this.f_19804_.set(DATA_STATE, finalFlag);
    }

    protected void clearState(int... flags) {
        int finalFlag = this.f_19804_.get(DATA_STATE);
        for (int i : flags) {
            finalFlag &= ~i;
        }
        this.f_19804_.set(DATA_STATE, finalFlag);
    }

    protected void clearState() {
        this.f_19804_.set(DATA_STATE, 0);
    }

    protected boolean flagSet(int flag) {
        int data = this.f_19804_.get(DATA_STATE);
        return (data & flag) != 0;
    }

    protected int getStateFlag() {
        return this.f_19804_.get(DATA_STATE);
    }

    protected void onFlagChanged(int newFlags) {
    }

    protected void showBossBar() {
        this.f_19804_.set(DATA_SHOW_BOSS_BAR, true);
        this.getBossEvent().setVisible(true);
    }

    protected void hideBossBar() {
        this.f_19804_.set(DATA_SHOW_BOSS_BAR, false);
        this.getBossEvent().setVisible(false);
    }

    protected boolean shouldShowBossBar() {
        return this.f_19804_.get(DATA_SHOW_BOSS_BAR);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> param) {
        super.m_7350_(param);
        if (param == DATA_STATE) {
            this.onFlagChanged(this.f_19804_.get(DATA_STATE));
        } else if (param == DATA_ID_TARGET) {
            this.target = null;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<BossMonster<?>> controller = new AnimationController<>(this, state -> this.handleAnimState(state));
        this.addControllerListeners(controller);
        registrar.add(controller);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    protected void addControllerListeners(AnimationController<BossMonster<?>> controller) {
        controller.setSoundKeyframeHandler(this);
        controller.setCustomInstructionKeyframeHandler(this);
        controller.transitionLength(5);
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<BossMonster<?>> event) {
    }

    @Override
    public void handle(SoundKeyframeEvent<BossMonster<?>> event) {
    }

    protected abstract PlayState handleAnimState(AnimationState<? extends BossMonster<?>> var1);

    protected void teleportToTarget(DamageSource type) {
        Entity e = type.getEntity();
        if (e != null) {
            if (e instanceof LivingEntity) {
                this.threat.initializeThreat((LivingEntity) e);
            }
            this.m_6021_(e.getX(), e.getY(), e.getZ());
        }
    }

    @Nullable
    protected Player getRandomNearbyPlayer(Predicate<Player> predicate) {
        List<Player> nearbyPlayers = this.m_9236_().m_45955_(TARGETING_CONDITIONS, this, this.m_20191_().inflate(20.0));
        MutableObject<Player> selection = new MutableObject(null);
        if (predicate != null) {
            CollectionUtils.getRandom((Collection<T>) nearbyPlayers.stream().filter(p -> predicate.test(p)).collect(Collectors.toList())).ifPresent(p -> selection.setValue(p));
        }
        if (selection.getValue() == null) {
            CollectionUtils.getRandom((Collection<T>) nearbyPlayers).ifPresent(p -> selection.setValue(p));
        }
        return (Player) selection.getValue();
    }

    public int getInvulnerableTicks() {
        return this.f_19804_.get(DATA_ID_INV);
    }

    public void setInvulnerableTicks(int ticks) {
        this.f_19804_.set(DATA_ID_INV, ticks);
    }

    public abstract void setupSpawn();

    @Nullable
    protected LivingEntity getRandomThreatTableTarget(double distance) {
        return this.getRandomThreatTableTarget(distance, null);
    }

    @Nullable
    protected LivingEntity getRandomThreatTableTarget(double distance, @Nullable Predicate<LivingEntity> selectionPredicate) {
        int entityID = this.threat.getRandomThreatEntry(this.m_9236_(), this, distance, selectionPredicate);
        if (entityID == -1) {
            return this.getTarget();
        } else {
            Entity e = this.m_9236_().getEntity(entityID);
            return e != null && e instanceof LivingEntity && this.m_21574_().hasLineOfSight(e) ? (LivingEntity) e : this.getTarget();
        }
    }

    protected void spawnParticles() {
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

    @Nullable
    public abstract ResourceLocation getArenaStructureID();

    public abstract int getArenaStructureSegment();

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public class AdjustableSpeedMeleeAttackGoal extends Goal {

        protected int attackRate;

        protected float reachMultiplier = 1.0F;

        protected final PathfinderMob mob;

        private final double speedModifier;

        private final boolean followingTargetEvenIfNotSeen;

        private Path path;

        private double pathedTargetX;

        private double pathedTargetY;

        private double pathedTargetZ;

        private int ticksUntilNextPathRecalculation;

        private int ticksUntilNextAttack;

        private long lastCanUseCheck;

        private int failedPathFindingPenalty = 0;

        private boolean canPenalize = true;

        public AdjustableSpeedMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, int attackRate) {
            this.mob = pMob;
            this.attackRate = attackRate;
            this.speedModifier = pSpeedModifier;
            this.followingTargetEvenIfNotSeen = pFollowingTargetEvenIfNotSeen;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public BossMonster<T>.AdjustableSpeedMeleeAttackGoal setReachMultiplier(float multiplier) {
            this.reachMultiplier = multiplier;
            return this;
        }

        @Override
        public boolean canUse() {
            long i = this.mob.m_9236_().getGameTime();
            if (i - this.lastCanUseCheck < 20L) {
                return false;
            } else {
                this.lastCanUseCheck = i;
                LivingEntity livingentity = this.mob.m_5448_();
                if (livingentity == null) {
                    return false;
                } else if (!livingentity.isAlive()) {
                    return false;
                } else if (this.canPenalize) {
                    if (--this.ticksUntilNextPathRecalculation <= 0) {
                        this.path = this.mob.m_21573_().createPath(livingentity, 0);
                        this.ticksUntilNextPathRecalculation = 4 + this.mob.m_217043_().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                } else {
                    this.path = this.mob.m_21573_().createPath(livingentity, 0);
                    return this.path != null ? true : this.getAttackReachSqr(livingentity) >= this.mob.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.mob.m_5448_();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!this.followingTargetEvenIfNotSeen) {
                return !this.mob.m_21573_().isDone();
            } else {
                return !this.mob.m_21444_(livingentity.m_20183_()) ? false : !(livingentity instanceof Player) || !livingentity.m_5833_() && !((Player) livingentity).isCreative();
            }
        }

        @Override
        public void start() {
            this.mob.m_21573_().moveTo(this.path, this.speedModifier);
            this.mob.m_21561_(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingentity = this.mob.m_5448_();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                this.mob.m_6710_((LivingEntity) null);
            }
            this.mob.m_21561_(false);
            this.mob.m_21573_().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingentity = this.mob.m_5448_();
            if (livingentity != null) {
                this.mob.m_21563_().setLookAt(livingentity, 30.0F, 30.0F);
                double d0 = this.mob.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
                this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
                if ((this.followingTargetEvenIfNotSeen || this.mob.m_21574_().hasLineOfSight(livingentity)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || livingentity.m_20275_(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.m_217043_().nextFloat() < 0.05F)) {
                    this.pathedTargetX = livingentity.m_20185_();
                    this.pathedTargetY = livingentity.m_20186_();
                    this.pathedTargetZ = livingentity.m_20189_();
                    this.ticksUntilNextPathRecalculation = 4 + this.mob.m_217043_().nextInt(7);
                    if (this.canPenalize) {
                        this.ticksUntilNextPathRecalculation = this.ticksUntilNextPathRecalculation + this.failedPathFindingPenalty;
                        if (this.mob.m_21573_().getPath() != null) {
                            Node finalPathPoint = this.mob.m_21573_().getPath().getEndNode();
                            if (finalPathPoint != null && livingentity.m_20275_((double) finalPathPoint.x, (double) finalPathPoint.y, (double) finalPathPoint.z) < 1.0) {
                                this.failedPathFindingPenalty = 0;
                            } else {
                                this.failedPathFindingPenalty += 10;
                            }
                        } else {
                            this.failedPathFindingPenalty += 10;
                        }
                    }
                    if (d0 > 1024.0) {
                        this.ticksUntilNextPathRecalculation += 10;
                    } else if (d0 > 256.0) {
                        this.ticksUntilNextPathRecalculation += 5;
                    }
                    if (!this.mob.m_21573_().moveTo(livingentity, this.speedModifier)) {
                        this.ticksUntilNextPathRecalculation += 15;
                    }
                    this.ticksUntilNextPathRecalculation = this.m_183277_(this.ticksUntilNextPathRecalculation);
                }
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
                this.checkAndPerformAttack(livingentity, d0);
            }
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.m_6674_(InteractionHand.MAIN_HAND);
                this.mob.m_7327_(pEnemy);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.m_183277_(this.attackRate);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        protected int getAttackInterval() {
            return this.m_183277_(this.attackRate);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return (double) ((this.mob.m_20205_() * 2.0F * this.mob.m_20205_() * 2.0F + pAttackTarget.m_20205_()) * this.reachMultiplier);
        }
    }

    public class CastSpellAtTargetGoal extends Goal {

        private final double moveSpeedAmp;

        private final float maxAttackDistance;

        private final float minAttackDistance;

        private final int attackCooldown;

        private int seeTime;

        private boolean strafingClockwise;

        private boolean strafingBackwards;

        private int strafingTime = -1;

        private ItemStack spell;

        private final int actionId;

        private Predicate<BossMonster<?>> additionalUsePredicate;

        private Predicate<LivingEntity> targetPredicate;

        private boolean interruptable = true;

        private int maximumChannelTime = -1;

        private final boolean randomThreatTarget;

        private LivingEntity target;

        private boolean isWaitingOnAnim = false;

        private final int effectDelayTicks;

        private final int resetTicks;

        private final Runnable startCallback;

        private final Runnable resetCallback;

        private final Predicate<BossMonster<?>> precastCallback;

        public CastSpellAtTargetGoal(ItemStack spell, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn, float minAttackDistanceIn, boolean randomTargetFromThreatTable, int cooldownId, int delayTicks, int resetTicks, Runnable startCallback, Runnable resetCallback, Predicate<BossMonster<?>> precastCallback) {
            this.moveSpeedAmp = moveSpeedAmpIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.minAttackDistance = minAttackDistanceIn * minAttackDistanceIn;
            this.spell = spell;
            this.actionId = cooldownId;
            this.attackCooldown = attackCooldownIn;
            this.effectDelayTicks = delayTicks;
            this.resetTicks = resetTicks;
            this.resetCallback = resetCallback;
            this.startCallback = startCallback;
            this.precastCallback = precastCallback;
            this.randomThreatTarget = randomTargetFromThreatTable;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public BossMonster<T>.CastSpellAtTargetGoal setUsePredicate(Predicate<BossMonster<?>> predicate) {
            this.additionalUsePredicate = predicate;
            return this;
        }

        public BossMonster<T>.CastSpellAtTargetGoal setTargetPredicate(Predicate<LivingEntity> targetPredicate) {
            this.targetPredicate = targetPredicate;
            return this;
        }

        public BossMonster<T>.CastSpellAtTargetGoal setUninterruptable() {
            this.interruptable = false;
            return this;
        }

        public BossMonster<T>.CastSpellAtTargetGoal setChannelTime(int ticks) {
            this.maximumChannelTime = ticks;
            return this;
        }

        @Override
        public boolean isInterruptable() {
            return this.interruptable;
        }

        @Override
        public boolean canUse() {
            boolean hasTarget = BossMonster.this.getTarget() != null;
            boolean notOnCooldown = !BossMonster.this.isOnCooldown(this.actionId) && !BossMonster.this.isOnCooldown(1);
            boolean closeEnough = hasTarget && BossMonster.this.getTarget().m_20280_(BossMonster.this) >= (double) this.minAttackDistance;
            return notOnCooldown && closeEnough && (this.additionalUsePredicate == null || this.additionalUsePredicate.test(BossMonster.this));
        }

        @Override
        public boolean canContinueToUse() {
            if (this.target == null || this.target.isDeadOrDying() || this.target.m_20182_().distanceToSqr(BossMonster.this.m_20182_()) > BossMonster.this.m_21133_(Attributes.FOLLOW_RANGE)) {
                return false;
            } else if (BossMonster.this.m_6117_()) {
                return BossMonster.this.m_21212_() > 0;
            } else if (this.isWaitingOnAnim) {
                return true;
            } else {
                return !BossMonster.this.isOnCooldown(this.actionId) ? false : this.canUse() || !BossMonster.this.m_21573_().isDone();
            }
        }

        @Override
        public void start() {
            super.start();
            this.target = null;
            if (this.randomThreatTarget) {
                for (int i = 0; i < 5; i++) {
                    this.target = BossMonster.this.getRandomThreatTableTarget(BossMonster.this.m_21133_(Attributes.FOLLOW_RANGE), this.targetPredicate);
                }
            }
            if (this.target == null) {
                this.target = BossMonster.this.getTarget();
            }
            if (this.startCallback != null) {
                this.startCallback.run();
            }
        }

        @Override
        public void stop() {
            super.stop();
            this.target = null;
            BossMonster.this.setCooldown(this.actionId, this.attackCooldown);
            this.seeTime = 0;
            this.isWaitingOnAnim = false;
            BossMonster.this.m_5810_();
        }

        @Override
        public void tick() {
            if (this.target == null) {
                this.stop();
            } else if (!this.isWaitingOnAnim && (this.maximumChannelTime <= -1 || !BossMonster.this.m_6117_())) {
                LivingEntity livingentity = BossMonster.this.getTarget();
                if (livingentity != null) {
                    double d0 = BossMonster.this.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
                    boolean canSee = BossMonster.this.m_21574_().hasLineOfSight(livingentity);
                    boolean positiveSeeTime = this.seeTime > 0;
                    if (canSee != positiveSeeTime) {
                        this.seeTime = 0;
                    }
                    if (canSee) {
                        this.seeTime++;
                    } else {
                        this.seeTime--;
                    }
                    if (!(d0 > (double) this.maxAttackDistance) && this.seeTime >= 20) {
                        BossMonster.this.m_21573_().stop();
                        this.strafingTime++;
                    } else {
                        BossMonster.this.m_21573_().moveTo(livingentity, this.moveSpeedAmp);
                        this.strafingTime = -1;
                    }
                    if (this.strafingTime >= 20) {
                        if ((double) BossMonster.this.m_217043_().nextFloat() < 0.3) {
                            this.strafingClockwise = !this.strafingClockwise;
                        }
                        if ((double) BossMonster.this.m_217043_().nextFloat() < 0.3) {
                            this.strafingBackwards = !this.strafingBackwards;
                        }
                        this.strafingTime = 0;
                    }
                    if (this.strafingTime > -1) {
                        if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
                            this.strafingBackwards = false;
                        } else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
                            this.strafingBackwards = true;
                        }
                        BossMonster.this.m_21566_().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                        BossMonster.this.m_21391_(livingentity, 30.0F, 30.0F);
                    } else {
                        BossMonster.this.m_21563_().setLookAt(livingentity, 30.0F, 30.0F);
                    }
                    if (!canSee && this.seeTime < -60) {
                        this.stop();
                    } else if (canSee) {
                        LivingEntity cachedTarget = this.target;
                        boolean runDelay = true;
                        if (this.precastCallback != null) {
                            runDelay = this.precastCallback.test(BossMonster.this);
                        }
                        BossMonster.this.f_21344_.stop();
                        if (runDelay) {
                            BossMonster.this.setTimer("cast", this.effectDelayTicks, () -> {
                                SpellRecipe recipe = SpellRecipe.fromNBT(this.spell.getTag());
                                if (recipe.isValid() && cachedTarget != null) {
                                    Vec3 origin = BossMonster.this.m_20182_().add(0.0, (double) BossMonster.this.m_20192_(), 0.0);
                                    Vec3 targetPos = cachedTarget.m_20182_().add(0.0, (double) cachedTarget.m_20192_(), 0.0);
                                    Vec3 targetVelocity = cachedTarget.m_20182_().subtract(cachedTarget.f_19790_, cachedTarget.f_19791_, cachedTarget.f_19792_);
                                    targetVelocity = targetVelocity.subtract(0.0, cachedTarget.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).getValue(), 0.0);
                                    if (cachedTarget.m_20096_()) {
                                        targetVelocity = targetVelocity.add(0.0, -targetVelocity.y, 0.0);
                                    }
                                    double distance = origin.distanceTo(targetPos);
                                    double speed = (double) recipe.getShape().getValue(Attribute.SPEED);
                                    int ticksToArrive = (int) Math.round(distance / speed);
                                    if (recipe.isChanneled()) {
                                        BossMonster.this.m_21008_(InteractionHand.MAIN_HAND, this.spell.copy());
                                        BossMonster.this.m_6672_(InteractionHand.MAIN_HAND);
                                    }
                                    Vec3 adjustedTargetPos = targetPos.add(targetVelocity.scale((double) ticksToArrive));
                                    Vec3 delta = adjustedTargetPos.subtract(origin).normalize();
                                    SpellTarget targetHint = new SpellTarget(cachedTarget);
                                    SpellCaster.Affect(this.spell, SpellRecipe.fromNBT(this.spell.getTag()), BossMonster.this.m_9236_(), new SpellSource(BossMonster.this, InteractionHand.MAIN_HAND, origin, delta), targetHint);
                                }
                            });
                        }
                        BossMonster.this.setTimer("reset", this.resetTicks, () -> {
                            this.isWaitingOnAnim = false;
                            if (this.resetCallback != null) {
                                this.resetCallback.run();
                            }
                        });
                        this.isWaitingOnAnim = true;
                    }
                }
            } else {
                BossMonster.this.f_21344_.stop();
                if (this.target != null) {
                    BossMonster.this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.target.m_20182_());
                }
            }
        }
    }

    class DoNothingGoal extends Goal {

        private final int actionId;

        public DoNothingGoal() {
            this(-1);
        }

        public DoNothingGoal(int actionId) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
            this.actionId = actionId;
        }

        @Override
        public boolean canUse() {
            return this.actionId == -1 ? BossMonster.this.getInvulnerableTicks() > 0 : BossMonster.this.flagSet(this.actionId);
        }
    }

    public class ThreatTableHurtByTargetGoal extends TargetGoal {

        private boolean alertSameType;

        private int timestamp;

        private final Class<?>[] toIgnoreDamage;

        private Class<?>[] toIgnoreAlert;

        public ThreatTableHurtByTargetGoal(PathfinderMob owner, Class<?>... ignoreDamageFrom) {
            super(owner, true);
            this.toIgnoreDamage = ignoreDamageFrom;
            this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            int i = this.f_26135_.m_21213_();
            LivingEntity livingentity = this.f_26135_.m_21188_();
            if (i != this.timestamp && livingentity != null) {
                if (livingentity.m_6095_() == EntityType.PLAYER && this.f_26135_.m_9236_().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                    return false;
                }
                for (Class<?> oclass : this.toIgnoreDamage) {
                    if (oclass.isAssignableFrom(livingentity.getClass())) {
                        return false;
                    }
                }
                if (this.m_26150_(livingentity, BossMonster.HURT_BY_TARGETING)) {
                    return BossMonster.this.threat.shouldSwitchTarget(BossMonster.this.getTarget(), livingentity);
                }
            }
            return false;
        }

        public BossMonster<T>.ThreatTableHurtByTargetGoal setAlertOthers(Class<?>... p_220794_1_) {
            this.alertSameType = true;
            this.toIgnoreAlert = p_220794_1_;
            return this;
        }

        @Override
        public void start() {
            this.f_26135_.setTarget(this.f_26135_.m_21188_());
            this.f_26137_ = this.f_26135_.getTarget();
            this.timestamp = this.f_26135_.m_21213_();
            this.f_26138_ = 300;
            if (this.alertSameType) {
                this.alertOthers();
            }
            super.start();
        }

        protected void alertOthers() {
            double d0 = this.m_7623_();
            AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.f_26135_.m_20182_()).inflate(d0, 10.0, d0);
            label56: for (Mob mobentity : this.f_26135_.m_9236_().m_45976_(this.f_26135_.getClass(), axisalignedbb)) {
                if (this.f_26135_ != mobentity && mobentity.getTarget() == null && (!(this.f_26135_ instanceof TamableAnimal) || ((TamableAnimal) this.f_26135_).m_269323_() == ((TamableAnimal) mobentity).m_269323_()) && !mobentity.m_7307_(this.f_26135_.m_21188_())) {
                    if (this.toIgnoreAlert != null) {
                        boolean flag = false;
                        Class[] var8 = this.toIgnoreAlert;
                        int var9 = var8.length;
                        int var10 = 0;
                        while (true) {
                            if (var10 < var9) {
                                Class<?> oclass = var8[var10];
                                if (mobentity.getClass() != oclass) {
                                    var10++;
                                    continue;
                                }
                                flag = true;
                            }
                            if (!flag) {
                                break;
                            }
                            continue label56;
                        }
                    }
                    this.alertOther(mobentity, this.f_26135_.m_21188_());
                }
            }
        }

        protected void alertOther(Mob other, LivingEntity target) {
            other.setTarget(target);
        }
    }
}