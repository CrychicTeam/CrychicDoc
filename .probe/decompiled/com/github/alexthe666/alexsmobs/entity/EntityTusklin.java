package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIMeleeNearby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIRide;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import javax.annotation.Nullable;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class EntityTusklin extends Animal implements IAnimatedEntity {

    public static final Animation ANIMATION_RUT = Animation.create(26);

    public static final Animation ANIMATION_GORE_L = Animation.create(25);

    public static final Animation ANIMATION_GORE_R = Animation.create(25);

    public static final Animation ANIMATION_FLING = Animation.create(15);

    public static final Animation ANIMATION_BUCK = Animation.create(15);

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(EntityTusklin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> PASSIVETICKS = SynchedEntityData.defineId(EntityTusklin.class, EntityDataSerializers.INT);

    private int animationTick;

    private Animation currentAnimation;

    private int ridingTime = 0;

    private int entityToLaunchId = -1;

    private int conversionTime = 0;

    protected EntityTusklin(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.m_274367_(1.1F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.tusklinSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canTusklinSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8 && (worldIn.m_8055_(pos.below()).m_280296_() || worldIn.m_8055_(pos.below()).m_60713_(Blocks.SNOW_BLOCK));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ATTACK_DAMAGE, 9.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.9F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.TUSKLIN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TUSKLIN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TUSKLIN_HURT.get();
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn);
    }

    public boolean isInNether() {
        return this.m_9236_().dimension() == Level.NETHER && !this.m_21525_();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new AnimalAIMeleeNearby(this, 15, 1.25));
        this.f_21345_.addGoal(3, new TameableAIRide(this, 2.0, false) {

            @Override
            public boolean shouldMoveForward() {
                return true;
            }

            @Override
            public boolean shouldMoveBackwards() {
                return false;
            }
        });
        this.f_21345_.addGoal(4, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 120, 0.6F, 14, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this).m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 100, true, false, this::isAngryAt));
    }

    public boolean isAngryAt(LivingEntity livingEntity0) {
        return this.canAttack(livingEntity0);
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        this.m_274367_(1.0F);
        this.m_21573_().stop();
        this.m_6710_(null);
        this.m_6858_(true);
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        if (this.isSaddled()) {
            for (Entity passenger : this.m_20197_()) {
                if (passenger instanceof Player) {
                    return (Player) passenger;
                }
            }
        }
        return null;
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        boolean prev = super.m_6779_(entity);
        return !(entity instanceof Player) || this.m_21188_() != null && this.m_21188_().equals(entity) || this.getPassiveTicks() <= 0 && !this.isMushroom(entity.getItemInHand(InteractionHand.MAIN_HAND)) && !this.isMushroom(entity.getItemInHand(InteractionHand.OFF_HAND)) ? prev : false;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            int anim = this.f_19796_.nextInt(3);
            switch(anim) {
                case 0:
                    this.setAnimation(ANIMATION_FLING);
                    break;
                case 1:
                    this.setAnimation(ANIMATION_GORE_L);
                    break;
                case 2:
                    this.setAnimation(ANIMATION_GORE_R);
            }
        }
        return true;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float radius = 0.4F;
            if (this.getAnimation() == ANIMATION_GORE_L || this.getAnimation() == ANIMATION_GORE_R) {
                if (this.getAnimationTick() <= 4) {
                    radius -= (float) this.getAnimationTick() * 0.1F;
                } else {
                    radius -= -0.4F + (float) Math.min(this.getAnimationTick() - 4, 4) * 0.1F;
                }
            }
            if (this.getAnimation() == ANIMATION_BUCK) {
                if (this.getAnimationTick() < 5) {
                    radius -= (float) this.getAnimationTick() * 0.1F;
                } else if (this.getAnimationTick() < 10) {
                    radius -= 0.4F - (float) (this.getAnimationTick() - 5) * 0.1F;
                }
            }
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.m_20189_() + extraZ);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = this.f_267362_.position();
        float f1 = this.f_267362_.speed();
        float f2 = 0.0F;
        if (this.getAnimation() == ANIMATION_FLING) {
            if ((float) this.getAnimationTick() <= 3.0F) {
                f2 = (float) this.getAnimationTick() * -0.1F;
            } else {
                f2 = -0.3F + (float) Mth.clamp(this.getAnimationTick() - 3, 0, 3) * 0.1F;
            }
        }
        if (this.getAnimation() == ANIMATION_BUCK) {
            if (this.getAnimationTick() < 5) {
                f2 = (float) this.getAnimationTick() * 0.2F * 0.8F;
            } else if (this.getAnimationTick() < 10) {
                f2 = (0.8F - (float) (this.getAnimationTick() - 5) * 0.2F) * 0.8F;
            }
        }
        return (double) this.m_20206_() - 0.3 + (double) ((float) Math.abs(Math.sin((double) (f * 0.7F)) * (double) f1 * 0.0625 * 1.6F)) + (double) f2;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (item == Items.SADDLE && !this.isSaddled() && !this.m_6162_()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setSaddled(true);
            return InteractionResult.SUCCESS;
        } else if (item == AMItemRegistry.PIGSHOES.get() && this.getShoeStack().isEmpty() && !this.m_6162_()) {
            this.setShoeStack(itemstack.copy());
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else if (!this.isMushroom(itemstack) || this.getPassiveTicks() > 0 && !(this.m_21223_() < this.m_21233_())) {
            InteractionResult type = super.mobInteract(player, hand);
            if (type != InteractionResult.SUCCESS && !this.isFood(itemstack) && !player.m_6144_() && !this.m_6162_() && this.isSaddled() && this.getAnimation() != ANIMATION_BUCK) {
                player.m_20329_(this);
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        } else {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.m_5634_(6.0F);
            this.setPassiveTicks(this.getPassiveTicks() + 1200);
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.RED_MUSHROOM);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.m_20088_().define(SADDLED, false);
        this.m_20088_().define(PASSIVETICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (!this.getShoeStack().isEmpty()) {
            compoundTag0.put("ShoeItem", this.getShoeStack().save(new CompoundTag()));
        }
        compoundTag0.putInt("PassiveTicks", this.getPassiveTicks());
        compoundTag0.putBoolean("Saddle", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setSaddled(compoundTag0.getBoolean("Saddle"));
        this.setPassiveTicks(compoundTag0.getInt("PassiveTicks"));
        CompoundTag compoundtag = compoundTag0.getCompound("ShoeItem");
        if (compoundtag != null && !compoundtag.isEmpty()) {
            ItemStack itemstack = ItemStack.of(compoundtag);
            if (itemstack.isEmpty()) {
                AlexsMobs.LOGGER.warn("Unable to load item from: {}", compoundtag);
            }
            this.setShoeStack(itemstack);
        }
    }

    public boolean isMushroom(ItemStack stack) {
        return stack.is(Items.BROWN_MUSHROOM);
    }

    public int getPassiveTicks() {
        return this.f_19804_.get(PASSIVETICKS);
    }

    private void setPassiveTicks(int passiveTicks) {
        this.f_19804_.set(PASSIVETICKS, passiveTicks);
    }

    public boolean isSaddled() {
        return this.f_19804_.get(SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.f_19804_.set(SADDLED, saddled);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isSaddled() && !this.m_9236_().isClientSide) {
            this.m_19998_(Items.SADDLE);
        }
        if (!this.getShoeStack().isEmpty() && !this.m_9236_().isClientSide) {
            this.m_19983_(this.getShoeStack().copy());
        }
        this.setSaddled(false);
        this.setShoeStack(ItemStack.EMPTY);
    }

    public ItemStack getShoeStack() {
        return this.m_6844_(EquipmentSlot.FEET);
    }

    public void setShoeStack(ItemStack shoe) {
        this.m_8061_(EquipmentSlot.FEET, shoe);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.isInNether()) {
            this.conversionTime++;
            if (this.conversionTime > 300 && !this.m_9236_().isClientSide) {
                Hoglin hoglin = (Hoglin) this.m_21406_(EntityType.HOGLIN, false);
                if (hoglin != null) {
                    hoglin.m_7292_(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                    this.dropEquipment();
                    this.m_9236_().m_7967_(hoglin);
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        if (this.entityToLaunchId != -1 && this.m_6084_()) {
            Entity launch = this.m_9236_().getEntity(this.entityToLaunchId);
            this.m_20153_();
            this.entityToLaunchId = -1;
            if (launch != null && !launch.isPassenger() && launch instanceof LivingEntity) {
                launch.setPos(this.m_146892_().add(0.0, 1.0, 0.0));
                float rot = 180.0F + this.m_146908_();
                float strength = (float) ((double) this.getLaunchStrength() * (1.0 - ((LivingEntity) launch).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                float x = Mth.sin(rot * (float) (Math.PI / 180.0));
                float z = -Mth.cos(rot * (float) (Math.PI / 180.0));
                if (!((double) strength <= 0.0)) {
                    launch.hasImpulse = true;
                    Vec3 vec3 = this.m_20184_();
                    Vec3 vec31 = vec3.add(new Vec3((double) x, 0.0, (double) z).normalize().scale((double) strength));
                    launch.setDeltaMovement(vec31.x, (double) strength, vec31.z);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_BUCK && this.getAnimationTick() >= 5) {
            Entity passenger = this.getControllingPassenger();
            if (passenger instanceof LivingEntity) {
                this.entityToLaunchId = passenger.getId();
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20160_()) {
                this.ridingTime++;
                if (this.ridingTime >= this.getMaxRidingTime() && this.getAnimation() != ANIMATION_BUCK) {
                    this.setAnimation(ANIMATION_BUCK);
                }
            } else {
                this.ridingTime = 0;
            }
            if (this.m_6084_() && this.ridingTime > 0 && this.m_20184_().horizontalDistanceSqr() > 0.1) {
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(1.0))) {
                    if (!(entity instanceof EntityTusklin) && !entity.isPassengerOfSameVehicle(this)) {
                        entity.hurt(this.m_269291_().mobAttack(this), 4.0F + this.f_19796_.nextFloat() * 3.0F);
                        if (entity.onGround()) {
                            double d0 = entity.getX() - this.m_20185_();
                            double d1 = entity.getZ() - this.m_20189_();
                            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
                            float f = 0.5F;
                            entity.push(d0 / d2 * (double) f, (double) f, d1 / d2 * (double) f);
                        }
                    }
                }
                this.m_274367_(2.0F);
            } else {
                this.m_274367_(1.1F);
            }
            if (this.m_5448_() != null && this.m_142582_(this.m_5448_()) && this.m_20270_(this.m_5448_()) < this.m_5448_().m_20205_() + this.m_20205_() + 1.8F) {
                if (this.getAnimation() == ANIMATION_FLING && this.getAnimationTick() == 6) {
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                    this.knockbackTarget(this.m_5448_(), 0.9F, 0.0F);
                }
                if (this.getAnimation() == ANIMATION_GORE_L && this.getAnimationTick() == 6) {
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                    this.knockbackTarget(this.m_5448_(), 0.5F, -90.0F);
                }
                if (this.getAnimation() == ANIMATION_GORE_R && this.getAnimationTick() == 6) {
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                    this.knockbackTarget(this.m_5448_(), 0.5F, 90.0F);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_RUT && this.getAnimationTick() == 23 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK) && this.m_217043_().nextInt(3) == 0) {
            if (this.m_6162_() && this.m_9236_().getBlockState(this.m_20183_()).m_247087_() && this.f_19796_.nextInt(3) == 0) {
                this.m_9236_().setBlockAndUpdate(this.m_20183_(), Blocks.BROWN_MUSHROOM.defaultBlockState());
                this.m_146850_(GameEvent.BLOCK_DESTROY);
                this.m_5496_(SoundEvents.CROP_PLANTED, this.m_6121_(), this.m_6100_());
            }
            this.m_9236_().m_46796_(2001, this.m_20183_().below(), Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
            this.m_9236_().setBlock(this.m_20183_().below(), Blocks.DIRT.defaultBlockState(), 2);
            this.m_5634_(5.0F);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == NO_ANIMATION && this.m_217043_().nextInt(this.m_6162_() ? 140 : 70) == 0 && (this.m_21188_() == null || this.m_20270_(this.m_21188_()) > 30.0F) && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK) && this.m_217043_().nextInt(3) == 0) {
            this.setAnimation(ANIMATION_RUT);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    private float getLaunchStrength() {
        return this.getShoeStack().is(AMItemRegistry.PIGSHOES.get()) ? 0.4F : 0.9F;
    }

    private int getMaxRidingTime() {
        return this.getShoeStack().is(AMItemRegistry.PIGSHOES.get()) ? 160 : 60;
    }

    private void knockbackTarget(LivingEntity entity, float strength, float angle) {
        float rot = this.m_146908_() + angle;
        if (entity != null) {
            entity.knockback((double) strength, (double) Mth.sin(rot * (float) (Math.PI / 180.0)), (double) (-Mth.cos(rot * (float) (Math.PI / 180.0))));
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMob.AgeableMobGroupData(0.34F);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_RUT, ANIMATION_GORE_L, ANIMATION_GORE_R, ANIMATION_FLING, ANIMATION_BUCK };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return AMEntityRegistry.TUSKLIN.get().create(this.m_9236_());
    }
}