package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.SeagullAIRevealTreasure;
import com.github.alexthe666.alexsmobs.entity.ai.SeagullAIStealFromPlayers;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntitySeagull extends Animal implements ITargetsDroppedItems {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntitySeagull.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> FLIGHT_LOOK_YAW = SynchedEntityData.defineId(EntitySeagull.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntitySeagull.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntitySeagull.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> TREASURE_POS = SynchedEntityData.defineId(EntitySeagull.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public float prevFlyProgress;

    public float flyProgress;

    public float prevFlapAmount;

    public float flapAmount;

    public boolean aiItemFlag = false;

    public float attackProgress;

    public float prevAttackProgress;

    public float sitProgress;

    public float prevSitProgress;

    public int stealCooldown = this.f_19796_.nextInt(2500);

    private boolean isLandNavigator;

    private int timeFlying;

    private BlockPos orbitPos = null;

    private double orbitDist = 5.0;

    private boolean orbitClockwise = false;

    private boolean fallFlag = false;

    private int flightLookCooldown = 0;

    private float targetFlightLookYaw;

    private int heldItemTime = 0;

    public int treasureSitTime;

    public UUID feederUUID = null;

    protected EntitySeagull(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.FENCE, -1.0F);
        this.switchNavigator(false);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SEAGULL_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SEAGULL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SEAGULL_HURT.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Sitting", this.isSitting());
        compound.putInt("StealCooldown", this.stealCooldown);
        compound.putInt("TreasureSitTime", this.treasureSitTime);
        if (this.feederUUID != null) {
            compound.putUUID("FeederUUID", this.feederUUID);
        }
        if (this.getTreasurePos() != null) {
            compound.putInt("TresX", this.getTreasurePos().m_123341_());
            compound.putInt("TresY", this.getTreasurePos().m_123342_());
            compound.putInt("TresZ", this.getTreasurePos().m_123343_());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setSitting(compound.getBoolean("Sitting"));
        this.stealCooldown = compound.getInt("StealCooldown");
        this.treasureSitTime = compound.getInt("TreasureSitTime");
        if (compound.hasUUID("FeederUUID")) {
            this.feederUUID = compound.getUUID("FeederUUID");
        }
        if (compound.contains("TresX") && compound.contains("TresY") && compound.contains("TresZ")) {
            this.setTreasurePos(new BlockPos(compound.getInt("TresX"), compound.getInt("TresY"), compound.getInt("TresZ")));
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21346_.addGoal(1, new SeagullAIRevealTreasure(this));
        this.f_21346_.addGoal(2, new SeagullAIStealFromPlayers(this));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(Items.COD, AMItemRegistry.LOBSTER_TAIL.get(), AMItemRegistry.COOKED_LOBSTER_TAIL.get()), false) {

            @Override
            public boolean canUse() {
                return !EntitySeagull.this.aiItemFlag && super.canUse();
            }
        });
        this.f_21345_.addGoal(5, new EntitySeagull.AIWanderIdle());
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, PathfinderMob.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new EntitySeagull.AIScatter());
        this.f_21346_.addGoal(1, new EntitySeagull.AITargetItems(this, false, false, 15, 16));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.COD;
    }

    public static boolean canSeagullSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8 && worldIn.m_6425_(pos.below()).isEmpty();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.seagullSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntitySeagull.MoveHelper(this);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(TREASURE_POS, Optional.empty());
        this.f_19804_.define(FLIGHT_LOOK_YAW, 0.0F);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.f_19804_.set(SITTING, sitting);
    }

    public float getFlightLookYaw() {
        return this.f_19804_.get(FLIGHT_LOOK_YAW);
    }

    public void setFlightLookYaw(float yaw) {
        this.f_19804_.set(FLIGHT_LOOK_YAW, yaw);
    }

    public BlockPos getTreasurePos() {
        return (BlockPos) this.f_19804_.get(TREASURE_POS).orElse(null);
    }

    public void setTreasurePos(BlockPos pos) {
        this.f_19804_.set(TREASURE_POS, Optional.ofNullable(pos));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            boolean prev = super.hurt(source, amount);
            if (prev) {
                this.setSitting(false);
                if (!this.m_21205_().isEmpty()) {
                    this.m_19983_(this.m_21205_());
                    this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    this.stealCooldown = 1500 + this.f_19796_.nextInt(1500);
                }
                this.feederUUID = null;
                this.treasureSitTime = 0;
            }
            return prev;
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevAttackProgress = this.attackProgress;
        this.prevSitProgress = this.sitProgress;
        float yMot = (float) (-((double) ((float) this.m_20184_().y) * 180.0F / (float) Math.PI));
        float absYaw = Math.abs(this.m_146908_() - this.f_19859_);
        boolean flying = this.isFlying();
        boolean sitting = this.isSitting();
        if (flying) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (sitting) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (absYaw > 8.0F) {
            this.flapAmount = Math.min(1.0F, this.flapAmount + 0.1F);
        } else if (yMot < 0.0F) {
            this.flapAmount = Math.min(-yMot * 0.2F, 1.0F);
        } else if (this.flapAmount > 0.0F) {
            this.flapAmount = this.flapAmount - Math.min(this.flapAmount, 0.05F);
        } else {
            this.flapAmount = 0.0F;
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying()) {
                float lookYawDist = Math.abs(this.getFlightLookYaw() - this.targetFlightLookYaw);
                if (this.flightLookCooldown > 0) {
                    this.flightLookCooldown--;
                }
                if (this.flightLookCooldown == 0 && this.f_19796_.nextInt(4) == 0 && lookYawDist < 0.5F) {
                    this.targetFlightLookYaw = Mth.clamp(this.f_19796_.nextFloat() * 120.0F - 60.0F, -60.0F, 60.0F);
                    this.flightLookCooldown = 3 + this.f_19796_.nextInt(15);
                }
                if (this.getFlightLookYaw() < this.targetFlightLookYaw && lookYawDist > 0.5F) {
                    this.setFlightLookYaw(this.getFlightLookYaw() + Math.min(lookYawDist, 4.0F));
                }
                if (this.getFlightLookYaw() > this.targetFlightLookYaw && lookYawDist > 0.5F) {
                    this.setFlightLookYaw(this.getFlightLookYaw() - Math.min(lookYawDist, 4.0F));
                }
                if (this.m_20096_() && !this.m_20072_() && this.timeFlying > 30) {
                    this.setFlying(false);
                }
                this.timeFlying++;
                this.m_20242_(true);
                if (this.m_20159_() || this.m_27593_()) {
                    this.setFlying(false);
                }
            } else {
                this.fallFlag = false;
                this.timeFlying = 0;
                this.m_20242_(false);
            }
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (!this.m_21205_().isEmpty()) {
            this.heldItemTime++;
            if (this.heldItemTime > 200 && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0F);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.eatItemEffect(this.m_21205_());
                this.m_21205_().shrink(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (this.stealCooldown > 0) {
            this.stealCooldown--;
        }
        if (this.treasureSitTime > 0) {
            this.treasureSitTime--;
        }
        if (this.isSitting() && this.m_20072_()) {
            this.m_20256_(this.m_20184_().add(0.0, 0.02F, 0.0));
        }
    }

    public void eatItem() {
        this.heldItemTime = 200;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem().isEdible() && !this.isSitting();
    }

    private void eatItemEffect(ItemStack heldItemMainhand) {
        for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            float radius = this.m_20205_() * 0.65F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            ParticleOptions data = new ItemParticleOption(ParticleTypes.ITEM, heldItemMainhand);
            if (heldItemMainhand.getItem() instanceof BlockItem) {
                data = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) heldItemMainhand.getItem()).getBlock().defaultBlockState());
            }
            this.m_9236_().addParticle(data, this.m_20185_() + extraX, this.m_20186_() + (double) (this.m_20206_() * 0.6F), this.m_20189_() + extraZ, d0, d1, d2);
        }
    }

    public void setDataFromTreasureMap(Player player) {
        boolean flag = false;
        for (ItemStack map : player.getHandSlots()) {
            if ((map.getItem() == Items.FILLED_MAP || map.getItem() == Items.MAP) && map.hasTag() && map.getTag().contains("Decorations", 9)) {
                ListTag listnbt = map.getTag().getList("Decorations", 10);
                for (int i = 0; i < listnbt.size(); i++) {
                    CompoundTag nbt = listnbt.getCompound(i);
                    byte type = nbt.getByte("type");
                    if (type == MapDecoration.Type.RED_X.getIcon() || type == MapDecoration.Type.TARGET_X.getIcon()) {
                        int x = nbt.getInt("x");
                        int z = nbt.getInt("z");
                        if (this.m_20275_((double) x, this.m_20186_(), (double) z) <= 400.0) {
                            flag = true;
                            this.setTreasurePos(new BlockPos(x, 0, z));
                        }
                    }
                }
            }
        }
        if (flag) {
            this.feederUUID = player.m_20148_();
            this.treasureSitTime = 300;
            this.stealCooldown = 1500 + this.f_19796_.nextInt(1500);
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    public boolean isWingull() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.equalsIgnoreCase("wingull");
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.stealCooldown = this.stealCooldown + 600 + this.f_19796_.nextInt(1200);
        Entity thrower = e.getOwner();
        if (thrower != null && (e.getItem().getItem() == AMItemRegistry.LOBSTER_TAIL.get() || e.getItem().getItem() == AMItemRegistry.COOKED_LOBSTER_TAIL.get())) {
            Player player = this.m_9236_().m_46003_(thrower.getUUID());
            if (player != null) {
                this.setDataFromTreasureMap(player);
                this.feederUUID = thrower.getUUID();
            }
        }
        this.setFlying(true);
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 5.0F + radiusAdd + (float) this.m_217043_().nextInt(5);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getSeagullGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 8 + this.m_217043_().nextInt(4);
        BlockPos newPos = ground.above(distFromGround > 3 ? flightHeight : this.m_217043_().nextInt(4) + 8);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public BlockPos getSeagullGround(BlockPos in) {
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
        BlockPos ground = this.getSeagullGround(radialPos);
        if (ground.m_123342_() == 0) {
            return this.m_20182_();
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -62 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    private Vec3 getOrbitVec(Vec3 vector3d, float gatheringCircleDist) {
        float angle = (float) (Math.PI / 180.0) * (float) this.orbitDist * (float) (this.orbitClockwise ? -this.f_19797_ : this.f_19797_);
        double extraX = (double) (gatheringCircleDist * Mth.sin(angle));
        double extraZ = (double) (gatheringCircleDist * Mth.cos(angle));
        if (this.orbitPos != null) {
            Vec3 pos = new Vec3((double) this.orbitPos.m_123341_() + extraX, (double) (this.orbitPos.m_123342_() + this.f_19796_.nextInt(2)), (double) this.orbitPos.m_123343_() + extraZ);
            if (this.m_9236_().m_46859_(AMBlockPos.fromVec3(pos))) {
                return pos;
            }
        }
        return null;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -64 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || position.m_123342_() <= -64;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.m_21205_().isEmpty() && type != InteractionResult.SUCCESS) {
            this.m_19983_(this.m_21205_().copy());
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.stealCooldown = 1500 + this.f_19796_.nextInt(1500);
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.SEAGULL.get().create(serverWorld);
    }

    @Override
    public void peck() {
        this.f_19804_.set(ATTACK_TICK, 7);
    }

    private class AIScatter extends Goal {

        protected final EntitySeagull.AIScatter.Sorter theNearestAttackableTargetSorter;

        protected final Predicate<? super Entity> targetEntitySelector;

        protected int executionChance = 8;

        protected boolean mustUpdate;

        private Entity targetEntity;

        private Vec3 flightTarget = null;

        private int cooldown = 0;

        AIScatter() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.theNearestAttackableTargetSorter = new EntitySeagull.AIScatter.Sorter(EntitySeagull.this);
            this.targetEntitySelector = new Predicate<Entity>() {

                public boolean apply(@Nullable Entity e) {
                    return e.isAlive() && e.getType().is(AMTagRegistry.SCATTERS_CROWS) || e instanceof Player && !((Player) e).isCreative();
                }
            };
        }

        @Override
        public boolean canUse() {
            if (!EntitySeagull.this.m_20159_() && !EntitySeagull.this.isSitting() && !EntitySeagull.this.aiItemFlag && !EntitySeagull.this.m_20160_()) {
                if (!this.mustUpdate) {
                    long worldTime = EntitySeagull.this.m_9236_().getGameTime() % 10L;
                    if (EntitySeagull.this.m_21216_() >= 100 && worldTime != 0L) {
                        return false;
                    }
                    if (EntitySeagull.this.m_217043_().nextInt(this.executionChance) != 0 && worldTime != 0L) {
                        return false;
                    }
                }
                List<Entity> list = EntitySeagull.this.m_9236_().m_6443_(Entity.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);
                if (list.isEmpty()) {
                    return false;
                } else {
                    Collections.sort(list, this.theNearestAttackableTargetSorter);
                    this.targetEntity = (Entity) list.get(0);
                    this.mustUpdate = false;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.targetEntity != null;
        }

        @Override
        public void stop() {
            this.flightTarget = null;
            this.targetEntity = null;
        }

        @Override
        public void tick() {
            if (this.cooldown > 0) {
                this.cooldown--;
            }
            if (this.flightTarget != null) {
                EntitySeagull.this.setFlying(true);
                EntitySeagull.this.m_21566_().setWantedPosition(this.flightTarget.x, this.flightTarget.y, this.flightTarget.z, 1.0);
                if (this.cooldown == 0 && EntitySeagull.this.isTargetBlocked(this.flightTarget)) {
                    this.cooldown = 30;
                    this.flightTarget = null;
                }
            }
            if (this.targetEntity != null) {
                if (EntitySeagull.this.m_20096_() || this.flightTarget == null || this.flightTarget != null && EntitySeagull.this.m_20238_(this.flightTarget) < 3.0) {
                    Vec3 vec = EntitySeagull.this.getBlockInViewAway(this.targetEntity.position(), 0.0F);
                    if (vec != null && vec.y() > EntitySeagull.this.m_20186_()) {
                        this.flightTarget = vec;
                    }
                }
                if (EntitySeagull.this.m_20270_(this.targetEntity) > 20.0F) {
                    this.stop();
                }
            }
        }

        protected double getTargetDistance() {
            return 4.0;
        }

        protected AABB getTargetableArea(double targetDistance) {
            Vec3 renderCenter = new Vec3(EntitySeagull.this.m_20185_(), EntitySeagull.this.m_20186_() + 0.5, EntitySeagull.this.m_20189_());
            AABB aabb = new AABB(-2.0, -2.0, -2.0, 2.0, 2.0, 2.0);
            return aabb.move(renderCenter);
        }

        public static record Sorter(Entity theEntity) implements Comparator<Entity> {

            public int compare(Entity p_compare_1_, Entity p_compare_2_) {
                double d0 = this.theEntity.distanceToSqr(p_compare_1_);
                double d1 = this.theEntity.distanceToSqr(p_compare_2_);
                return Double.compare(d0, d1);
            }
        }
    }

    private static class AITargetItems extends CreatureAITargetItems {

        public AITargetItems(PathfinderMob creature, boolean checkSight, boolean onlyNearby, int tickThreshold, int radius) {
            super(creature, checkSight, onlyNearby, tickThreshold, radius);
            this.executionChance = 1;
        }

        @Override
        public void stop() {
            super.stop();
            ((EntitySeagull) this.f_26135_).aiItemFlag = false;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !((EntitySeagull) this.f_26135_).isSitting() && (this.f_26135_.getTarget() == null || !this.f_26135_.getTarget().isAlive());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !((EntitySeagull) this.f_26135_).isSitting() && (this.f_26135_.getTarget() == null || !this.f_26135_.getTarget().isAlive());
        }

        @Override
        protected void moveTo() {
            EntitySeagull crow = (EntitySeagull) this.f_26135_;
            if (this.targetEntity != null) {
                crow.aiItemFlag = true;
                if (this.f_26135_.m_20270_(this.targetEntity) < 2.0F) {
                    crow.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    crow.peck();
                }
                if (!(this.f_26135_.m_20270_(this.targetEntity) > 8.0F) && !crow.isFlying()) {
                    this.f_26135_.getNavigation().moveTo(this.targetEntity.m_20185_(), this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                } else {
                    crow.setFlying(true);
                    float f = (float) (crow.m_20185_() - this.targetEntity.m_20185_());
                    float f2 = (float) (crow.m_20189_() - this.targetEntity.m_20189_());
                    if (!crow.m_142582_(this.targetEntity)) {
                        crow.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), 1.0 + crow.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    } else {
                        float f1 = 1.8F;
                        float xzDist = Mth.sqrt(f * f + f2 * f2);
                        if (xzDist < 5.0F) {
                            f1 = 0.0F;
                        }
                        crow.m_21566_().setWantedPosition(this.targetEntity.m_20185_(), (double) f1 + this.targetEntity.m_20186_(), this.targetEntity.m_20189_(), 1.5);
                    }
                }
            }
        }

        @Override
        public void tick() {
            super.tick();
            this.moveTo();
        }
    }

    private class AIWanderIdle extends Goal {

        protected final EntitySeagull eagle;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        private int orbitResetCooldown = 0;

        private int maxOrbitTime = 360;

        private int orbitTime = 0;

        public AIWanderIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.eagle = EntitySeagull.this;
        }

        @Override
        public boolean canUse() {
            if (this.orbitResetCooldown < 0) {
                this.orbitResetCooldown++;
            }
            if ((this.eagle.m_5448_() == null || !this.eagle.m_5448_().isAlive() || this.eagle.m_20160_()) && !this.eagle.isSitting() && !this.eagle.m_20159_()) {
                if ((this.eagle.m_217043_().nextInt(20) == 0 || this.eagle.isFlying()) && !this.eagle.aiItemFlag) {
                    if (this.eagle.m_6162_()) {
                        this.flightTarget = false;
                    } else if (this.eagle.m_20072_()) {
                        this.flightTarget = true;
                    } else if (this.eagle.m_20096_()) {
                        this.flightTarget = EntitySeagull.this.f_19796_.nextInt(10) == 0;
                    } else {
                        if (this.orbitResetCooldown == 0 && EntitySeagull.this.f_19796_.nextInt(6) == 0) {
                            this.orbitResetCooldown = 100 + EntitySeagull.this.f_19796_.nextInt(300);
                            this.eagle.orbitPos = this.eagle.m_20183_();
                            this.eagle.orbitDist = (double) (4 + EntitySeagull.this.f_19796_.nextInt(5));
                            this.eagle.orbitClockwise = EntitySeagull.this.f_19796_.nextBoolean();
                            this.orbitTime = 0;
                            this.maxOrbitTime = (int) (180.0F + 360.0F * EntitySeagull.this.f_19796_.nextFloat());
                        }
                        this.flightTarget = EntitySeagull.this.f_19796_.nextInt(5) != 0 && this.eagle.timeFlying < 400;
                    }
                    Vec3 lvt_1_1_ = this.getPosition();
                    if (lvt_1_1_ == null) {
                        return false;
                    } else {
                        this.x = lvt_1_1_.x;
                        this.y = lvt_1_1_.y;
                        this.z = lvt_1_1_.z;
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            if (this.orbitResetCooldown > 0) {
                this.orbitResetCooldown--;
            }
            if (this.orbitResetCooldown < 0) {
                this.orbitResetCooldown++;
            }
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                if (this.orbitTime < this.maxOrbitTime && !this.eagle.m_20072_()) {
                    this.orbitTime++;
                } else {
                    this.orbitTime = 0;
                    this.eagle.orbitPos = null;
                    this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.nextInt(400);
                }
            }
            if (this.eagle.f_19862_ && !this.eagle.m_20096_()) {
                this.stop();
            }
            if (this.flightTarget) {
                this.eagle.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else if (!this.eagle.isFlying() || this.eagle.m_20096_()) {
                this.eagle.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntitySeagull.this.isFlying()) {
                this.eagle.fallFlag = true;
                if (this.eagle.m_20096_()) {
                    this.eagle.setFlying(false);
                    this.orbitTime = 0;
                    this.eagle.orbitPos = null;
                    this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.nextInt(400);
                }
            }
            if (EntitySeagull.this.isFlying() && (!EntitySeagull.this.m_9236_().m_46859_(this.eagle.m_20099_()) || this.eagle.m_20096_()) && !this.eagle.m_20072_() && this.eagle.timeFlying > 30) {
                this.eagle.setFlying(false);
                this.orbitTime = 0;
                this.eagle.orbitPos = null;
                this.orbitResetCooldown = -400 - EntitySeagull.this.f_19796_.nextInt(400);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.eagle.m_20182_();
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                return this.eagle.getOrbitVec(vector3d, (float) (4 + EntitySeagull.this.f_19796_.nextInt(4)));
            } else {
                if (this.eagle.m_20160_() || this.eagle.isOverWaterOrVoid()) {
                    this.flightTarget = true;
                }
                if (this.flightTarget) {
                    return this.eagle.timeFlying >= 340 && !this.eagle.m_20160_() && !this.eagle.isOverWaterOrVoid() ? this.eagle.getBlockGrounding(vector3d) : this.eagle.getBlockInViewAway(vector3d, 0.0F);
                } else {
                    return LandRandomPos.getPos(this.eagle, 10, 7);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.flightTarget ? this.eagle.isFlying() && this.eagle.m_20275_(this.x, this.y, this.z) > 5.0 : !this.eagle.m_21573_().isDone() && !this.eagle.m_20160_();
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.eagle.setFlying(true);
                this.eagle.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.eagle.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.eagle.m_21573_().stop();
            super.stop();
        }
    }

    static class MoveHelper extends MoveControl {

        private final EntitySeagull parentEntity;

        public MoveHelper(EntitySeagull bird) {
            super(bird);
            this.parentEntity = bird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < 0.3) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.03 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }
    }
}