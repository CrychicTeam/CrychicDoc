package com.mna.entities.faction;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.entities.faction.util.WitchHunterArrow;
import com.mna.entities.projectile.WitchhunterTrickshot;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.entities.sorcery.targeting.Smite;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;

public class WitchHunter extends BaseFactionMob<WitchHunter> implements RangedAttackMob, AnimationController.CustomKeyframeHandler<WitchHunter> {

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_TRICKSHOT = "trickshot";

    private static final int TRICKSHOT_CD = 200;

    private static final int ROLL_CD = 60;

    private boolean isAttacking = false;

    private boolean isTrickshotting = false;

    private boolean isRolling = false;

    private int trickshotCooldown = 0;

    private HashMap<String, Integer> trickshotCooldowns;

    private int rollCooldown = 0;

    private int nextTrickshotDuration = 200;

    private int nextTrickshotMagnitude = 200;

    private MobEffect nextTrickshotEffect = null;

    private boolean instantTransition = false;

    public WitchHunter(EntityType<WitchHunter> type, Level worldIn) {
        super(type, worldIn);
        this.trickshotCooldowns = new HashMap();
    }

    @Override
    public void tick() {
        super.tick();
        for (String e : this.trickshotCooldowns.keySet()) {
            this.trickshotCooldowns.put(e, (Integer) this.trickshotCooldowns.get(e) - 1);
        }
        this.trickshotCooldown--;
        this.rollCooldown--;
    }

    @Override
    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    @Override
    protected void addControllerListeners(AnimationController<WitchHunter> controller) {
        controller.setCustomInstructionKeyframeHandler(this);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        if (!this.isAttacking) {
            return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.run")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.idle"));
        } else if (this.isTrickshotting) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.throw"));
        } else {
            return this.isRolling ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.roll_short").thenLoop("animation.model.idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.shoot"));
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new WitchHunter.DodgeGoal());
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(2, this, new WitchHunter.TrickshotGoal(EffectInit.SILENCE.get(), 100, 0, 8.0F, 16.0F)));
        this.f_21345_.addGoal(2, new FactionTierWrapperGoal(1, this, new WitchHunter.TrickshotGoal(EffectInit.MANA_STUNT.get(), 400, 4, 8.0F, 16.0F)));
        this.f_21345_.addGoal(2, new FactionTierWrapperGoal(1, this, new WitchHunter.TrickshotGoal(MobEffects.HARM, 1, 1, 8.0F, 16.0F)));
        this.f_21345_.addGoal(3, new FactionTierWrapperGoal(0, this, new WitchHunter.TrickshotGoal(MobEffects.MOVEMENT_SLOWDOWN, 160, 3, 1.0F, 16.0F)));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, Player.class, p -> {
            if (p instanceof Player) {
                IPlayerProgression progression = (IPlayerProgression) ((Player) p).getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (progression != null && (progression.getAlliedFaction() == Factions.COUNCIL || progression.getAlliedFaction() == null)) {
                    return false;
                }
            }
            return true;
        }, 6.0F, this.m_21133_(Attributes.MOVEMENT_SPEED), this.m_21133_(Attributes.MOVEMENT_SPEED) * 1.5, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, LivingEntity.class, p -> p instanceof IFactionEnemy && !SummonUtils.isSummon(this) ? ((IFactionEnemy) p).getFaction() != Factions.COUNCIL : false, 6.0F, this.m_21133_(Attributes.MOVEMENT_SPEED), this.m_21133_(Attributes.MOVEMENT_SPEED) * 1.5, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        this.f_21345_.addGoal(4, new WitchHunter.ShootGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), 20, 30, 12.0F));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Witch.class, 10, true, false, e -> true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isAttacking) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 10, target, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", this::handleDelayCallback));
            this.isAttacking = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    public boolean spawnBolt(Entity target) {
        Vec3 direction = target == this ? Vec3.directionFromRotation(this.m_20155_()).normalize() : target.position().add(0.0, (double) (target.getEyeHeight() / 2.0F), 0.0).subtract(this.m_20182_()).normalize();
        WitchHunterArrow arrow = new WitchHunterArrow(this.m_9236_(), this.m_20185_() + direction.x, this.m_20186_() + 1.0 + direction.y, this.m_20189_() + direction.z);
        arrow.m_5602_(this);
        arrow.m_6686_(direction.x, direction.y, direction.z, 1.6F, 0.0F);
        this.m_9236_().m_7967_(arrow);
        this.m_5496_(SoundEvents.CROSSBOW_SHOOT, 1.0F, (float) (0.9 + Math.random() * 0.2));
        return true;
    }

    public void spawnTrickshot(MobEffect effect, int duration, int magnitude) {
        if (this.m_5448_() != null) {
            WitchhunterTrickshot trick = new WitchhunterTrickshot(this.m_9236_(), this.m_20182_().add(0.0, 1.0, 0.0), this.m_5448_().m_20182_().add(0.0, 3.0, 0.0), effect, duration, magnitude);
            this.m_9236_().m_7967_(trick);
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.55F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 20.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    private void handleDelayCallback(String identifier, LivingEntity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            switch(identifier) {
                case "damage":
                    this.spawnBolt(entity);
                    break;
                case "trickshot":
                    this.spawnTrickshot(this.nextTrickshotEffect, this.nextTrickshotDuration, this.nextTrickshotMagnitude);
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isAttacking = false;
            if (this.isTrickshotting) {
                this.isTrickshotting = false;
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(data));
                if (effect != null) {
                    this.trickshotCooldowns.put(data, 600);
                }
                this.trickshotCooldown = 200;
            } else if (this.isRolling) {
                this.isRolling = false;
                this.rollCooldown = 60;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<WitchHunter> event) {
        if (event.getKeyframeData().getInstructions().contains("sound:woosh")) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Entity.Generic.WOOSH, SoundSource.HOSTILE, 0.05F, (float) (0.9 + Math.random() * 0.2), false);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isAttacking);
        nbt.putBoolean("trickshot", this.isTrickshotting);
        nbt.putBoolean("roll", this.isRolling);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isAttacking = nbt.getBoolean("attacking");
        this.isTrickshotting = nbt.getBoolean("trickshot");
        boolean wasRolling = this.isRolling;
        this.isRolling = nbt.getBoolean("roll");
        if (!this.isRolling && wasRolling) {
            this.instantTransition = true;
        }
    }

    public class DodgeGoal extends Goal {

        private Vec3 dodgeDirection;

        private Entity dodgeEntity;

        public DodgeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            if (WitchHunter.this.rollCooldown > 0) {
                return false;
            } else {
                Vec3 rt_startVec = WitchHunter.this.m_20182_();
                Vec3 thisFwd = Vec3.directionFromRotation(WitchHunter.this.m_20155_());
                Vec3 rt_endVec = rt_startVec.add(thisFwd.scale(10.0));
                List<Entity> potentials = (List<Entity>) WitchHunter.this.m_9236_().m_45933_(WitchHunter.this, WitchHunter.this.m_20191_().inflate(10.0)).stream().filter(e -> e instanceof SpellProjectile || e instanceof Smite || e instanceof ChanneledSpellEntity).filter(e -> {
                    AABB axisalignedbb = e.getBoundingBox().inflate(2.0);
                    Optional<Vec3> optional = axisalignedbb.clip(rt_startVec, rt_endVec);
                    return optional.isPresent();
                }).collect(Collectors.toList());
                if (potentials.size() == 0) {
                    return false;
                } else {
                    potentials.sort(new Comparator<Entity>() {

                        public int compare(Entity o1, Entity o2) {
                            Double d1 = o1.distanceToSqr(WitchHunter.this);
                            Double d2 = o2.distanceToSqr(WitchHunter.this);
                            return d1.compareTo(d2);
                        }
                    });
                    this.dodgeEntity = (Entity) potentials.get(0);
                    Vec3 fwd = this.dodgeEntity.getDeltaMovement();
                    this.dodgeDirection = new Vec3(fwd.x, 0.0, fwd.z).cross(new Vec3(0.0, 1.0, 0.0)).normalize();
                    float angle = (float) (Math.acos(this.dodgeDirection.dot(thisFwd.normalize())) * 180.0 / Math.PI);
                    if (angle > 90.0F) {
                        this.dodgeDirection.scale(-1.0);
                    }
                    return true;
                }
            }
        }

        @Override
        public void start() {
            WitchHunter.this.m_21573_().stop();
            WitchHunter.this.isRolling = true;
            WitchHunter.this.isAttacking = true;
            WitchHunter.this.m_21563_().setLookAt(this.dodgeDirection.x, this.dodgeDirection.y, this.dodgeDirection.z, 30.0F, 30.0F);
            WitchHunter.this.m_20256_(this.dodgeDirection);
            DelayedEventQueue.pushEvent(WitchHunter.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 80, "", WitchHunter.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(WitchHunter.this);
        }

        @Override
        public void tick() {
            WitchHunter.this.m_21563_().setLookAt(this.dodgeDirection.x, this.dodgeDirection.y, this.dodgeDirection.z, 30.0F, 30.0F);
        }

        @Override
        public boolean canContinueToUse() {
            return WitchHunter.this.isRolling;
        }

        protected EntityHitResult rayTraceEntity(Vec3 startVec, Vec3 endVec, float size, Entity searchEntity) {
            return ProjectileUtil.getEntityHitResult(WitchHunter.this.m_9236_(), WitchHunter.this, startVec, endVec, WitchHunter.this.m_20191_().inflate((double) size), entity -> searchEntity == entity);
        }
    }

    public class ShootGoal extends Goal {

        private int rangedAttackTime = -1;

        private final int attackIntervalMin;

        private final int maxRangedAttackTime;

        private final float attackRadius;

        private final float maxAttackDistance;

        public ShootGoal(RangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
            this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
        }

        public ShootGoal(RangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
            this.attackIntervalMin = p_i1650_4_;
            this.maxRangedAttackTime = maxAttackTime;
            this.attackRadius = maxAttackDistanceIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = WitchHunter.this.m_5448_();
            return livingentity != null && livingentity.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() || !WitchHunter.this.m_21573_().isStuck();
        }

        @Override
        public void stop() {
            this.rangedAttackTime = -1;
        }

        @Override
        public void tick() {
            LivingEntity e = WitchHunter.this.m_5448_();
            if (e != null) {
                double distance = WitchHunter.this.m_20275_(e.m_20185_(), e.m_20186_(), e.m_20189_());
                boolean canSeeTarget = WitchHunter.this.m_21574_().hasLineOfSight(e);
                if (distance <= (double) this.maxAttackDistance && canSeeTarget) {
                    WitchHunter.this.m_21573_().stop();
                } else {
                    WitchHunter.this.m_21573_().moveTo(e, WitchHunter.this.m_21133_(Attributes.MOVEMENT_SPEED));
                }
                WitchHunter.this.m_21563_().setLookAt(e, 30.0F, 30.0F);
                int tier = MathUtils.clamp(WitchHunter.this.getTier() + 1, 1, 3);
                if (--this.rangedAttackTime == 0) {
                    if (!canSeeTarget) {
                        return;
                    }
                    float f = Mth.sqrt((float) distance) / this.attackRadius;
                    float lvt_5_1_ = Mth.clamp(f, 0.1F, 1.0F);
                    WitchHunter.this.performRangedAttack(e, lvt_5_1_);
                    this.rangedAttackTime = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) / tier;
                } else if (this.rangedAttackTime < 0) {
                    float f2 = Mth.sqrt((float) distance) / this.attackRadius;
                    this.rangedAttackTime = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin) / tier;
                }
            }
        }
    }

    public class TrickshotGoal extends Goal {

        private MobEffect potionEffect;

        private int potionDuration;

        private int potionMagnitude;

        private float minDistance;

        private float maxDistance;

        private boolean startedAction;

        public TrickshotGoal(MobEffect effect, int duration, int magnitude, float minDistance, float maxDistance) {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
            this.potionEffect = effect;
            this.potionDuration = duration;
            this.potionMagnitude = magnitude;
            this.minDistance = minDistance * minDistance;
            this.maxDistance = maxDistance * maxDistance;
            this.startedAction = false;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            return WitchHunter.this.trickshotCooldown <= 0 && (Integer) WitchHunter.this.trickshotCooldowns.getOrDefault(ForgeRegistries.MOB_EFFECTS.getKey(this.potionEffect).toString(), 0) <= 0 && !WitchHunter.this.isTrickshotting && !WitchHunter.this.isRolling && WitchHunter.this.m_5448_() != null && !WitchHunter.this.m_5448_().hasEffect(this.potionEffect) && WitchHunter.this.m_20280_(WitchHunter.this.m_5448_()) >= (double) this.minDistance;
        }

        @Override
        public void start() {
            this.startedAction = false;
        }

        @Override
        public void tick() {
            LivingEntity at = WitchHunter.this.m_5448_();
            if (at != null) {
                if (!WitchHunter.this.isTrickshotting) {
                    if (this.tryMove(at)) {
                        this.startAction();
                    }
                } else {
                    WitchHunter.this.m_21563_().setLookAt(at, 30.0F, 30.0F);
                }
            }
        }

        private void startAction() {
            WitchHunter.this.m_21573_().stop();
            WitchHunter.this.isTrickshotting = true;
            WitchHunter.this.isAttacking = true;
            WitchHunter.this.nextTrickshotDuration = this.potionDuration;
            WitchHunter.this.nextTrickshotMagnitude = this.potionMagnitude;
            WitchHunter.this.nextTrickshotEffect = this.potionEffect;
            DelayedEventQueue.pushEvent(WitchHunter.this.m_9236_(), new TimedDelayedEvent<>("trickshot", 50, WitchHunter.this, WitchHunter.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(WitchHunter.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 100, ForgeRegistries.MOB_EFFECTS.getKey(this.potionEffect).toString(), WitchHunter.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(WitchHunter.this);
            this.startedAction = true;
        }

        private boolean tryMove(LivingEntity at) {
            double distance = WitchHunter.this.m_20275_(at.m_20185_(), at.m_20186_(), at.m_20189_());
            boolean canSeeTarget = WitchHunter.this.m_21574_().hasLineOfSight(at);
            if (distance <= (double) this.maxDistance && canSeeTarget) {
                WitchHunter.this.m_21573_().stop();
                return true;
            } else {
                WitchHunter.this.m_21573_().moveTo(at, WitchHunter.this.m_21133_(Attributes.MOVEMENT_SPEED));
                WitchHunter.this.m_21563_().setLookAt(at, 30.0F, 30.0F);
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.startedAction || WitchHunter.this.isTrickshotting;
        }
    }
}