package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRideParent;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.AnteaterAIRaidNest;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;

public class EntityAnteater extends Animal implements NeutralMob, IAnimatedEntity, ITargetsDroppedItems {

    public static final Animation ANIMATION_SLASH_R = Animation.create(20);

    public static final Animation ANIMATION_TOUNGE_IDLE = Animation.create(10);

    public static final Animation ANIMATION_SLASH_L = Animation.create(20);

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityAnteater.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LEANING_DOWN = SynchedEntityData.defineId(EntityAnteater.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> ANT_ON_TONGUE = SynchedEntityData.defineId(EntityAnteater.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(EntityAnteater.class, EntityDataSerializers.INT);

    public float prevStandProgress;

    public float standProgress;

    public float prevTongueProgress;

    public float tongueProgress;

    public float prevLeaningProgress;

    public float leaningProgress;

    public int eatAntCooldown = 0;

    public int ticksAntOnTongue = 0;

    private int animationTick;

    private Animation currentAnimation;

    private int maxStandTime = 75;

    private int standingTime = 0;

    private int antsEatenRecently = 0;

    private int heldItemTime;

    private UUID lastHurtBy;

    private static final UniformInt ANGRY_TIMER = TimeUtil.rangeOfSeconds(30, 60);

    protected EntityAnteater(EntityType type, Level world) {
        super(type, world);
        this.m_274367_(1.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public static boolean canAnteaterSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.anteaterSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new EntityAnteater.AIMelee());
        this.f_21345_.addGoal(3, new AnteaterAIRaidNest(this));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(5, new AnimalAIRideParent(this, 1.25));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.2, Ingredient.of(AMTagRegistry.INSECT_ITEMS), false));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 110, 1.0, 10, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false, false, 25, 16));
        this.f_21346_.addGoal(2, new AnimalAIHurtByTargetNotBaby(this));
        this.f_21346_.addGoal(3, new EntityAnteater.AITargetAnts());
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.m_6673_(source) || source.getDirectEntity() != null && source.getDirectEntity() instanceof EntityLeafcutterAnt;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ANTEATER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ANTEATER_HURT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Standing", this.isStanding());
        compound.putInt("AntCooldown", this.eatAntCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setStanding(compound.getBoolean("Standing"));
        this.eatAntCooldown = compound.getInt("AntCooldown");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.LEAFCUTTER_ANT_PUPA.get();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(STANDING, Boolean.FALSE);
        this.f_19804_.define(ANT_ON_TONGUE, Boolean.FALSE);
        this.f_19804_.define(LEANING_DOWN, Boolean.FALSE);
        this.f_19804_.define(ANGER_TIME, 0);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.f_19804_.set(ANGER_TIME, time);
    }

    @Override
    public UUID getPersistentAngerTarget() {
        return this.lastHurtBy;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.lastHurtBy = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGRY_TIMER.sample(this.f_19796_));
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public boolean hasAntOnTongue() {
        return this.f_19804_.get(ANT_ON_TONGUE);
    }

    public void setAntOnTongue(boolean standing) {
        this.f_19804_.set(ANT_ON_TONGUE, standing);
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof EntityLeafcutterAnt) && super.m_7337_(entity);
    }

    @Override
    public void push(Entity entity) {
        if (!(entity instanceof EntityLeafcutterAnt)) {
            super.m_7334_(entity);
        }
    }

    public boolean isLeaning() {
        return this.f_19804_.get(LEANING_DOWN);
    }

    public void setLeaning(boolean leaning) {
        this.f_19804_.set(LEANING_DOWN, leaning);
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_();
    }

    @Override
    protected void customServerAiStep() {
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), false);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.mobInteract(player, hand);
        boolean isInsect = itemstack.is(AMTagRegistry.INSECT_ITEMS);
        if (isInsect) {
            Item item = itemstack.getItem();
            ItemStack rippedStack = itemstack.copy();
            rippedStack.setCount(1);
            this.m_21662_();
            this.m_5634_(4.0F);
            this.m_21008_(InteractionHand.MAIN_HAND, rippedStack);
            if (item == AMItemRegistry.LEAFCUTTER_ANT_PUPA.get()) {
                return type;
            } else {
                this.m_142075_(player, hand, itemstack);
                return InteractionResult.SUCCESS;
            }
        } else {
            return type;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevStandProgress = this.standProgress;
        this.prevTongueProgress = this.tongueProgress;
        this.prevLeaningProgress = this.leaningProgress;
        if (this.isStanding()) {
            if (this.standProgress < 5.0F) {
                this.standProgress++;
            }
        } else if (this.standProgress > 0.0F) {
            this.standProgress--;
        }
        boolean isTongueOut = this.getAnimation() == ANIMATION_TOUNGE_IDLE;
        if (isTongueOut) {
            if (this.tongueProgress < 5.0F) {
                this.tongueProgress++;
            }
        } else if (this.tongueProgress > 0.0F) {
            this.tongueProgress--;
        }
        if (this.isLeaning()) {
            if (this.leaningProgress < 5.0F) {
                this.leaningProgress++;
            }
        } else if (this.leaningProgress > 0.0F) {
            this.leaningProgress--;
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (this.m_20159_() && this.m_20202_() instanceof EntityAnteater mount) {
            if (this.m_6162_()) {
                this.m_146922_(mount.f_20883_);
                this.f_20885_ = mount.f_20883_;
                this.f_20883_ = mount.f_20883_;
            } else {
                this.m_6038_();
            }
        }
        if (this.eatAntCooldown > 0) {
            this.eatAntCooldown--;
        }
        if (this.antsEatenRecently >= 3 && this.eatAntCooldown <= 0) {
            this.resetAntCooldown();
        }
        if (this.ticksAntOnTongue > 10 && this.hasAntOnTongue()) {
            this.m_5634_(6.0F);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
            this.setAntOnTongue(false);
        }
        if (this.hasAntOnTongue()) {
            this.ticksAntOnTongue++;
        } else {
            this.ticksAntOnTongue = 0;
        }
        if (!this.m_9236_().isClientSide && this.getTongueStickOut() > 0.6F && !this.hasAntOnTongue() && this.antsEatenRecently < 3) {
            EntityLeafcutterAnt closestAnt = null;
            for (EntityLeafcutterAnt entity : this.m_9236_().m_45976_(EntityLeafcutterAnt.class, this.m_20191_().inflate(2.6F))) {
                if (closestAnt == null || entity.m_20270_(this) < closestAnt.m_20270_(this) && this.m_142582_(entity)) {
                    closestAnt = entity;
                }
            }
            if (closestAnt != null) {
                closestAnt.m_142687_(Entity.RemovalReason.KILLED);
                this.ticksAntOnTongue = 0;
                this.setAntOnTongue(true);
                this.antsEatenRecently++;
            }
        }
        if (!this.m_21205_().isEmpty()) {
            this.heldItemTime++;
            if (this.heldItemTime > 10 && this.getTongueStickOut() < 0.3F && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0F);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                this.m_146850_(GameEvent.EAT);
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.m_21662_();
                this.m_21205_().shrink(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_217043_().nextInt(300) == 0) {
                this.setAnimation(ANIMATION_TOUNGE_IDLE);
            }
            LivingEntity attackTarget = this.m_5448_();
            if (attackTarget != null && this.m_20270_(attackTarget) < attackTarget.m_20205_() + this.m_20205_() + 2.0F && this.getAnimationTick() == 7) {
                if (this.getAnimation() == ANIMATION_SLASH_L) {
                    this.m_7327_(attackTarget);
                    float rot = this.m_146908_() + 90.0F;
                    attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                } else if (this.getAnimation() == ANIMATION_SLASH_R) {
                    this.m_7327_(attackTarget);
                    float rot = this.m_146908_() - 90.0F;
                    attackTarget.knockback(0.5, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
                }
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void resetAntCooldown() {
        this.eatAntCooldown = 600 + this.f_19796_.nextInt(1000);
        this.antsEatenRecently = 0;
    }

    public void standFor(int time) {
        this.setStanding(true);
        this.maxStandTime = time;
    }

    public float getTongueStickOut() {
        if (this.tongueProgress > 0.0F) {
            double tongueM = Math.min(Math.sin((double) ((float) this.f_19797_ * 0.15F)), 0.0);
            return (float) (-tongueM) * this.tongueProgress * 0.2F;
        } else {
            return 0.0F;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return AMEntityRegistry.ANTEATER.get().create(this.m_9236_());
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
    public boolean canTargetItem(ItemStack stack) {
        return !this.hasAntOnTongue() && stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.setAnimation(ANIMATION_TOUNGE_IDLE);
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SLASH_L, ANIMATION_SLASH_R, ANIMATION_TOUNGE_IDLE };
    }

    private boolean shouldTargetAnts() {
        return !this.m_21660_();
    }

    public boolean isPeter() {
        String name = ChatFormatting.stripFormatting(this.m_7755_().getString());
        if (name == null) {
            return false;
        } else {
            String lowercaseName = name.toLowerCase(Locale.ROOT);
            return lowercaseName.contains("peter") || lowercaseName.contains("petr") || lowercaseName.contains("zot");
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(0.5F);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private class AIMelee extends Goal {

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return EntityAnteater.this.m_5448_() != null && EntityAnteater.this.m_5448_().isAlive() && !EntityAnteater.this.m_6162_();
        }

        @Override
        public void tick() {
            LivingEntity enemy = EntityAnteater.this.m_5448_();
            double attackReachSqr = this.getAttackReachSqr(enemy);
            double distToEnemySqr = (double) EntityAnteater.this.m_20270_(enemy);
            EntityAnteater.this.m_21391_(enemy, 100.0F, 5.0F);
            if (enemy instanceof EntityLeafcutterAnt) {
                if (distToEnemySqr <= attackReachSqr + 1.5) {
                    EntityAnteater.this.setAnimation(EntityAnteater.ANIMATION_TOUNGE_IDLE);
                } else {
                    EntityAnteater.this.m_21391_(enemy, 5.0F, 5.0F);
                }
                EntityAnteater.this.m_21573_().moveTo(enemy, 1.0);
            } else {
                if (distToEnemySqr <= attackReachSqr) {
                    EntityAnteater.this.m_21573_().moveTo(enemy, 1.0);
                    EntityAnteater.this.setAnimation(EntityAnteater.this.m_217043_().nextBoolean() ? EntityAnteater.ANIMATION_SLASH_L : EntityAnteater.ANIMATION_SLASH_R);
                }
                double x = enemy.m_20185_() - EntityAnteater.this.m_20185_();
                double z = enemy.m_20189_() - EntityAnteater.this.m_20189_();
                float f = (float) (Mth.atan2(z, x) * 180.0F / (float) Math.PI) - 90.0F;
                EntityAnteater.this.m_146922_(f);
                EntityAnteater.this.f_20883_ = f;
                EntityAnteater.this.setStanding(true);
            }
        }

        @Override
        public void stop() {
            EntityAnteater.this.setStanding(false);
            super.stop();
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return (double) (2.0F + attackTarget.m_20205_());
        }
    }

    private class AITargetAnts extends NearestAttackableTargetGoal {

        private static final Predicate<EntityLeafcutterAnt> QUEEN_ANT = entity -> !entity.isQueen();

        public AITargetAnts() {
            super(EntityAnteater.this, EntityLeafcutterAnt.class, 30, true, false, QUEEN_ANT);
        }

        @Override
        public boolean canUse() {
            return EntityAnteater.this.shouldTargetAnts() && !EntityAnteater.this.m_6162_() && !EntityAnteater.this.hasAntOnTongue() && !EntityAnteater.this.isStanding() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return EntityAnteater.this.shouldTargetAnts() && !EntityAnteater.this.hasAntOnTongue() && !EntityAnteater.this.isStanding() && super.m_8045_();
        }
    }
}