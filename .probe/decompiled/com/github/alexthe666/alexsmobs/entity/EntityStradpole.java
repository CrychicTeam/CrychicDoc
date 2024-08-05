package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;

public class EntityStradpole extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityStradpole.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DESPAWN_SOON = SynchedEntityData.defineId(EntityStradpole.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(EntityStradpole.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityStradpole.class, EntityDataSerializers.OPTIONAL_UUID);

    public float swimPitch = 0.0F;

    public float prevSwimPitch = 0.0F;

    private int despawnTimer = 0;

    private int ricochetCount = 0;

    protected EntityStradpole(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.f_21342_ = new AquaticMoveController(this, 1.4F);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.STRADPOLE_BUCKET.get());
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
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() == AMItemRegistry.MOSQUITO_LARVA.get()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            if (this.f_19796_.nextFloat() < 0.45F) {
                EntityStraddler straddler = AMEntityRegistry.STRADDLER.get().create(this.m_9236_());
                straddler.m_20359_(this);
                if (!this.m_9236_().isClientSide && this.m_9236_().m_7967_(straddler)) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if (itemstack.getItem() == Items.LAVA_BUCKET && this.m_6084_()) {
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = this.getBucketItemStack();
            this.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.m_21008_(hand, itemstack2);
            Level level = this.m_9236_();
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }
            this.m_146870_();
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.m_6071_(player, hand);
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(DESPAWN_SOON, false);
        this.f_19804_.define(LAUNCHED, false);
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

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.getParentId() != null) {
            compound.putUUID("ParentUUID", this.getParentId());
        }
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("DespawnSoon", this.isDespawnSoon());
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.stradpoleSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canStradpoleSpawn(EntityType<EntityStradpole> p_234314_0_, LevelAccessor p_234314_1_, MobSpawnType p_234314_2_, BlockPos p_234314_3_, RandomSource p_234314_4_) {
        return p_234314_1_.m_6425_(p_234314_3_).is(FluidTags.LAVA) && !p_234314_1_.m_6425_(p_234314_3_.below()).is(FluidTags.LAVA) ? p_234314_1_.m_46859_(p_234314_3_.above()) : false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.hasUUID("ParentUUID")) {
            this.setParentId(compound.getUUID("ParentUUID"));
        }
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setDespawnSoon(compound.getBoolean("DespawnSoon"));
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new EntityStradpole.StradpoleAISwim(this, 1.0, 10));
        this.f_21345_.addGoal(4, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return !worldIn.m_8055_(pos).m_60819_().isEmpty() ? 15.0F : Float.NEGATIVE_INFINITY;
    }

    public boolean isDespawnSoon() {
        return this.f_19804_.get(DESPAWN_SOON);
    }

    public void setDespawnSoon(boolean despawnSoon) {
        this.f_19804_.set(DESPAWN_SOON, despawnSoon);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new BoneSerpentPathNavigator(this, worldIn);
    }

    @Override
    public void tick() {
        float f = 1.0F;
        if (this.f_19804_.get(LAUNCHED)) {
            this.f_20883_ = this.m_146908_();
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
                this.onImpact(raytraceresult);
            }
            f = 0.1F;
        }
        super.m_8119_();
        boolean liquid = this.m_20069_() || this.m_20077_();
        this.prevSwimPitch = this.swimPitch;
        this.swimPitch = (float) (-((double) ((float) this.m_20184_().y * (liquid ? 2.5F : f)) * 180.0F / (float) Math.PI));
        if (this.m_20096_() && !this.m_20069_() && !this.m_20077_()) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_6853_(false);
            this.f_19812_ = true;
        }
        this.m_20242_(false);
        if (liquid) {
            this.m_20242_(true);
        }
        if (this.isDespawnSoon()) {
            this.despawnTimer++;
            if (this.despawnTimer > 100) {
                this.despawnTimer = 0;
                this.m_21373_();
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private void onImpact(HitResult raytraceresult) {
        HitResult.Type raytraceresult$type = raytraceresult.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) raytraceresult);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            BlockHitResult traceResult = (BlockHitResult) raytraceresult;
            BlockState blockstate = this.m_9236_().getBlockState(traceResult.getBlockPos());
            if (!blockstate.m_60816_(this.m_9236_(), traceResult.getBlockPos()).isEmpty()) {
                Direction face = traceResult.getDirection();
                Vec3 prevMotion = this.m_20184_();
                double motionX = prevMotion.x();
                double motionY = prevMotion.y();
                double motionZ = prevMotion.z();
                switch(face) {
                    case EAST:
                    case WEST:
                        motionX = -motionX;
                        break;
                    case SOUTH:
                    case NORTH:
                        motionZ = -motionZ;
                        break;
                    default:
                        motionY = -motionY;
                }
                this.m_20334_(motionX, motionY, motionZ);
                if (this.f_19797_ <= 200 && this.ricochetCount <= 20) {
                    this.ricochetCount++;
                } else {
                    this.f_19804_.set(LAUNCHED, false);
                }
            }
        }
    }

    public Entity getParent() {
        UUID id = this.getParentId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    private void onEntityHit(EntityHitResult raytraceresult) {
        Entity entity = this.getParent();
        if (entity instanceof LivingEntity && !this.m_9236_().isClientSide && raytraceresult.getEntity() instanceof LivingEntity target) {
            if (!target.isBlocking()) {
                target.hurt(this.m_269291_().mobProjectile(this, (LivingEntity) entity), 3.0F);
                target.knockback(0.7F, entity.getX() - this.m_20185_(), entity.getZ() - this.m_20189_());
            } else if (this.m_5448_() instanceof Player) {
                this.damageShieldFor((Player) this.m_5448_(), 3.0F);
            }
            this.f_19804_.set(LAUNCHED, false);
        }
    }

    protected void damageShieldFor(Player holder, float damage) {
        if (holder.m_21211_().canPerformAction(ToolActions.SHIELD_BLOCK)) {
            if (!this.m_9236_().isClientSide) {
                holder.awardStat(Stats.ITEM_USED.get(holder.m_21211_().getItem()));
            }
            if (damage >= 3.0F) {
                int i = 1 + Mth.floor(damage);
                InteractionHand hand = holder.m_7655_();
                holder.m_21211_().hurtAndBreak(i, holder, p_213833_1_ -> {
                    p_213833_1_.m_21190_(hand);
                    ForgeEventFactory.onPlayerDestroyItem(holder, holder.m_21211_(), hand);
                });
                if (holder.m_21211_().isEmpty()) {
                    if (hand == InteractionHand.MAIN_HAND) {
                        holder.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        holder.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                    holder.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
                }
            }
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        return !p_230298_1_.isSpectator() && !(p_230298_1_ instanceof EntityStraddler) && !(p_230298_1_ instanceof EntityStradpole);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return p_230285_1_.is(FluidTags.LAVA);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && (this.m_20069_() || this.m_20077_())) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.05, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void handleAirSupply(int p_209207_1_) {
    }

    public void shoot(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        Vec3 lvt_9_1_ = new Vec3(p_70186_1_, p_70186_3_, p_70186_5_).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) p_70186_8_, this.f_19796_.nextGaussian() * 0.0075F * (double) p_70186_8_, this.f_19796_.nextGaussian() * 0.0075F * (double) p_70186_8_).scale((double) p_70186_7_);
        this.m_20256_(lvt_9_1_);
        float lvt_10_1_ = (float) lvt_9_1_.horizontalDistanceSqr();
        this.m_146922_((float) (Mth.atan2(lvt_9_1_.x, lvt_9_1_.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(lvt_9_1_.y, (double) lvt_10_1_) * 180.0F / (float) Math.PI));
        this.f_19860_ = this.m_146909_();
        this.f_20883_ = this.m_146908_();
        this.f_20885_ = this.m_146908_();
        this.f_20886_ = this.m_146908_();
        this.f_19859_ = this.m_146908_();
        this.setDespawnSoon(true);
        this.f_19804_.set(LAUNCHED, true);
    }

    static class StradpoleAISwim extends RandomStrollGoal {

        public StradpoleAISwim(EntityStradpole creature, double speed, int chance) {
            super(creature, speed, chance, false);
        }

        @Override
        public boolean canUse() {
            if ((this.f_25725_.m_20077_() || this.f_25725_.m_20069_()) && !this.f_25725_.m_20159_() && this.f_25725_.m_5448_() == null && (this.f_25725_.m_20069_() || this.f_25725_.m_20077_() || !(this.f_25725_ instanceof ISemiAquatic) || ((ISemiAquatic) this.f_25725_).shouldEnterWater())) {
                if (!this.f_25731_ && this.f_25725_.m_217043_().nextInt(this.f_25730_) != 0) {
                    return false;
                } else {
                    Vec3 vector3d = this.getPosition();
                    if (vector3d == null) {
                        return false;
                    } else {
                        this.f_25726_ = vector3d.x;
                        this.f_25727_ = vector3d.y;
                        this.f_25728_ = vector3d.z;
                        this.f_25731_ = false;
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            if (this.f_25725_.m_217043_().nextFloat() < 0.3F) {
                Vec3 vector3d = this.findSurfaceTarget(this.f_25725_, 15, 7);
                if (vector3d != null) {
                    return vector3d;
                }
            }
            Vec3 vector3d = LandRandomPos.getPos(this.f_25725_, 7, 3);
            int i = 0;
            while (vector3d != null && !this.f_25725_.m_9236_().getFluidState(AMBlockPos.fromVec3(vector3d)).is(FluidTags.LAVA) && !this.f_25725_.m_9236_().getBlockState(AMBlockPos.fromVec3(vector3d)).m_60647_(this.f_25725_.m_9236_(), AMBlockPos.fromVec3(vector3d), PathComputationType.WATER) && i++ < 15) {
                vector3d = LandRandomPos.getPos(this.f_25725_, 10, 7);
            }
            return vector3d;
        }

        private boolean canJumpTo(BlockPos pos, int dx, int dz, int scale) {
            BlockPos blockpos = pos.offset(dx * scale, 0, dz * scale);
            return this.f_25725_.m_9236_().getFluidState(blockpos).is(FluidTags.LAVA) || this.f_25725_.m_9236_().getFluidState(blockpos).is(FluidTags.WATER) && !this.f_25725_.m_9236_().getBlockState(blockpos).m_280555_();
        }

        private boolean isAirAbove(BlockPos pos, int dx, int dz, int scale) {
            return this.f_25725_.m_9236_().getBlockState(pos.offset(dx * scale, 1, dz * scale)).m_60795_() && this.f_25725_.m_9236_().getBlockState(pos.offset(dx * scale, 2, dz * scale)).m_60795_();
        }

        private Vec3 findSurfaceTarget(PathfinderMob creature, int i, int i1) {
            BlockPos upPos = creature.m_20183_();
            while (creature.m_9236_().getFluidState(upPos).is(FluidTags.WATER) || creature.m_9236_().getFluidState(upPos).is(FluidTags.LAVA)) {
                upPos = upPos.above();
            }
            return this.isAirAbove(upPos.below(), 0, 0, 0) && this.canJumpTo(upPos.below(), 0, 0, 0) ? new Vec3((double) ((float) upPos.m_123341_() + 0.5F), (double) ((float) upPos.m_123342_() - 1.0F), (double) ((float) upPos.m_123343_() + 0.5F)) : null;
        }
    }
}