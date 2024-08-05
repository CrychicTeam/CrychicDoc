package com.mna.entities.faction;

import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.ai.AIPredicateWrapperGoal;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.entities.projectile.SkeletonAssassinBolo;
import com.mna.entities.projectile.SkeletonAssassinShuriken;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.Vector3;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class SkeletonAssassin extends BaseFactionMob<SkeletonAssassin> implements RangedAttackMob {

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_BOLO = "bolo";

    private static final String INSTRUCTION_SMOKEBOMB = "smokebomb";

    private static final String INSTRUCTION_BACKFLIP = "backflip";

    private static final int COOLDOWN_BOLO = 300;

    private static final int COOLDOWN_BACKFLIP = 100;

    private static final int COOLDOWN_SMOKEBOMB = 1200;

    private boolean isActing = false;

    private boolean throwingShuriken = false;

    private boolean smokeBomb = false;

    private boolean throwingBolo = false;

    private boolean backflipping = false;

    private int boloCooldown = 0;

    private int backflipCooldown = 0;

    private int smokeBombCooldown = 0;

    private boolean instantTransition = false;

    public SkeletonAssassin(EntityType<SkeletonAssassin> type, Level worldIn) {
        super(type, worldIn);
    }

    public SkeletonAssassin(Level worldIn) {
        this(EntityInit.SKELETON_ASSASSIN.get(), worldIn);
    }

    public static boolean canSpawnPredicate(EntityType<SkeletonAssassin> p_234351_0_, LevelAccessor p_234351_1_, MobSpawnType p_234351_2_, BlockPos p_234351_3_, RandomSource p_234351_4_) {
        return !(p_234351_1_ instanceof ServerLevelAccessor) ? false : p_234351_3_.m_123342_() <= 60 && Monster.checkMonsterSpawnRules(p_234351_0_, (ServerLevelAccessor) p_234351_1_, p_234351_2_, p_234351_3_, p_234351_4_);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        if (this.instantTransition) {
            state.getController().transitionLength(0);
            this.instantTransition = false;
        } else {
            state.getController().transitionLength(5);
        }
        if (!this.isActing) {
            return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.skeleton_assassin.run")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.skeleton_assassin.idle"));
        } else if (this.throwingShuriken) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.skeleton_assassin.quick_throw"));
        } else if (this.smokeBomb) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.skeleton_assassin.smoke_bomb"));
        } else {
            return this.throwingBolo ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.skeleton_assassin.outerhand_throw")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.skeleton_assassin.slash"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.boloCooldown > 0) {
            this.boloCooldown--;
        }
        if (this.backflipCooldown > 0) {
            this.backflipCooldown--;
        }
        if (this.smokeBombCooldown > 0) {
            this.smokeBombCooldown--;
        }
        if (this.m_6084_() && this.m_21527_()) {
            this.m_20254_(8);
        }
    }

    @Override
    public IFaction getFaction() {
        return Factions.UNDEAD;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AIPredicateWrapperGoal<>(this, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false)).executionPredicate(me -> me.m_5448_() != null && me.m_5448_().isAlive() && me.m_20145_()));
        this.f_21345_.addGoal(2, new FleeSunGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED)));
        this.f_21345_.addGoal(3, new FactionTierWrapperGoal(2, this, new SkeletonAssassin.SmokeBombGoal()));
        this.f_21345_.addGoal(4, new FactionTierWrapperGoal(1, this, new SkeletonAssassin.BoloGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), 20, 15, 24.0F)));
        this.f_21345_.addGoal(5, new SkeletonAssassin.ShootGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), 20, 15, 12.0F));
        this.f_21345_.addGoal(6, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 0.35F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 12, target, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 20, "", this::handleDelayCallback));
            this.isActing = true;
            this.throwingShuriken = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    private void throwBolo(LivingEntity target, float distanceFactor) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("bolo", 17, target, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 22, "", this::handleDelayCallback));
            this.isActing = true;
            this.throwingBolo = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    private void throwSmokeBomb() {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("smokebomb", 28, this, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 30, "", this::handleDelayCallback));
            this.isActing = true;
            this.smokeBomb = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    private void doBackflip() {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("backflip", 31, this, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 32, "", this::handleDelayCallback));
            this.isActing = true;
            this.backflipping = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    public boolean spawnShuriken(Entity target) {
        Vec3 direction = target == this ? Vec3.directionFromRotation(this.m_20155_()).normalize() : target.position().add(0.0, (double) (target.getBbHeight() * 0.5F), 0.0).subtract(this.m_20182_()).normalize();
        SkeletonAssassinShuriken shuriken = new SkeletonAssassinShuriken(this.m_9236_(), this);
        shuriken.m_5602_(this);
        shuriken.m_6686_(direction.x, direction.y, direction.z, 1.6F, 0.0F);
        this.m_9236_().m_7967_(shuriken);
        this.m_5496_(SFX.Entity.SkeletonAssassin.SHURIKEN_THROW, 1.0F, (float) (0.9 + Math.random() * 0.2));
        return true;
    }

    public boolean spawnBolo(Entity target) {
        Vec3 direction = target.position().add(target.getDeltaMovement()).add(0.0, (double) (target.getBbHeight() * 0.5F), 0.0).subtract(this.m_20182_()).normalize();
        SkeletonAssassinBolo bolo = new SkeletonAssassinBolo(this.m_9236_(), this);
        bolo.m_5602_(this);
        bolo.m_6686_(direction.x, direction.y, direction.z, 2.5F, 0.0F);
        this.m_9236_().m_7967_(bolo);
        this.m_5496_(SFX.Entity.SkeletonAssassin.BOLO_THROW, 1.0F, (float) (0.9 + Math.random() * 0.2));
        return true;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.isActing) {
            return true;
        } else {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 10, entityIn, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 20, "", this::handleDelayCallback));
            this.isActing = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL)) {
            return false;
        } else {
            if (source.is(DamageTypes.FREEZE)) {
                amount *= 0.5F;
            }
            return super.hurt(source, amount);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    public boolean damageEntity(Entity entityIn) {
        if (this.m_5448_() == null) {
            return false;
        } else {
            double dist = this.m_20280_(this.m_5448_());
            if (dist > 9.0) {
                return false;
            } else {
                float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
                float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
                if (entityIn instanceof LivingEntity) {
                    f += EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entityIn).getMobType());
                    f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
                    if (this.m_20145_() || ((LivingEntity) entityIn).hasEffect(MobEffects.BLINDNESS)) {
                        if (!((LivingEntity) entityIn).hasEffect(EffectInit.ELDRIN_SIGHT.get())) {
                            f *= (float) (this.getTier() + 2);
                        }
                        this.m_21195_(MobEffects.INVISIBILITY);
                    }
                }
                int i = EnchantmentHelper.getFireAspect(this);
                if (i > 0 || this.m_6060_()) {
                    entityIn.setSecondsOnFire(i * 4);
                }
                boolean flag = entityIn.hurt(this.m_269291_().mobAttack(this), f);
                if (flag) {
                    if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                        ((LivingEntity) entityIn).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                        this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
                    }
                    if (entityIn instanceof Player playerentity) {
                        this.maybeDisableShield(playerentity, this.m_21205_(), playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
                    }
                    this.m_19970_(this, entityIn);
                    this.m_21335_(entityIn);
                }
                return flag;
            }
        }
    }

    private void maybeDisableShield(Player p_233655_1_, ItemStack p_233655_2_, ItemStack p_233655_3_) {
        if (!p_233655_2_.isEmpty() && !p_233655_3_.isEmpty() && p_233655_2_.getItem() instanceof AxeItem && p_233655_3_.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.f_19796_.nextFloat() < f) {
                p_233655_1_.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.m_9236_().broadcastEntityEvent(p_233655_1_, (byte) 30);
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (this.m_6084_()) {
            if (!this.m_9236_().isClientSide()) {
                switch(identifier) {
                    case "damage":
                        if (this.throwingShuriken) {
                            this.spawnShuriken(entity);
                        } else {
                            this.damageEntity(entity);
                        }
                        break;
                    case "bolo":
                        this.spawnBolo(entity);
                        this.boloCooldown = 300;
                        break;
                    case "smokebomb":
                        this.m_7292_(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
                        this.m_5496_(SFX.Entity.SkeletonAssassin.SMOKE_BOMB, 1.0F, (float) (0.8 + Math.random() * 0.2));
                        this.smokeBombCooldown = 1200;
                        break;
                    case "backflip":
                        this.backflipCooldown = 100;
                }
            } else {
                switch(identifier) {
                    case "smokebomb":
                        for (int i = 0; i < 50; i++) {
                            this.m_9236_().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.m_20185_() - 0.25 + Math.random() * 0.5, this.m_20186_() + Math.random() * 2.0, this.m_20189_() - 0.25 + Math.random() * 0.5, 0.0, 0.0, 0.0);
                        }
                }
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            boolean wasFlipping = this.backflipping;
            this.isActing = false;
            this.throwingShuriken = false;
            this.throwingBolo = false;
            this.smokeBomb = false;
            this.backflipping = false;
            if (!this.backflipping && wasFlipping) {
                this.instantTransition = true;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isActing);
        nbt.putBoolean("shuriken", this.throwingShuriken);
        nbt.putBoolean("bolo", this.throwingBolo);
        nbt.putBoolean("smoke", this.smokeBomb);
        nbt.putBoolean("backflip", this.backflipping);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isActing = nbt.getBoolean("attacking");
        this.throwingShuriken = nbt.getBoolean("shuriken");
        this.throwingBolo = nbt.getBoolean("bolo");
        this.smokeBomb = nbt.getBoolean("smoke");
        this.backflipping = nbt.getBoolean("backflip");
        if (this.smokeBomb) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("smokebomb", 22, this, this::handleDelayCallback));
        }
    }

    public class BackflipGoal extends Goal {

        private Vector3 start;

        private Vector3 end;

        private int tick_count;

        private float move_time = 31.0F;

        @Override
        public boolean canUse() {
            LivingEntity target = SkeletonAssassin.this.m_5448_();
            if (target != null && !SkeletonAssassin.this.isActing && SkeletonAssassin.this.backflipCooldown == 0) {
                double dist = SkeletonAssassin.this.m_20280_(target);
                if (dist > 9.0) {
                    return false;
                } else {
                    Vec3 startPos = SkeletonAssassin.this.m_20182_().add(0.0, 0.5, 0.0);
                    Vec3 delta = startPos.subtract(target.m_20182_().add(0.0, 0.5, 0.0)).normalize();
                    delta = new Vec3(delta.x, 0.0, delta.z);
                    Vec3 endPos = startPos.add(delta.scale(10.0));
                    int count;
                    for (count = 0; !SkeletonAssassin.this.m_9236_().m_46859_(BlockPos.containing(endPos)) && count < 10; endPos = endPos.add(0.0, 1.0, 0.0)) {
                        count++;
                    }
                    if (count >= 10) {
                        return false;
                    } else {
                        ClipContext ctx = new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, SkeletonAssassin.this);
                        BlockHitResult brtr = SkeletonAssassin.this.m_9236_().m_45547_(ctx);
                        if (brtr.getType() == HitResult.Type.MISS) {
                            this.start = new Vector3(SkeletonAssassin.this.m_20182_());
                            this.end = new Vector3(endPos.subtract(0.0, 0.5, 0.0));
                        } else {
                            this.start = new Vector3(SkeletonAssassin.this.m_20182_());
                            this.end = new Vector3(brtr.m_82450_().add(delta));
                        }
                        return this.start != null && this.end != null && !(this.start.distanceSqTo(this.end) < 5.0);
                    }
                }
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            super.start();
            SkeletonAssassin.this.doBackflip();
        }

        @Override
        public boolean canContinueToUse() {
            return SkeletonAssassin.this.isActing && SkeletonAssassin.this.backflipping;
        }

        @Override
        public void stop() {
            super.stop();
            this.start = null;
            this.end = null;
            this.tick_count = 0;
        }

        @Override
        public void tick() {
            this.tick_count++;
            if ((float) this.tick_count <= this.move_time) {
                Vector3 interp_pos = Vector3.lerp(this.start, this.end, (float) this.tick_count / this.move_time);
                SkeletonAssassin.this.m_6034_((double) interp_pos.x, (double) interp_pos.y, (double) interp_pos.z);
                SkeletonAssassin.this.m_6853_(true);
            }
        }
    }

    public class BoloGoal extends Goal {

        private int rangedAttackTime = -1;

        private final int attackIntervalMin;

        private final int maxRangedAttackTime;

        private final float attackRadius;

        private final float maxAttackDistance;

        public BoloGoal(RangedAttackMob attacker, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
            this(attacker, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
        }

        public BoloGoal(RangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
            this.attackIntervalMin = p_i1650_4_;
            this.maxRangedAttackTime = maxAttackTime;
            this.attackRadius = maxAttackDistanceIn;
            this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!SkeletonAssassin.this.isActing && SkeletonAssassin.this.boloCooldown <= 0) {
                LivingEntity livingentity = SkeletonAssassin.this.m_5448_();
                return livingentity != null && livingentity.isAlive() && livingentity.m_20186_() > SkeletonAssassin.this.m_20186_() + 2.0 && (livingentity.f_19812_ || !livingentity.m_20096_() || livingentity instanceof Player && ((Player) livingentity).getAbilities().flying);
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && !SkeletonAssassin.this.m_21573_().isStuck();
        }

        @Override
        public void stop() {
            this.rangedAttackTime = -1;
        }

        @Override
        public void tick() {
            LivingEntity e = SkeletonAssassin.this.m_5448_();
            if (e != null) {
                double distance = SkeletonAssassin.this.m_20275_(e.m_20185_(), e.m_20186_(), e.m_20189_());
                boolean canSeeTarget = SkeletonAssassin.this.m_21574_().hasLineOfSight(e);
                if (distance <= (double) this.maxAttackDistance && canSeeTarget) {
                    SkeletonAssassin.this.m_21573_().stop();
                } else {
                    SkeletonAssassin.this.m_21573_().moveTo(e, SkeletonAssassin.this.m_21133_(Attributes.MOVEMENT_SPEED));
                }
                SkeletonAssassin.this.m_21563_().setLookAt(e, 30.0F, 30.0F);
                if (--this.rangedAttackTime == 0) {
                    if (!canSeeTarget) {
                        return;
                    }
                    float f = Mth.sqrt((float) distance) / this.attackRadius;
                    float lvt_5_1_ = Mth.clamp(f, 0.1F, 1.0F);
                    SkeletonAssassin.this.throwBolo(e, lvt_5_1_);
                    this.rangedAttackTime = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                } else if (this.rangedAttackTime < 0) {
                    float f2 = Mth.sqrt((float) distance) / this.attackRadius;
                    this.rangedAttackTime = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                }
            }
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
            LivingEntity livingentity = SkeletonAssassin.this.m_5448_();
            return livingentity != null && livingentity.isAlive() && livingentity.m_20280_(SkeletonAssassin.this) > 16.0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && !SkeletonAssassin.this.m_21573_().isStuck();
        }

        @Override
        public void stop() {
            this.rangedAttackTime = -1;
        }

        @Override
        public void tick() {
            LivingEntity e = SkeletonAssassin.this.m_5448_();
            if (e != null) {
                double distance = SkeletonAssassin.this.m_20275_(e.m_20185_(), e.m_20186_(), e.m_20189_());
                boolean canSeeTarget = SkeletonAssassin.this.m_21574_().hasLineOfSight(e);
                if (distance <= (double) this.maxAttackDistance && canSeeTarget) {
                    SkeletonAssassin.this.m_21573_().stop();
                } else {
                    SkeletonAssassin.this.m_21573_().moveTo(e, SkeletonAssassin.this.m_21133_(Attributes.MOVEMENT_SPEED));
                }
                SkeletonAssassin.this.m_21563_().setLookAt(e, 30.0F, 30.0F);
                if (--this.rangedAttackTime == 0 && !SkeletonAssassin.this.isActing) {
                    if (!canSeeTarget) {
                        return;
                    }
                    float f = Mth.sqrt((float) distance) / this.attackRadius;
                    float lvt_5_1_ = Mth.clamp(f, 0.1F, 1.0F);
                    SkeletonAssassin.this.performRangedAttack(e, lvt_5_1_);
                    this.rangedAttackTime = Mth.floor(f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                } else if (this.rangedAttackTime < 0) {
                    float f2 = Mth.sqrt((float) distance) / this.attackRadius;
                    this.rangedAttackTime = Mth.floor(f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float) this.attackIntervalMin);
                }
            }
        }
    }

    public class SmokeBombGoal extends Goal {

        public SmokeBombGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return SkeletonAssassin.this.m_5448_() != null && !SkeletonAssassin.this.isActing && SkeletonAssassin.this.smokeBombCooldown == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return SkeletonAssassin.this.isActing && SkeletonAssassin.this.throwingBolo;
        }

        @Override
        public void start() {
            SkeletonAssassin.this.throwSmokeBomb();
        }
    }
}