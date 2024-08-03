package snownee.kiwi.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

public class KiwiEntityTypeBuilder<T extends Entity> {

    private final Class<?> type;

    private EntityType.EntityFactory<T> factory;

    private MobCategory category = MobCategory.MISC;

    private ImmutableSet<Block> immuneTo = ImmutableSet.of();

    private boolean serialize = true;

    private boolean summon = true;

    private boolean fireImmune;

    private boolean canSpawnFarFromPlayer;

    private int clientTrackingRange = 5;

    private int updateInterval = 3;

    private EntityDimensions dimensions = EntityDimensions.scalable(-1.0F, -1.0F);

    private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

    private Boolean forceTrackedVelocityUpdates;

    @Nullable
    private Supplier<AttributeSupplier.Builder> defaultAttributeBuilder;

    private SpawnPlacements.Type restrictionLocation;

    private Heightmap.Types restrictionHeightmap;

    private SpawnPlacements.SpawnPredicate<T> spawnPredicate;

    private KiwiEntityTypeBuilder(Class<?> type) {
        this.type = type;
    }

    public static <T extends Entity> KiwiEntityTypeBuilder<T> create() {
        return new KiwiEntityTypeBuilder<>(Entity.class);
    }

    public static <T extends LivingEntity> KiwiEntityTypeBuilder<T> createLiving() {
        return new KiwiEntityTypeBuilder<>(LivingEntity.class);
    }

    public static <T extends Entity> KiwiEntityTypeBuilder<T> createMob() {
        return new KiwiEntityTypeBuilder<>(Mob.class);
    }

    public KiwiEntityTypeBuilder<T> spawnGroup(MobCategory group) {
        Objects.requireNonNull(group, "Spawn group cannot be null");
        this.category = group;
        return this;
    }

    public <N extends T> KiwiEntityTypeBuilder<N> entityFactory(EntityType.EntityFactory<N> factory) {
        Objects.requireNonNull(factory, "Entity Factory cannot be null");
        this.factory = factory;
        return this;
    }

    public KiwiEntityTypeBuilder<T> dimensions(EntityDimensions dimensions) {
        Objects.requireNonNull(dimensions, "Cannot set null dimensions");
        this.dimensions = dimensions;
        return this;
    }

    public KiwiEntityTypeBuilder<T> disableSummon() {
        this.summon = false;
        return this;
    }

    public KiwiEntityTypeBuilder<T> disableSaving() {
        this.serialize = false;
        return this;
    }

    public KiwiEntityTypeBuilder<T> fireImmune() {
        this.fireImmune = true;
        return this;
    }

    public KiwiEntityTypeBuilder<T> specificSpawnBlocks(Block... blocks) {
        this.immuneTo = ImmutableSet.copyOf(blocks);
        return this;
    }

    public KiwiEntityTypeBuilder<T> spawnableFarFromPlayer() {
        this.canSpawnFarFromPlayer = true;
        return this;
    }

    public KiwiEntityTypeBuilder<T> trackRangeChunks(int i) {
        this.clientTrackingRange = i;
        return this;
    }

    public KiwiEntityTypeBuilder<T> trackedUpdateRate(int i) {
        this.updateInterval = i;
        return this;
    }

    public KiwiEntityTypeBuilder<T> requiredFeatures(FeatureFlag... featureFlags) {
        this.requiredFeatures = FeatureFlags.REGISTRY.subset(featureFlags);
        return this;
    }

    public KiwiEntityTypeBuilder<T> forceTrackedVelocityUpdates(boolean forceTrackedVelocityUpdates) {
        this.forceTrackedVelocityUpdates = forceTrackedVelocityUpdates;
        return this;
    }

    public KiwiEntityTypeBuilder<T> defaultAttributes(Supplier<AttributeSupplier.Builder> defaultAttributeBuilder) {
        Objects.requireNonNull(defaultAttributeBuilder, "Cannot set null attribute builder");
        Preconditions.checkState(this.type == LivingEntity.class || this.type == Mob.class, "Only living entities can have default attributes.");
        this.defaultAttributeBuilder = defaultAttributeBuilder;
        return this;
    }

    public KiwiEntityTypeBuilder<T> spawnRestriction(SpawnPlacements.Type location, Heightmap.Types heightmap, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        Preconditions.checkState(this.type == Mob.class, "Only mobs can have spawn restrictions.");
        this.restrictionLocation = (SpawnPlacements.Type) Objects.requireNonNull(location, "Location cannot be null.");
        this.restrictionHeightmap = (Heightmap.Types) Objects.requireNonNull(heightmap, "Heightmap type cannot be null.");
        this.spawnPredicate = (SpawnPlacements.SpawnPredicate<T>) Objects.requireNonNull(spawnPredicate, "Spawn predicate cannot be null.");
        return this;
    }

    public EntityType<T> build() {
        return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions, this.clientTrackingRange, this.updateInterval, this.requiredFeatures);
    }
}