package net.mehvahdjukaar.supplementaries.common.misc.mob_container;

import java.util.Optional;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.CapturedMobInstance;
import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MobContainer {

    private final float width;

    private final float height;

    private final boolean isAquarium;

    @Nullable
    private MobContainer.MobNBTData data;

    @Nullable
    private CapturedMobInstance<?> mobInstance;

    private ICatchableMob mobProperties;

    private boolean needsInitialization = false;

    public MobContainer(float width, float height, boolean isAquarium) {
        this.width = width;
        this.height = height;
        this.isAquarium = isAquarium;
    }

    public CompoundTag save(CompoundTag tag) {
        if (this.data != null) {
            this.data.save(tag, this.isAquarium);
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        MobContainer.MobNBTData d = MobContainer.MobNBTData.load(tag);
        this.setData(d);
    }

    private void setData(@Nullable MobContainer.MobNBTData data) {
        this.data = data;
        this.mobInstance = null;
        this.needsInitialization = true;
    }

    private void initializeEntity(Level level, BlockPos pos) {
        this.needsInitialization = false;
        if (this.data != null && level != null && pos != null) {
            if (this.data instanceof MobContainer.MobNBTData.Bucket bucketData) {
                EntityType<?> type = BucketHelper.getEntityTypeFromBucket(bucketData.filledBucket.getItem());
                this.mobProperties = CapturedMobHandler.getDataCap(type, true);
            } else if (this.data instanceof MobContainer.MobNBTData.Entity entityData) {
                net.minecraft.world.entity.Entity entity = createStaticMob(entityData, level, pos);
                if (entity != null) {
                    this.mobProperties = CapturedMobHandler.getCatchableMobCapOrDefault(entity);
                    this.mobInstance = this.mobProperties.createCapturedMobInstance(entity, this.width, this.height);
                    this.mobInstance.onContainerWaterlogged(level.getFluidState(pos).getType() != Fluids.EMPTY);
                    this.updateLightLevel(level, pos);
                }
            }
        }
    }

    public void updateLightLevel(Level level, BlockPos pos) {
        int light = 0;
        if (level != null && !level.isClientSide && this.data != null) {
            if (this.mobProperties != null) {
                light = this.mobProperties.getLightLevel(level, pos);
            }
            BlockState state = level.getBlockState(pos);
            if ((Integer) state.m_61143_(ModBlockProperties.LIGHT_LEVEL_0_15) != light) {
                level.setBlock(pos, (BlockState) state.m_61124_(ModBlockProperties.LIGHT_LEVEL_0_15, light), 22);
            }
        }
    }

    @Nullable
    public static net.minecraft.world.entity.Entity createStaticMob(MobContainer.MobNBTData.Entity data, @NotNull Level world, BlockPos pos) {
        net.minecraft.world.entity.Entity entity = null;
        if (data != null) {
            entity = createEntityFromNBT(data.mobTag, data.uuid, world);
            if (entity == null) {
                return null;
            }
            double px = (double) pos.m_123341_() + entity.getX();
            double py = (double) pos.m_123342_() + entity.getY();
            double pz = (double) pos.m_123343_() + entity.getZ();
            entity.setPos(px, py, pz);
            entity.xOld = px;
            entity.yOld = py;
            entity.zOld = pz;
            entity.xo = px;
            entity.yo = py;
            entity.zo = pz;
        }
        return entity;
    }

    @Nullable
    public static net.minecraft.world.entity.Entity createEntityFromNBT(CompoundTag tag, @Nullable UUID id, Level world) {
        if (tag != null && tag.contains("id")) {
            net.minecraft.world.entity.Entity entity = EntityType.loadEntityRecursive(tag, world, o -> o);
            if (id != null && entity != null) {
                entity.setUUID(id);
                if (entity.hasCustomName()) {
                    entity.setCustomName(entity.getCustomName());
                }
            }
            return entity;
        } else {
            return null;
        }
    }

    public boolean interactWithBucket(ItemStack stack, Level world, BlockPos pos, @Nullable Player player, InteractionHand hand) {
        Item item = stack.getItem();
        ItemStack returnStack = ItemStack.EMPTY;
        if (this.isEmpty()) {
            if (BucketHelper.isFishBucket(item)) {
                world.playSound(null, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                returnStack = new ItemStack(Items.BUCKET);
                EntityType<?> type = BucketHelper.getEntityTypeFromBucket(stack.getItem());
                ICatchableMob cap = CapturedMobHandler.getDataCap(type, true);
                Optional<Holder<SoftFluid>> f = cap.shouldRenderWithFluid();
                ResourceLocation fluidId = (ResourceLocation) f.map(h -> ((ResourceKey) h.unwrapKey().get()).location()).orElse(null);
                if (stack.isEmpty()) {
                    Supplementaries.LOGGER.error("Bucket error 3: name none, bucket " + stack + " fluid, " + fluidId);
                }
                MobContainer.MobNBTData data = new MobContainer.MobNBTData.Bucket(null, stack.copy(), cap.getFishTextureIndex(), fluidId);
                this.setData(data);
            }
        } else if (item == Items.BUCKET) {
            if (this.data instanceof MobContainer.MobNBTData.Bucket bucketData) {
                world.playSound(null, pos, SoundEvents.BUCKET_FILL_FISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                returnStack = bucketData.filledBucket.copy();
                this.setData(null);
            } else if (this.data instanceof MobContainer.MobNBTData.Entity && this.mobInstance != null) {
                net.minecraft.world.entity.Entity temp = this.mobInstance.getEntityForRenderer();
                if (temp != null) {
                    ItemStack bucket = BucketHelper.getBucketFromEntity(temp);
                    if (!bucket.isEmpty()) {
                        world.playSound(null, pos, SoundEvents.BUCKET_FILL_FISH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        returnStack = bucket.copy();
                        this.setData(null);
                    }
                }
            }
        }
        if (!returnStack.isEmpty()) {
            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(item));
                if (!player.isCreative()) {
                    Utils.swapItem(player, hand, returnStack);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return this.data == null;
    }

    public void tick(Level pLevel, BlockPos pPos) {
        if (this.needsInitialization) {
            this.initializeEntity(pLevel, pPos);
        }
        if (this.mobInstance != null && this.data instanceof MobContainer.MobNBTData.Entity entityData) {
            this.mobInstance.containerTick(pLevel, pPos, entityData.scale, entityData.mobTag);
        }
    }

    public InteractionResult onInteract(Level world, BlockPos pos, Player player, InteractionHand hand) {
        return this.mobInstance != null && this.data instanceof MobContainer.MobNBTData.Entity entityData ? this.mobInstance.onPlayerInteract(world, pos, player, hand, entityData.mobTag) : InteractionResult.PASS;
    }

    @Nullable
    public MobContainer.MobNBTData getData() {
        return this.data;
    }

    @Nullable
    public net.minecraft.world.entity.Entity getDisplayedMob() {
        return this.mobInstance != null ? this.mobInstance.getEntityForRenderer() : null;
    }

    public Optional<Holder<SoftFluid>> shouldRenderWithFluid() {
        return this.data != null && this.isAquarium && this.mobProperties != null ? this.mobProperties.shouldRenderWithFluid() : Optional.empty();
    }

    public static CompoundTag createMobHolderItemTag(net.minecraft.world.entity.Entity mob, float blockW, float blockH, ItemStack bucketStack, boolean isAquarium) {
        String name = mob.getName().getString();
        ICatchableMob cap = CapturedMobHandler.getCatchableMobCapOrDefault(mob);
        MobContainer.MobNBTData data;
        if (isAquarium && !bucketStack.isEmpty() && cap.renderAs2DFish()) {
            Optional<Holder<SoftFluid>> f = cap.shouldRenderWithFluid();
            ResourceLocation fluidId = (ResourceLocation) f.map(h -> ((ResourceKey) h.unwrapKey().get()).location()).orElse(null);
            if (bucketStack.isEmpty()) {
                Supplementaries.LOGGER.error("Bucket error 2: name " + name + ", bucket " + bucketStack + " fluid, " + fluidId);
            }
            data = new MobContainer.MobNBTData.Bucket(name, bucketStack, cap.getFishTextureIndex(), fluidId);
        } else {
            Pair<Float, Float> dimensions = calculateMobDimensionsForContainer(mob, blockW, blockH, false);
            float scale = (Float) dimensions.getLeft();
            float yOffset = (Float) dimensions.getRight();
            CompoundTag mobTag = prepareMobTagForContainer(mob, (double) yOffset);
            if (mobTag == null) {
                return null;
            }
            UUID id = mob.getUUID();
            data = new MobContainer.MobNBTData.Entity(name, mobTag, scale, id);
        }
        CompoundTag cmp = new CompoundTag();
        data.save(cmp, isAquarium && cap.renderAs2DFish());
        return cmp;
    }

    @Nullable
    private static CompoundTag prepareMobTagForContainer(net.minecraft.world.entity.Entity entity, double yOffset) {
        double px = 0.5;
        double py = yOffset + 1.0E-4;
        double pz = 0.5;
        entity.setPos(px, py, pz);
        entity.xOld = px;
        entity.yOld = py;
        entity.zOld = pz;
        if (entity.isPassenger()) {
            entity.getVehicle().ejectPassengers();
        }
        if (entity instanceof Mob mob && !(mob instanceof Allay) && entity instanceof Bucketable) {
            mob.setPersistenceRequired();
        }
        if (entity instanceof Bucketable bucketable) {
            bucketable.setFromBucket(true);
        }
        if (entity instanceof LivingEntity le) {
            le.yHeadRotO = 0.0F;
            le.yHeadRot = 0.0F;
            le.walkAnimation.setSpeed(0.0F);
            le.walkAnimation.update(-le.walkAnimation.position(), 1.0F);
            le.walkAnimation.setSpeed(0.0F);
            le.hurtDuration = 0;
            le.hurtTime = 0;
            le.attackAnim = 0.0F;
        }
        entity.setYRot(0.0F);
        entity.yRotO = 0.0F;
        entity.xRotO = 0.0F;
        entity.setXRot(0.0F);
        entity.clearFire();
        entity.invulnerableTime = 0;
        if (entity instanceof Bat bat) {
            bat.setResting(true);
        }
        if (entity instanceof Fox fox) {
            fox.setSleeping(true);
        }
        if (entity instanceof AbstractFish abstractFish) {
            abstractFish.setFromBucket(true);
        }
        CompoundTag mobTag = new CompoundTag();
        entity.save(mobTag);
        if (mobTag.isEmpty()) {
            Supplementaries.LOGGER.error("failed to capture entity " + entity + "Something went wrong :/");
            return null;
        } else {
            mobTag.remove("Passengers");
            mobTag.remove("Leash");
            mobTag.remove("UUID");
            if (mobTag.contains("FromBucket")) {
                mobTag.putBoolean("FromBucket", true);
            }
            if (mobTag.contains("FromPot")) {
                mobTag.putBoolean("FromPot", true);
            }
            return mobTag;
        }
    }

    public static Pair<Float, Float> calculateMobDimensionsForContainer(net.minecraft.world.entity.Entity mob, float blockW, float blockH, boolean waterlogged) {
        ICatchableMob cap = CapturedMobHandler.getCatchableMobCapOrDefault(mob);
        float babyScale = 1.0F;
        if (mob instanceof LivingEntity livingEntity && livingEntity.isBaby()) {
            if (mob instanceof Villager) {
                babyScale = 1.125F;
            } else if (mob instanceof AgeableMob) {
                babyScale = 2.0F;
            } else {
                babyScale = 1.125F;
            }
        }
        float scale = 1.0F;
        float w = mob.getBbWidth() * babyScale;
        float h = mob.getBbHeight() * babyScale;
        boolean isAir = cap.shouldHover(mob, waterlogged);
        float aW = w + cap.getHitBoxWidthIncrement(mob);
        float aH = h + cap.getHitBoxHeightIncrement(mob);
        float margin = 0.125F;
        float yMargin = 0.0625F;
        float maxH = blockH - 2.0F * (isAir ? margin : yMargin);
        float maxW = blockW - 2.0F * margin;
        if (aW > maxW || aH > maxH) {
            if (aW - maxW > aH - maxH) {
                scale = maxW / aW;
            } else {
                scale = maxH / aH;
            }
        }
        String name = Utils.getID(mob.getType()).toString();
        if (name.equals("iceandfire:fire_dragon") || name.equals("iceandfire:ice_dragon") || name.equals("iceandfire:lightning_dragon")) {
            scale = (float) ((double) scale * 0.45);
        }
        float yOffset = isAir ? blockH / 2.0F - aH * scale / 2.0F : yMargin;
        if (mob instanceof Bat) {
            yOffset *= 1.5F;
        }
        return new ImmutablePair(scale, yOffset);
    }

    public void clear() {
        this.setData(null);
    }

    public abstract static class MobNBTData {

        protected final String name;

        protected int fishTexture;

        @Nullable
        protected ResourceLocation fluidID;

        private MobNBTData(String name, int fishTexture, @Nullable ResourceLocation fluidID) {
            this.name = name;
            this.fishTexture = fishTexture;
            this.fluidID = fluidID;
        }

        public boolean is2DFish() {
            return this.fishTexture != 0;
        }

        public int getFishTexture() {
            return this.fishTexture;
        }

        @Nullable
        public ResourceLocation getFluidID() {
            return this.fluidID;
        }

        protected abstract void save(CompoundTag var1, boolean var2);

        @Nullable
        protected static MobContainer.MobNBTData load(CompoundTag tag) {
            if (tag.contains("BucketHolder")) {
                return MobContainer.MobNBTData.Bucket.of(tag.getCompound("BucketHolder"));
            } else {
                return tag.contains("MobHolder") ? MobContainer.MobNBTData.Entity.of(tag.getCompound("MobHolder")) : null;
            }
        }

        @Nullable
        public String getName() {
            return this.name;
        }

        protected static class Bucket extends MobContainer.MobNBTData {

            private final ItemStack filledBucket;

            protected Bucket(@Nullable String name, ItemStack filledBucket, int fishTexture, @Nullable ResourceLocation fluidId) {
                super(getDefaultName(name, filledBucket), fishTexture, fluidId);
                this.filledBucket = filledBucket;
            }

            private static String getDefaultName(@Nullable String name, @NotNull ItemStack filledBucket) {
                if (name == null) {
                    EntityType<?> type = BucketHelper.getEntityTypeFromBucket(filledBucket.getItem());
                    name = type == null ? "Mob" : type.getDescriptionId();
                }
                return name;
            }

            @Override
            protected void save(CompoundTag tag, boolean rendersAsFish) {
                CompoundTag cmp = new CompoundTag();
                cmp.putString("Name", this.name);
                cmp.put("Bucket", this.filledBucket.save(new CompoundTag()));
                if (rendersAsFish) {
                    cmp.putInt("FishTexture", this.fishTexture);
                    if (this.fluidID != null) {
                        cmp.putString("Fluid", this.fluidID.toString());
                    }
                }
                tag.put("BucketHolder", cmp);
            }

            private static MobContainer.MobNBTData.Bucket of(CompoundTag cmp) {
                String name = cmp.getString("Name");
                int fish = cmp.getInt("FishTexture");
                ItemStack bucket = ItemStack.of(cmp.getCompound("Bucket"));
                ResourceLocation fluid = null;
                if (cmp.contains("Fluid")) {
                    fluid = new ResourceLocation(cmp.getString("Fluid"));
                }
                if (bucket.isEmpty()) {
                    Supplementaries.LOGGER.error("Bucket error 1: name " + name + ", bucket " + bucket + " fluid, " + fluid);
                }
                return new MobContainer.MobNBTData.Bucket(name, bucket, fish, fluid);
            }
        }

        public static class Entity extends MobContainer.MobNBTData {

            public final CompoundTag mobTag;

            private final float scale;

            @Nullable
            private final UUID uuid;

            protected Entity(String name, CompoundTag tag, float scale, UUID uuid) {
                this(name, 0, null, tag, scale, uuid);
            }

            protected Entity(String name, int fishTexture, @Nullable ResourceLocation fishFluid, CompoundTag tag, float scale, UUID uuid) {
                super(name, fishTexture, fishFluid);
                this.mobTag = tag;
                this.scale = scale;
                this.uuid = uuid;
            }

            private static MobContainer.MobNBTData.Entity of(CompoundTag cmp) {
                String name = cmp.getString("Name");
                int fish = cmp.getInt("FishTexture");
                ResourceLocation fluid = null;
                if (cmp.contains("Fluid")) {
                    fluid = new ResourceLocation(cmp.getString("Fluid"));
                }
                CompoundTag entityData = cmp.getCompound("EntityData");
                float scale = cmp.getFloat("Scale");
                UUID uuid = cmp.contains("UUID") ? cmp.getUUID("UUID") : null;
                return new MobContainer.MobNBTData.Entity(name, fish, fluid, entityData, scale, uuid);
            }

            public float getScale() {
                return this.scale;
            }

            @Override
            protected void save(CompoundTag tag, boolean isAquarium) {
                CompoundTag cmp = new CompoundTag();
                cmp.putString("Name", this.name);
                if (isAquarium) {
                    cmp.putInt("FishTexture", this.fishTexture);
                    if (this.fluidID != null) {
                        cmp.putString("Fluid", this.fluidID.toString());
                    }
                }
                cmp.put("EntityData", this.mobTag);
                cmp.putFloat("Scale", this.scale);
                if (this.uuid != null) {
                    cmp.putUUID("UUID", this.uuid);
                }
                tag.put("MobHolder", cmp);
            }
        }
    }
}