package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAITargetDroppedItems;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityToucan extends Animal implements ITargetsDroppedItems {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> PECK_TICK = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> GOLDEN_TIME = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockState>> SAPLING_STATE = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);

    private static final EntityDataAccessor<Integer> SAPLING_TIME = SynchedEntityData.defineId(EntityToucan.class, EntityDataSerializers.INT);

    private static final HashMap<String, String> FEEDING_DATA = new HashMap();

    private static final List<ItemStack> FEEDING_STACKS = new ArrayList();

    private static boolean initFeedingData = false;

    public float prevFlyProgress;

    public float flyProgress;

    public float prevPeckProgress;

    public float peckProgress;

    private boolean isLandNavigator;

    private int timeFlying;

    private int heldItemTime;

    private boolean aiItemFlag;

    protected EntityToucan(EntityType type, Level worldIn) {
        super(type, worldIn);
        initFeedingData();
        this.m_21441_(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, -1.0F);
        this.m_21441_(BlockPathTypes.COCOA, -1.0F);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
        this.switchNavigator(true);
    }

    public static boolean canToucanSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return true;
    }

    private static void initFeedingData() {
        if (!initFeedingData || FEEDING_DATA.isEmpty()) {
            initFeedingData = true;
            for (String str : AMConfig.toucanFruitMatches) {
                String[] split = str.split("\\|");
                if (split.length >= 2) {
                    FEEDING_DATA.put(split[0], split[1]);
                    FEEDING_STACKS.add(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(split[0]))));
                }
            }
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.TOUCAN_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TOUCAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TOUCAN_HURT.get();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader0) {
        if (levelReader0.m_45784_(this) && !levelReader0.containsAnyLiquid(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            if (blockpos.m_123342_() < levelReader0.getSeaLevel()) {
                return false;
            } else {
                BlockState blockstate2 = levelReader0.m_8055_(blockpos.below());
                return blockstate2.m_60713_(Blocks.GRASS_BLOCK) || blockstate2.m_204336_(BlockTags.LEAVES);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.toucanSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Nullable
    private BlockState getSaplingFor(ItemStack stack) {
        ResourceLocation name = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (!stack.isEmpty() && name != null && FEEDING_DATA.containsKey(name.toString())) {
            String str = (String) FEEDING_DATA.get(name.toString());
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(str));
            if (block != null) {
                return block.defaultBlockState();
            }
        }
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (this.getSaplingFor(itemstack) != null && this.getSaplingTime() <= 0 && this.m_21205_().isEmpty()) {
            this.peck();
            ItemStack duplicate = itemstack.copy();
            duplicate.setCount(1);
            this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        initFeedingData();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.3));
        this.f_21345_.addGoal(2, new EntityToucan.AIPlantTrees());
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(FEEDING_STACKS.stream()), false) {

            @Override
            public boolean canUse() {
                return !EntityToucan.this.aiItemFlag && super.canUse();
            }
        });
        this.f_21345_.addGoal(5, new EntityToucan.AIWanderIdle());
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, PathfinderMob.class, 6.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new FlyingAITargetDroppedItems(this, false, false, 15, 16));
    }

    @Override
    public void setItemFlag(boolean itemAIFlag) {
        this.aiItemFlag = itemAIFlag;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.EGG;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SAPLING_STATE, Optional.empty());
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(PECK_TICK, 0);
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(GOLDEN_TIME, 0);
        this.f_19804_.define(SAPLING_TIME, 0);
        this.f_19804_.define(ENCHANTED, false);
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevFlyProgress = this.flyProgress;
        this.prevPeckProgress = this.peckProgress;
        if (this.getGoldenTime() > 0 && !this.m_9236_().isClientSide) {
            this.setGoldenTime(this.getGoldenTime() - 1);
        }
        boolean flying = this.isFlying();
        if (flying) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (flying) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (flying) {
                this.m_20242_(true);
                if (this.isFlying() && !this.m_20096_() && !this.m_20072_()) {
                    this.m_20256_(this.m_20184_().multiply(1.0, 0.6F, 1.0));
                }
                this.timeFlying++;
            } else {
                this.m_20242_(false);
                this.timeFlying = 0;
            }
        }
        if (this.f_19804_.get(PECK_TICK) > 0) {
            this.f_19804_.set(PECK_TICK, this.f_19804_.get(PECK_TICK) - 1);
            if (this.peckProgress < 5.0F) {
                this.peckProgress++;
            }
        } else if (this.peckProgress > 0.0F) {
            this.peckProgress--;
        }
        if (this.peckProgress >= 5.0F && this.m_21205_().isEmpty() && this.getSaplingState() != null) {
            this.peckBlockEffect();
        }
        if (!this.m_21205_().isEmpty()) {
            this.heldItemTime++;
            if (this.heldItemTime > 10 && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0F);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                Item mainHandItem = this.m_21205_().getItem();
                if (mainHandItem == Items.GOLDEN_APPLE) {
                    this.setGoldenTime(12000);
                } else if (mainHandItem == Items.ENCHANTED_GOLDEN_APPLE) {
                    this.setGoldenTime(-1);
                    this.setEnchanted(true);
                }
                this.setSaplingState(this.getSaplingFor(this.m_21205_()));
                this.eatItemEffect(this.m_21205_());
                this.m_21205_().shrink(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (this.isFlying() && this.m_146900_().m_60713_(Blocks.VINE)) {
            float f = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f) * 0.2F), 0.4F, (double) (Mth.cos(f) * 0.2F)));
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    @Override
    public void setFlying(boolean flying) {
        if (!flying || !this.m_6162_()) {
            this.f_19804_.set(FLYING, flying);
        }
    }

    @Override
    public void peck() {
        if (this.peckProgress == 0.0F) {
            this.f_19804_.set(PECK_TICK, 7);
        }
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = 7.0F + radiusAdd + (float) this.m_217043_().nextInt(8);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getToucanGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 8 + this.m_217043_().nextInt(4);
        int j = this.m_217043_().nextInt(6) + 18;
        BlockPos newPos = ground.above(distFromGround > 9 ? flightHeight : j);
        if (this.m_9236_().getBlockState(ground).m_204336_(BlockTags.LEAVES)) {
            newPos = ground.above(1 + this.m_217043_().nextInt(3));
        }
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
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
        if (ground.m_123342_() == -64) {
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

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 0.6F, false, true);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        BlockState blockstate = this.getSaplingState();
        if (blockstate != null) {
            compound.put("SaplingState", NbtUtils.writeBlockState(blockstate));
        }
        compound.putInt("Variant", this.getVariant());
        compound.putInt("GoldenTime", this.getGoldenTime());
        compound.putBoolean("Enchanted", this.isEnchanted());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        BlockState blockstate = null;
        if (compound.contains("SaplingState", 10)) {
            blockstate = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compound.getCompound("SaplingState"));
            if (blockstate.m_60795_()) {
                blockstate = null;
            }
        }
        this.setSaplingState(blockstate);
        this.setVariant(compound.getInt("Variant"));
        this.setGoldenTime(compound.getInt("GoldenTime"));
        this.setEnchanted(compound.getBoolean("Enchanted"));
    }

    public boolean isSam() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("sam");
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getSaplingTime() {
        return this.f_19804_.get(SAPLING_TIME);
    }

    public void setSaplingTime(int time) {
        this.f_19804_.set(SAPLING_TIME, time);
    }

    public boolean isGolden() {
        return this.getGoldenTime() > 0 || this.getGoldenTime() == -1 || this.isEnchanted();
    }

    public int getGoldenTime() {
        return this.f_19804_.get(GOLDEN_TIME);
    }

    public void setGoldenTime(int goldenTime) {
        this.f_19804_.set(GOLDEN_TIME, goldenTime);
    }

    public boolean isEnchanted() {
        return this.f_19804_.get(ENCHANTED);
    }

    public void setEnchanted(boolean enchanted) {
        this.f_19804_.set(ENCHANTED, enchanted);
    }

    @Nullable
    public BlockState getSaplingState() {
        return (BlockState) this.f_19804_.get(SAPLING_STATE).orElse(null);
    }

    public void setSaplingState(@Nullable BlockState state) {
        this.f_19804_.set(SAPLING_STATE, Optional.ofNullable(state));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.m_217043_().nextInt(4));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        EntityToucan toucan = AMEntityRegistry.TOUCAN.get().create(this.m_9236_());
        toucan.setVariant(this.getVariant());
        return toucan;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_8055_(pos).m_204336_(BlockTags.LEAVES) ? 10.0F : super.getWalkTargetValue(pos, worldIn);
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -62 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || this.m_9236_().getBlockState(position).m_60713_(Blocks.VINE) || position.m_123342_() <= 0;
    }

    private boolean isOverLeaves() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -62 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return this.m_9236_().getBlockState(position).m_204336_(BlockTags.LEAVES) || this.m_9236_().getBlockState(position).m_60713_(Blocks.VINE);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return this.getSaplingTime() <= 0 && this.getSaplingFor(stack) != null;
    }

    private void peckBlockEffect() {
        BlockState beneath = this.m_20075_();
        if (this.m_9236_().isClientSide && !beneath.m_60795_() && beneath.m_60819_().isEmpty()) {
            for (int i = 0; i < 2 + this.f_19796_.nextInt(2); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                float radius = this.m_20205_() * 0.65F;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraZ = (double) (radius * Mth.cos(angle));
                ParticleOptions data = new BlockParticleOption(ParticleTypes.BLOCK, beneath);
                this.m_9236_().addParticle(data, this.m_20185_() + extraX, this.m_20186_() + 0.1F, this.m_20189_() + extraZ, d0, d1, d2);
            }
        }
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

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.peck();
        this.setFlying(true);
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
    }

    private boolean hasLineOfSightSapling(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    private class AIPlantTrees extends Goal {

        protected final EntityToucan toucan;

        protected BlockPos pos;

        private int runCooldown = 0;

        private int encircleTime = 0;

        private int plantTime = 0;

        private boolean clockwise;

        public AIPlantTrees() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.toucan = EntityToucan.this;
        }

        @Override
        public boolean canUse() {
            if (this.toucan.getSaplingState() != null && this.runCooldown-- <= 0) {
                BlockPos target = this.getSaplingPlantPos();
                this.runCooldown = this.resetCooldown();
                if (target != null) {
                    this.pos = target;
                    this.clockwise = EntityToucan.this.f_19796_.nextBoolean();
                    this.encircleTime = (this.toucan.isGolden() ? 20 : 100) + EntityToucan.this.f_19796_.nextInt(100);
                    return true;
                }
            }
            return false;
        }

        private int resetCooldown() {
            return this.toucan.isGolden() && !this.toucan.isEnchanted() ? 50 + EntityToucan.this.f_19796_.nextInt(40) : 200 + EntityToucan.this.f_19796_.nextInt(200);
        }

        @Override
        public void tick() {
            this.toucan.aiItemFlag = true;
            double up = 3.0;
            if (this.encircleTime > 0) {
                this.encircleTime--;
            }
            if (this.isWithinXZDist(this.pos, this.toucan.m_20182_(), 5.0) && this.encircleTime <= 0) {
                up = 0.0;
            }
            if (this.toucan.m_20238_(Vec3.atCenterOf(this.pos)) < 3.0) {
                this.toucan.setFlying(false);
                this.toucan.peck();
                this.plantTime++;
                if (this.plantTime > 60) {
                    BlockState state = this.toucan.getSaplingState();
                    if (state != null && state.m_60710_(this.toucan.m_9236_(), this.pos) && this.toucan.m_9236_().getBlockState(this.pos).m_247087_()) {
                        this.toucan.m_9236_().setBlockAndUpdate(this.pos, state);
                        if (!this.toucan.isEnchanted()) {
                            this.toucan.setSaplingState(null);
                        }
                    }
                    this.stop();
                }
            } else {
                BlockPos moveTo = this.pos;
                if (this.encircleTime > 0) {
                    moveTo = this.getVultureCirclePos(this.pos, 3.0F, up);
                }
                if (moveTo != null) {
                    if (this.encircleTime <= 0 && !this.toucan.hasLineOfSightSapling(this.pos)) {
                        this.toucan.setFlying(false);
                        this.toucan.m_21573_().moveTo((double) ((float) moveTo.m_123341_() + 0.5F), (double) moveTo.m_123342_() + up + 0.5, (double) ((float) moveTo.m_123343_() + 0.5F), 1.0);
                    } else {
                        this.toucan.setFlying(true);
                        this.toucan.m_21566_().setWantedPosition((double) ((float) moveTo.m_123341_() + 0.5F), (double) moveTo.m_123342_() + up + 0.5, (double) ((float) moveTo.m_123343_() + 0.5F), 1.0);
                    }
                }
            }
        }

        public BlockPos getVultureCirclePos(BlockPos target, float circleDistance, double yLevel) {
            float angle = 0.13962634F * (float) (this.clockwise ? -this.encircleTime : this.encircleTime);
            double extraX = (double) (circleDistance * Mth.sin(angle));
            double extraZ = (double) (circleDistance * Mth.cos(angle));
            BlockPos pos = new BlockPos((int) ((double) ((float) target.m_123341_() + 0.5F) + extraX), (int) ((double) (target.m_123342_() + 1) + yLevel), (int) ((double) ((float) target.m_123343_() + 0.5F) + extraZ));
            return this.toucan.m_9236_().m_46859_(pos) ? pos : null;
        }

        @Override
        public void stop() {
            this.toucan.aiItemFlag = false;
            this.pos = null;
            this.plantTime = 0;
            this.encircleTime = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return this.pos != null && this.toucan.getSaplingState() != null;
        }

        private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
            return blockpos.m_123331_(new BlockPos((int) positionVec.x(), blockpos.m_123342_(), (int) positionVec.z())) < distance * distance;
        }

        private BlockPos getSaplingPlantPos() {
            BlockState state = this.toucan.getSaplingState();
            if (state != null) {
                for (int i = 0; i < 15; i++) {
                    BlockPos pos = this.toucan.m_20183_().offset(EntityToucan.this.f_19796_.nextInt(10) - 8, EntityToucan.this.f_19796_.nextInt(8) - 4, EntityToucan.this.f_19796_.nextInt(16) - 8);
                    if (state.m_60710_(this.toucan.m_9236_(), pos) && this.toucan.m_9236_().m_46859_(pos.above()) && this.toucan.hasLineOfSightSapling(pos)) {
                        return pos;
                    }
                }
            }
            return null;
        }
    }

    private class AIWanderIdle extends Goal {

        protected final EntityToucan toucan;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        public AIWanderIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.toucan = EntityToucan.this;
        }

        @Override
        public boolean canUse() {
            if (this.toucan.m_20160_() || this.toucan.getSaplingState() != null || EntityToucan.this.aiItemFlag || this.toucan.m_5448_() != null && this.toucan.m_5448_().isAlive() || this.toucan.m_20159_()) {
                return false;
            } else if (this.toucan.m_217043_().nextInt(45) != 0 && !this.toucan.isFlying()) {
                return false;
            } else {
                if (this.toucan.m_20096_()) {
                    this.flightTarget = EntityToucan.this.f_19796_.nextInt(6) == 0;
                } else {
                    this.flightTarget = EntityToucan.this.f_19796_.nextInt(5) != 0 && this.toucan.timeFlying < 200;
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
            }
        }

        @Override
        public void tick() {
            if (this.flightTarget) {
                this.toucan.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.toucan.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityToucan.this.isFlying() && this.toucan.m_20096_()) {
                this.toucan.setFlying(false);
            }
            if (EntityToucan.this.isFlying() && this.toucan.m_20096_() && this.toucan.timeFlying > 10) {
                this.toucan.setFlying(false);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.toucan.m_20182_();
            if (this.toucan.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                if (this.toucan.timeFlying > 50 && this.toucan.isOverLeaves() && !this.toucan.m_20096_()) {
                    return this.toucan.getBlockGrounding(vector3d);
                } else {
                    return this.toucan.timeFlying >= 200 && !this.toucan.isOverWaterOrVoid() ? this.toucan.getBlockGrounding(vector3d) : this.toucan.getBlockInViewAway(vector3d, 0.0F);
                }
            } else if (!this.toucan.m_20096_()) {
                return this.toucan.getBlockGrounding(vector3d);
            } else {
                if (this.toucan.isOverLeaves()) {
                    for (int i = 0; i < 15; i++) {
                        BlockPos pos = this.toucan.m_20183_().offset(EntityToucan.this.f_19796_.nextInt(16) - 8, EntityToucan.this.f_19796_.nextInt(8) - 4, EntityToucan.this.f_19796_.nextInt(16) - 8);
                        if (!this.toucan.m_9236_().getBlockState(pos.above()).m_280296_() && this.toucan.m_9236_().getBlockState(pos).m_280296_() && this.toucan.m_21692_(pos) >= 0.0F) {
                            return Vec3.atBottomCenterOf(pos);
                        }
                    }
                }
                return LandRandomPos.getPos(this.toucan, 16, 7);
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.toucan.aiItemFlag) {
                return false;
            } else {
                return this.flightTarget ? this.toucan.isFlying() && this.toucan.m_20275_(this.x, this.y, this.z) > 2.0 : !this.toucan.m_21573_().isDone() && !this.toucan.m_20160_();
            }
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.toucan.setFlying(true);
                this.toucan.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.toucan.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.toucan.m_21573_().stop();
            super.stop();
        }
    }
}