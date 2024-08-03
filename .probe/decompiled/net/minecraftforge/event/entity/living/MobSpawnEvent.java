package net.minecraftforge.event.entity.living;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class MobSpawnEvent extends EntityEvent {

    private final ServerLevelAccessor level;

    private final double x;

    private final double y;

    private final double z;

    @Internal
    protected MobSpawnEvent(Mob mob, ServerLevelAccessor level, double x, double y, double z) {
        super(mob);
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Mob getEntity() {
        return (Mob) super.getEntity();
    }

    public ServerLevelAccessor getLevel() {
        return this.level;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    @HasResult
    public static class AllowDespawn extends MobSpawnEvent {

        public AllowDespawn(Mob mob, ServerLevelAccessor level) {
            super(mob, level, mob.m_20185_(), mob.m_20186_(), mob.m_20189_());
        }
    }

    @Cancelable
    public static class FinalizeSpawn extends MobSpawnEvent {

        private final MobSpawnType spawnType;

        @Nullable
        private final BaseSpawner spawner;

        private DifficultyInstance difficulty;

        @Nullable
        private SpawnGroupData spawnData;

        @Nullable
        private CompoundTag spawnTag;

        @Internal
        public FinalizeSpawn(Mob entity, ServerLevelAccessor level, double x, double y, double z, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag spawnTag, @Nullable BaseSpawner spawner) {
            super(entity, level, x, y, z);
            this.difficulty = difficulty;
            this.spawnType = spawnType;
            this.spawnData = spawnData;
            this.spawnTag = spawnTag;
            this.spawner = spawner;
        }

        public DifficultyInstance getDifficulty() {
            return this.difficulty;
        }

        public void setDifficulty(DifficultyInstance inst) {
            this.difficulty = inst;
        }

        public MobSpawnType getSpawnType() {
            return this.spawnType;
        }

        @Nullable
        public SpawnGroupData getSpawnData() {
            return this.spawnData;
        }

        public void setSpawnData(@Nullable SpawnGroupData data) {
            this.spawnData = data;
        }

        @Nullable
        public CompoundTag getSpawnTag() {
            return this.spawnTag;
        }

        public void setSpawnTag(@Nullable CompoundTag tag) {
            this.spawnTag = tag;
        }

        @Nullable
        public BaseSpawner getSpawner() {
            return this.spawner;
        }

        public void setSpawnCancelled(boolean cancel) {
            this.getEntity().setSpawnCancelled(cancel);
        }

        public boolean isSpawnCancelled() {
            return this.getEntity().isSpawnCancelled();
        }
    }

    @HasResult
    public static class PositionCheck extends MobSpawnEvent {

        @Nullable
        private final BaseSpawner spawner;

        private final MobSpawnType spawnType;

        public PositionCheck(Mob mob, ServerLevelAccessor level, MobSpawnType spawnType, @Nullable BaseSpawner spawner) {
            super(mob, level, mob.m_20185_(), mob.m_20186_(), mob.m_20189_());
            this.spawnType = spawnType;
            this.spawner = spawner;
        }

        @Nullable
        public BaseSpawner getSpawner() {
            return this.spawner;
        }

        public MobSpawnType getSpawnType() {
            return this.spawnType;
        }
    }

    @HasResult
    public static class SpawnPlacementCheck extends Event {

        private final EntityType<?> entityType;

        private final ServerLevelAccessor level;

        private final MobSpawnType spawnType;

        private final BlockPos pos;

        private final RandomSource random;

        private final boolean defaultResult;

        @Internal
        public SpawnPlacementCheck(EntityType<?> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, boolean defaultResult) {
            this.entityType = entityType;
            this.level = level;
            this.spawnType = spawnType;
            this.pos = pos;
            this.random = random;
            this.defaultResult = defaultResult;
        }

        public EntityType<?> getEntityType() {
            return this.entityType;
        }

        public ServerLevelAccessor getLevel() {
            return this.level;
        }

        public MobSpawnType getSpawnType() {
            return this.spawnType;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public RandomSource getRandom() {
            return this.random;
        }

        public boolean getDefaultResult() {
            return this.defaultResult;
        }
    }
}