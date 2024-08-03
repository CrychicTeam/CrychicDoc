package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.CosmicCodAIFollowLeader;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;

public class EntityCosmicCod extends Mob implements Bucketable {

    private static final EntityDataAccessor<Float> FISH_PITCH = SynchedEntityData.defineId(EntityCosmicCod.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityCosmicCod.class, EntityDataSerializers.BOOLEAN);

    public float prevFishPitch;

    private int baitballCooldown = 100 + this.f_19796_.nextInt(100);

    private int circleTime = 0;

    private int maxCircleTime = 300;

    private BlockPos circlePos;

    private int teleportIn;

    private EntityCosmicCod groupLeader;

    private int groupSize = 1;

    protected EntityCosmicCod(EntityType<? extends Mob> mob, Level level) {
        super(mob, level);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.f_21342_ = new FlightMoveController(this, 1.0F, false, true);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cosmicCodSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.COSMIC_COD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.COSMIC_COD_HURT.get();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.f_21345_.addGoal(0, new EntityCosmicCod.AISwimIdle(this));
        this.f_21345_.addGoal(1, new CosmicCodAIFollowLeader(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FISH_PITCH, 0.0F);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean p_203706_1_) {
        this.f_19804_.set(FROM_BUCKET, p_203706_1_);
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.COSMIC_COD_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("CosmicCodData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("CosmicCodData")) {
            this.readAdditionalSaveData(compound.getCompound("CosmicCodData"));
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.m_8077_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    private void doInitialPosing(LevelAccessor world, EntityCosmicCod.GroupData data) {
        BlockPos down = this.m_20183_();
        while (world.m_46859_(down) && down.m_123342_() > -62) {
            down = down.below();
        }
        if (down.m_123342_() <= -60) {
            if (data != null && data.groupLeader != null) {
                this.m_6034_((double) ((float) down.m_123341_() + 0.5F), data.groupLeader.m_20186_() - 1.0 + (double) this.f_19796_.nextInt(1), (double) ((float) down.m_123343_() + 0.5F));
            } else {
                this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 90 + this.f_19796_.nextInt(60)), (double) ((float) down.m_123343_() + 0.5F));
            }
        } else {
            this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 1), (double) ((float) down.m_123343_() + 0.5F));
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevFishPitch = this.getFishPitch();
        if (!this.m_9236_().isClientSide) {
            double ydist = this.f_19855_ - this.m_20186_();
            float fishDist = (float) ((Math.abs(this.m_20184_().x) + Math.abs(this.m_20184_().z)) * 6.0) / this.getPitchSensitivity();
            this.incrementFishPitch((float) ydist * 10.0F * this.getPitchSensitivity());
            this.setFishPitch(Mth.clamp(this.getFishPitch(), -60.0F, 40.0F));
            if (this.getFishPitch() > 2.0F) {
                this.decrementFishPitch(fishDist * Math.abs(this.getFishPitch()) / 90.0F);
            }
            if (this.getFishPitch() < -2.0F) {
                this.incrementFishPitch(fishDist * Math.abs(this.getFishPitch()) / 90.0F);
            }
            if (this.getFishPitch() > 2.0F) {
                this.decrementFishPitch(1.0F);
            } else if (this.getFishPitch() < -2.0F) {
                this.incrementFishPitch(1.0F);
            }
            if (this.baitballCooldown > 0) {
                this.baitballCooldown--;
            }
        }
        if (this.teleportIn > 0) {
            this.teleportIn--;
            if (this.teleportIn == 0 && !this.m_9236_().isClientSide) {
                double range = 8.0;
                AABB bb = new AABB(this.m_20185_() - 8.0, this.m_20186_() - 8.0, this.m_20189_() - 8.0, this.m_20185_() + 8.0, this.m_20186_() + 8.0, this.m_20189_() + 8.0);
                List<EntityCosmicCod> list = this.m_9236_().m_45976_(EntityCosmicCod.class, bb);
                Vec3 vec3 = this.teleport();
                if (vec3 != null) {
                    this.baitballCooldown = 5;
                    for (EntityCosmicCod cod : list) {
                        if (cod != this) {
                            cod.baitballCooldown = 5;
                            cod.teleport(vec3.x, vec3.y, vec3.z);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void handleEntityEvent(byte msg) {
        if (msg == 46) {
            this.m_146850_(GameEvent.TELEPORT);
            this.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
        super.handleEntityEvent(msg);
    }

    public void resetBaitballCooldown() {
        this.baitballCooldown = 120 + this.f_19796_.nextInt(100);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev) {
            this.teleportIn = 5;
        }
        return prev;
    }

    private float getPitchSensitivity() {
        return 3.0F;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushedByWater() {
        return false;
    }

    public float getFishPitch() {
        return this.f_19804_.get(FISH_PITCH);
    }

    public void setFishPitch(float pitch) {
        this.f_19804_.set(FISH_PITCH, pitch);
    }

    public void incrementFishPitch(float pitch) {
        this.f_19804_.set(FISH_PITCH, this.getFishPitch() + pitch);
    }

    public void decrementFishPitch(float pitch) {
        this.f_19804_.set(FISH_PITCH, this.getFishPitch() - pitch);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        double x = (double) ((float) pos.m_123341_() + 0.5F);
        double y = (double) ((float) pos.m_123342_() + 0.5F);
        double z = (double) ((float) pos.m_123343_() + 0.5F);
        HitResult result = this.m_9236_().m_45547_(new ClipContext(this.m_146892_(), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }

    protected Vec3 teleport() {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            double d0 = this.m_20185_() + (this.f_19796_.nextDouble() - 0.5) * 64.0;
            double d1 = this.m_20186_() + (double) (this.f_19796_.nextInt(64) - 32);
            double d2 = this.m_20189_() + (this.f_19796_.nextDouble() - 0.5) * 64.0;
            if (this.teleport(d0, d1, d2)) {
                this.circlePos = null;
                return new Vec3(d0, d1, d2);
            }
        }
        return null;
    }

    private boolean teleport(double x, double y, double z) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(x, y, z);
        BlockState blockstate = this.m_9236_().getBlockState(blockpos$mutableblockpos);
        boolean flag = blockstate.m_60795_();
        if (flag && !blockstate.m_60819_().is(FluidTags.WATER)) {
            this.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(this, x, y, z);
            if (event.isCanceled()) {
                return false;
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 46);
                this.m_6021_(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                return true;
            }
        } else {
            return false;
        }
    }

    public void leaveGroup() {
        if (this.groupLeader != null) {
            this.groupLeader.decreaseGroupSize();
        }
        this.groupLeader = null;
    }

    protected boolean hasNoLeader() {
        return !this.hasGroupLeader();
    }

    public boolean hasGroupLeader() {
        return this.groupLeader != null && this.groupLeader.m_6084_();
    }

    private void increaseGroupSize() {
        this.groupSize++;
    }

    private void decreaseGroupSize() {
        this.groupSize--;
    }

    public boolean canGroupGrow() {
        return this.isGroupLeader() && this.groupSize < this.getMaxGroupSize();
    }

    private int getMaxGroupSize() {
        return 15;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 7;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public boolean isGroupLeader() {
        return this.groupSize > 1;
    }

    public boolean inRangeOfGroupLeader() {
        return this.m_20280_(this.groupLeader) <= 121.0;
    }

    public void moveToGroupLeader() {
        if (this.hasGroupLeader()) {
            this.m_21566_().setWantedPosition(this.groupLeader.m_20185_(), this.groupLeader.m_20186_(), this.groupLeader.m_20189_(), 1.0);
        }
    }

    public EntityCosmicCod createAndSetLeader(EntityCosmicCod leader) {
        this.groupLeader = leader;
        leader.increaseGroupSize();
        return leader;
    }

    public void createFromStream(Stream<EntityCosmicCod> stream) {
        stream.limit((long) (this.getMaxGroupSize() - this.groupSize)).filter(fishe -> fishe != this).forEach(fishe -> fishe.createAndSetLeader(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new EntityCosmicCod.GroupData(this);
        } else {
            this.createAndSetLeader(((EntityCosmicCod.GroupData) spawnDataIn).groupLeader);
        }
        if (reason == MobSpawnType.NATURAL && spawnDataIn instanceof EntityCosmicCod.GroupData) {
            this.doInitialPosing(worldIn, (EntityCosmicCod.GroupData) spawnDataIn);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public boolean isCircling() {
        return this.circlePos != null && this.circleTime < this.maxCircleTime;
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() == Items.BUCKET && this.m_6084_()) {
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = this.getBucketItemStack();
            this.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.m_21008_(hand, itemstack2);
            Level level = this.m_9236_();
            if (!this.m_9236_().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }
            this.m_146870_();
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.mobInteract(player, hand);
        }
    }

    private static class AISwimIdle extends Goal {

        private final EntityCosmicCod cod;

        float circleDistance = 5.0F;

        boolean clockwise = false;

        public AISwimIdle(EntityCosmicCod cod) {
            this.cod = cod;
        }

        @Override
        public boolean canUse() {
            return this.cod.isGroupLeader() || this.cod.hasNoLeader() || this.cod.hasGroupLeader() && this.cod.groupLeader.circlePos != null;
        }

        @Override
        public void tick() {
            if (this.cod.circleTime > this.cod.maxCircleTime) {
                this.cod.circleTime = 0;
                this.cod.circlePos = null;
            }
            if (this.cod.circlePos != null && this.cod.circleTime <= this.cod.maxCircleTime) {
                this.cod.circleTime++;
                Vec3 movePos = this.getSharkCirclePos(this.cod.circlePos);
                this.cod.m_21566_().setWantedPosition(movePos.x(), movePos.y(), movePos.z(), 1.0);
            } else if (this.cod.isGroupLeader()) {
                if (this.cod.baitballCooldown == 0) {
                    this.cod.resetBaitballCooldown();
                    if (this.cod.circlePos == null || this.cod.circleTime >= this.cod.maxCircleTime) {
                        this.cod.circleTime = 0;
                        this.cod.maxCircleTime = 360 + this.cod.f_19796_.nextInt(80);
                        this.circleDistance = 1.0F + this.cod.f_19796_.nextFloat();
                        this.clockwise = this.cod.f_19796_.nextBoolean();
                        this.cod.circlePos = this.cod.m_20183_().above();
                    }
                }
            } else if (this.cod.f_19796_.nextInt(40) == 0 || this.cod.hasNoLeader()) {
                Vec3 movepos = this.cod.m_20182_().add((double) (this.cod.f_19796_.nextInt(4) - 2), this.cod.m_20186_() < 0.0 ? 1.0 : (double) (this.cod.f_19796_.nextInt(4) - 2), (double) (this.cod.f_19796_.nextInt(4) - 2));
                this.cod.m_21566_().setWantedPosition(movepos.x, movepos.y, movepos.z, 1.0);
            } else if (this.cod.hasGroupLeader() && this.cod.groupLeader.circlePos != null && this.cod.circlePos == null) {
                this.cod.circlePos = this.cod.groupLeader.circlePos;
                this.cod.circleTime = this.cod.groupLeader.circleTime;
                this.cod.maxCircleTime = this.cod.groupLeader.maxCircleTime;
                this.circleDistance = 1.0F + this.cod.f_19796_.nextFloat();
                this.clockwise = this.cod.f_19796_.nextBoolean();
            }
        }

        public Vec3 getSharkCirclePos(BlockPos target) {
            float prog = 1.0F - (float) this.cod.circleTime / (float) this.cod.maxCircleTime;
            float angle = (float) (Math.PI / 18) * (float) (this.clockwise ? -this.cod.circleTime : this.cod.circleTime);
            float circleDistanceTimesProg = this.circleDistance * prog;
            double extraX = (double) ((circleDistanceTimesProg + 0.75F) * Mth.sin(angle));
            double extraZ = (double) ((circleDistanceTimesProg + 0.75F) * prog * Mth.cos(angle));
            return new Vec3((double) ((float) target.m_123341_() + 0.5F) + extraX, (double) Math.max(target.m_123342_() + this.cod.f_19796_.nextInt(4) - 2, -62), (double) ((float) target.m_123343_() + 0.5F) + extraZ);
        }
    }

    public static class GroupData extends AgeableMob.AgeableMobGroupData {

        public final EntityCosmicCod groupLeader;

        public GroupData(EntityCosmicCod groupLeaderIn) {
            super(0.05F);
            this.groupLeader = groupLeaderIn;
        }
    }
}