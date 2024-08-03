package com.mna.entities.faction;

import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.MathUtils;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class HulkingZombie extends BaseFactionMob<HulkingZombie> {

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_ROAR = "roar";

    private static final int LEAP_ATTACK_CD = 300;

    private static final int ROAR_CD = 300;

    private int leapAttackCooldown = 0;

    private int roarCooldown = 0;

    private boolean isActing = false;

    private boolean isMeleeAttacking = false;

    private boolean leftHandPunch = false;

    private boolean isLeapAttacking = false;

    private boolean isRoaring = false;

    public HulkingZombie(EntityType<HulkingZombie> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public float getStepHeight() {
        return 1.2F;
    }

    public HulkingZombie(Level worldIn) {
        this(EntityInit.HULKING_ZOMBIE.get(), worldIn);
    }

    public static boolean canSpawnPredicate(EntityType<HulkingZombie> p_234351_0_, LevelAccessor p_234351_1_, MobSpawnType p_234351_2_, BlockPos p_234351_3_, RandomSource p_234351_4_) {
        return !(p_234351_1_ instanceof ServerLevelAccessor) ? false : p_234351_3_.m_123342_() <= 45 && Monster.checkMonsterSpawnRules(p_234351_0_, (ServerLevelAccessor) p_234351_1_, p_234351_2_, p_234351_3_, p_234351_4_);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        if (!this.isActing) {
            return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.hulking_zombie.run")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.hulking_zombie.idle"));
        } else if (this.isLeapAttacking) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.hulking_zombie.ground_slam_loop"));
        } else if (this.isRoaring) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.hulking_zombie.roar"));
        } else {
            return this.leftHandPunch ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.hulking_zombie.punch_left")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.hulking_zombie.punch_right"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.leapAttackCooldown--;
        this.roarCooldown--;
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
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(1, this, new HulkingZombie.LerpLeap(16.0F, 1, 10)));
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(2, this, new HulkingZombie.RoarGoal()));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SFX.Entity.HulkingZombie.IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.HulkingZombie.DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SFX.Entity.HulkingZombie.HURT;
    }

    @Override
    public void swing(InteractionHand p_226292_1_, boolean p_226292_2_) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", this::handleDelayCallback));
            this.isActing = true;
            this.isMeleeAttacking = true;
            this.leftHandPunch = Math.random() < 0.5;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.m_5496_(SFX.Entity.HulkingZombie.ATTACK, 1.0F, (float) (0.9 + Math.random() * 0.2));
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isMeleeAttacking) {
            return true;
        } else {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 20, entityIn, this::handleDelayCallback));
            this.swing(InteractionHand.MAIN_HAND, true);
            this.isMeleeAttacking = false;
            return true;
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FREEZING)) {
            amount *= 0.75F;
        }
        return super.hurt(source, amount);
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
                if (this.isLeapAttacking) {
                    f += 4.0F;
                }
                float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
                if (entityIn instanceof LivingEntity) {
                    f += EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entityIn).getMobType());
                    f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
                }
                int i = EnchantmentHelper.getFireAspect(this);
                if (i > 0 || this.m_6060_()) {
                    entityIn.setSecondsOnFire(i * 4);
                }
                boolean flag = entityIn.hurt(this.m_269291_().mobAttack(this), f);
                if (flag) {
                    if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                        ((LivingEntity) entityIn).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                        if (!this.leftHandPunch && this.getTier() > 0) {
                            ((LivingEntity) entityIn).m_5997_(0.0, 1.0, 0.0);
                        }
                        this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
                    }
                    if (entityIn instanceof Player playerentity) {
                        this.maybeDisableShield(playerentity, this.m_21205_(), playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
                    }
                    this.m_19970_(this, entityIn);
                    this.m_21335_(entityIn);
                    if (this.f_19796_.nextFloat() < 0.2F && entityIn instanceof Mob) {
                        ((Mob) entityIn).setTarget(this);
                    }
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 24.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.8F);
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            switch(identifier) {
                case "damage":
                    this.damageEntity(entity);
                    break;
                case "roar":
                    this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, false));
                    this.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1, false, false));
                    this.m_7292_(new MobEffectInstance(EffectInit.ENLARGE.get(), 200, 4, false, false));
                    this.m_5496_(SFX.Entity.HulkingZombie.ROAR, 1.0F, 1.0F);
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isActing = false;
            if (this.isLeapAttacking) {
                this.leapAttackCooldown = 20;
                this.isLeapAttacking = false;
            } else if (this.isRoaring) {
                this.roarCooldown = 300;
                this.isRoaring = false;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isActing);
        nbt.putBoolean("leap_attacking", this.isLeapAttacking);
        nbt.putBoolean("left_hand_punch", this.leftHandPunch);
        nbt.putBoolean("roaring", this.isRoaring);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isActing = nbt.getBoolean("attacking");
        this.isLeapAttacking = nbt.getBoolean("leap_attacking");
        this.leftHandPunch = nbt.getBoolean("left_hand_punch");
        this.isRoaring = nbt.getBoolean("roaring");
    }

    public class LerpLeap extends Goal {

        private float maxDist;

        private int lerpTicks;

        private int initialDelay;

        private int lerpCount = 0;

        private Vec3 start;

        private Vec3 end;

        private Vec3 control_1;

        private Vec3 control_2;

        public LerpLeap(float maxDist, int initialDelay, int lerpTime) {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
            this.maxDist = maxDist;
            this.lerpTicks = lerpTime;
            this.initialDelay = initialDelay;
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            if (HulkingZombie.this.m_5448_() != null && HulkingZombie.this.m_6084_() && HulkingZombie.this.m_5448_().isAlive() && HulkingZombie.this.leapAttackCooldown <= 0) {
                double dist = HulkingZombie.this.m_20280_(HulkingZombie.this.m_5448_());
                if (dist >= 4.0 && dist <= (double) (this.maxDist * this.maxDist)) {
                    LivingEntity livingentity = HulkingZombie.this.m_5448_();
                    if (livingentity != null && livingentity.isAlive() && (livingentity instanceof FlyingMob || livingentity.f_19812_ || !livingentity.m_20096_() || livingentity instanceof Player && ((Player) livingentity).getAbilities().flying) && this.getEntityHeightOffGround(livingentity) > 2.0F) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        private float getEntityHeightOffGround(LivingEntity living) {
            BlockPos livingPos = living.m_20183_();
            int count;
            for (count = 0; count < 10 && living.m_9236_().m_46859_(livingPos); livingPos = livingPos.below()) {
                count++;
            }
            return (float) count;
        }

        @Override
        public void stop() {
            this.lerpCount = 0;
            HulkingZombie.this.m_20242_(false);
            HulkingZombie.this.isActing = false;
            HulkingZombie.this.isLeapAttacking = false;
            ServerMessageDispatcher.sendEntityStateMessage(HulkingZombie.this);
        }

        @Override
        public boolean canContinueToUse() {
            return HulkingZombie.this.m_5448_() != null && this.lerpCount < this.lerpTicks + this.initialDelay;
        }

        @Override
        public void start() {
            HulkingZombie.this.m_20242_(true);
            HulkingZombie.this.m_21573_().stop();
            HulkingZombie.this.m_20256_(Vec3.ZERO);
            HulkingZombie.this.isActing = true;
            HulkingZombie.this.isLeapAttacking = true;
            ServerMessageDispatcher.sendEntityStateMessage(HulkingZombie.this);
        }

        @Override
        public void tick() {
            if (HulkingZombie.this.m_5448_() != null) {
                LivingEntity ent = HulkingZombie.this.m_5448_();
                float lerpPct = (float) (this.lerpCount++ - this.initialDelay) / (float) this.lerpTicks;
                if (this.lerpCount == this.initialDelay) {
                    this.start = HulkingZombie.this.m_20182_();
                    HulkingZombie.this.m_5496_(SFX.Entity.HulkingZombie.LEAP, 1.0F, (float) (0.9 + Math.random() * 0.2));
                }
                if (this.lerpCount > this.initialDelay && this.lerpCount - this.initialDelay <= this.lerpTicks) {
                    Vec3 direction = ent.m_20182_().subtract(this.start).normalize();
                    this.end = ent.m_20182_().add(ent.m_20184_()).subtract(direction);
                    Vec3 difference = this.end.subtract(this.start);
                    this.control_1 = this.start.add(difference.scale(0.3)).add(0.0, 1.0, 0.0);
                    this.control_2 = this.start.add(difference.scale(0.6)).add(0.0, 1.0, 0.0);
                    Vec3 position = MathUtils.bezierVector3d(this.start, this.end, this.control_1, this.control_2, lerpPct);
                    HulkingZombie.this.m_7618_(EntityAnchorArgument.Anchor.FEET, ent.m_20182_());
                    HulkingZombie.this.m_6034_(position.x, position.y, position.z);
                    if (this.lerpCount - this.initialDelay == this.lerpTicks) {
                        HulkingZombie.this.leapAttackCooldown = 300;
                        double dist = HulkingZombie.this.m_20280_(ent);
                        if (dist < 16.0) {
                            HulkingZombie.this.damageEntity(ent);
                            ent.addEffect(new MobEffectInstance(EffectInit.GRAVITY_WELL.get(), 20));
                        }
                    }
                }
            }
        }
    }

    public class RoarGoal extends Goal {

        public RoarGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return HulkingZombie.this.roarCooldown <= 0 && HulkingZombie.this.m_5448_() != null && HulkingZombie.this.m_21223_() < HulkingZombie.this.m_21233_() * 0.75F && !HulkingZombie.this.m_21023_(MobEffects.MOVEMENT_SPEED) && !HulkingZombie.this.m_21023_(MobEffects.DAMAGE_RESISTANCE) && !HulkingZombie.this.m_21023_(MobEffects.DAMAGE_BOOST) && !HulkingZombie.this.isActing;
        }

        @Override
        public void start() {
            HulkingZombie.this.m_21573_().stop();
            HulkingZombie.this.isRoaring = true;
            HulkingZombie.this.isActing = true;
            DelayedEventQueue.pushEvent(HulkingZombie.this.m_9236_(), new TimedDelayedEvent<>("roar", 20, HulkingZombie.this, HulkingZombie.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(HulkingZombie.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", HulkingZombie.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(HulkingZombie.this);
        }

        @Override
        public boolean canContinueToUse() {
            return HulkingZombie.this.isRoaring;
        }
    }
}