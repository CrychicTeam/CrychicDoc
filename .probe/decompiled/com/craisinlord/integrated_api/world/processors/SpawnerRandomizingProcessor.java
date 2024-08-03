package com.craisinlord.integrated_api.world.processors;

import com.craisinlord.integrated_api.misc.mobspawners.MobSpawnerManager;
import com.craisinlord.integrated_api.modinit.IAProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SpawnerRandomizingProcessor extends StructureProcessor {

    public static final Codec<SpawnerRandomizingProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(ResourceLocation.CODEC.fieldOf("integrated_api_spawner_resourcelocation").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.rsSpawnerResourcelocation), InclusiveRange.INT.optionalFieldOf("valid_block_light_level").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.validBlockLightLevel), InclusiveRange.INT.optionalFieldOf("valid_sky_light_level").forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.validSkyLightLevel), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("delay").orElse(20).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.delay), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("max_nearby_entities").orElse(6).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.maxNearbyEntities), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("max_spawn_delay").orElse(800).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.maxSpawnDelay), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("min_spawn_delay").orElse(200).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.minSpawnDelay), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("required_player_range").orElse(16).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.requiredPlayerRange), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spawn_count").orElse(4).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.spawnCount), Codec.intRange(0, Integer.MAX_VALUE).fieldOf("spawn_range").orElse(4).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.spawnRange), BlockState.CODEC.fieldOf("spawner_replacement_block").orElse(Blocks.AIR.defaultBlockState()).forGetter(spawnerRandomizingProcessor -> spawnerRandomizingProcessor.replacementState)).apply(instance, instance.stable(SpawnerRandomizingProcessor::new)));

    public final ResourceLocation rsSpawnerResourcelocation;

    public final Optional<InclusiveRange<Integer>> validBlockLightLevel;

    public final Optional<InclusiveRange<Integer>> validSkyLightLevel;

    public final int delay;

    public final int maxNearbyEntities;

    public final int maxSpawnDelay;

    public final int minSpawnDelay;

    public final int requiredPlayerRange;

    public final int spawnCount;

    public final int spawnRange;

    public final BlockState replacementState;

    private SpawnerRandomizingProcessor(ResourceLocation rsSpawnerResourcelocation, Optional<InclusiveRange<Integer>> validBlockLightLevel, Optional<InclusiveRange<Integer>> validSkyLightLevel, int delay, int maxNearbyEntities, int maxSpawnDelay, int minSpawnDelay, int requiredPlayerRange, int spawnCount, int spawnRange, BlockState replacementState) {
        this.rsSpawnerResourcelocation = rsSpawnerResourcelocation;
        this.validBlockLightLevel = validBlockLightLevel;
        this.validSkyLightLevel = validSkyLightLevel;
        this.delay = delay;
        this.maxNearbyEntities = maxNearbyEntities;
        this.maxSpawnDelay = maxSpawnDelay;
        this.minSpawnDelay = minSpawnDelay;
        this.requiredPlayerRange = requiredPlayerRange;
        this.spawnCount = spawnCount;
        this.spawnRange = spawnRange;
        this.replacementState = replacementState;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60734_() instanceof SpawnerBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos();
            RandomSource random = structurePlacementData.getRandom(structureBlockInfoWorld.pos());
            CompoundTag spawnerNBT = this.SetMobSpawnerEntity(random);
            return spawnerNBT == null ? new StructureTemplate.StructureBlockInfo(worldPos, this.replacementState, null) : new StructureTemplate.StructureBlockInfo(worldPos, structureBlockInfoWorld.state(), spawnerNBT);
        } else {
            return structureBlockInfoWorld;
        }
    }

    private CompoundTag SetMobSpawnerEntity(RandomSource random) {
        EntityType<?> entity = MobSpawnerManager.MOB_SPAWNER_MANAGER.getSpawnerMob(this.rsSpawnerResourcelocation, random);
        if (entity == null) {
            return null;
        } else {
            ResourceLocation entityRL = BuiltInRegistries.ENTITY_TYPE.getKey(entity);
            CompoundTag compound = new CompoundTag();
            compound.putShort("Delay", (short) this.delay);
            compound.putShort("MinSpawnDelay", (short) this.minSpawnDelay);
            compound.putShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
            compound.putShort("SpawnCount", (short) this.spawnCount);
            compound.putShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
            compound.putShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
            compound.putShort("SpawnRange", (short) this.spawnRange);
            CompoundTag spawnData = new CompoundTag();
            CompoundTag spawnPotentialData = new CompoundTag();
            CompoundTag entityData = new CompoundTag();
            entityData.putString("id", entityRL.toString());
            spawnPotentialData.put("entity", entityData);
            if (this.validBlockLightLevel.isPresent() || this.validSkyLightLevel.isPresent()) {
                CompoundTag customSpawnRule = new CompoundTag();
                this.validBlockLightLevel.ifPresent(blockLightLimit -> {
                    CompoundTag blockLightTag = new CompoundTag();
                    blockLightTag.putInt("min_inclusive", (Integer) blockLightLimit.minInclusive());
                    blockLightTag.putInt("max_inclusive", (Integer) blockLightLimit.maxInclusive());
                    customSpawnRule.put("block_light_limit", blockLightTag);
                });
                this.validSkyLightLevel.ifPresent(skyLightLimit -> {
                    CompoundTag skyLightTag = new CompoundTag();
                    skyLightTag.putInt("min_inclusive", (Integer) skyLightLimit.minInclusive());
                    skyLightTag.putInt("max_exclusive", (Integer) skyLightLimit.maxInclusive());
                    customSpawnRule.put("sky_light_limit", skyLightTag);
                });
                spawnPotentialData.put("custom_spawn_rules", customSpawnRule);
                spawnData.put("custom_spawn_rules", customSpawnRule);
            }
            CompoundTag listEntry = new CompoundTag();
            listEntry.put("data", spawnPotentialData);
            listEntry.putInt("weight", 1);
            ListTag listTag = new ListTag();
            listTag.add(listEntry);
            compound.put("SpawnPotentials", listTag);
            CompoundTag entityEntry = new CompoundTag();
            entityEntry.putString("id", entityRL.toString());
            spawnData.put("entity", entityEntry);
            compound.put("SpawnData", spawnData);
            return compound;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IAProcessors.SPAWNER_RANDOMIZING_PROCESSOR.get();
    }
}