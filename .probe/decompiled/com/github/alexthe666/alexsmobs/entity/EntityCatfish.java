package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityCatfish extends WaterAnimal implements FlyingAnimal, Bucketable, ContainerListener {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CATFISH_SIZE = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SPIT_TIME = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HAS_SWALLOWED_ENTITY = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<String> SWALLOWED_ENTITY_TYPE = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<CompoundTag> SWALLOWED_ENTITY_DATA = SynchedEntityData.defineId(EntityCatfish.class, EntityDataSerializers.COMPOUND_TAG);

    private static final EntityDimensions SMALL_SIZE = EntityDimensions.scalable(0.9F, 0.6F);

    private static final EntityDimensions MEDIUM_SIZE = EntityDimensions.scalable(1.25F, 0.9F);

    private static final EntityDimensions LARGE_SIZE = EntityDimensions.scalable(1.9F, 0.9F);

    public static final ResourceLocation MEDIUM_LOOT = new ResourceLocation("alexsmobs", "entities/catfish_medium");

    public static final ResourceLocation LARGE_LOOT = new ResourceLocation("alexsmobs", "entities/catfish_large");

    public SimpleContainer catfishInventory;

    private int eatCooldown = 0;

    protected EntityCatfish(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.initCatfishInventory();
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 15.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sze) {
        return sze > 2;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(3, new EntityCatfish.TargetFoodGoal(this));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(Items.SEA_LANTERN), false));
        this.f_21345_.addGoal(5, new EntityCatfish.FascinateLanternGoal(this));
        this.f_21345_.addGoal(6, new AnimalAISwimBottom(this, 1.0, 7));
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.fromBucket() && !this.requiresCustomPersistence() && !this.m_8077_();
    }

    private void initCatfishInventory() {
        SimpleContainer animalchest = this.catfishInventory;
        int size = this.getCatfishSize() > 2 ? 1 : (this.getCatfishSize() == 1 ? 9 : 3);
        this.catfishInventory = new SimpleContainer(size) {

            @Override
            public boolean stillValid(Player player) {
                return EntityCatfish.this.m_6084_() && !EntityCatfish.this.f_19817_;
            }
        };
        this.catfishInventory.addListener(this);
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.catfishInventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.catfishInventory.setItem(j, itemstack.copy());
                }
            }
        }
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.catfishInventory != null) {
            for (int i = 0; i < this.catfishInventory.getContainerSize(); i++) {
                this.m_19983_(this.catfishInventory.getItem(i));
            }
            this.catfishInventory.clearContent();
        }
        if (this.getCatfishSize() == 2) {
            this.spit();
        }
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.fromBucket() || this.hasSwallowedEntity() || this.catfishInventory != null && !this.catfishInventory.isEmpty();
    }

    public static boolean canCatfishSpawn(EntityType<EntityCatfish> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_8055_(pos).m_60819_().is(Fluids.WATER) && random.nextInt(1) == 0;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.catfishSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(CATFISH_SIZE, 0);
        this.f_19804_.define(SPIT_TIME, 0);
        this.f_19804_.define(SWALLOWED_ENTITY_TYPE, "minecraft:pig");
        this.f_19804_.define(SWALLOWED_ENTITY_DATA, new CompoundTag());
        this.f_19804_.define(HAS_SWALLOWED_ENTITY, false);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide) {
            if (this.getSpitTime() > 0) {
                this.setSpitTime(this.getSpitTime() - 1);
            }
            if (this.eatCooldown > 0) {
                this.eatCooldown--;
            }
        }
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        boolean inSeaPickle = false;
        int width = (int) Math.ceil((double) (this.m_20205_() / 2.0F));
        int height = (int) Math.ceil((double) (this.m_20206_() / 2.0F));
        BlockPos.MutableBlockPos pos = this.m_20183_().mutable();
        BlockPos vomitTo = null;
        for (int i = -width; i <= width; i++) {
            for (int j = -height; j <= height; j++) {
                for (int k = -width; k <= width; k++) {
                    pos.set(this.m_20185_() + (double) i, this.m_20186_() + (double) j, this.m_20189_() + (double) k);
                    if (this.m_9236_().getBlockState(pos).m_60713_(Blocks.SEA_PICKLE)) {
                        inSeaPickle = true;
                        vomitTo = pos;
                        break;
                    }
                }
            }
        }
        if (inSeaPickle && this.canSpit()) {
            if (this.getSpitTime() == 0) {
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.PLAYER_BURP, this.m_6121_(), this.getVoicePitch());
            }
            if (vomitTo != null) {
                Vec3 face = Vec3.atCenterOf(vomitTo).subtract(this.getMouthVec());
                double d0 = face.horizontalDistance();
                this.m_146926_((float) (-Mth.atan2(face.y, d0) * 180.0F / (float) Math.PI));
                this.m_146922_((float) Mth.atan2(face.z, face.x) * (180.0F / (float) Math.PI) - 90.0F);
                this.f_20883_ = this.m_146908_();
                this.f_20885_ = this.m_146908_();
            }
            this.spit();
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getDefaultLootTable() {
        if (this.getCatfishSize() == 2) {
            return LARGE_LOOT;
        } else {
            return this.getCatfishSize() == 1 ? MEDIUM_LOOT : super.m_7582_();
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (CATFISH_SIZE.equals(accessor)) {
            this.m_6210_();
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) (10.0F * (float) this.getCatfishSize() + 10.0F));
            this.m_5634_(50.0F);
        }
        super.m_7350_(accessor);
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean bucketed) {
        this.f_19804_.set(FROM_BUCKET, bucketed);
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compound = bucket.getOrCreateTag();
        this.addAdditionalSaveData(compound);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        this.readAdditionalSaveData(compound);
    }

    @Override
    public ItemStack getBucketItemStack() {
        int catfishSize = this.getCatfishSize();
        Item item = switch(catfishSize) {
            case 1 ->
                (Item) AMItemRegistry.MEDIUM_CATFISH_BUCKET.get();
            case 2 ->
                (Item) AMItemRegistry.LARGE_CATFISH_BUCKET.get();
            default ->
                (Item) AMItemRegistry.SMALL_CATFISH_BUCKET.get();
        };
        return new ItemStack(item);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public int getCatfishSize() {
        return Mth.clamp(this.f_19804_.get(CATFISH_SIZE), 0, 2);
    }

    public void setCatfishSize(int catfishSize) {
        this.f_19804_.set(CATFISH_SIZE, catfishSize);
    }

    public int getSpitTime() {
        return this.f_19804_.get(SPIT_TIME);
    }

    public void setSpitTime(int time) {
        this.f_19804_.set(SPIT_TIME, time);
    }

    public boolean isSpitting() {
        return this.getSpitTime() > 0;
    }

    public String getSwallowedEntityType() {
        return this.f_19804_.get(SWALLOWED_ENTITY_TYPE);
    }

    public void setSwallowedEntityType(String containedEntityType) {
        this.f_19804_.set(SWALLOWED_ENTITY_TYPE, containedEntityType);
    }

    public CompoundTag getSwallowedData() {
        return this.f_19804_.get(SWALLOWED_ENTITY_DATA);
    }

    public void setSwallowedData(CompoundTag containedData) {
        this.f_19804_.set(SWALLOWED_ENTITY_DATA, containedData);
    }

    public boolean hasSwallowedEntity() {
        return this.f_19804_.get(HAS_SWALLOWED_ENTITY);
    }

    public void setHasSwallowedEntity(boolean swallowedEntity) {
        this.f_19804_.set(HAS_SWALLOWED_ENTITY, swallowedEntity);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.getDimsForCatfish().scale(this.m_6134_());
    }

    @Override
    public boolean hurt(DamageSource source, float f) {
        if (super.m_6469_(source, f)) {
            this.spit();
            return true;
        } else {
            return false;
        }
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.getItem() == Items.SEA_PICKLE) {
            this.spit();
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putFloat("CatfishSize", (float) this.getCatfishSize());
        if (this.catfishInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.catfishInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.catfishInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putString("ContainedEntityType", this.getSwallowedEntityType());
        compound.put("ContainedData", this.getSwallowedData());
        compound.putBoolean("HasSwallowedEntity", this.hasSwallowedEntity());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setCatfishSize(compound.getInt("CatfishSize"));
        if (this.catfishInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initCatfishInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.catfishInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
        this.setSwallowedEntityType(compound.getString("ContainedEntityType"));
        if (!compound.getCompound("ContainedData").isEmpty()) {
            this.setSwallowedData(compound.getCompound("ContainedData"));
        }
        this.setHasSwallowedEntity(compound.getBoolean("HasSwallowedEntity"));
    }

    private EntityDimensions getDimsForCatfish() {
        return switch(this.getCatfishSize()) {
            case 1 ->
                MEDIUM_SIZE;
            case 2 ->
                LARGE_SIZE;
            default ->
                SMALL_SIZE;
        };
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setCatfishSize(this.f_19796_.nextFloat() < 0.35F ? 1 : 0);
        if (this.f_19796_.nextFloat() < 0.1F) {
            Holder<Biome> holder = worldIn.m_204166_(this.m_20183_());
            if (holder.is(AMTagRegistry.SPAWNS_HUGE_CATFISH) || reason == MobSpawnType.SPAWN_EGG) {
                this.setCatfishSize(2);
            }
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
    }

    @Override
    public void containerChanged(Container container0) {
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.getCatfishSize() != 2 && !this.isFull() && this.catfishInventory != null && this.catfishInventory.addItem(itemstack).isEmpty()) {
            this.m_21053_(itemEntity);
            this.m_7938_(itemEntity, itemstack.getCount());
            itemEntity.m_146870_();
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.getVoicePitch());
        }
    }

    public boolean isFull() {
        if (this.getCatfishSize() != 2 && this.catfishInventory != null) {
            for (int i = 0; i < this.catfishInventory.getContainerSize(); i++) {
                if (this.catfishInventory.getItem(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return this.hasSwallowedEntity();
        }
    }

    @Override
    public float getVoicePitch() {
        float f = (float) (3 - this.getCatfishSize()) * 0.33F;
        return (float) ((double) super.m_6100_() * Math.sqrt((double) f) * 1.2F);
    }

    public boolean swallowEntity(Entity entity) {
        if (this.getCatfishSize() == 2 && entity instanceof Mob mob) {
            this.setHasSwallowedEntity(true);
            ResourceLocation mobtype = ForgeRegistries.ENTITY_TYPES.getKey(mob.m_6095_());
            if (mobtype != null) {
                this.setSwallowedEntityType(mobtype.toString());
            }
            CompoundTag tag = new CompoundTag();
            mob.addAdditionalSaveData(tag);
            this.setSwallowedData(tag);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.getVoicePitch());
            return true;
        } else {
            if (this.getCatfishSize() < 2 && entity instanceof ItemEntity item) {
                this.pickUpItem(item);
            }
            return false;
        }
    }

    public boolean canSpit() {
        return this.getCatfishSize() == 2 ? this.hasSwallowedEntity() : this.catfishInventory != null && !this.catfishInventory.isEmpty();
    }

    public void spit() {
        this.setSpitTime(10);
        this.eatCooldown = 60 + this.f_19796_.nextInt(60);
        if (this.getCatfishSize() == 2) {
            if (this.hasSwallowedEntity()) {
                EntityType type = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(this.getSwallowedEntityType()));
                if (type != null && type.create(this.m_9236_()) instanceof LivingEntity alive) {
                    alive.readAdditionalSaveData(this.getSwallowedData());
                    alive.setHealth(Math.max(2.0F, alive.getMaxHealth() * 0.25F));
                    alive.m_146922_(this.f_19796_.nextFloat() * 360.0F - 180.0F);
                    alive.m_146884_(this.getMouthVec());
                    if (this.m_9236_().m_7967_(alive)) {
                        this.setHasSwallowedEntity(false);
                        this.setSwallowedEntityType("minecraft:pig");
                        this.setSwallowedData(new CompoundTag());
                    }
                }
            }
        } else {
            ItemStack itemStack = ItemStack.EMPTY;
            int index = -1;
            if (this.catfishInventory != null) {
                for (int i = 0; i < this.catfishInventory.getContainerSize(); i++) {
                    if (!this.catfishInventory.getItem(i).isEmpty()) {
                        itemStack = this.catfishInventory.getItem(i);
                        index = i;
                        break;
                    }
                }
            }
            if (!itemStack.isEmpty()) {
                Vec3 vec3 = this.getMouthVec();
                Vec3 vec32 = vec3.subtract(this.m_20182_()).normalize().scale(0.14F);
                ItemEntity item = new ItemEntity(this.m_9236_(), vec3.x, vec3.y, vec3.z, itemStack, vec32.x, vec32.y, vec32.z);
                item.m_20256_(Vec3.ZERO);
                item.setPickUpDelay(30);
                if (this.m_9236_().m_7967_(item) && this.catfishInventory != null) {
                    this.catfishInventory.setItem(index, ItemStack.EMPTY);
                }
            }
        }
    }

    private Vec3 getMouthVec() {
        Vec3 vec3 = new Vec3(0.0, (double) (this.m_20206_() * 0.25F), (double) (this.m_20205_() * 0.8F)).xRot(this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
        return this.m_20182_().add(vec3);
    }

    private boolean isFood(Entity entity) {
        return this.getCatfishSize() == 2 ? entity instanceof Mob && !(entity instanceof EntityCatfish) && entity.getBbHeight() <= 1.0F : entity instanceof ItemEntity && ((ItemEntity) entity).getAge() > 35;
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
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
    public boolean isFlying() {
        return false;
    }

    private class FascinateLanternGoal extends Goal {

        private final int searchLength;

        private final int verticalSearchRange;

        protected BlockPos destinationBlock;

        private final EntityCatfish fish;

        private int runDelay = 70;

        private int chillTime = 0;

        private int maxChillTime = 200;

        private FascinateLanternGoal(EntityCatfish fish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.fish = fish;
            this.searchLength = 16;
            this.verticalSearchRange = 6;
        }

        @Override
        public boolean canContinueToUse() {
            return this.destinationBlock != null && this.isSeaLantern(this.fish.m_9236_(), this.destinationBlock.mutable()) && this.isCloseToLantern(16.0) && !this.fish.isFull();
        }

        public boolean isCloseToLantern(double dist) {
            return this.destinationBlock == null || this.fish.m_20238_(Vec3.atCenterOf(this.destinationBlock)) < dist * dist;
        }

        @Override
        public boolean canUse() {
            if (!this.fish.m_20072_()) {
                return false;
            } else if (this.runDelay > 0) {
                this.runDelay--;
                return false;
            } else {
                this.runDelay = 70 + this.fish.f_19796_.nextInt(70);
                return !this.fish.isFull() && this.searchForDestination();
            }
        }

        @Override
        public void start() {
            this.chillTime = 0;
            this.maxChillTime = 10 + EntityCatfish.this.f_19796_.nextInt(20);
        }

        @Override
        public void tick() {
            Vec3 vec = Vec3.atCenterOf(this.destinationBlock);
            this.fish.m_21573_().moveTo(vec.x, vec.y, vec.z, 1.0);
            if (this.fish.m_20238_(vec) < (double) (1.0F + this.fish.m_20205_() * 0.6F)) {
                Vec3 face = vec.subtract(this.fish.m_20182_());
                this.fish.m_20256_(this.fish.m_20184_().add(face.normalize().scale(0.1F)));
                if (this.chillTime++ > this.maxChillTime) {
                    this.destinationBlock = null;
                }
            }
        }

        @Override
        public void stop() {
            this.destinationBlock = null;
        }

        protected boolean searchForDestination() {
            int lvt_1_1_ = this.searchLength;
            BlockPos lvt_3_1_ = this.fish.m_20183_();
            BlockPos.MutableBlockPos lvt_4_1_ = new BlockPos.MutableBlockPos();
            for (int lvt_5_1_ = -8; lvt_5_1_ <= 2; lvt_5_1_++) {
                for (int lvt_6_1_ = 0; lvt_6_1_ < lvt_1_1_; lvt_6_1_++) {
                    for (int lvt_7_1_ = 0; lvt_7_1_ <= lvt_6_1_; lvt_7_1_ = lvt_7_1_ > 0 ? -lvt_7_1_ : 1 - lvt_7_1_) {
                        for (int lvt_8_1_ = lvt_7_1_ < lvt_6_1_ && lvt_7_1_ > -lvt_6_1_ ? lvt_6_1_ : 0; lvt_8_1_ <= lvt_6_1_; lvt_8_1_ = lvt_8_1_ > 0 ? -lvt_8_1_ : 1 - lvt_8_1_) {
                            lvt_4_1_.setWithOffset(lvt_3_1_, lvt_7_1_, lvt_5_1_ - 1, lvt_8_1_);
                            if (this.isSeaLantern(this.fish.m_9236_(), lvt_4_1_) && this.fish.canSeeBlock(lvt_4_1_)) {
                                this.destinationBlock = lvt_4_1_;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean isSeaLantern(Level world, BlockPos.MutableBlockPos pos) {
            return world.getBlockState(pos).m_60713_(Blocks.SEA_LANTERN);
        }
    }

    private class TargetFoodGoal extends Goal {

        private final EntityCatfish catfish;

        private Entity food;

        private int executionCooldown = 50;

        public TargetFoodGoal(EntityCatfish catfish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.catfish = catfish;
        }

        @Override
        public boolean canUse() {
            if (this.catfish.m_20072_() && this.catfish.eatCooldown <= 0) {
                if (this.executionCooldown > 0) {
                    this.executionCooldown--;
                } else {
                    this.executionCooldown = 50 + EntityCatfish.this.f_19796_.nextInt(50);
                    if (!this.catfish.isFull()) {
                        List<Entity> list = this.catfish.m_9236_().m_6443_(Entity.class, this.catfish.m_20191_().inflate(8.0, 8.0, 8.0), EntitySelector.NO_SPECTATORS.and(entity -> entity != this.catfish && this.catfish.isFood(entity)));
                        list.sort(Comparator.comparingDouble(this.catfish::m_20280_));
                        if (!list.isEmpty()) {
                            this.food = (Entity) list.get(0);
                            return true;
                        }
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.food != null && this.food.isAlive() && !this.catfish.isFull();
        }

        @Override
        public void stop() {
            this.executionCooldown = 5;
        }

        @Override
        public void tick() {
            this.catfish.m_21573_().moveTo(this.food.getX(), this.food.getY(0.5), this.food.getZ(), 1.0);
            float eatDist = this.catfish.m_20205_() * 0.65F + this.food.getBbWidth();
            if (this.catfish.m_20270_(this.food) < eatDist + 3.0F && this.catfish.m_142582_(this.food)) {
                Vec3 delta = this.catfish.getMouthVec().subtract(this.food.position()).normalize().scale(0.1F);
                this.food.setDeltaMovement(this.food.getDeltaMovement().add(delta));
                if (this.catfish.m_20270_(this.food) < eatDist) {
                    if (this.food instanceof Player) {
                        this.food.hurt(this.catfish.m_269291_().mobAttack(this.catfish), 12000.0F);
                    } else if (this.catfish.swallowEntity(this.food)) {
                        this.catfish.m_146850_(GameEvent.EAT);
                        this.catfish.m_5496_(SoundEvents.GENERIC_EAT, this.catfish.m_6121_(), this.catfish.getVoicePitch());
                        this.food.discard();
                    }
                }
            }
        }
    }
}