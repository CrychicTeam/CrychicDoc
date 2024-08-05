package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityRattlesnake extends Animal implements IAnimatedEntity {

    public float prevCurlProgress;

    public float curlProgress;

    public int randomToungeTick = 0;

    public int maxCurlTime = 75;

    private int curlTime = 0;

    private static final EntityDataAccessor<Boolean> RATTLING = SynchedEntityData.defineId(EntityRattlesnake.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CURLED = SynchedEntityData.defineId(EntityRattlesnake.class, EntityDataSerializers.BOOLEAN);

    private static final Predicate<LivingEntity> WARNABLE_PREDICATE = mob -> mob instanceof Player && !((Player) mob).isCreative() && !mob.m_5833_() || mob instanceof EntityRoadrunner;

    private static final Predicate<LivingEntity> TARGETABLE_PREDICATE = mob -> mob instanceof Player && !((Player) mob).isCreative() && !mob.m_5833_() || mob instanceof EntityRoadrunner;

    private int animationTick;

    private Animation currentAnimation;

    public static final Animation ANIMATION_BITE = Animation.create(20);

    private int loopSoundTick = 0;

    protected EntityRattlesnake(EntityType type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.2, true));
        this.f_21345_.addGoal(2, new EntityRattlesnake.WarnPredatorsGoal());
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(5, new AnimalAIWanderRanged(this, 60, 1.0, 7, 7));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Rabbit.class, 15, true, true, null));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, EntityJerboa.class, 15, true, true, null));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new EntityRattlesnake.ShortDistanceTarget());
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.RATTLESNAKE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.RATTLESNAKE_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rattlesnakeSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.setAnimation(ANIMATION_BITE);
        return true;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() == MobEffects.POISON ? false : super.m_7301_(potioneffectIn);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CURLED, false);
        this.f_19804_.define(RATTLING, false);
    }

    public boolean isCurled() {
        return this.f_19804_.get(CURLED);
    }

    public void setCurled(boolean curled) {
        this.f_19804_.set(CURLED, curled);
    }

    public boolean isRattling() {
        return this.f_19804_.get(RATTLING);
    }

    public void setRattling(boolean rattling) {
        this.f_19804_.set(RATTLING, rattling);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevCurlProgress = this.curlProgress;
        if (this.isCurled()) {
            if (this.curlProgress < 5.0F) {
                this.curlProgress += 0.5F;
            }
        } else if (this.curlProgress > 0.0F) {
            this.curlProgress--;
        }
        if (this.randomToungeTick == 0 && this.f_19796_.nextInt(15) == 0) {
            this.randomToungeTick = 10 + this.f_19796_.nextInt(20);
        }
        if (this.randomToungeTick > 0) {
            this.randomToungeTick--;
        }
        if (this.isCurled() && !this.isRattling() && ++this.curlTime > this.maxCurlTime) {
            this.setCurled(false);
            this.curlTime = 0;
            this.maxCurlTime = 75 + this.f_19796_.nextInt(50);
        }
        LivingEntity target = this.m_5448_();
        if (!this.m_9236_().isClientSide) {
            if (this.isCurled() && target != null && target.isAlive()) {
                this.setCurled(false);
            }
            if (this.isRattling() && target == null) {
                this.setCurled(true);
            }
            if (!this.isCurled() && this.m_5448_() == null && this.f_19796_.nextInt(500) == 0) {
                this.maxCurlTime = 300 + this.f_19796_.nextInt(250);
                this.setCurled(true);
            }
        }
        if (this.getAnimation() == ANIMATION_BITE) {
            if (this.getAnimationTick() == 4) {
                this.m_5496_(AMSoundRegistry.RATTLESNAKE_ATTACK.get(), this.m_6121_(), this.m_6100_());
            }
            if (this.getAnimationTick() == 8 && target != null && (double) this.m_20270_(target) < 2.0) {
                boolean meepMeep = target instanceof EntityRoadrunner;
                int f = this.m_6162_() ? 2 : 1;
                target.hurt(this.m_269291_().mobAttack(this), meepMeep ? 1.0F : (float) f * (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                if (!meepMeep) {
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, 300, f * 2));
                }
            }
        }
        if (this.isRattling()) {
            if (this.loopSoundTick == 0) {
                this.m_146850_(GameEvent.ENTITY_ROAR);
                this.m_5496_(AMSoundRegistry.RATTLESNAKE_LOOP.get(), this.m_6121_() * 0.5F, this.m_6100_());
            }
            this.loopSoundTick++;
            if (this.loopSoundTick > 50) {
                this.loopSoundTick = 0;
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.m_20096_() && this.isCurled()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ARMOR, 0.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.28F);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null && stack.getItem().getFoodProperties().isMeat();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.RATTLESNAKE.get().create(p_241840_1_);
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
        return new Animation[] { ANIMATION_BITE };
    }

    public static boolean canRattlesnakeSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.RATTLESNAKE_SPAWNS);
        return spawnBlock && worldIn.m_45524_(pos, 0) > 8;
    }

    class ShortDistanceTarget extends NearestAttackableTargetGoal<Player> {

        public ShortDistanceTarget() {
            super(EntityRattlesnake.this, Player.class, 3, true, true, EntityRattlesnake.TARGETABLE_PREDICATE);
        }

        @Override
        public boolean canUse() {
            return EntityRattlesnake.this.m_6162_() ? false : super.canUse();
        }

        @Override
        public void start() {
            super.start();
            EntityRattlesnake.this.setCurled(false);
            EntityRattlesnake.this.setRattling(true);
        }

        @Override
        protected double getFollowDistance() {
            return 2.0;
        }
    }

    class WarnPredatorsGoal extends Goal {

        int executionChance = 20;

        Entity target = null;

        @Override
        public boolean canUse() {
            if (EntityRattlesnake.this.m_217043_().nextInt(this.executionChance) == 0) {
                double dist = 5.0;
                List<LivingEntity> list = EntityRattlesnake.this.m_9236_().m_6443_(LivingEntity.class, EntityRattlesnake.this.m_20191_().inflate(5.0, 5.0, 5.0), EntityRattlesnake.WARNABLE_PREDICATE);
                double d0 = Double.MAX_VALUE;
                Entity possibleTarget = null;
                for (Entity entity : list) {
                    double d1 = EntityRattlesnake.this.m_20280_(entity);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        possibleTarget = entity;
                    }
                }
                this.target = possibleTarget;
                return !list.isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && (double) EntityRattlesnake.this.m_20270_(this.target) < 5.0 && EntityRattlesnake.this.m_5448_() == null;
        }

        @Override
        public void stop() {
            this.target = null;
            EntityRattlesnake.this.setRattling(false);
        }

        @Override
        public void tick() {
            EntityRattlesnake.this.setRattling(true);
            EntityRattlesnake.this.setCurled(true);
            EntityRattlesnake.this.curlTime = 0;
            EntityRattlesnake.this.m_21563_().setLookAt(this.target, 30.0F, 30.0F);
        }
    }
}