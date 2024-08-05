package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWaterLava;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHerdPanic;
import com.github.alexthe666.alexsmobs.entity.ai.BoneSerpentPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.LaviathanAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.util.Maths;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;

public class EntityLaviathan extends Animal implements ISemiAquatic, IHerdPanic {

    private static final EntityDataAccessor<Boolean> OBSIDIAN = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> HEAD_HEIGHT = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> HEAD_YROT = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> CHILL_TIME = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HAS_BODY_GEAR = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_HEAD_GEAR = SynchedEntityData.defineId(EntityLaviathan.class, EntityDataSerializers.BOOLEAN);

    private static final Predicate<EntityCrimsonMosquito> HEALTHY_MOSQUITOES = mob -> mob.m_6084_() && mob.m_21223_() > 0.0F && !mob.isSick();

    public static final ResourceLocation OBSIDIAN_LOOT = new ResourceLocation("alexsmobs", "entities/laviathan_obsidian");

    public final EntityLaviathanPart headPart;

    public final EntityLaviathanPart neckPart1;

    public final EntityLaviathanPart neckPart2;

    public final EntityLaviathanPart neckPart3;

    public final EntityLaviathanPart neckPart4;

    public final EntityLaviathanPart neckPart5;

    public final EntityLaviathanPart seat1;

    public final EntityLaviathanPart seat2;

    public final EntityLaviathanPart seat3;

    public final EntityLaviathanPart seat4;

    public final EntityLaviathanPart[] theEntireNeck;

    public final EntityLaviathanPart[] allParts;

    public final EntityLaviathanPart[] seatParts;

    private final UUID[] riderPositionMap = new UUID[4];

    public float prevHeadHeight = 0.0F;

    public float swimProgress = 0.0F;

    public float prevSwimProgress = 0.0F;

    public float biteProgress;

    public float prevBiteProgress;

    public int revengeCooldown = 0;

    private boolean isLandNavigator;

    private int conversionTime = 0;

    private int dismountCooldown = 0;

    private int headPeakCooldown = 0;

    private boolean hasObsidianArmor;

    private int blockBreakCounter;

    protected EntityLaviathan(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.m_274367_(1.3F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.headPart = new EntityLaviathanPart(this, 1.2F, 0.9F);
        this.neckPart1 = new EntityLaviathanPart(this, 0.9F, 0.9F);
        this.neckPart2 = new EntityLaviathanPart(this, 0.9F, 0.9F);
        this.neckPart3 = new EntityLaviathanPart(this, 0.9F, 0.9F);
        this.neckPart4 = new EntityLaviathanPart(this, 0.9F, 0.9F);
        this.neckPart5 = new EntityLaviathanPart(this, 0.9F, 0.9F);
        this.seat1 = new EntityLaviathanPart(this, 0.9F, 0.4F);
        this.seat2 = new EntityLaviathanPart(this, 0.9F, 0.4F);
        this.seat3 = new EntityLaviathanPart(this, 0.9F, 0.4F);
        this.seat4 = new EntityLaviathanPart(this, 0.9F, 0.4F);
        this.theEntireNeck = new EntityLaviathanPart[] { this.neckPart1, this.neckPart2, this.neckPart3, this.neckPart4, this.neckPart5, this.headPart };
        this.allParts = new EntityLaviathanPart[] { this.neckPart1, this.neckPart2, this.neckPart3, this.neckPart4, this.neckPart5, this.headPart, this.seat1, this.seat2, this.seat3, this.seat4 };
        this.seatParts = new EntityLaviathanPart[] { this.seat1, this.seat2, this.seat3, this.seat4 };
        this.switchNavigator(true);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.laviathanSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canLaviathanSpawn(EntityType<EntityLaviathan> p_234314_0_, LevelAccessor p_234314_1_, MobSpawnType p_234314_2_, BlockPos p_234314_3_, RandomSource p_234314_4_) {
        BlockPos.MutableBlockPos blockpos$mutable = p_234314_3_.mutable();
        do {
            blockpos$mutable.move(Direction.UP);
        } while (p_234314_1_.m_6425_(blockpos$mutable).is(FluidTags.LAVA));
        return p_234314_1_.m_8055_(blockpos$mutable).m_60795_();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.LAVIATHAN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.LAVIATHAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.LAVIATHAN_HURT.get();
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isObsidian() ? OBSIDIAN_LOOT : super.m_7582_();
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.ARMOR, 10.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.m_6084_();
    }

    @Override
    protected boolean canAddPassenger(Entity entity0) {
        return this.m_20197_().size() < 4 && !this.m_204029_(FluidTags.LAVA) && !this.m_204029_(FluidTags.WATER);
    }

    @Override
    public void push(Entity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().add(this.m_20184_()));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMItemRegistry.MOSQUITO_LARVA.get());
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0.0F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        if (item == Items.MAGMA_CREAM && this.m_21223_() < this.m_21233_()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.m_5634_(10.0F);
            return InteractionResult.SUCCESS;
        } else if (item == AMItemRegistry.STRADDLE_HELMET.get() && !this.hasHeadGear() && !this.m_6162_()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setHeadGear(true);
            return InteractionResult.SUCCESS;
        } else if (item == AMItemRegistry.STRADDLE_SADDLE.get() && !this.hasBodyGear() && !this.m_6162_()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setBodyGear(true);
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult type = super.mobInteract(player, hand);
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && !this.isFood(itemstack) && this.hasBodyGear() && !this.m_6162_()) {
                if (!player.m_6144_()) {
                    if (!this.m_9236_().isClientSide) {
                        player.m_20329_(this);
                    }
                } else {
                    this.m_20153_();
                }
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        }
    }

    public int getClosestOpenSeat(Vec3 entityPos) {
        int closest = -1;
        double closestDistance = Double.MAX_VALUE;
        for (int i = 0; i < this.seatParts.length; i++) {
            double dist = entityPos.distanceTo(this.seatParts[i].m_20182_());
            if ((closest == -1 || closestDistance > dist) && this.riderPositionMap[i] == null) {
                closest = i;
                closestDistance = dist;
            }
        }
        return closest;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        int playerPosition = -1;
        Player player = null;
        if (this.hasHeadGear() && this.hasBodyGear()) {
            for (Entity passenger : this.m_20197_()) {
                if (passenger instanceof Player) {
                    Player player2 = (Player) passenger;
                    int player2Position = this.getRiderPosition(passenger);
                    if (player == null || playerPosition > player2Position) {
                        player = player2;
                        playerPosition = player2Position;
                    }
                }
            }
        }
        return player;
    }

    public int getSeatRaytrace(Entity player) {
        HitResult result = player.pick((double) player.distanceTo(this), 0.0F, false);
        if (result != null) {
            Vec3 vec = result.getLocation();
            return this.getClosestOpenSeat(vec);
        } else {
            return -1;
        }
    }

    @Override
    public void removePassenger(Entity entity) {
        super.m_20351_(entity);
        this.dismountCooldown = 40 + this.f_19796_.nextInt(40);
        if (entity != null && entity.getUUID() != null) {
            for (int i = 0; i < this.riderPositionMap.length; i++) {
                if (this.riderPositionMap[i] != null && this.riderPositionMap[i].equals(entity.getUUID())) {
                    this.riderPositionMap[i] = null;
                }
            }
        }
    }

    public int getRiderPosition(Entity passenger) {
        int posit = -1;
        for (int i = 0; i < this.riderPositionMap.length; i++) {
            if (this.riderPositionMap[i] != null && passenger != null && passenger.getUUID().equals(this.riderPositionMap[i])) {
                posit = i;
            }
        }
        return posit;
    }

    @Override
    protected void addPassenger(Entity entity) {
        int rayTrace = this.getSeatRaytrace(entity);
        if (rayTrace >= 0 && rayTrace < 4) {
            if (this.riderPositionMap[rayTrace] != null && !this.m_9236_().isClientSide && this.m_9236_() instanceof ServerLevel) {
                Entity kickOff = ((ServerLevel) this.m_9236_()).getEntity(this.riderPositionMap[rayTrace]);
                this.riderPositionMap[rayTrace] = null;
                if (kickOff != null) {
                    kickOff.stopRiding();
                }
            }
            this.riderPositionMap[rayTrace] = entity.getUUID();
            super.m_20348_(entity);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Obsidian", this.isObsidian());
        compound.putBoolean("HeadGear", this.hasHeadGear());
        compound.putBoolean("BodyGear", this.hasBodyGear());
        compound.putInt("ChillTime", this.getChillTime());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setObsidian(compound.getBoolean("Obsidian"));
        this.setHeadGear(compound.getBoolean("HeadGear"));
        this.setBodyGear(compound.getBoolean("BodyGear"));
        this.setChillTime(compound.getInt("ChillTime"));
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            int posit = this.getRiderPosition(passenger);
            if (posit >= 0 && posit <= 3) {
                EntityLaviathanPart seat = this.seatParts[posit];
                passenger.setPos(seat.m_20185_(), this.m_20186_() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), seat.m_20189_());
            } else {
                passenger.stopRiding();
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        float f = this.f_267362_.position();
        float f1 = this.f_267362_.speed();
        float f2 = 0.0F;
        return (double) this.m_20206_() - 0.4F;
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.hasBodyGear() && !this.m_9236_().isClientSide) {
            this.m_19998_(AMItemRegistry.STRADDLE_SADDLE.get());
        }
        if (this.hasHeadGear() && !this.m_9236_().isClientSide) {
            this.m_19998_(AMItemRegistry.STRADDLE_HELMET.get());
        }
        this.setBodyGear(false);
        this.setHeadGear(false);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21365_ = new LookControl(this);
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.createNavigation(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21365_ = new SmoothSwimmingLookControl(this, 15);
            this.f_21342_ = new EntityLaviathan.MoveController(this);
            this.f_21344_ = new BoneSerpentPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new GroundPathNavigatorWide(this, level0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new AnimalAIHerdPanic(this, 1.0) {

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityLaviathan.this.hasHeadGear();
            }
        });
        this.f_21345_.addGoal(1, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.MAGMA_CREAM, AMItemRegistry.MOSQUITO_LARVA.get()), false));
        this.f_21345_.addGoal(4, new AnimalAIFindWaterLava(this, 1.0));
        this.f_21345_.addGoal(5, new LaviathanAIRandomSwimming(this, 1.0, 22) {

            @Override
            public boolean canUse() {
                return super.m_8036_() && !EntityLaviathan.this.hasHeadGear() && !EntityLaviathan.this.hasBodyGear();
            }
        });
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    @Override
    protected float getBlockSpeedFactor() {
        return !this.shouldSwim() && !this.m_6046_() ? super.m_6041_() : 1.0F;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        if (!worldIn.m_8055_(pos).m_60819_().is(FluidTags.WATER) && !worldIn.m_8055_(pos).m_60819_().is(FluidTags.LAVA)) {
            return this.m_20077_() ? -1.0F : 0.0F;
        } else {
            return 10.0F;
        }
    }

    @Override
    public int getMaxFallDistance() {
        return 256;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        if (player.f_20902_ != 0.0F) {
            float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
            Vec3 lookVec = player.m_20154_();
            float y = (float) lookVec.y * 0.3F;
            float waterAt = (float) this.getMaxFluidHeight();
            float half = this.m_20206_() * 0.5F;
            if (waterAt > half) {
                y = Mth.clamp(waterAt - half, 0.0F, 0.5F);
            } else if (waterAt < half) {
                y = Mth.clamp(waterAt - half, -0.5F, 0.0F);
            }
            return new Vec3((double) (player.f_20900_ * 0.25F), (double) y, (double) (player.f_20902_ * (this.shouldSwim() ? 2.0F : 0.5F) * f));
        } else {
            this.m_6858_(false);
            return Vec3.ZERO;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (this.shouldSwim()) {
            if (this.m_20184_().y < 0.0) {
                this.setDeltaMovement(this.m_20184_().multiply(1.0, 0.3F, 1.0));
            }
            if (this.f_19862_) {
                this.setDeltaMovement(this.m_20184_().add(0.0, 0.1F, 0.0));
            }
        }
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_146922_(player.m_146908_());
            this.m_274367_(1.0F);
            this.m_21573_().stop();
            this.m_6710_(null);
            this.m_6858_(true);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev && source.getEntity() != null) {
            double range = 15.0;
            int fleeTime = 100 + this.m_217043_().nextInt(150);
            this.revengeCooldown = fleeTime;
            this.setChillTime(0);
        }
        return prev;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(OBSIDIAN, false);
        this.f_19804_.define(HAS_BODY_GEAR, false);
        this.f_19804_.define(HAS_HEAD_GEAR, false);
        this.f_19804_.define(HEAD_HEIGHT, 0.0F);
        this.f_19804_.define(HEAD_YROT, 0.0F);
        this.f_19804_.define(CHILL_TIME, 0);
        this.f_19804_.define(ATTACK_TICK, 0);
    }

    @Override
    public void travel(Vec3 travelVector) {
        boolean liquid = this.shouldSwim();
        if (this.m_21515_() && liquid) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.setDeltaMovement(this.m_20184_().scale(0.9));
            if (!this.isChilling()) {
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public void setDeltaMovement(Vec3 vec0) {
        super.m_20256_(vec0);
    }

    @Override
    public int getMaxHeadXRot() {
        return 50;
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    @Override
    public int getHeadRotSpeed() {
        return 4;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new EntityLaviathan.LaviathanBodyRotationControl(this);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSwimProgress = this.swimProgress;
        this.prevBiteProgress = this.biteProgress;
        this.prevHeadHeight = this.getHeadHeight();
        this.f_20883_ = this.m_146908_();
        if (this.shouldSwim()) {
            if (this.swimProgress < 5.0F) {
                this.swimProgress++;
            }
        } else if (this.swimProgress > 0.0F) {
            this.swimProgress--;
        }
        if (this.isObsidian()) {
            if (!this.hasObsidianArmor) {
                this.hasObsidianArmor = true;
                this.m_21051_(Attributes.ARMOR).setBaseValue(30.0);
            }
        } else if (this.hasObsidianArmor) {
            this.hasObsidianArmor = false;
            this.m_21051_(Attributes.ARMOR).setBaseValue(10.0);
        }
        if (!this.m_9236_().isClientSide) {
            if (!this.isObsidian() && this.m_20072_()) {
                if (this.conversionTime < 300) {
                    this.conversionTime++;
                } else {
                    this.setObsidian(true);
                }
            }
            if (this.shouldSwim()) {
                this.f_19789_ = 0.0F;
            }
        }
        float neckBase = 0.8F;
        if (!this.m_21525_()) {
            Vec3[] avector3d = new Vec3[this.allParts.length];
            for (int j = 0; j < this.allParts.length; j++) {
                this.allParts[j].collideWithNearbyEntities();
                avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
            }
            float yaw = this.m_146908_() * (float) (Math.PI / 180.0);
            float neckContraction = 2.0F * Math.abs(this.getHeadHeight() / 3.0F) + 0.5F * Math.abs(this.getHeadYaw(0.0F) / 50.0F);
            for (int l = 0; l < this.theEntireNeck.length; l++) {
                float f = (float) l / (float) this.theEntireNeck.length;
                float f1 = -(2.2F + (float) l - f * neckContraction);
                float f2 = Mth.sin(yaw + Maths.rad((double) (f * this.getHeadYaw(0.0F)))) * (1.0F - Math.abs(this.m_146909_() / 90.0F));
                float f3 = Mth.cos(yaw + Maths.rad((double) (f * this.getHeadYaw(0.0F)))) * (1.0F - Math.abs(this.m_146909_() / 90.0F));
                this.setPartPosition(this.theEntireNeck[l], (double) (f2 * f1), (double) neckBase + Math.sin((double) f * Math.PI * 0.5) * (double) (this.getHeadHeight() * 1.1F), (double) (-f3 * f1));
            }
            this.setPartPosition(this.seat1, (double) (this.getXForPart(yaw, 145.0F) * 0.75F), 2.0, (double) (this.getZForPart(yaw, 145.0F) * 0.75F));
            this.setPartPosition(this.seat2, (double) (this.getXForPart(yaw, -145.0F) * 0.75F), 2.0, (double) (this.getZForPart(yaw, -145.0F) * 0.75F));
            this.setPartPosition(this.seat3, (double) (this.getXForPart(yaw, 35.0F) * 0.95F), 2.0, (double) (this.getZForPart(yaw, 35.0F) * 0.95F));
            this.setPartPosition(this.seat4, (double) (this.getXForPart(yaw, -35.0F) * 0.95F), 2.0, (double) (this.getZForPart(yaw, -35.0F) * 0.95F));
            if (this.m_9236_().isClientSide && this.isChilling()) {
                if (!this.m_6162_()) {
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + (double) (this.getXForPart(yaw, 158.0F) * 1.75F), this.m_20227_(1.0), this.m_20189_() + (double) (this.getZForPart(yaw, 158.0F) * 1.75F), 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + (double) (this.getXForPart(yaw, -166.0F) * 1.48F), this.m_20227_(1.0), this.m_20189_() + (double) (this.getZForPart(yaw, -166.0F) * 1.48F), 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + (double) (this.getXForPart(yaw, 14.0F) * 1.78F), this.m_20227_(0.9), this.m_20189_() + (double) (this.getZForPart(yaw, 14.0F) * 1.78F), 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                    this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_() + (double) (this.getXForPart(yaw, -14.0F) * 1.6F), this.m_20227_(1.1), this.m_20189_() + (double) (this.getZForPart(yaw, -14.0F) * 1.6F), 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
                }
                this.m_9236_().addParticle(ParticleTypes.SMOKE, this.headPart.m_20208_(0.6), this.headPart.m_20227_(0.9), this.headPart.m_20262_(0.6), 0.0, this.f_19796_.nextDouble() / 5.0, 0.0);
            }
            for (int l = 0; l < this.allParts.length; l++) {
                this.allParts[l].f_19854_ = avector3d[l].x;
                this.allParts[l].f_19855_ = avector3d[l].y;
                this.allParts[l].f_19856_ = avector3d[l].z;
                this.allParts[l].f_19790_ = avector3d[l].x;
                this.allParts[l].f_19791_ = avector3d[l].y;
                this.allParts[l].f_19792_ = avector3d[l].z;
            }
        }
        if ((this.m_20077_() || this.m_20072_()) && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!this.m_20077_() && !this.m_20072_() && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.getChillTime() > 0) {
                this.setChillTime(this.getChillTime() - 1);
            } else if (this.shouldSwim() && this.f_19796_.nextInt(this.m_20160_() ? 200 : 2000) == 0 && this.revengeCooldown == 0) {
                this.setChillTime(100 + this.f_19796_.nextInt(500));
            }
            if (this.revengeCooldown > 0) {
                this.revengeCooldown--;
            }
            if (this.headPeakCooldown > 0) {
                this.headPeakCooldown--;
            }
            if (this.revengeCooldown == 0 && this.m_21188_() != null) {
                this.m_6703_(null);
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.getControllingPassenger() == null && (this.getChillTime() > 0 || this.hasHeadGear() || this.dismountCooldown > 0)) {
                this.floatLaviathan();
            }
            if (!this.isChilling() && this.headPeakCooldown == 0) {
                float low = this.getLowHeadHeight();
                this.setHeadHeight(this.getHeadHeight() + (0.5F + (this.getLowHeadHeight() + this.getHighHeadHeight(low)) / 2.0F - this.getHeadHeight()) * 0.2F);
            } else if (this.getMaxFluidHeight() <= (double) (this.m_20206_() * 0.5F) && this.getMaxFluidHeight() >= (double) (this.m_20206_() * 0.25F)) {
                float mot = (float) this.m_20184_().lengthSqr();
                this.setHeadHeight(Mth.clamp(this.getHeadHeight() + 0.1F - 0.2F * mot, 0.0F, 2.0F));
                this.headPeakCooldown = 5;
            }
        }
        if (this.isChilling()) {
            boolean keepChillin = false;
            boolean startBiting = false;
            for (EntityCrimsonMosquito entity : this.m_9236_().m_6443_(EntityCrimsonMosquito.class, this.m_20191_().inflate(30.0), HEALTHY_MOSQUITOES)) {
                entity.setLuringLaviathan(this.m_19879_());
                keepChillin = true;
            }
            if (keepChillin) {
                this.setChillTime(Math.max(20, this.getChillTime()));
            }
            for (EntityCrimsonMosquito entity : this.m_9236_().m_6443_(EntityCrimsonMosquito.class, this.headPart.m_20191_().inflate(1.0), HEALTHY_MOSQUITOES)) {
                startBiting = true;
                if (this.biteProgress == 5.0F) {
                    entity.hurt(this.m_269291_().mobAttack(this), 1000.0F);
                    entity.setShrink(true);
                    this.setChillTime(0);
                }
            }
            if (startBiting && this.f_19804_.get(ATTACK_TICK) <= 0 && this.biteProgress == 0.0F) {
                this.f_19804_.set(ATTACK_TICK, 7);
            }
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0 && this.biteProgress < 5.0F) {
            this.biteProgress++;
        }
        if (this.f_19804_.get(ATTACK_TICK) <= 0 && this.biteProgress > 0.0F) {
            this.biteProgress--;
        }
        if (this.dismountCooldown > 0) {
            this.dismountCooldown--;
        }
        if (this.hasBodyGear()) {
            List<Entity> list = this.m_9236_().getEntities(this, this.m_20191_().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
            if (!list.isEmpty()) {
                boolean flag2 = !this.m_9236_().isClientSide;
                for (int j = 0; j < list.size(); j++) {
                    Entity entityx = (Entity) list.get(j);
                    if (!entityx.hasPassenger(this)) {
                        if (flag2 && !(entityx instanceof Player) && !entityx.isPassenger() && entityx.getBbWidth() < this.m_20205_() && !(entityx instanceof EntityLaviathan) && !(entityx instanceof Enemy) && entityx instanceof Mob && this.canAddPassenger(entityx) && !(entityx instanceof WaterAnimal)) {
                            entityx.startRiding(this);
                        } else {
                            this.push(entityx);
                        }
                    }
                }
            }
        }
        if (this.m_20160_() && !this.m_9236_().isClientSide && this.f_19797_ % 40 == 0 && this.m_20197_().size() > 3) {
            for (Entity entityx : this.m_20197_()) {
                if (entityx instanceof ServerPlayer) {
                    AMAdvancementTriggerRegistry.LAVIATHAN_FOUR_PASSENGERS.trigger((ServerPlayer) entityx);
                }
            }
        }
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.breakBlock();
    }

    public void breakBlock() {
        if (this.blockBreakCounter > 0) {
            this.blockBreakCounter--;
        } else {
            boolean flag = false;
            if (!this.m_9236_().isClientSide && this.m_20160_() && this.blockBreakCounter == 0 && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this)) {
                for (int a = (int) Math.round(this.m_20191_().minX); a <= (int) Math.round(this.m_20191_().maxX); a++) {
                    for (int b = (int) Math.round(this.m_20191_().minY) - 1; b <= (int) Math.round(this.m_20191_().maxY) + 1 && b <= 127; b++) {
                        for (int c = (int) Math.round(this.m_20191_().minZ); c <= (int) Math.round(this.m_20191_().maxZ); c++) {
                            BlockPos pos = new BlockPos(a, b, c);
                            BlockState state = this.m_9236_().getBlockState(pos);
                            FluidState fluidState = this.m_9236_().getFluidState(pos);
                            Block block = state.m_60734_();
                            if (!state.m_60795_() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && state.m_204336_(AMTagRegistry.LAVIATHAN_BREAKABLES) && fluidState.isEmpty() && block != Blocks.AIR) {
                                this.setDeltaMovement(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                                flag = true;
                                this.m_9236_().m_46961_(pos, true);
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.blockBreakCounter = 10;
            }
        }
    }

    public float getLowHeadHeight() {
        float checkAt = 0.0F;
        while (checkAt > -3.0F && !this.isHeadInWall((float) this.m_20186_() + checkAt) && !this.isHeadInLava((float) this.m_20186_() + checkAt)) {
            checkAt -= 0.2F;
        }
        return checkAt;
    }

    public float getHighHeadHeight(float low) {
        float checkAt = 3.0F;
        while (checkAt > 0.0F && (!this.isHeadInWall((float) this.m_20186_() + checkAt) || this.isHeadInLava((float) this.m_20186_() + checkAt))) {
            checkAt -= 0.2F;
        }
        return checkAt;
    }

    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return false;
    }

    public boolean isHeadInWall(float offset) {
        if (this.f_19794_) {
            return false;
        } else {
            float f = 0.8F;
            Vec3 vec3 = new Vec3(this.headPart.m_20185_(), (double) offset, this.headPart.m_20189_());
            AABB axisalignedbb = AABB.ofSize(vec3, (double) f, 1.0E-6, (double) f);
            return this.m_9236_().m_45556_(axisalignedbb).filter(Predicate.not(BlockBehaviour.BlockStateBase::m_60795_)).anyMatch(p_185969_ -> {
                BlockPos blockpos = AMBlockPos.fromVec3(vec3);
                return p_185969_.m_60828_(this.m_9236_(), blockpos) && Shapes.joinIsNotEmpty(p_185969_.m_60812_(this.m_9236_(), blockpos).move(vec3.x, vec3.y, vec3.z), Shapes.create(axisalignedbb), BooleanOp.AND);
            });
        }
    }

    public boolean isHeadInLava(float offset) {
        if (this.f_19794_) {
            return false;
        } else {
            float f = 0.8F;
            BlockPos pos = AMBlockPos.fromCoords(this.headPart.m_20185_(), (double) offset, this.headPart.m_20189_());
            return !this.m_9236_().getFluidState(pos).isEmpty();
        }
    }

    private void floatLaviathan() {
        if (this.shouldSwim()) {
            if (this.getMaxFluidHeight() >= (double) this.m_20206_()) {
                this.m_20334_(this.m_20184_().x, 0.12F, this.m_20184_().z);
            } else if (this.getMaxFluidHeight() >= (double) (this.m_20206_() * 0.5F)) {
                this.m_20334_(this.m_20184_().x, 0.08F, this.m_20184_().z);
            } else {
                this.m_20334_(this.m_20184_().x, 0.0, this.m_20184_().z);
            }
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        float expand = this.m_20205_() + 1.0F;
        Vec3[] avector3d = new Vec3[] { m_19903_((double) expand, (double) livingEntity.m_20205_(), livingEntity.m_146908_()), m_19903_((double) expand, (double) livingEntity.m_20205_(), livingEntity.m_146908_() - 22.5F), m_19903_((double) expand, (double) livingEntity.m_20205_(), livingEntity.m_146908_() + 22.5F), m_19903_((double) expand, (double) livingEntity.m_20205_(), livingEntity.m_146908_() - 45.0F), m_19903_((double) expand, (double) livingEntity.m_20205_(), livingEntity.m_146908_() + 45.0F) };
        Set<BlockPos> set = Sets.newLinkedHashSet();
        double d0 = this.m_20191_().maxY;
        double d1 = this.m_20191_().minY - 0.5;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (Vec3 vector3d : avector3d) {
            blockpos$mutable.set(this.m_20185_() + vector3d.x, d0, this.m_20189_() + vector3d.z);
            for (double d2 = d0; d2 > d1; d2--) {
                set.add(blockpos$mutable.immutable());
                blockpos$mutable.move(Direction.DOWN);
            }
        }
        for (BlockPos blockpos : set) {
            if (!this.m_9236_().getFluidState(blockpos).is(FluidTags.LAVA)) {
                double d3 = this.m_9236_().m_45573_(blockpos);
                if (DismountHelper.isBlockFloorValid(d3)) {
                    Vec3 vector3d1 = Vec3.upFromBottomCenterOf(blockpos, d3);
                    UnmodifiableIterator var15 = livingEntity.getDismountPoses().iterator();
                    while (var15.hasNext()) {
                        Pose pose = (Pose) var15.next();
                        AABB axisalignedbb = livingEntity.getLocalBoundsForPose(pose);
                        if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity, axisalignedbb.move(vector3d1))) {
                            livingEntity.m_20124_(pose);
                            return vector3d1;
                        }
                    }
                }
            }
        }
        return new Vec3(this.m_20185_(), this.m_20191_().maxY, this.m_20189_());
    }

    public float getWaterLevelAbove() {
        AABB axisalignedbb = this.m_20191_();
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.ceil(axisalignedbb.maxX);
        int k = Mth.floor(axisalignedbb.maxY);
        int l = Mth.ceil(axisalignedbb.maxY);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.ceil(axisalignedbb.maxZ);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        label44: for (int k1 = k; k1 < l; k1++) {
            float f = 0.0F;
            for (int l1 = i; l1 < j; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    blockpos$mutable.set(l1, k1, i2);
                    FluidState fluidstate = this.m_9236_().getFluidState(blockpos$mutable);
                    if (fluidstate.is(FluidTags.WATER) || fluidstate.is(FluidTags.LAVA)) {
                        f = Math.max(f, fluidstate.getHeight(this.m_9236_(), blockpos$mutable));
                    }
                    if (f >= 1.0F) {
                        continue label44;
                    }
                }
            }
            if (f < 1.0F) {
                return (float) blockpos$mutable.m_123342_() + f;
            }
        }
        return (float) (l + 1);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    public boolean shouldSwim() {
        return this.getMaxFluidHeight() >= 0.1F || this.m_20077_() || this.m_20072_();
    }

    private float getXForPart(float yaw, float degree) {
        return Mth.sin((float) ((double) yaw + Math.toRadians((double) degree)));
    }

    private float getZForPart(float yaw, float degree) {
        return -Mth.cos((float) ((double) yaw + Math.toRadians((double) degree)));
    }

    public float getHeadHeight() {
        return Mth.clamp(this.f_19804_.get(HEAD_HEIGHT), -3.0F, 3.0F);
    }

    public void setHeadHeight(float height) {
        this.f_19804_.set(HEAD_HEIGHT, Mth.clamp(height, -3.0F, 3.0F));
    }

    public float getHeadYRotPlecio() {
        return Mth.wrapDegrees(this.f_19804_.get(HEAD_YROT));
    }

    public void setHeadYRotPlecio(float rot) {
        this.f_19804_.set(HEAD_YROT, rot);
    }

    public boolean isObsidian() {
        return this.f_19804_.get(OBSIDIAN);
    }

    public void setObsidian(boolean obsidian) {
        this.f_19804_.set(OBSIDIAN, obsidian);
    }

    public boolean hasHeadGear() {
        return this.f_19804_.get(HAS_HEAD_GEAR);
    }

    public void setHeadGear(boolean headGear) {
        this.f_19804_.set(HAS_HEAD_GEAR, headGear);
    }

    public boolean hasBodyGear() {
        return this.f_19804_.get(HAS_BODY_GEAR);
    }

    public void setBodyGear(boolean bodyGear) {
        this.f_19804_.set(HAS_BODY_GEAR, bodyGear);
    }

    public int getChillTime() {
        return this.f_19804_.get(CHILL_TIME);
    }

    public void setChillTime(int chillTime) {
        this.f_19804_.set(CHILL_TIME, chillTime);
    }

    public float getHeadYaw(float interp) {
        float f;
        if (interp == 0.0F) {
            f = this.m_6080_() - this.f_20883_;
        } else {
            float yBodyRot1 = this.f_20884_ + (this.f_20883_ - this.f_20884_) * interp;
            float yHeadRot1 = this.f_20886_ + (this.m_6080_() - this.f_20886_) * interp;
            f = yHeadRot1 - yBodyRot1;
        }
        return Mth.clamp(Mth.wrapDegrees(f), -50.0F, 50.0F);
    }

    private void setPartPosition(EntityLaviathanPart part, double offsetX, double offsetY, double offsetZ) {
        part.m_6034_(this.m_20185_() + offsetX * (double) part.scale, this.m_20186_() + offsetY * (double) part.scale, this.m_20189_() + offsetZ * (double) part.scale);
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    public boolean attackEntityPartFrom(EntityLaviathanPart part, DamageSource source, float amount) {
        return this.hurt(source, amount);
    }

    @Override
    public boolean shouldEnterWater() {
        return !this.m_20160_();
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.m_20160_();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.m_20160_();
    }

    @Override
    public int getWaterSearchRange() {
        return 15;
    }

    private double getMaxFluidHeight() {
        return Math.max(this.m_204036_(FluidTags.LAVA), this.m_204036_(FluidTags.WATER));
    }

    public boolean isChilling() {
        return this.getChillTime() > 0 && this.getMaxFluidHeight() <= (double) (this.m_20206_() * 0.5F);
    }

    public void scaleParts() {
        for (EntityLaviathanPart parts : this.allParts) {
            float prev = parts.scale;
            parts.scale = this.m_6162_() ? 0.5F : 1.0F;
            if (prev != parts.scale) {
                parts.m_6210_();
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.scaleParts();
    }

    public Vec3 getLureMosquitoPos() {
        return new Vec3(this.headPart.m_20185_(), this.headPart.m_20227_(0.4F), this.headPart.m_20189_());
    }

    @Override
    public void onPanic() {
    }

    @Override
    public boolean canPanic() {
        return true;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.LAVIATHAN.get().create(serverWorld);
    }

    private static class LaviathanBodyRotationControl extends BodyRotationControl {

        private final EntityLaviathan laviathan;

        public LaviathanBodyRotationControl(EntityLaviathan laviathan) {
            super(laviathan);
            this.laviathan = laviathan;
        }

        @Override
        public void clientTick() {
        }
    }

    static class MoveController extends MoveControl {

        private final EntityLaviathan laviathan;

        public MoveController(EntityLaviathan dolphinIn) {
            super(dolphinIn);
            this.laviathan = dolphinIn;
        }

        @Override
        public void tick() {
            float speed = (float) (this.f_24978_ * 3.0 * this.laviathan.m_21133_(Attributes.MOVEMENT_SPEED));
            if (!this.laviathan.m_20160_()) {
                if (this.laviathan.isChilling()) {
                    speed *= 0.5F;
                } else if (this.laviathan.shouldSwim()) {
                    this.laviathan.setDeltaMovement(this.laviathan.m_20184_().add(0.0, -0.005, 0.0));
                }
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && (!this.laviathan.m_21573_().isDone() || this.laviathan.getControllingPassenger() != null)) {
                double lvt_1_1_ = this.f_24975_ - this.laviathan.m_20185_();
                double lvt_3_1_ = this.f_24976_ - this.laviathan.m_20186_();
                double lvt_5_1_ = this.f_24977_ - this.laviathan.m_20189_();
                double lvt_7_1_ = lvt_1_1_ * lvt_1_1_ + lvt_3_1_ * lvt_3_1_ + lvt_5_1_ * lvt_5_1_;
                if (lvt_7_1_ < 2.5) {
                    this.laviathan.m_21564_(0.0F);
                } else {
                    float lvt_9_1_ = (float) (Mth.atan2(lvt_5_1_, lvt_1_1_) * 180.0F / (float) Math.PI) - 90.0F;
                    this.laviathan.m_146922_(this.m_24991_(this.laviathan.m_146908_(), lvt_9_1_, 5.0F));
                    this.laviathan.m_5616_(this.m_24991_(this.laviathan.m_6080_(), lvt_9_1_, 90.0F));
                    if (lvt_3_1_ >= 0.0 && this.laviathan.f_19862_) {
                        this.laviathan.setDeltaMovement(this.laviathan.m_20184_().add(0.0, 0.5, 0.0));
                    } else {
                        this.laviathan.setDeltaMovement(this.laviathan.m_20184_().add(0.0, (double) this.laviathan.m_6113_() * lvt_3_1_ * 0.6, 0.0));
                    }
                    if (this.laviathan.shouldSwim()) {
                        this.laviathan.m_7910_(speed * 0.03F);
                        float lvt_11_1_ = -((float) (Mth.atan2(lvt_3_1_, (double) Mth.sqrt((float) (lvt_1_1_ * lvt_1_1_ + lvt_5_1_ * lvt_5_1_))) * 180.0F / (float) Math.PI));
                        lvt_11_1_ = Mth.clamp(Mth.wrapDegrees(lvt_11_1_), -85.0F, 85.0F);
                        this.laviathan.m_146926_(this.m_24991_(this.laviathan.m_146909_(), lvt_11_1_, 25.0F));
                        float lvt_12_1_ = Mth.cos(this.laviathan.m_146909_() * (float) (Math.PI / 180.0));
                        float lvt_13_1_ = Mth.sin(this.laviathan.m_146909_() * (float) (Math.PI / 180.0));
                        this.laviathan.f_20902_ = lvt_12_1_ * speed;
                        this.laviathan.f_20901_ = -lvt_13_1_ * speed;
                    } else {
                        this.laviathan.m_7910_(speed * 0.1F);
                    }
                }
            } else if (!this.laviathan.m_9236_().getBlockState(this.laviathan.m_20183_().above()).m_60819_().isEmpty() && this.laviathan.getChillTime() <= 0) {
                this.laviathan.setDeltaMovement(this.laviathan.m_20184_().add(0.0, -0.05, 0.0));
            }
        }
    }
}