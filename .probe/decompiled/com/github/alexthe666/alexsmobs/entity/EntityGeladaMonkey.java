package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.GeladaAIGroom;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityGeladaMonkey extends Animal implements IAnimatedEntity, IHerdPanic {

    public static final Animation ANIMATION_SWIPE_R = Animation.create(13);

    public static final Animation ANIMATION_SWIPE_L = Animation.create(13);

    public static final Animation ANIMATION_GROOM = Animation.create(35);

    public static final Animation ANIMATION_CHEST = Animation.create(35);

    private static final EntityDataAccessor<Boolean> LEADER = SynchedEntityData.defineId(EntityGeladaMonkey.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityGeladaMonkey.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_TARGET = SynchedEntityData.defineId(EntityGeladaMonkey.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> GRASS_TIME = SynchedEntityData.defineId(EntityGeladaMonkey.class, EntityDataSerializers.INT);

    public float prevSitProgress;

    public float sitProgress;

    public boolean isGrooming = false;

    public int groomerID = -1;

    private int animationTick;

    private Animation currentAnimation;

    private int sittingTime;

    private int maxSitTime;

    private int leaderFightTime;

    private HurtByTargetGoal hurtByTargetGoal = null;

    private NearestAttackableTargetGoal<EntityGeladaMonkey> leaderFightGoal = null;

    private int revengeCooldown = 0;

    private boolean hasSpedUp = false;

    protected EntityGeladaMonkey(EntityType type, Level lvl) {
        super(type, lvl);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.geladaMonkeySpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 18.0).add(Attributes.ATTACK_DAMAGE, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 10;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.GELADA_MONKEY_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.GELADA_MONKEY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.GELADA_MONKEY_HURT.get();
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.5, true) {

            @Override
            protected double getAttackReachSqr(LivingEntity attackTarget) {
                return super.getAttackReachSqr(attackTarget) + 1.5;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && EntityGeladaMonkey.this.revengeCooldown <= 0;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && EntityGeladaMonkey.this.revengeCooldown <= 0;
            }
        });
        this.f_21345_.addGoal(2, new EntityGeladaMonkey.AIClearGrass());
        this.f_21345_.addGoal(3, new AnimalAIHerdPanic(this, 1.5));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.0, Ingredient.of(Items.WHEAT, Items.DEAD_BUSH), false));
        this.f_21345_.addGoal(7, new GeladaAIGroom(this));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, 1.0, 120));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, this.hurtByTargetGoal = new HurtByTargetGoal(this, EntityGeladaMonkey.class).setAlertOthers());
        this.f_21346_.addGoal(2, this.leaderFightGoal = new NearestAttackableTargetGoal<>(this, EntityGeladaMonkey.class, 70, false, false, monkey -> this.isLeader() && this.leaderFightTime == 0 && ((EntityGeladaMonkey) monkey).isLeader() && ((EntityGeladaMonkey) monkey).leaderFightTime == 0));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Leader", this.isLeader());
        compound.putInt("GrassTime", this.getClearGrassTime());
        compound.putInt("FightTime", this.leaderFightTime);
        compound.putBoolean("MonkeySitting", this.isSitting());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setLeader(compound.getBoolean("Leader"));
        this.setClearGrassTime(compound.getInt("GrassTime"));
        this.setSitting(compound.getBoolean("MonkeySitting"));
        this.leaderFightTime = compound.getInt("FightTime");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.DEAD_BUSH;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(LEADER, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(HAS_TARGET, false);
        this.f_19804_.define(GRASS_TIME, 0);
    }

    public boolean isLeader() {
        return this.f_19804_.get(LEADER) && !this.m_6162_();
    }

    public void setLeader(boolean leader) {
        this.f_19804_.set(LEADER, leader);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    public void setSitting(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isAggro() {
        return this.f_19804_.get(HAS_TARGET);
    }

    public void setAggro(boolean sit) {
        this.f_19804_.set(HAS_TARGET, sit);
    }

    public int getClearGrassTime() {
        return this.f_19804_.get(GRASS_TIME);
    }

    public void setClearGrassTime(int i) {
        this.f_19804_.set(GRASS_TIME, i);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isSitting() && ++this.sittingTime > this.maxSitTime) {
                this.setSitting(false);
                this.sittingTime = 0;
                this.maxSitTime = 75 + this.f_19796_.nextInt(50);
            }
            if (this.m_20184_().lengthSqr() < 0.03 && this.getAnimation() == NO_ANIMATION && !this.isSitting() && this.f_19796_.nextInt(500) == 0) {
                this.sittingTime = 0;
                this.maxSitTime = 200 + this.f_19796_.nextInt(550);
                this.setSitting(true);
            }
            if (this.isSitting() && (this.m_5448_() != null || this.m_27593_())) {
                this.setSitting(false);
            }
            if (this.m_5448_() != null && (this.getAnimation() == ANIMATION_SWIPE_L || this.getAnimation() == ANIMATION_SWIPE_R) && this.getAnimationTick() == 7 && this.m_142582_(this.m_5448_()) && this.m_20270_(this.m_5448_()) < this.m_20206_() + this.m_5448_().m_20206_() + 1.0F) {
                this.m_5448_().knockback(0.4F, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
                float dmg = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
                if (this.isLeader() && this.m_5448_() instanceof EntityGeladaMonkey monkey && monkey.isLeader()) {
                    monkey.m_6710_(this);
                    monkey.leaderFightTime = this.leaderFightTime;
                    dmg = 0.0F;
                }
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), dmg);
            }
            if (this.m_5448_() != null && this.m_5448_().isAlive()) {
                this.setAggro(true);
                if (this.isLeader() && this.m_5448_() instanceof EntityGeladaMonkey monkey) {
                    if (monkey.isLeader()) {
                        this.leaderFightTime++;
                    }
                    if (this.leaderFightTime < 10 && this.f_19796_.nextInt(5) == 0 && this.getAnimation() == NO_ANIMATION) {
                        this.setAnimation(ANIMATION_CHEST);
                    }
                    if (Math.max(this.leaderFightTime, monkey.leaderFightTime) >= 250) {
                        this.resetAttackAI();
                        monkey.resetAttackAI();
                    }
                }
            } else {
                this.setAggro(false);
            }
            if (this.leaderFightTime < 0) {
                this.leaderFightTime++;
            }
        }
        if (this.isAggro()) {
            if (!this.hasSpedUp) {
                this.hasSpedUp = true;
                this.m_6858_(true);
                this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.31F);
            }
        } else if (this.hasSpedUp) {
            this.hasSpedUp = false;
            this.m_6858_(false);
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
        }
        if (this.getClearGrassTime() > 0) {
            this.setClearGrassTime(this.getClearGrassTime() - 1);
        }
        if (this.getClearGrassTime() < 0) {
            this.setClearGrassTime(this.getClearGrassTime() + 1);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private void resetAttackAI() {
        this.leaderFightTime = -500 - this.f_19796_.nextInt(2000);
        this.m_6710_(null);
        this.m_6703_(null);
        if (this.leaderFightGoal != null) {
            this.leaderFightGoal.m_8041_();
        }
        if (this.hurtByTargetGoal != null) {
            this.hurtByTargetGoal.m_8041_();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.attackAnimation();
        }
        return true;
    }

    public float getGeladaScale() {
        return this.m_6162_() ? 0.5F : (this.isLeader() ? 1.15F : 1.0F);
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
        return new Animation[] { ANIMATION_SWIPE_R, ANIMATION_SWIPE_L, ANIMATION_GROOM, ANIMATION_CHEST };
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev) {
            Entity direct = source.getEntity();
            if (direct instanceof EntityGeladaMonkey) {
                int fleeTime = 100 + this.m_217043_().nextInt(5);
                this.revengeCooldown = fleeTime;
                this.revengeCooldown = 10 + this.m_217043_().nextInt(30);
            }
        }
        return prev;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (item == Items.WHEAT && this.getClearGrassTime() == 0) {
            this.m_142075_(player, hand, itemstack);
            this.eatGrassWithBuddies(3 + this.f_19796_.nextInt(2));
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel lvl, AgeableMob mob) {
        EntityGeladaMonkey baby = AMEntityRegistry.GELADA_MONKEY.get().create(lvl);
        baby.setLeader(this.f_19796_.nextInt(2) == 0);
        return baby;
    }

    public void eatGrassWithBuddies(int otherMonkies) {
        int i = 300 + this.f_19796_.nextInt(300);
        this.setClearGrassTime(i);
        int monky = 0;
        for (EntityGeladaMonkey entity : this.m_9236_().m_45976_(EntityGeladaMonkey.class, this.m_20191_().inflate(15.0))) {
            if (monky < otherMonkies && entity.m_19879_() != this.m_19879_() && !entity.shouldStopBeingGroomed()) {
                monky++;
                entity.setClearGrassTime(i);
            }
        }
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return this.m_21188_() instanceof EntityGeladaMonkey && this.f_19796_.nextInt(3) == 0;
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting() || this.getAnimation() == ANIMATION_CHEST) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @javax.annotation.Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        if (spawnDataIn instanceof AgeableMob.AgeableMobGroupData pack) {
            if (pack.getGroupSize() == 0 || pack.getGroupSize() > 4 && this.f_19796_.nextInt(2) == 0) {
                this.setLeader(true);
            }
        } else {
            this.setLeader(this.m_217043_().nextInt(4) == 0);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean canBeGroomed() {
        return this.groomerID == -1;
    }

    public boolean shouldStopBeingGroomed() {
        return this.m_5448_() != null && this.m_5448_().isAlive() || this.m_27593_() || this.revengeCooldown > 0;
    }

    private void attackAnimation() {
        this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_SWIPE_L : ANIMATION_SWIPE_R);
    }

    private class AIClearGrass extends Goal {

        private BlockPos target;

        public AIClearGrass() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (EntityGeladaMonkey.this.getClearGrassTime() > 0) {
                this.target = this.generateTarget();
                return this.target != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && EntityGeladaMonkey.this.m_9236_().getBlockState(this.target).m_204336_(AMTagRegistry.GELADA_MONKEY_GRASS);
        }

        @Override
        public void tick() {
            EntityGeladaMonkey.this.setSitting(false);
            EntityGeladaMonkey.this.m_21573_().moveTo((double) ((float) this.target.m_123341_() + 0.5F), (double) ((float) this.target.m_123342_() + 0.5F), (double) ((float) this.target.m_123343_() + 0.5F), 1.4F);
            if (EntityGeladaMonkey.this.m_20238_(Vec3.atCenterOf(this.target)) < 3.4F) {
                if (EntityGeladaMonkey.this.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                    EntityGeladaMonkey.this.attackAnimation();
                } else if (EntityGeladaMonkey.this.getAnimationTick() > 7) {
                    EntityGeladaMonkey.this.m_9236_().m_46961_(this.target, true);
                }
            }
        }

        public BlockPos generateTarget() {
            BlockPos blockpos = null;
            Random random = new Random();
            int range = 7;
            for (int i = 0; i < 15; i++) {
                BlockPos blockpos1 = EntityGeladaMonkey.this.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
                while (EntityGeladaMonkey.this.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > -63) {
                    blockpos1 = blockpos1.below();
                }
                if (EntityGeladaMonkey.this.m_9236_().getBlockState(blockpos1).m_204336_(AMTagRegistry.GELADA_MONKEY_GRASS)) {
                    blockpos = blockpos1;
                }
            }
            return blockpos;
        }
    }
}