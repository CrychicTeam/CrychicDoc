package com.yungnickyoung.minecraft.yungsapi.world.spawner;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;

public class MobSpawnerData {

    public final int spawnDelay;

    public final SimpleWeightedRandomList<SpawnData> spawnPotentials;

    public final SpawnData nextSpawnData;

    public final int minSpawnDelay;

    public final int maxSpawnDelay;

    public final int spawnCount;

    public final int maxNearbyEntities;

    public final int requiredPlayerRange;

    public final int spawnRange;

    public MobSpawnerData(MobSpawnerData.Builder builder) {
        this.spawnDelay = builder.spawnDelay;
        this.spawnPotentials = builder.spawnPotentials;
        this.nextSpawnData = builder.nextSpawnData;
        this.minSpawnDelay = builder.minSpawnDelay;
        this.maxSpawnDelay = builder.maxSpawnDelay;
        this.spawnCount = builder.spawnCount;
        this.maxNearbyEntities = builder.maxNearbyEntities;
        this.requiredPlayerRange = builder.requiredPlayerRange;
        this.spawnRange = builder.spawnRange;
    }

    public CompoundTag save() {
        return this.save(new CompoundTag());
    }

    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putShort("Delay", (short) this.spawnDelay);
        compoundTag.putShort("MinSpawnDelay", (short) this.minSpawnDelay);
        compoundTag.putShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
        compoundTag.putShort("SpawnCount", (short) this.spawnCount);
        compoundTag.putShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
        compoundTag.putShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
        compoundTag.putShort("SpawnRange", (short) this.spawnRange);
        compoundTag.put("SpawnData", (Tag) SpawnData.CODEC.encodeStart(NbtOps.INSTANCE, this.nextSpawnData).result().orElseThrow(() -> new IllegalStateException("Invalid SpawnData")));
        compoundTag.put("SpawnPotentials", (Tag) SpawnData.LIST_CODEC.encodeStart(NbtOps.INSTANCE, this.spawnPotentials).result().orElseThrow());
        return compoundTag;
    }

    public static MobSpawnerData.Builder builder() {
        return new MobSpawnerData.Builder();
    }

    public static class Builder {

        private int spawnDelay = 20;

        private SimpleWeightedRandomList<SpawnData> spawnPotentials = SimpleWeightedRandomList.empty();

        private SpawnData nextSpawnData = new SpawnData();

        private int minSpawnDelay = 200;

        private int maxSpawnDelay = 800;

        private int spawnCount = 4;

        private int maxNearbyEntities = 6;

        private int requiredPlayerRange = 16;

        private int spawnRange = 4;

        public MobSpawnerData build() {
            return new MobSpawnerData(this);
        }

        public MobSpawnerData.Builder spawnDelay(int spawnDelay) {
            this.spawnDelay = spawnDelay;
            return this;
        }

        public MobSpawnerData.Builder spawnPotentials(SimpleWeightedRandomList<SpawnData> spawnPotentials) {
            this.spawnPotentials = spawnPotentials;
            return this;
        }

        public MobSpawnerData.Builder setEntityType(EntityType<?> entityType) {
            this.nextSpawnData.getEntityToSpawn().putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString());
            return this;
        }

        public MobSpawnerData.Builder minSpawnDelay(int minSpawnDelay) {
            this.minSpawnDelay = minSpawnDelay;
            return this;
        }

        public MobSpawnerData.Builder maxSpawnDelay(int maxSpawnDelay) {
            this.maxSpawnDelay = maxSpawnDelay;
            return this;
        }

        public MobSpawnerData.Builder spawnCount(int spawnCount) {
            this.spawnCount = spawnCount;
            return this;
        }

        public MobSpawnerData.Builder maxNearbyEntities(int maxNearbyEntities) {
            this.maxNearbyEntities = maxNearbyEntities;
            return this;
        }

        public MobSpawnerData.Builder requiredPlayerRange(int requiredPlayerRange) {
            this.requiredPlayerRange = requiredPlayerRange;
            return this;
        }

        public MobSpawnerData.Builder spawnRange(int spawnRange) {
            this.spawnRange = spawnRange;
            return this;
        }
    }
}