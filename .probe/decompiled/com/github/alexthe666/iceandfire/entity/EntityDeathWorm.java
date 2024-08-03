package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAIAttack;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAIFindSandTarget;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAIGetInSand;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAIJump;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAITarget;
import com.github.alexthe666.iceandfire.entity.ai.DeathWormAIWander;
import com.github.alexthe666.iceandfire.entity.ai.DeathwormAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.EntityGroundAIRide;
import com.github.alexthe666.iceandfire.entity.ai.IAFLookHelper;
import com.github.alexthe666.iceandfire.entity.util.BlockLaunchExplosion;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.IGroundMount;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.message.MessageDeathWormHitbox;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormLand;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateDeathWormSand;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.PositionImpl;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class EntityDeathWorm extends TamableAnimal implements ISyncMount, ICustomCollisions, IBlacklistedFromStatues, IAnimatedEntity, IVillagerFear, IAnimalFear, IGroundMount, IHasCustomizableAttributes, ICustomMoveController {

    public static final ResourceLocation TAN_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_tan");

    public static final ResourceLocation WHITE_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_white");

    public static final ResourceLocation RED_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_red");

    public static final ResourceLocation TAN_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_tan_giant");

    public static final ResourceLocation WHITE_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_white_giant");

    public static final ResourceLocation RED_GIANT_LOOT = new ResourceLocation("iceandfire", "entities/deathworm_red_giant");

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> JUMP_TICKS = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> WORM_AGE = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<BlockPos> HOME = SynchedEntityData.defineId(EntityDeathWorm.class, EntityDataSerializers.BLOCK_POS);

    public static Animation ANIMATION_BITE = Animation.create(10);

    public ChainBuffer tail_buffer;

    public float jumpProgress;

    public float prevJumpProgress;

    private int animationTick;

    private boolean willExplode = false;

    private int ticksTillExplosion = 60;

    private Animation currentAnimation;

    private EntityMutlipartPart[] segments = new EntityMutlipartPart[6];

    private boolean isSandNavigator;

    private final float prevScale = 0.0F;

    private final LookControl lookHelper;

    private int growthCounter = 0;

    private Player thrower;

    public DeathwormAITargetItems targetItemsGoal;

    public EntityDeathWorm(EntityType<EntityDeathWorm> type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.OPEN, 2.0F);
        this.m_21441_(BlockPathTypes.WATER, 4.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 4.0F);
        this.lookHelper = new IAFLookHelper(this);
        this.f_19811_ = true;
        if (worldIn.isClientSide) {
            this.tail_buffer = new ChainBuffer();
        }
        this.m_274367_(1.0F);
        this.switchNavigator(false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new EntityGroundAIRide<>(this));
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new DeathWormAIAttack(this));
        this.f_21345_.addGoal(3, new DeathWormAIJump(this, 12));
        this.f_21345_.addGoal(4, new DeathWormAIFindSandTarget(this, 10));
        this.f_21345_.addGoal(5, new DeathWormAIGetInSand(this, 1.0));
        this.f_21345_.addGoal(6, new DeathWormAIWander(this, 1.0));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, this.targetItemsGoal = new DeathwormAITargetItems(this, false, false));
        this.f_21346_.addGoal(5, new DeathWormAITarget(this, LivingEntity.class, false, new Predicate<LivingEntity>() {

            public boolean apply(@Nullable LivingEntity input) {
                if (EntityDeathWorm.this.m_21824_()) {
                    return input instanceof Monster;
                } else if (input != null) {
                    if (input.m_20069_() || !DragonUtils.isAlive(input) || EntityDeathWorm.this.m_21830_(input)) {
                        return false;
                    } else {
                        return !(input instanceof Player) && !(input instanceof Animal) ? IafConfig.deathWormAttackMonsters : true;
                    }
                } else {
                    return false;
                }
            }
        }));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.deathWormMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, IafConfig.deathWormAttackStrength).add(Attributes.FOLLOW_RANGE, (double) IafConfig.deathWormTargetSearchLength).add(Attributes.ARMOR, 3.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(Math.max(6.0, IafConfig.deathWormMaxHealth));
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(1.0, IafConfig.deathWormAttackStrength));
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) IafConfig.deathWormTargetSearchLength);
    }

    @NotNull
    @Override
    public LookControl getLookControl() {
        return this.lookHelper;
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean getCanSpawnHere() {
        int i = Mth.floor(this.m_20185_());
        int j = Mth.floor(this.m_20191_().minY);
        int k = Mth.floor(this.m_20189_());
        BlockPos blockpos = new BlockPos(i, j, k);
        this.m_9236_().getBlockState(blockpos.below()).m_204336_(BlockTags.SAND);
        return this.m_9236_().getBlockState(blockpos.below()).m_204336_(BlockTags.SAND) && this.m_9236_().m_46803_(blockpos) > 8;
    }

    public void onUpdateParts() {
        this.addSegmentsToWorld();
    }

    @Override
    public int getExperienceReward() {
        return this.getScale() > 3.0F ? 20 : 10;
    }

    public void initSegments(float scale) {
        this.segments = new EntityMutlipartPart[7];
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = new EntitySlowPart(this, (-0.8F - (float) i * 0.8F) * scale, 0.0F, 0.0F, 0.7F * scale, 0.7F * scale, 1.0F);
            this.segments[i].m_20359_(this);
            this.segments[i].setParent(this);
        }
    }

    private void addSegmentsToWorld() {
        for (EntityMutlipartPart entity : this.segments) {
            EntityUtil.updatePart(entity, this);
        }
    }

    private void clearSegments() {
        for (Entity entity : this.segments) {
            if (entity != null) {
                entity.kill();
                entity.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    public void setExplosive(boolean explosive, Player thrower) {
        this.willExplode = true;
        this.ticksTillExplosion = 60;
        this.thrower = thrower;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(ANIMATION_BITE);
            this.m_5496_(this.getScale() > 3.0F ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1.0F, 1.0F);
        }
        if (this.m_217043_().nextInt(3) == 0 && this.getScale() > 1.0F && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && !MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, entityIn.getX(), entityIn.getY(), entityIn.getZ()))) {
            BlockLaunchExplosion explosion = new BlockLaunchExplosion(this.m_9236_(), this, entityIn.getX(), entityIn.getY(), entityIn.getZ(), this.getScale());
            explosion.m_46061_();
            explosion.finalizeExplosion(true);
        }
        return false;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        this.clearSegments();
        super.die(cause);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        switch(this.getVariant()) {
            case 0:
                return this.getScale() > 3.0F ? TAN_GIANT_LOOT : TAN_LOOT;
            case 1:
                return this.getScale() > 3.0F ? RED_GIANT_LOOT : RED_LOOT;
            case 2:
                return this.getScale() > 3.0F ? WHITE_GIANT_LOOT : WHITE_LOOT;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(SCALE, 1.0F);
        this.f_19804_.define(CONTROL_STATE, (byte) 0);
        this.f_19804_.define(WORM_AGE, 10);
        this.f_19804_.define(HOME, BlockPos.ZERO);
        this.f_19804_.define(JUMP_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("GrowthCounter", this.growthCounter);
        compound.putFloat("Scale", this.getDeathwormScale());
        compound.putInt("WormAge", this.getWormAge());
        compound.putLong("WormHome", this.getWormHome().asLong());
        compound.putBoolean("WillExplode", this.willExplode);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.growthCounter = compound.getInt("GrowthCounter");
        this.setDeathWormScale(compound.getFloat("Scale"));
        this.setWormAge(compound.getInt("WormAge"));
        this.setWormHome(BlockPos.of(compound.getLong("WormHome")));
        this.willExplode = compound.getBoolean("WillExplode");
        this.setConfigurableAttributes();
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = this.f_19804_.get(CONTROL_STATE);
        if (newState) {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState | 1 << i));
        } else {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return this.f_19804_.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        this.f_19804_.set(CONTROL_STATE, state);
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getWormJumping() {
        return this.f_19804_.get(JUMP_TICKS);
    }

    public void setWormJumping(int jump) {
        this.f_19804_.set(JUMP_TICKS, jump);
    }

    public BlockPos getWormHome() {
        return this.f_19804_.get(HOME);
    }

    public void setWormHome(BlockPos home) {
        if (home instanceof BlockPos) {
            this.f_19804_.set(HOME, home);
        }
    }

    public int getWormAge() {
        return Math.max(1, this.f_19804_.get(WORM_AGE));
    }

    public void setWormAge(int age) {
        this.f_19804_.set(WORM_AGE, age);
    }

    @Override
    public float getScale() {
        return Math.min(this.getDeathwormScale() * ((float) this.getWormAge() / 5.0F), 7.0F);
    }

    public float getDeathwormScale() {
        return this.f_19804_.get(SCALE);
    }

    public void setDeathWormScale(float scale) {
        this.f_19804_.set(SCALE, scale);
        this.updateAttributes();
        this.clearSegments();
        if (!this.m_9236_().isClientSide) {
            this.initSegments(scale * ((float) this.getWormAge() / 5.0F));
            IceAndFire.sendMSGToAll(new MessageDeathWormHitbox(this.m_19879_(), scale * ((float) this.getWormAge() / 5.0F)));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.m_217043_().nextInt(3));
        float size = 0.25F + (float) (Math.random() * 0.35F);
        this.setDeathWormScale(this.m_217043_().nextInt(20) == 0 ? size * 4.0F : size);
        return spawnDataIn;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            this.m_5618_(passenger.getYRot());
            float radius = -0.5F * this.getScale();
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + (double) this.m_20192_() - 0.55F, this.m_20189_() + extraZ);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player) {
                return (Player) passenger;
            }
        }
        return null;
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (this.getWormAge() > 4 && player.m_20202_() == null && player.m_21205_().getItem() == Items.FISHING_ROD && player.m_21206_().getItem() == Items.FISHING_ROD && !this.m_9236_().isClientSide) {
            player.m_20329_(this);
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6071_(player, hand);
        }
    }

    private void switchNavigator(boolean inSand) {
        if (inSand) {
            this.f_21342_ = new EntityDeathWorm.SandMoveHelper();
            this.f_21344_ = new PathNavigateDeathWormSand(this, this.m_9236_());
            this.isSandNavigator = true;
        } else {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new PathNavigateDeathWormLand(this, this.m_9236_());
            this.isSandNavigator = false;
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!source.is(DamageTypes.IN_WALL) && !source.is(DamageTypes.FALLING_BLOCK)) {
            return this.m_20160_() && source.getEntity() != null && this.getControllingPassenger() != null && source.getEntity() == this.getControllingPassenger() ? false : super.m_6469_(source, amount);
        } else {
            return false;
        }
    }

    @Override
    public void move(@NotNull MoverType typeIn, @NotNull Vec3 pos) {
        super.m_6478_(typeIn, pos);
    }

    @NotNull
    @Override
    public Vec3 collide(@NotNull Vec3 vec) {
        return ICustomCollisions.getAllowedMovementForEntity(this, vec);
    }

    @Override
    public boolean isInWall() {
        return this.isInSand() ? false : super.m_5830_();
    }

    @Override
    protected void moveTowardsClosestSpace(double x, double y, double z) {
        PositionImpl blockpos = new PositionImpl(x, y, z);
        Vec3i vec3i = new Vec3i((int) Math.round(blockpos.x()), (int) Math.round(blockpos.y()), (int) Math.round(blockpos.z()));
        Vec3 vector3d = new Vec3(x - blockpos.x(), y - blockpos.y(), z - blockpos.z());
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        Direction direction = Direction.UP;
        double d0 = Double.MAX_VALUE;
        for (Direction direction1 : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
            blockpos$mutable.setWithOffset(vec3i, direction1);
            if (!this.m_9236_().getBlockState(blockpos$mutable).m_60838_(this.m_9236_(), blockpos$mutable) || this.m_9236_().getBlockState(blockpos$mutable).m_204336_(BlockTags.SAND)) {
                double d1 = vector3d.get(direction1.getAxis());
                double d2 = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - d1 : d1;
                if (d2 < d0) {
                    d0 = d2;
                    direction = direction1;
                }
            }
        }
        float f = this.f_19796_.nextFloat() * 0.2F + 0.1F;
        float f1 = (float) direction.getAxisDirection().getStep();
        Vec3 vector3d1 = this.m_20184_().scale(0.75);
        if (direction.getAxis() == Direction.Axis.X) {
            this.m_20334_((double) (f1 * f), vector3d1.y, vector3d1.z);
        } else if (direction.getAxis() == Direction.Axis.Y) {
            this.m_20334_(vector3d1.x, (double) (f1 * f), vector3d1.z);
        } else if (direction.getAxis() == Direction.Axis.Z) {
            this.m_20334_(vector3d1.x, vector3d1.y, (double) (f1 * f));
        }
    }

    private void updateAttributes() {
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(Math.min(0.2, 0.15 * (double) this.getScale()));
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(1.0, IafConfig.deathWormAttackStrength * (double) this.getScale()));
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(Math.max(6.0, IafConfig.deathWormMaxHealth * (double) this.getScale()));
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) IafConfig.deathWormTargetSearchLength);
        this.m_21153_((float) this.m_21051_(Attributes.MAX_HEALTH).getBaseValue());
    }

    @Override
    public boolean killedEntity(@NotNull ServerLevel world, @NotNull LivingEntity entity) {
        if (this.m_21824_()) {
            this.m_5634_(14.0F);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        this.prevJumpProgress = this.jumpProgress;
        if (this.getWormJumping() > 0 && this.jumpProgress < 5.0F) {
            this.jumpProgress++;
        }
        if (this.getWormJumping() == 0 && this.jumpProgress > 0.0F) {
            this.jumpProgress--;
        }
        if (this.isInSand() && this.f_19862_) {
            this.m_20256_(this.m_20184_().add(0.0, 0.05, 0.0));
        }
        if (this.getWormJumping() > 0) {
            float f2 = (float) (-((double) ((float) this.m_20184_().y) * 180.0F / (float) Math.PI));
            this.m_146926_(f2);
            if (this.isInSand() || this.m_20096_()) {
                this.setWormJumping(this.getWormJumping() - 1);
            }
        }
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        if (this.m_5448_() != null && (!this.m_5448_().isAlive() || !DragonUtils.isAlive(this.m_5448_()))) {
            this.m_6710_(null);
        }
        if (this.willExplode) {
            if (this.ticksTillExplosion == 0) {
                boolean b = !MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, this.m_20185_(), this.m_20186_(), this.m_20189_()));
                if (b) {
                    this.m_9236_().explode(this.thrower, this.m_20185_(), this.m_20186_(), this.m_20189_(), 2.5F * this.getScale(), false, Level.ExplosionInteraction.MOB);
                }
                this.thrower = null;
            } else {
                this.ticksTillExplosion--;
            }
        }
        if (this.f_19797_ == 1) {
            this.initSegments(this.getScale());
        }
        if (this.isInSandStrict()) {
            this.m_20256_(this.m_20184_().add(0.0, 0.08, 0.0));
        }
        if (this.growthCounter > 1000 && this.getWormAge() < 5) {
            this.growthCounter = 0;
            this.setWormAge(Math.min(5, this.getWormAge() + 1));
            this.clearSegments();
            this.m_5634_(15.0F);
            this.setDeathWormScale(this.getDeathwormScale());
            if (this.m_9236_().isClientSide) {
                for (int i = 0; (float) i < 10.0F * this.getScale(); i++) {
                    this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), (double) ((float) this.getSurface((int) Math.floor(this.m_20185_()), (int) Math.floor(this.m_20186_()), (int) Math.floor(this.m_20189_())) + 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02);
                }
            }
        }
        if (this.getWormAge() < 5) {
            this.growthCounter++;
        }
        if (this.getControllingPassenger() != null && this.m_5448_() != null) {
            this.m_21573_().stop();
            this.m_6710_(null);
        }
        if (this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < Math.min(4.0, 4.0 * (double) this.getScale()) && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 5) {
            float f = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue();
            this.m_5448_().hurt(this.m_9236_().damageSources().mobAttack(this), f);
            this.m_20256_(this.m_20184_().add(0.0, -0.4F, 0.0));
        }
    }

    public int getWormBrightness(boolean sky) {
        Vec3 vec3 = this.m_20299_(1.0F);
        BlockPos eyePos = BlockPos.containing(vec3);
        while (eyePos.m_123342_() < 256 && !this.m_9236_().m_46859_(eyePos)) {
            eyePos = eyePos.above();
        }
        return this.m_9236_().m_45517_(sky ? LightLayer.SKY : LightLayer.BLOCK, eyePos.above());
    }

    public int getSurface(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        while (!this.m_9236_().m_46859_(pos)) {
            pos = pos.above();
        }
        return pos.m_123342_();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.getScale() > 3.0F ? IafSoundRegistry.DEATHWORM_GIANT_IDLE : IafSoundRegistry.DEATHWORM_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return this.getScale() > 3.0F ? IafSoundRegistry.DEATHWORM_GIANT_HURT : IafSoundRegistry.DEATHWORM_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.getScale() > 3.0F ? IafSoundRegistry.DEATHWORM_GIANT_DIE : IafSoundRegistry.DEATHWORM_DIE;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.m_6210_();
        this.onUpdateParts();
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity target = DragonUtils.riderLookingAtEntity(this, this.getControllingPassenger(), 3.0);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
                this.m_5496_(this.getScale() > 3.0F ? IafSoundRegistry.DEATHWORM_GIANT_ATTACK : IafSoundRegistry.DEATHWORM_ATTACK, 1.0F, 1.0F);
                if (this.m_217043_().nextInt(3) == 0 && this.getScale() > 1.0F) {
                    float radius = 1.5F * this.getScale();
                    float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                    double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                    double extraZ = (double) (radius * Mth.cos(angle));
                    BlockLaunchExplosion explosion = new BlockLaunchExplosion(this.m_9236_(), this, this.m_20185_() + extraX, this.m_20186_() - (double) this.m_20192_(), this.m_20189_() + extraZ, this.getScale() * 0.75F);
                    explosion.m_46061_();
                    explosion.finalizeExplosion(true);
                }
            }
            if (target != null) {
                target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.isInSand()) {
            BlockPos pos = new BlockPos(this.m_146903_(), this.getSurface(this.m_146903_(), this.m_146904_(), this.m_146907_()), this.m_146907_()).below();
            BlockState state = this.m_9236_().getBlockState(pos);
            if (state.m_60804_(this.m_9236_(), pos) && this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), (double) ((float) this.getSurface((int) Math.floor(this.m_20185_()), (int) Math.floor(this.m_20186_()), (int) Math.floor(this.m_20189_())) + 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02, this.f_19796_.nextGaussian() * 0.02);
            }
            if (this.f_19797_ % 10 == 0) {
                this.m_5496_(SoundEvents.SAND_BREAK, 1.0F, 0.5F);
            }
        }
        if (this.up() && this.m_20096_()) {
            this.m_6135_();
        }
        boolean inSand = this.isInSand() || this.getControllingPassenger() == null;
        if (inSand && !this.isSandNavigator) {
            this.switchNavigator(true);
        }
        if (!inSand && this.isSandNavigator) {
            this.switchNavigator(false);
        }
        if (this.m_9236_().isClientSide) {
            this.tail_buffer.calculateChainSwingBuffer(90.0F, 20, 5.0F, this);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public boolean up() {
        return (this.f_19804_.get(CONTROL_STATE) & 1) == 1;
    }

    public boolean dismountIAF() {
        return (this.f_19804_.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (this.f_19804_.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        this.setStateField(0, up);
    }

    @Override
    public void down(boolean down) {
    }

    @Override
    public void dismount(boolean dismount) {
        this.setStateField(1, dismount);
    }

    @Override
    public void attack(boolean attack) {
        this.setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {
    }

    public boolean isSandBelow() {
        int i = Mth.floor(this.m_20185_());
        int j = Mth.floor(this.m_20186_() + 1.0);
        int k = Mth.floor(this.m_20189_());
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState BlockState = this.m_9236_().getBlockState(blockpos);
        return BlockState.m_204336_(BlockTags.SAND);
    }

    public boolean isInSand() {
        return this.getControllingPassenger() == null && this.isInSandStrict();
    }

    public boolean isInSandStrict() {
        return this.m_9236_().getBlockState(this.m_20183_()).m_204336_(BlockTags.SAND);
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

    public Entity[] getWormParts() {
        return this.segments;
    }

    @Override
    public int getMaxHeadYRot() {
        return 10;
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean canPassThrough(BlockPos pos, BlockState state, VoxelShape shape) {
        return this.m_9236_().getBlockState(pos).m_204336_(BlockTags.SAND);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isRidingPlayer(Player player) {
        return this.getRidingPlayer() != null && player != null && this.getRidingPlayer().m_20148_().equals(player.m_20148_());
    }

    @Nullable
    @Override
    public Player getRidingPlayer() {
        return this.getControllingPassenger() instanceof Player ? (Player) this.getControllingPassenger() : null;
    }

    @Override
    public double getRideSpeedModifier() {
        return this.isInSand() ? 1.5 : 1.0;
    }

    public double processRiderY(double y) {
        return this.isInSand() ? y + 0.2F : y;
    }

    public class SandMoveHelper extends MoveControl {

        private final EntityDeathWorm worm = EntityDeathWorm.this;

        public SandMoveHelper() {
            super(EntityDeathWorm.this);
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                double d1 = this.f_24976_ - this.worm.m_20186_();
                double d2 = this.f_24977_ - this.worm.m_20189_();
                Vec3 Vector3d = new Vec3(this.f_24975_ - this.worm.m_20185_(), this.f_24976_ - this.worm.m_20186_(), this.f_24977_ - this.worm.m_20189_());
                double d0 = Vector3d.length();
                if (d0 < 2.5000003E-7F) {
                    this.f_24974_.setZza(0.0F);
                } else {
                    this.f_24978_ = 1.0;
                    this.worm.m_20256_(this.worm.m_20184_().add(Vector3d.scale(this.f_24978_ * 0.05 / d0)));
                    Vec3 Vector3d1 = this.worm.m_20184_();
                    this.worm.m_146922_(-((float) Mth.atan2(Vector3d1.x, Vector3d1.z)) * (180.0F / (float) Math.PI));
                }
            }
        }
    }
}