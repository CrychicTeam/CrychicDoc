package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EntityBananaSlug extends Animal {

    private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(EntityBananaSlug.class, EntityDataSerializers.DIRECTION);

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntityBananaSlug.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityBananaSlug.class, EntityDataSerializers.INT);

    private static final Direction[] POSSIBLE_DIRECTIONS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public float trailYaw;

    public float prevTrailYaw;

    public float trailVisability;

    public float prevTrailVisability;

    public float attachChangeProgress = 0.0F;

    public float prevAttachChangeProgress = 0.0F;

    public Direction prevAttachDir = Direction.DOWN;

    public int timeUntilSlime = this.f_19796_.nextInt(12000) + 24000;

    protected EntityBananaSlug(EntityType<? extends Animal> animal, Level level) {
        super(animal, level);
        this.prevTrailYaw = this.f_20883_;
        this.trailYaw = this.f_20883_;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BANANA_SLUG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BANANA_SLUG_HURT.get();
    }

    public static boolean checkBananaSlugSpawnRules(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return !worldIn.m_8055_(pos.below()).m_60795_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.bananaSlugSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CLIMBING, (byte) 0);
        this.f_19804_.define(ATTACHED_FACE, Direction.DOWN);
        this.f_19804_.define(VARIANT, 0);
    }

    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void onInsideBlock(BlockState state) {
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.f_19796_.nextInt(4));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return (this.f_19804_.get(CLIMBING) & 1) != 0 && this.getAttachmentFacing() != Direction.DOWN;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.BROWN_MUSHROOM);
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.f_19804_.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.f_19804_.set(CLIMBING, b0);
    }

    public Direction getAttachmentFacing() {
        return this.f_19804_.get(ATTACHED_FACE);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.0, Ingredient.of(Items.BROWN_MUSHROOM), false));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalAIWanderRanged(this, 40, 1.0, 10, 7));
        this.f_21345_.addGoal(5, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevTrailYaw = this.trailYaw;
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.getMaxHeadYRot());
        this.trailYaw = Mth.approachDegrees(this.trailYaw, this.f_20883_, 2.0F);
        this.prevTrailVisability = this.trailVisability;
        this.prevAttachChangeProgress = this.attachChangeProgress;
        boolean showTrail = this.isTrailVisible() && this.m_20184_().length() > 0.03F;
        if (this.prevAttachDir != this.getAttachmentFacing()) {
            if (this.attachChangeProgress < 5.0F) {
                this.attachChangeProgress++;
                this.trailYaw = this.f_20883_;
            } else if (this.attachChangeProgress >= 5.0F) {
                this.prevAttachDir = this.getAttachmentFacing();
            }
        } else {
            this.attachChangeProgress = 5.0F;
        }
        if (this.trailVisability < 1.0F && showTrail) {
            this.trailVisability = Math.min(1.0F, this.trailVisability + 0.1F);
        }
        if (this.trailVisability > 0.0F && !showTrail) {
            float dec = this.m_20184_().length() > 0.03F ? 1.0F : 0.1F;
            this.trailVisability = Math.max(0.0F, this.trailVisability - dec);
        }
        Vec3 vector3d = this.m_20184_();
        if (!this.m_9236_().isClientSide) {
            this.setBesideClimbableBlock(this.f_19862_);
            this.setBesideClimbableBlock(this.f_19862_ || this.f_19863_ && !this.m_20096_());
            if (this.m_20096_() || this.m_20072_() || this.m_20077_()) {
                this.f_19804_.set(ATTACHED_FACE, Direction.DOWN);
            } else if (this.f_19863_) {
                this.f_19804_.set(ATTACHED_FACE, Direction.UP);
            } else {
                boolean flag = false;
                Direction closestDirection = Direction.DOWN;
                double closestDistance = 100.0;
                for (Direction dir : POSSIBLE_DIRECTIONS) {
                    BlockPos antPos = new BlockPos(Mth.floor(this.m_20185_()), Mth.floor(this.m_20186_()), Mth.floor(this.m_20189_()));
                    BlockPos offsetPos = antPos.relative(dir);
                    Vec3 offset = Vec3.atCenterOf(offsetPos);
                    if (closestDistance > this.m_20182_().distanceTo(offset) && this.m_9236_().loadedAndEntityCanStandOnFace(offsetPos, this, dir.getOpposite())) {
                        closestDistance = this.m_20182_().distanceTo(offset);
                        closestDirection = dir;
                    }
                }
                this.f_19804_.set(ATTACHED_FACE, closestDirection);
            }
        }
        boolean flag = false;
        if (this.getAttachmentFacing() != Direction.DOWN) {
            if (this.getAttachmentFacing() == Direction.UP) {
                this.m_20256_(this.m_20184_().add(0.0, 1.0, 0.0));
            } else {
                if (!this.f_19862_ && this.getAttachmentFacing() != Direction.UP) {
                    Vec3 vec = Vec3.atLowerCornerOf(this.getAttachmentFacing().getNormal());
                    this.m_20256_(this.m_20184_().add(vec.normalize().multiply(0.1F, 0.1F, 0.1F)));
                }
                if (!this.m_20096_() && vector3d.y < 0.0) {
                    this.m_20256_(this.m_20184_().multiply(1.0, 0.5, 1.0));
                    flag = true;
                }
            }
        }
        if (this.getAttachmentFacing() == Direction.UP) {
            this.m_20242_(true);
            this.m_20256_(vector3d.multiply(0.7, 1.0, 0.7));
        } else {
            this.m_20242_(false);
        }
        if (!flag && this.onClimbable()) {
            this.m_20256_(vector3d.multiply(1.0, 0.4, 1.0));
        }
        if (!this.m_9236_().isClientSide && this.m_6084_() && !this.m_6162_() && --this.timeUntilSlime <= 0) {
            this.m_19998_(AMItemRegistry.BANANA_SLUG_SLIME.get());
            this.timeUntilSlime = this.f_19796_.nextInt(12000) + 24000;
        }
    }

    @Override
    protected float getJumpPower() {
        return super.m_6118_();
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 4;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private boolean isTrailVisible() {
        if (this.m_20072_()) {
            return false;
        } else if (this.m_20096_()) {
            Vec3 modelBack = new Vec3(0.0, -0.1F, this.m_6162_() ? -0.35F : -0.7F).yRot(-this.trailYaw * (float) (Math.PI / 180.0));
            Vec3 slugBack = this.m_20182_().add(modelBack);
            BlockPos backPos = AMBlockPos.fromVec3(slugBack);
            BlockState state = this.m_9236_().getBlockState(backPos);
            VoxelShape shape = state.m_60812_(this.m_9236_(), backPos);
            if (shape.isEmpty()) {
                return false;
            } else {
                Optional<Vec3> closest = shape.closestPointTo(modelBack.add(0.0, 1.0, 0.0));
                return closest.isPresent() && Math.min((float) ((Vec3) closest.get()).y, 1.0F) >= 0.8F;
            }
        } else if (this.getAttachmentFacing().getAxis() != Direction.Axis.Y) {
            BlockPos pos = this.m_20183_().relative(this.getAttachmentFacing()).above(this.m_20184_().y <= -0.001F ? 1 : -1);
            BlockState state = this.m_9236_().getBlockState(pos);
            VoxelShape shape = state.m_60812_(this.m_9236_(), pos);
            return !shape.isEmpty();
        } else {
            return this.getAttachmentFacing() != Direction.DOWN;
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.m_7350_(entityDataAccessor);
        if (ATTACHED_FACE.equals(entityDataAccessor)) {
            this.prevAttachChangeProgress = 0.0F;
            this.attachChangeProgress = 0.0F;
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.5 * (this.m_20186_() - this.f_19855_), this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 16.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        EntityBananaSlug slug = AMEntityRegistry.BANANA_SLUG.get().create(this.m_9236_());
        slug.setVariant(this.getVariant());
        return slug;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("SlimeTime", this.timeUntilSlime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("SlimeTime")) {
            this.timeUntilSlime = compound.getInt("SlimeTime");
        }
        this.setVariant(compound.getInt("Variant"));
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int i) {
        this.f_19804_.set(VARIANT, i);
    }
}