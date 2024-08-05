package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntityPotoo extends Animal implements IFalconry {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> PERCHING = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> PERCH_POS = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Direction> PERCH_DIRECTION = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.DIRECTION);

    private static final EntityDataAccessor<Integer> MOUTH_TICK = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TEMP_BRIGHTNESS = SynchedEntityData.defineId(EntityPotoo.class, EntityDataSerializers.INT);

    public final float[] ringBuffer = new float[64];

    public float prevFlyProgress;

    public float flyProgress;

    public float mouthProgress;

    public float prevMouthProgress;

    public float prevPerchProgress;

    public float perchProgress;

    public int ringBufferIndex = -1;

    private int lastScreamTimestamp;

    private int perchCooldown = 100;

    private boolean isLandNavigator;

    private int timeFlying;

    protected EntityPotoo(EntityType type, Level level) {
        super(type, level);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(AMTagRegistry.INSECT_ITEMS), false));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(4, new EntityPotoo.AIPerch());
        this.f_21345_.addGoal(5, new EntityPotoo.AIMelee());
        this.f_21345_.addGoal(6, new EntityPotoo.AIFlyIdle());
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, EntityFly.class, 100, true, true, null));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 0.6F, false, true);
            this.f_21344_ = new FlyingPathNavigation(this, this.m_9236_()) {

                @Override
                public boolean isStableDestination(BlockPos pos) {
                    return !this.f_26495_.getBlockState(pos.below(2)).m_60795_();
                }
            };
            this.f_21344_.setCanFloat(false);
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(PERCHING, false);
        this.f_19804_.define(PERCH_POS, Optional.empty());
        this.f_19804_.define(PERCH_DIRECTION, Direction.NORTH);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(MOUTH_TICK, 0);
        this.f_19804_.define(TEMP_BRIGHTNESS, 0);
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
    }

    public static boolean canPotooSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return m_186209_(worldIn, pos);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader reader) {
        if (reader.m_45784_(this) && !reader.containsAnyLiquid(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            BlockState blockstate2 = reader.m_8055_(blockpos.below());
            return blockstate2.m_204336_(BlockTags.LEAVES) || blockstate2.m_204336_(BlockTags.LOGS);
        } else {
            return false;
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.potooSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevPerchProgress = this.perchProgress;
        this.prevMouthProgress = this.mouthProgress;
        this.prevFlyProgress = this.flyProgress;
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.isPerching()) {
            if (this.perchProgress < 5.0F) {
                this.perchProgress++;
            }
        } else if (this.perchProgress > 0.0F) {
            this.perchProgress--;
        }
        if (this.ringBufferIndex < 0) {
            Arrays.fill(this.ringBuffer, 15.0F);
        }
        this.ringBufferIndex++;
        if (this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }
        this.ringBuffer[this.ringBufferIndex] = (float) this.f_19804_.get(TEMP_BRIGHTNESS).intValue();
        if (this.perchCooldown > 0) {
            this.perchCooldown--;
        }
        if (!this.m_9236_().isClientSide) {
            this.f_19804_.set(TEMP_BRIGHTNESS, this.m_9236_().m_46803_(this.m_20183_()));
            if (this.isFlying()) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.isFlying()) {
                if (!this.m_20096_()) {
                    if (!this.m_20072_()) {
                        this.m_20256_(this.m_20184_().multiply(1.0, 0.6F, 1.0));
                    }
                } else if (this.timeFlying > 20) {
                    this.setFlying(false);
                }
                this.timeFlying++;
            } else {
                this.timeFlying = 0;
            }
            if (this.isPerching() && !this.m_20160_()) {
                this.setSleeping(this.m_9236_().isDay() && (this.m_5448_() == null || !this.m_5448_().isAlive()));
            } else if (this.isSleeping()) {
                this.setSleeping(false);
            }
            if (this.isPerching() && this.getPerchPos() != null) {
                if (this.m_9236_().getBlockState(this.getPerchPos()).m_204336_(AMTagRegistry.POTOO_PERCHES) && !(this.m_20238_(Vec3.atCenterOf(this.getPerchPos())) > 2.25)) {
                    this.slideTowardsPerch();
                } else {
                    this.setPerching(false);
                }
            }
        }
        if (this.f_19804_.get(MOUTH_TICK) > 0) {
            this.f_19804_.set(MOUTH_TICK, this.f_19804_.get(MOUTH_TICK) - 1);
            if (this.mouthProgress < 5.0F) {
                this.mouthProgress++;
            }
        } else if (this.mouthProgress > 0.0F) {
            this.mouthProgress--;
        }
        if (!this.isSleeping() && (this.m_5448_() == null || !this.m_5448_().isAlive())) {
            int j = this.f_19797_ - this.lastScreamTimestamp;
            if (this.getEyeScale(10, 1.0F) == 0.0F) {
                if (j > 40) {
                    this.openMouth(30);
                    this.m_216990_(AMSoundRegistry.POTOO_CALL.get());
                    this.m_146850_(GameEvent.ENTITY_ROAR);
                }
            } else if (this.getEyeScale(10, 1.0F) < 7.0F && j > 300 && j % 300 == 0 && this.f_19796_.nextInt(4) == 0) {
                this.openMouth(30);
                this.m_216990_(AMSoundRegistry.POTOO_CALL.get());
                this.m_146850_(GameEvent.ENTITY_ROAR);
            }
        }
    }

    @Override
    public float getHandOffset() {
        return 1.0F;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.POTOO_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.POTOO_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev && source.getDirectEntity() instanceof LivingEntity) {
            this.setPerching(false);
        }
        return prev;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (!this.m_20159_() || entity.isAlive() && this.m_6084_()) {
            if (entity instanceof LivingEntity) {
                this.m_20334_(0.0, 0.0, 0.0);
                this.tick();
                this.setFlying(false);
                this.setSleeping(false);
                this.setPerching(false);
                if (this.m_20159_()) {
                    Entity mount = this.m_20202_();
                    if (mount instanceof Player) {
                        float yawAdd = 0.0F;
                        if (((Player) mount).m_21120_(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                            yawAdd = ((Player) mount).getMainArm() == HumanoidArm.LEFT ? 135.0F : -135.0F;
                        } else if (((Player) mount).m_21120_(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                            yawAdd = ((Player) mount).getMainArm() == HumanoidArm.LEFT ? -135.0F : 135.0F;
                        } else {
                            this.m_6038_();
                            this.m_20359_(mount);
                        }
                        float birdYaw = yawAdd * 0.5F;
                        this.f_20883_ = Mth.wrapDegrees(((LivingEntity) mount).yBodyRot + birdYaw);
                        this.m_146922_(Mth.wrapDegrees(mount.getYRot() + birdYaw));
                        this.f_20885_ = Mth.wrapDegrees(((LivingEntity) mount).yHeadRot + birdYaw);
                        float radius = 0.6F;
                        float angle = (float) (Math.PI / 180.0) * (((LivingEntity) mount).yBodyRot - 180.0F + yawAdd);
                        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                        double extraZ = (double) (radius * Mth.cos(angle));
                        this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) (mount.getBbHeight() * 0.45F), mount.getY()), mount.getZ() + extraZ);
                    }
                    if (!mount.isAlive()) {
                        this.m_6038_();
                    }
                }
            } else {
                super.m_6083_();
            }
        } else {
            this.m_8127_();
        }
    }

    public void openMouth(int duration) {
        this.f_19804_.set(MOUTH_TICK, duration);
        this.lastScreamTimestamp = this.f_19797_;
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (!flying || !this.m_6162_()) {
            this.f_19804_.set(FLYING, flying);
        }
    }

    public BlockPos getPerchPos() {
        return (BlockPos) this.f_19804_.get(PERCH_POS).orElse(null);
    }

    public void setPerchPos(BlockPos pos) {
        this.f_19804_.set(PERCH_POS, Optional.ofNullable(pos));
    }

    public Direction getPerchDirection() {
        return this.f_19804_.get(PERCH_DIRECTION);
    }

    public void setPerchDirection(Direction direction) {
        this.f_19804_.set(PERCH_DIRECTION, direction);
    }

    public boolean isPerching() {
        return this.f_19804_.get(PERCHING);
    }

    public void setPerching(boolean perching) {
        this.f_19804_.set(PERCHING, perching);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Perching", this.isPerching());
        compound.putInt("PerchDir", this.getPerchDirection().ordinal());
        if (this.getPerchPos() != null) {
            compound.putInt("PerchX", this.getPerchPos().m_123341_());
            compound.putInt("PerchY", this.getPerchPos().m_123342_());
            compound.putInt("PerchZ", this.getPerchPos().m_123343_());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setPerching(compound.getBoolean("Perching"));
        this.setPerchDirection(Direction.from3DDataValue(compound.getInt("PerchDir")));
        if (compound.contains("PerchX") && compound.contains("PerchY") && compound.contains("PerchZ")) {
            this.setPerchPos(new BlockPos(compound.getInt("PerchX"), compound.getInt("PerchY"), compound.getInt("PerchZ")));
        }
    }

    public boolean isValidPerchFromSide(BlockPos pos, Direction direction) {
        BlockPos offset = pos.relative(direction);
        BlockState state = this.m_9236_().getBlockState(pos);
        return state.m_204336_(AMTagRegistry.POTOO_PERCHES) && (!this.m_9236_().getBlockState(pos.above()).m_60838_(this.m_9236_(), pos.above()) || this.m_9236_().m_46859_(pos.above())) && (!this.m_9236_().getBlockState(offset).m_60838_(this.m_9236_(), offset) && !this.m_9236_().getBlockState(offset).m_204336_(AMTagRegistry.POTOO_PERCHES) || this.m_9236_().m_46859_(offset));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return AMEntityRegistry.POTOO.get().create(serverLevel);
    }

    public float getEyeScale(int bufferOffset, float partialTicks) {
        int i = this.ringBufferIndex - bufferOffset & 63;
        int j = this.ringBufferIndex - bufferOffset - 1 & 63;
        float prevBuffer = this.ringBuffer[j];
        float buffer = this.ringBuffer[i];
        return prevBuffer + (buffer - prevBuffer) * partialTicks;
    }

    private void slideTowardsPerch() {
        Vec3 block = Vec3.upFromBottomCenterOf(this.getPerchPos(), 1.0);
        Vec3 look = block.subtract(this.m_20182_()).normalize();
        Vec3 onBlock = block.add((double) ((float) this.getPerchDirection().getStepX() * 0.35F), 0.0, (double) ((float) this.getPerchDirection().getStepZ() * 0.35F));
        Vec3 diff = onBlock.subtract(this.m_20182_());
        float f = (float) diff.length();
        float f1 = f > 1.0F ? 0.25F : f * 0.1F;
        Vec3 sub = diff.normalize().scale((double) f1);
        float f2 = -((float) (Mth.atan2(look.x, look.z) * 180.0F / (float) Math.PI));
        this.m_146922_(f2);
        this.f_20885_ = f2;
        this.f_20883_ = f2;
        this.m_20256_(this.m_20184_().add(sub));
    }

    public BlockPos getToucanGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 320 && !this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.above();
        }
        while (position.m_123342_() > -64 && !this.m_9236_().getBlockState(position).m_280296_() && this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.below();
        }
        return position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = (float) (10 + this.m_217043_().nextInt(15));
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, this.m_20186_(), fleePos.z() + extraZ);
        BlockPos ground = this.getToucanGround(radialPos);
        if (ground.m_123342_() < -64) {
            return null;
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -64 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground.below()) : null;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.m_6162_() && this.getRidingFalcons(player) <= 0 && (player.m_21120_(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get() || player.m_21120_(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get())) {
            this.f_19851_ = 30;
            this.m_20153_();
            this.m_7998_(player, true);
            if (!this.m_9236_().isClientSide) {
                AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.m_19879_(), player.m_19879_()));
            }
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5.0F + radiusAdd + (float) this.m_217043_().nextInt(5);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getToucanGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 5 + this.m_217043_().nextInt(5);
        int j = this.m_217043_().nextInt(5) + 5;
        BlockPos newPos = ground.above(distFromGround > 5 ? flightHeight : j);
        if (this.m_9236_().getBlockState(ground).m_204336_(BlockTags.LEAVES)) {
            newPos = ground.above(1 + this.m_217043_().nextInt(3));
        }
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -65 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || this.m_9236_().getBlockState(position).m_60713_(Blocks.VINE) || position.m_123342_() <= -65;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS);
    }

    @Override
    public void onLaunch(Player player, Entity pointedEntity) {
    }

    private class AIFlyIdle extends Goal {

        protected double x;

        protected double y;

        protected double z;

        public AIFlyIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (EntityPotoo.this.m_20160_() || EntityPotoo.this.isPerching() || EntityPotoo.this.m_5448_() != null && EntityPotoo.this.m_5448_().isAlive() || EntityPotoo.this.m_20159_()) {
                return false;
            } else if (EntityPotoo.this.m_217043_().nextInt(45) != 0 && !EntityPotoo.this.isFlying()) {
                return false;
            } else {
                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            }
        }

        @Override
        public void tick() {
            EntityPotoo.this.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            if (EntityPotoo.this.isFlying() && EntityPotoo.this.m_20096_() && EntityPotoo.this.timeFlying > 10) {
                EntityPotoo.this.setFlying(false);
            }
        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = EntityPotoo.this.m_20182_();
            return EntityPotoo.this.timeFlying >= 200 && !EntityPotoo.this.isOverWaterOrVoid() ? EntityPotoo.this.getBlockGrounding(vector3d) : EntityPotoo.this.getBlockInViewAway(vector3d, 0.0F);
        }

        @Override
        public boolean canContinueToUse() {
            return EntityPotoo.this.isFlying() && EntityPotoo.this.m_20275_(this.x, this.y, this.z) > 5.0;
        }

        @Override
        public void start() {
            EntityPotoo.this.setFlying(true);
            EntityPotoo.this.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
        }

        @Override
        public void stop() {
            EntityPotoo.this.m_21573_().stop();
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
            super.stop();
        }
    }

    private class AIMelee extends Goal {

        private int biteCooldown = 0;

        public AIMelee() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !EntityPotoo.this.isSleeping() && !EntityPotoo.this.m_20159_() && EntityPotoo.this.m_5448_() != null && EntityPotoo.this.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            if (this.biteCooldown > 0) {
                this.biteCooldown--;
            }
            LivingEntity entity = EntityPotoo.this.m_5448_();
            if (entity != null) {
                EntityPotoo.this.setFlying(true);
                EntityPotoo.this.setPerching(false);
                EntityPotoo.this.m_21566_().setWantedPosition(entity.m_20185_(), entity.m_20227_(0.5), entity.m_20189_(), 1.5);
                if (EntityPotoo.this.m_20270_(entity) < 1.4F) {
                    if (this.biteCooldown == 0) {
                        EntityPotoo.this.openMouth(7);
                        this.biteCooldown = 10;
                    }
                    if (EntityPotoo.this.mouthProgress >= 4.5F) {
                        entity.hurt(EntityPotoo.this.m_269291_().mobAttack(EntityPotoo.this), 2.0F);
                        if (entity.m_20205_() <= 0.5F) {
                            entity.remove(Entity.RemovalReason.KILLED);
                        }
                    }
                }
            }
        }
    }

    private class AIPerch extends Goal {

        private BlockPos perch = null;

        private Direction perchDirection = null;

        private int perchingTime = 0;

        private int runCooldown = 0;

        private int pathRecalcTime = 0;

        public AIPerch() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (EntityPotoo.this.m_5448_() != null && EntityPotoo.this.m_5448_().isAlive()) {
                return false;
            } else {
                if (this.runCooldown > 0) {
                    this.runCooldown--;
                } else if (!EntityPotoo.this.isPerching() && EntityPotoo.this.perchCooldown == 0 && EntityPotoo.this.f_19796_.nextInt(35) == 0) {
                    this.perchingTime = 0;
                    if (EntityPotoo.this.getPerchPos() != null && EntityPotoo.this.isValidPerchFromSide(EntityPotoo.this.getPerchPos(), EntityPotoo.this.getPerchDirection())) {
                        this.perch = EntityPotoo.this.getPerchPos();
                        this.perchDirection = EntityPotoo.this.getPerchDirection();
                    } else {
                        this.findPerch();
                    }
                    this.runCooldown = 120 + EntityPotoo.this.m_217043_().nextInt(140);
                    return this.perch != null && this.perchDirection != null;
                }
                return false;
            }
        }

        private void findPerch() {
            RandomSource random = EntityPotoo.this.m_217043_();
            Direction[] horiz = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST };
            if (EntityPotoo.this.isValidPerchFromSide(EntityPotoo.this.m_20099_(), EntityPotoo.this.m_6350_())) {
                this.perch = EntityPotoo.this.m_20099_();
                this.perchDirection = EntityPotoo.this.m_6350_();
            } else {
                for (Direction dir : horiz) {
                    if (EntityPotoo.this.isValidPerchFromSide(EntityPotoo.this.m_20099_(), dir)) {
                        this.perch = EntityPotoo.this.m_20099_();
                        this.perchDirection = dir;
                        return;
                    }
                }
                int range = 14;
                for (int i = 0; i < 15; i++) {
                    BlockPos blockpos1 = EntityPotoo.this.m_20183_().offset(random.nextInt(range) - range / 2, 3, random.nextInt(range) - range / 2);
                    if (EntityPotoo.this.m_9236_().isLoaded(blockpos1)) {
                        while (EntityPotoo.this.m_9236_().m_46859_(blockpos1) && blockpos1.m_123342_() > -64) {
                            blockpos1 = blockpos1.below();
                        }
                        Direction dirx = Direction.from2DDataValue(random.nextInt(3));
                        if (EntityPotoo.this.isValidPerchFromSide(blockpos1, dirx)) {
                            this.perch = blockpos1;
                            this.perchDirection = dirx;
                            break;
                        }
                    }
                }
            }
        }

        @Override
        public void start() {
            this.pathRecalcTime = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return (this.perchingTime < 300 || EntityPotoo.this.m_9236_().isDay()) && (EntityPotoo.this.m_5448_() == null || !EntityPotoo.this.m_5448_().isAlive()) && !EntityPotoo.this.m_20159_();
        }

        @Override
        public void tick() {
            if (EntityPotoo.this.isPerching()) {
                this.perchingTime++;
                EntityPotoo.this.m_21573_().stop();
                Vec3 block = Vec3.upFromBottomCenterOf(EntityPotoo.this.getPerchPos(), 1.0);
                Vec3 onBlock = block.add((double) ((float) EntityPotoo.this.getPerchDirection().getStepX() * 0.35F), 0.0, (double) ((float) EntityPotoo.this.getPerchDirection().getStepZ() * 0.35F));
                double dist = EntityPotoo.this.m_20238_(onBlock);
                Vec3 dirVec = block.subtract(EntityPotoo.this.m_20182_());
                if (this.perchingTime <= 10 || !(dist > 2.3F) && EntityPotoo.this.isValidPerchFromSide(EntityPotoo.this.getPerchPos(), EntityPotoo.this.getPerchDirection())) {
                    if (dist > 1.0) {
                        EntityPotoo.this.slideTowardsPerch();
                        if ((double) ((float) EntityPotoo.this.getPerchPos().m_123342_() + 1.2F) > EntityPotoo.this.m_20191_().minY) {
                            EntityPotoo.this.m_20256_(EntityPotoo.this.m_20184_().add(0.0, 0.2F, 0.0));
                        }
                        float f = -((float) (Mth.atan2(dirVec.x, dirVec.z) * 180.0F / (float) Math.PI));
                        EntityPotoo.this.m_146922_(f);
                        EntityPotoo.this.f_20885_ = f;
                        EntityPotoo.this.f_20883_ = f;
                    }
                } else {
                    EntityPotoo.this.setPerching(false);
                }
            } else if (this.perch != null) {
                if (EntityPotoo.this.m_20238_(Vec3.atCenterOf(this.perch)) > 100.0) {
                    EntityPotoo.this.setFlying(true);
                }
                double distX = (double) ((float) this.perch.m_123341_() + 0.5F) - EntityPotoo.this.m_20185_();
                double distZ = (double) ((float) this.perch.m_123343_() + 0.5F) - EntityPotoo.this.m_20189_();
                if (!(distX * distX + distZ * distZ < 1.0) && EntityPotoo.this.isFlying()) {
                    if (this.pathRecalcTime <= 0) {
                        this.pathRecalcTime = EntityPotoo.this.m_217043_().nextInt(30) + 30;
                        EntityPotoo.this.m_21573_().moveTo((double) ((float) this.perch.m_123341_() + 0.5F), (double) ((float) this.perch.m_123342_() + 2.5F), (double) ((float) this.perch.m_123343_() + 0.5F), 1.0);
                    }
                } else {
                    if (this.pathRecalcTime <= 0) {
                        this.pathRecalcTime = EntityPotoo.this.m_217043_().nextInt(30) + 30;
                        EntityPotoo.this.m_21573_().moveTo((double) ((float) this.perch.m_123341_() + 0.5F), (double) ((float) this.perch.m_123342_() + 1.5F), (double) ((float) this.perch.m_123343_() + 0.5F), 1.0);
                    }
                    if (EntityPotoo.this.m_21573_().isDone()) {
                        EntityPotoo.this.m_21566_().setWantedPosition((double) ((float) this.perch.m_123341_() + 0.5F), (double) ((float) this.perch.m_123342_() + 1.5F), (double) ((float) this.perch.m_123343_() + 0.5F), 1.0);
                    }
                }
                if (EntityPotoo.this.m_20099_().equals(this.perch)) {
                    EntityPotoo.this.m_20256_(Vec3.ZERO);
                    EntityPotoo.this.setPerching(true);
                    EntityPotoo.this.setFlying(false);
                    EntityPotoo.this.setPerchPos(this.perch);
                    EntityPotoo.this.setPerchDirection(this.perchDirection);
                    EntityPotoo.this.m_21573_().stop();
                    this.perch = null;
                } else {
                    EntityPotoo.this.setPerching(false);
                }
            }
            if (this.pathRecalcTime > 0) {
                this.pathRecalcTime--;
            }
        }

        @Override
        public void stop() {
            EntityPotoo.this.setPerching(false);
            EntityPotoo.this.perchCooldown = 120 + EntityPotoo.this.f_19796_.nextInt(1200);
            this.perch = null;
            this.perchDirection = null;
        }
    }
}