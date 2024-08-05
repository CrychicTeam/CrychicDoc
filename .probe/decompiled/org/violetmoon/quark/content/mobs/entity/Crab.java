package org.violetmoon.quark.content.mobs.entity;

import com.google.common.collect.UnmodifiableIterator;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.ai.RaveGoal;
import org.violetmoon.quark.content.mobs.module.CrabsModule;

public class Crab extends Animal implements IEntityAdditionalSpawnData, Bucketable {

    public static final int COLORS = 3;

    public static final ResourceLocation CRAB_LOOT_TABLE = new ResourceLocation("quark", "entities/crab");

    private static final EntityDataAccessor<Float> SIZE_MODIFIER = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> RAVING = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Crab.class, EntityDataSerializers.BOOLEAN);

    private int lightningCooldown;

    private Ingredient temptationItems;

    private boolean noSpike;

    private BlockPos jukeboxPosition;

    private final DynamicGameEventListener<Crab.JukeboxListener> dynamicJukeboxListener;

    public Crab(EntityType<? extends Crab> type, Level worldIn) {
        this(type, worldIn, 1.0F);
    }

    public Crab(EntityType<? extends Crab> type, Level worldIn, float sizeModifier) {
        super(type, worldIn);
        this.m_21441_(BlockPathTypes.LAVA, -1.0F);
        if (sizeModifier != 1.0F) {
            this.f_19804_.set(SIZE_MODIFIER, sizeModifier);
        }
        PositionSource source = new EntityPositionSource(this, this.m_20192_());
        this.dynamicJukeboxListener = new DynamicGameEventListener<>(new Crab.JukeboxListener(source, GameEvent.JUKEBOX_PLAY.getNotificationRadius()));
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.f_19804_.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CompoundTag tag = stack.getOrCreateTag();
        if (this.noSpike) {
            tag.putBoolean("NoSpike", true);
        }
        tag.putInt("Variant", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
        if (tag.contains("NoSpike")) {
            this.noSpike = tag.getBoolean("NoSpike");
        }
        this.f_19804_.set(VARIANT, tag.getInt("Variant"));
    }

    @NotNull
    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CrabsModule.crab_bucket);
    }

    @NotNull
    @Override
    public SoundEvent getPickupSound() {
        return QuarkSounds.BUCKET_FILL_CRAB;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> acceptor) {
        if (this.m_9236_() instanceof ServerLevel serverlevel) {
            acceptor.accept(this.dynamicJukeboxListener, serverlevel);
        }
    }

    public static boolean spawnPredicate(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return world.m_8055_(pos.below()).m_204336_(CrabsModule.crabSpawnableTag) && world.m_46803_(pos) > 8;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader world) {
        return world.m_8055_(pos.below()).m_204336_(CrabsModule.crabSpawnableTag) ? 10.0F : (float) world.m_45524_(pos, 0) - 0.5F;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @NotNull
    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SIZE_MODIFIER, 1.0F);
        this.f_19804_.define(VARIANT, -1);
        this.f_19804_.define(RAVING, false);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.getSizeModifier() >= 2.0F) {
            if (!this.isFood(player.m_21120_(hand)) && !this.m_20160_() && !player.isSecondaryUseActive()) {
                if (!this.m_9236_().isClientSide) {
                    player.m_20329_(this);
                }
                return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
            }
        } else {
            Optional<InteractionResult> result = Bucketable.bucketMobPickup(player, hand, this);
            if (result.isPresent()) {
                return (InteractionResult) result.get();
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.m_6048_() / 0.75 * 0.9;
    }

    @NotNull
    @Override
    public Vec3 getDismountLocationForPassenger(@NotNull LivingEntity entity) {
        Direction direction = this.m_6374_();
        if (direction.getAxis() != Direction.Axis.Y) {
            float scale = this.m_6134_();
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.m_20183_();
            BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
            UnmodifiableIterator var7 = entity.getDismountPoses().iterator();
            while (var7.hasNext()) {
                Pose pose = (Pose) var7.next();
                AABB aabb = entity.getLocalBoundsForPose(pose);
                for (int[] aint1 : aint) {
                    mutPos.set((double) ((float) blockpos.m_123341_() + (float) aint1[0] * scale), (double) blockpos.m_123342_(), (double) ((float) blockpos.m_123343_() + (float) aint1[1] * scale));
                    double d0 = this.m_9236_().m_45573_(mutPos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(mutPos, d0);
                        if (DismountHelper.canDismountTo(this.m_9236_(), entity, aabb.move(vec3))) {
                            entity.m_20124_(pose);
                            return vec3;
                        }
                    }
                }
            }
        }
        return super.m_7688_(entity);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return QuarkSounds.ENTITY_CRAB_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return QuarkSounds.ENTITY_CRAB_DIE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return QuarkSounds.ENTITY_CRAB_HURT;
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions size) {
        return 0.2F * size.height;
    }

    public float getSizeModifier() {
        return this.f_19804_.get(SIZE_MODIFIER);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.25));
        this.f_21345_.addGoal(2, new RaveGoal(this));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.2, this.getTemptationItems(), false));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ARMOR, 3.0).add(Attributes.ARMOR_TOUGHNESS, 2.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide && this.f_19804_.get(VARIANT) == -1) {
            int variant = 0;
            if (this.f_19796_.nextBoolean()) {
                variant += this.f_19796_.nextInt(2) + 1;
            }
            if (this.f_19796_.nextInt(3) == 0) {
                variant += 3;
            }
            this.f_19804_.set(VARIANT, variant);
        }
        if (this.lightningCooldown > 0) {
            this.lightningCooldown--;
            this.m_20095_();
        }
        if (this.isRaving() && this.m_9236_().isClientSide && this.f_19797_ % 10 == 0) {
            BlockPos below = this.m_20183_().below();
            BlockState belowState = this.m_9236_().getBlockState(below);
            if (belowState.m_204336_(BlockTags.SAND)) {
                this.m_9236_().m_46796_(2001, below, Block.getId(belowState));
            }
        }
        if (this.isRaving() && !this.m_9236_().isClientSide && this.f_19797_ % 20 == 0 && this.shouldStopRaving()) {
            this.setRaving(false);
            this.jukeboxPosition = null;
        }
    }

    public float getStepHeight() {
        float baseStep = this.f_19798_ ? 1.0F : 0.6F;
        AttributeInstance stepHeightAttribute = this.m_21051_(ForgeMod.STEP_HEIGHT_ADDITION.get());
        return stepHeightAttribute != null ? (float) Math.max(0.0, (double) baseStep + stepHeightAttribute.getValue()) : baseStep;
    }

    @NotNull
    @Override
    public EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return super.m_6972_(poseIn).scale(this.getSizeModifier());
    }

    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        DamageSources sources = this.m_9236_().damageSources();
        return super.m_6673_(source) || source == sources.cactus() || source == sources.sweetBerryBush() || source == sources.lightningBolt() || this.getSizeModifier() > 1.0F && source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE);
    }

    @Override
    public boolean fireImmune() {
        return super.m_5825_() || this.getSizeModifier() > 1.0F;
    }

    @Override
    public void thunderHit(@NotNull ServerLevel sworld, @NotNull LightningBolt lightningBolt) {
        if (this.lightningCooldown <= 0 && !this.m_9236_().isClientSide) {
            float sizeMod = this.getSizeModifier();
            if (sizeMod <= 15.0F) {
                AttributeInstance healthAttr = this.m_21051_(Attributes.MAX_HEALTH);
                if (healthAttr != null) {
                    healthAttr.addPermanentModifier(new AttributeModifier("Lightning Bonus", 0.5, AttributeModifier.Operation.ADDITION));
                }
                AttributeInstance speedAttr = this.m_21051_(Attributes.MOVEMENT_SPEED);
                if (speedAttr != null) {
                    speedAttr.addPermanentModifier(new AttributeModifier("Lightning Debuff", -0.05, AttributeModifier.Operation.ADDITION));
                }
                AttributeInstance armorAttr = this.m_21051_(Attributes.ARMOR);
                if (armorAttr != null) {
                    armorAttr.addPermanentModifier(new AttributeModifier("Lightning Bonus", 0.125, AttributeModifier.Operation.ADDITION));
                }
                float sizeModifier = Math.min(sizeMod + 1.0F, 16.0F);
                this.f_19804_.set(SIZE_MODIFIER, sizeModifier);
                this.m_6210_();
                this.lightningCooldown = 150;
            }
        }
    }

    @Override
    public void push(@NotNull Entity entityIn) {
        if (this.getSizeModifier() <= 1.0F) {
            super.m_7334_(entityIn);
        }
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        super.m_7324_(entityIn);
        if (this.m_9236_().m_46791_() != Difficulty.PEACEFUL && !this.noSpike && !this.m_20363_(entityIn) && entityIn instanceof LivingEntity && !(entityIn instanceof Crab)) {
            entityIn.hurt(this.m_9236_().damageSources().cactus(), 1.0F);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return !stack.isEmpty() && this.getTemptationItems().test(stack);
    }

    private Ingredient getTemptationItems() {
        if (this.temptationItems == null) {
            this.temptationItems = Ingredient.of(ItemTags.create(new ResourceLocation("quark", "crab_tempt_items")));
        }
        return this.temptationItems;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel sworld, @NotNull AgeableMob other) {
        return new Crab(CrabsModule.crabType, this.m_9236_());
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return CRAB_LOOT_TABLE;
    }

    public int getVariant() {
        return Math.max(0, this.f_19804_.get(VARIANT));
    }

    public void party(BlockPos pos, boolean isPartying) {
        if (isPartying) {
            if (!this.isRaving()) {
                this.jukeboxPosition = pos;
                this.setRaving(true);
            }
        } else if (pos.equals(this.jukeboxPosition) || this.jukeboxPosition == null) {
            this.jukeboxPosition = null;
            this.setRaving(false);
        }
    }

    public boolean shouldStopRaving() {
        return this.jukeboxPosition == null || !this.jukeboxPosition.m_203195_(this.m_20182_(), (double) GameEvent.JUKEBOX_PLAY.getNotificationRadius()) || !this.m_9236_().getBlockState(this.jukeboxPosition).m_60713_(Blocks.JUKEBOX);
    }

    public boolean isRaving() {
        return this.f_19804_.get(RAVING);
    }

    public void setRaving(boolean raving) {
        this.f_19804_.set(RAVING, raving);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> parameter) {
        if (parameter.equals(SIZE_MODIFIER)) {
            this.m_6210_();
        }
        super.m_7350_(parameter);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.getSizeModifier());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.f_19804_.set(SIZE_MODIFIER, buffer.readFloat());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.lightningCooldown = compound.getInt("LightningCooldown");
        this.noSpike = compound.getBoolean("NoSpike");
        if (compound.contains("EnemyCrabRating")) {
            float sizeModifier = compound.getFloat("EnemyCrabRating");
            this.f_19804_.set(SIZE_MODIFIER, sizeModifier);
        }
        if (compound.contains("Variant")) {
            this.f_19804_.set(VARIANT, compound.getInt("Variant"));
        }
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("EnemyCrabRating", this.getSizeModifier());
        compound.putInt("LightningCooldown", this.lightningCooldown);
        compound.putInt("Variant", this.f_19804_.get(VARIANT));
        compound.putBoolean("NoSpike", this.noSpike);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    public class JukeboxListener implements GameEventListener {

        private final PositionSource listenerSource;

        private final int listenerRadius;

        public JukeboxListener(PositionSource source, int radius) {
            this.listenerSource = source;
            this.listenerRadius = radius;
        }

        @NotNull
        @Override
        public PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRadius;
        }

        @Override
        public boolean handleGameEvent(@NotNull ServerLevel serverLevel, @NotNull GameEvent gameEvent, @NotNull GameEvent.Context context, @NotNull Vec3 vec3) {
            if (gameEvent == GameEvent.JUKEBOX_PLAY) {
                Crab.this.party(BlockPos.containing(vec3), true);
                return true;
            } else if (gameEvent == GameEvent.JUKEBOX_STOP_PLAY) {
                Crab.this.party(BlockPos.containing(vec3), false);
                return true;
            } else {
                return false;
            }
        }
    }
}