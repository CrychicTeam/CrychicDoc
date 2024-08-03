package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PewenBranchBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AtlatitanMeleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AtlatitanNibbleTreesGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.LookForwardsGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AtlatitanEntity extends SauropodBaseEntity implements KeybindUsingMount {

    private static final EntityDataAccessor<Optional<BlockPos>> EATING_POS = SynchedEntityData.defineId(AtlatitanEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Optional<BlockState>> LAST_EATEN_BLOCK = SynchedEntityData.defineId(AtlatitanEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final EntityDataAccessor<Integer> RIDEABLE_FOR = SynchedEntityData.defineId(AtlatitanEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> METER_AMOUNT = SynchedEntityData.defineId(AtlatitanEntity.class, EntityDataSerializers.FLOAT);

    public AtlatitanEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AtlatitanMeleeGoal(this));
        this.f_21345_.addGoal(2, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(ACBlockRegistry.TREE_STAR.get()), false));
        this.f_21345_.addGoal(5, new AtlatitanNibbleTreesGoal(this, 30));
        this.f_21345_.addGoal(6, new RandomStrollGoal(this, 1.0, 50) {

            @Override
            protected Vec3 getPosition() {
                return DefaultRandomPos.getPos(this.f_25725_, 30, 7);
            }
        });
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Mob.class, 10.0F));
        this.f_21345_.addGoal(10, new LookForwardsGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this, AtlatitanEntity.class));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(EATING_POS, Optional.empty());
        this.f_19804_.define(LAST_EATEN_BLOCK, Optional.empty());
        this.f_19804_.define(RIDEABLE_FOR, 0);
        this.f_19804_.define(METER_AMOUNT, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.325).add(Attributes.MAX_HEALTH, 400.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0).add(Attributes.ATTACK_DAMAGE, 8.0);
    }

    @Override
    protected void onStep() {
        if (!this.m_6162_()) {
            if (this.screenShakeAmount <= 1.0F) {
                this.m_5496_(ACSoundRegistry.ATLATITAN_STEP.get(), 2.0F, 1.0F);
            }
            if (this.screenShakeAmount <= 1.0F) {
                this.screenShakeAmount = 1.0F;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide) {
            if (this.getAnimation() == ANIMATION_EAT_LEAVES && this.getAnimationTick() > 35 && this.getAnimationTick() < 90) {
                BlockState lastEatenBlock = this.getLastEatenBlock();
                if (lastEatenBlock != null) {
                    Vec3 crumbPos = this.headPart.m_20182_().add((double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * this.getScale()), (double) ((0.5F + (this.f_19796_.nextFloat() - 0.5F) * 0.2F) * this.getScale()), (double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * this.getScale()));
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, lastEatenBlock), crumbPos.x, crumbPos.y, crumbPos.z, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1);
                }
            }
            if (this.getRideableFor() > 0 && this.m_9236_().random.nextInt(2) == 0 && !this.isDancing()) {
                Vec3 particlePos = this.headPart.m_20182_().add((double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * this.getScale()), (double) (this.f_19796_.nextFloat() * 2.0F * this.getScale()), (double) ((this.f_19796_.nextFloat() - 0.5F) * 2.0F * this.getScale())).add(this.m_20184_());
                this.m_9236_().addParticle(ACParticleRegistry.HAPPINESS.get(), particlePos.x, particlePos.y, particlePos.z, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1, ((double) this.f_19796_.nextFloat() - 0.5) * 0.1);
            }
            Player player = AlexsCaves.PROXY.getClientSidePlayer();
            if (player != null && player.m_20365_(this) && AlexsCaves.PROXY.isKeyDown(2) && this.getMeterAmount() >= 1.0F) {
                AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 2));
            }
        } else {
            if (this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 30) {
                this.m_5496_(ACSoundRegistry.ATLATITAN_STOMP.get(), 3.0F, 1.0F);
                if (this.screenShakeAmount < 4.0F) {
                    this.screenShakeAmount = 4.0F;
                }
                this.crushBlocksInRing(15, this.m_146903_(), this.m_146907_(), 1.0F);
                if (this.m_20160_() && !this.m_9236_().isClientSide) {
                    for (Entity passenger : this.m_20197_()) {
                        ACAdvancementTriggerRegistry.ATLATITAN_STOMP.triggerForEntity(passenger);
                    }
                }
            }
            if (this.getRideableFor() > 0) {
                this.setRideableFor(this.getRideableFor() - 1);
            }
            if (this.f_19797_ % 100 == 0 && this.m_21223_() < this.m_21233_()) {
                this.m_5634_(2.0F);
            }
            if (this.getAnimation() == ANIMATION_RIGHT_KICK && this.getAnimationTick() == 8) {
                Vec3 armPos = this.m_20182_().add(this.rotateOffsetVec(new Vec3(-2.0, 0.0, 2.5), 0.0F, this.f_20883_));
                this.hurtEntitiesAround(armPos, 5.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 0.8F, 1.0F, false, false);
            }
            if (this.getAnimation() == ANIMATION_LEFT_KICK && this.getAnimationTick() == 8) {
                Vec3 armPos = this.m_20182_().add(this.rotateOffsetVec(new Vec3(2.0, 0.0, 2.5), 0.0F, this.f_20883_));
                this.hurtEntitiesAround(armPos, 5.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE) * 0.8F, 1.0F, false, false);
            }
            if ((this.getAnimation() == ANIMATION_LEFT_WHIP || this.getAnimation() == ANIMATION_RIGHT_WHIP) && this.getAnimationTick() > 20 && this.getAnimationTick() < 30) {
                this.hurtEntitiesAround(this.tailPart2.m_20182_(), 12.0F, (float) this.m_21133_(Attributes.ATTACK_DAMAGE), 1.0F, false, false);
            }
        }
        if (this.m_20160_()) {
            if (this.getMeterAmount() < 1.0F) {
                this.setMeterAmount(Math.min(this.getMeterAmount() + 0.0025F, 1.0F));
            }
        } else {
            this.setMeterAmount(0.0F);
        }
    }

    public BlockPos getEatingPos() {
        return (BlockPos) this.f_19804_.get(EATING_POS).orElse(null);
    }

    public void setEatingPos(BlockPos eatingPos) {
        this.f_19804_.set(EATING_POS, Optional.ofNullable(eatingPos));
    }

    public BlockState getLastEatenBlock() {
        return (BlockState) this.f_19804_.get(LAST_EATEN_BLOCK).orElse(null);
    }

    public void setLastEatenBlock(BlockState eatingPos) {
        this.f_19804_.set(LAST_EATEN_BLOCK, Optional.ofNullable(eatingPos));
    }

    public void setRideableFor(int time) {
        this.f_19804_.set(RIDEABLE_FOR, time);
    }

    public int getRideableFor() {
        return this.f_19804_.get(RIDEABLE_FOR);
    }

    @Override
    public boolean onFeedMixture(ItemStack itemStack, Player player) {
        if (itemStack.is(ACItemRegistry.SERENE_SALAD.get())) {
            this.setRideableFor(12000);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ACBlockRegistry.TREE_STAR.get().asItem());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setRideableFor(compound.getInt("RideableTime"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putInt("RideableTime", this.getRideableFor());
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this) && type == 2 && this.getMeterAmount() >= 1.0F && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
            this.f_20883_ = keyPresser.getYHeadRot();
            this.m_146922_(keyPresser.getYHeadRot());
            this.setAnimation(ANIMATION_STOMP);
            this.setMeterAmount(0.0F);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult prev = super.m_6071_(player, hand);
        ItemStack itemstack = player.m_21120_(hand);
        if (!prev.consumesAction() && itemstack.is(ACItemRegistry.SERENE_SALAD.get()) && !this.m_6162_()) {
            if (!itemstack.getCraftingRemainingItem().isEmpty()) {
                this.m_19983_(itemstack.getCraftingRemainingItem().copy());
            }
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        } else {
            if (!prev.consumesAction() && this.getRideableFor() > 0 && !this.m_6162_() && this.m_7310_(player)) {
                player.m_20329_(this);
            }
            return prev;
        }
    }

    @Override
    public float getTargetNeckXRot() {
        if (this.getAnimation() == ANIMATION_EAT_LEAVES && this.getAnimationTick() <= 35) {
            BlockPos eatingPos = this.getEatingPos();
            if (eatingPos != null) {
                float peckDist = (float) Mth.clamp((double) ((float) eatingPos.m_123342_() + 0.5F) - this.m_20186_(), -6.0, 12.0) - 6.0F;
                return peckDist * 1.2F / 6.0F * -90.0F;
            }
        }
        return super.getTargetNeckXRot();
    }

    public BlockPos getStandAtTreePos(BlockPos target) {
        Vec3 vec3 = Vec3.atCenterOf(target).subtract(this.m_20182_());
        float f = -((float) Mth.atan2(vec3.x, vec3.z)) * 180.0F / (float) Math.PI;
        BlockState state = this.m_9236_().getBlockState(target);
        Direction dir = Direction.fromYRot((double) f);
        if (state.m_60713_(ACBlockRegistry.PEWEN_BRANCH.get())) {
            dir = Direction.fromYRot((double) ((Integer) state.m_61143_(PewenBranchBlock.ROTATION) * 45));
        }
        if (this.m_9236_().getBlockState(target.below()).m_60795_()) {
            target = target.relative(dir);
        }
        return target.relative(dir.getOpposite(), (int) Math.floor((double) (13.0F * this.getScale()))).atY((int) this.m_20186_());
    }

    public boolean lockTreePosition(BlockPos target) {
        Vec3 vec3 = Vec3.atCenterOf(target).subtract(this.m_20182_());
        float f = -((float) Mth.atan2(vec3.x, vec3.z)) * 180.0F / (float) Math.PI;
        int headDistToBody = (int) Math.floor((double) (14.0F * this.getScale()));
        BlockState state = this.m_9236_().getBlockState(target);
        Direction dir = Direction.fromYRot((double) f);
        if (state.m_60713_(ACBlockRegistry.PEWEN_BRANCH.get())) {
            dir = Direction.fromYRot((double) ((Integer) state.m_61143_(PewenBranchBlock.ROTATION) * 45));
        }
        float targetRot = Mth.approachDegrees(this.m_146908_(), dir.toYRot(), 20.0F);
        this.m_146922_(targetRot);
        this.m_5616_(targetRot);
        this.f_20883_ = Mth.approachDegrees(this.f_20883_, targetRot, 10.0F);
        this.m_21563_().setLookAt((double) target.m_123341_(), (double) target.m_123342_(), (double) target.m_123343_());
        if (this.m_9236_().getBlockState(target.below()).m_60795_()) {
            target = target.relative(dir);
        }
        Vec3 vec31 = Vec3.atCenterOf(target.relative(dir.getOpposite(), headDistToBody - 1));
        if (vec31.distanceToSqr(this.m_20185_(), vec31.y, this.m_20189_()) > 1.0) {
            this.m_21566_().setWantedPosition(vec31.x, this.m_20186_(), vec31.z, 1.0);
        }
        return this.m_20275_(vec31.x, this.m_20186_(), vec31.z) < (double) headDistToBody && Mth.degreesDifferenceAbs(this.f_20883_, dir.toYRot()) < 7.0F;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
        return new Vec3((double) (player.f_20900_ * 0.35F), 0.0, (double) (player.f_20902_ * 0.8F * f));
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ == 0.0F && player.f_20900_ == 0.0F) {
            this.f_19804_.set(WALKING, false);
        } else {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.m_6710_(null);
            this.f_19804_.set(WALKING, true);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        float f1 = 0.0F;
        if (this.areLegsMoving()) {
            float f = this.getLegSlamAmount(2.0F, 0.66F);
            float threshold = 0.65F;
            if (f >= threshold) {
                f1 = (f - threshold) / (1.0F - threshold);
            }
        }
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED) * f1;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            float seatY = 0.5F;
            float seatZ = 0.5F;
            if (this.getAnimation() == ANIMATION_STOMP) {
                float animationIntensity = ACMath.cullAnimationTick(this.getAnimationTick(), 1.0F, ANIMATION_STOMP, 1.0F, 0, 30);
                seatY += animationIntensity * 1.5F;
                seatZ += animationIntensity * -4.5F;
            }
            Vec3 seatOffset = new Vec3(0.0, (double) seatY, (double) seatZ).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            passenger.setYBodyRot(this.f_20883_);
            passenger.fallDistance = 0.0F;
            this.clampRotation(living, 105.0F);
            moveFunction.accept(passenger, this.m_20185_() + seatOffset.x, this.m_20186_() + seatOffset.y + this.m_6048_() - (double) this.getLegSolverBodyOffset(), this.m_20189_() + seatOffset.z);
            return;
        }
        super.m_19956_(passenger, moveFunction);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        return new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
    }

    @Override
    public int getExperienceReward() {
        return 30;
    }

    @Override
    public boolean hasRidingMeter() {
        return true;
    }

    @Override
    public float getMeterAmount() {
        return this.f_19804_.get(METER_AMOUNT);
    }

    public void setMeterAmount(float roarPower) {
        this.f_19804_.set(METER_AMOUNT, roarPower);
    }

    @Override
    public BlockState createEggBlockState() {
        return ACBlockRegistry.ATLATITAN_EGG.get().defaultBlockState();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return ACEntityRegistry.ATLATITAN.get().create(level);
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.15F : 1.0F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.ATLATITAN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.ATLATITAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.ATLATITAN_DEATH.get();
    }
}