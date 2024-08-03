package com.yungnickyoung.minecraft.yungsapi.api.world.randomize;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import com.yungnickyoung.minecraft.yungsapi.world.structure.condition.StructureCondition;
import com.yungnickyoung.minecraft.yungsapi.world.structure.condition.StructureConditionType;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateRandomizer {

    public static final Codec<BlockStateRandomizer> CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockStateRandomizer.Entry.CODEC.listOf().fieldOf("entries").forGetter(randomizer -> randomizer.entries), BlockState.CODEC.fieldOf("defaultBlockState").forGetter(randomizer -> randomizer.defaultBlockState)).apply(instance, BlockStateRandomizer::new));

    private List<BlockStateRandomizer.Entry> entries = new ArrayList();

    private BlockState defaultBlockState = Blocks.AIR.defaultBlockState();

    public CompoundTag saveTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("defaultBlockStateId", Block.BLOCK_STATE_REGISTRY.getId(this.defaultBlockState));
        ListTag entriesTag = Util.make(new ListTag(), tag -> this.entries.forEach(entry -> {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("entryBlockStateId", Block.BLOCK_STATE_REGISTRY.getId(entry.blockState));
            entryTag.putFloat("entryChance", entry.probability);
            tag.add(entryTag);
        }));
        compoundTag.put("entries", entriesTag);
        return compoundTag;
    }

    public BlockStateRandomizer(CompoundTag compoundTag) {
        this.defaultBlockState = Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("defaultBlockStateId"));
        this.entries = new ArrayList();
        ListTag entriesTag = compoundTag.getList("entries", 10);
        entriesTag.forEach(entryTag -> {
            CompoundTag entryCompoundTag = (CompoundTag) entryTag;
            BlockState blockState = Block.BLOCK_STATE_REGISTRY.byId(entryCompoundTag.getInt("entryBlockStateId"));
            float chance = entryCompoundTag.getFloat("entryChance");
            this.addBlock(blockState, chance);
        });
    }

    public BlockStateRandomizer(Map<BlockState, Float> entries, BlockState defaultBlockState) {
        this.entries = new ArrayList();
        entries.forEach(this::addBlock);
        this.defaultBlockState = defaultBlockState;
    }

    public BlockStateRandomizer(List<BlockStateRandomizer.Entry> entries, BlockState defaultBlockState) {
        this.entries = entries;
        this.defaultBlockState = defaultBlockState;
    }

    public BlockStateRandomizer(BlockState defaultBlockState) {
        this.defaultBlockState = defaultBlockState;
    }

    public BlockStateRandomizer() {
    }

    public static BlockStateRandomizer from(BlockState... blockStates) {
        BlockStateRandomizer randomizer = new BlockStateRandomizer();
        float chance = 1.0F / (float) blockStates.length;
        for (BlockState state : blockStates) {
            randomizer.addBlock(state, chance);
        }
        return randomizer;
    }

    public BlockStateRandomizer addBlock(BlockState blockState, float chance) {
        if (this.entries.stream().anyMatch(entry -> entry.blockState.equals(blockState))) {
            YungsApiCommon.LOGGER.warn("WARNING: duplicate block {} added to BlockStateRandomizer!", blockState.toString());
            return this;
        } else {
            float currTotal = (Float) this.entries.stream().map(entry -> entry.probability).reduce(Float::sum).orElse(0.0F);
            float newTotal = currTotal + chance;
            if (newTotal > 1.0F) {
                YungsApiCommon.LOGGER.warn("WARNING: block {} added to BlockStateRandomizer exceeds max probabiltiy of 1!", blockState.toString());
                return this;
            } else {
                this.entries.add(new BlockStateRandomizer.Entry(blockState, chance));
                return this;
            }
        }
    }

    public BlockState get(Random random) {
        float target = random.nextFloat();
        float currBottom = 0.0F;
        for (BlockStateRandomizer.Entry entry : this.entries) {
            if (currBottom <= target && target < currBottom + entry.probability) {
                return entry.blockState;
            }
            currBottom += entry.probability;
        }
        return this.defaultBlockState;
    }

    public BlockState get(RandomSource randomSource) {
        float target = randomSource.nextFloat();
        float currBottom = 0.0F;
        for (BlockStateRandomizer.Entry entry : this.entries) {
            if (currBottom <= target && target < currBottom + entry.probability) {
                return entry.blockState;
            }
            currBottom += entry.probability;
        }
        return this.defaultBlockState;
    }

    public BlockState get(RandomSource randomSource, StructureContext ctx) {
        float target = randomSource.nextFloat();
        float currBottom = 0.0F;
        for (BlockStateRandomizer.Entry entry : this.entries) {
            if (currBottom <= target && target < currBottom + entry.probability && entry.passesCondition(ctx)) {
                return entry.blockState;
            }
            currBottom += entry.probability;
        }
        return this.defaultBlockState;
    }

    public void setDefaultBlockState(BlockState blockState) {
        this.defaultBlockState = blockState;
    }

    public Map<BlockState, Float> getEntriesAsMap() {
        Map<BlockState, Float> map = new HashMap();
        this.entries.forEach(entry -> map.put(entry.blockState, entry.probability));
        return map;
    }

    public List<BlockStateRandomizer.Entry> getEntries() {
        return this.entries;
    }

    public BlockState getDefaultBlockState() {
        return this.defaultBlockState;
    }

    public static class Entry {

        public static Codec<BlockStateRandomizer.Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockState.CODEC.fieldOf("blockState").forGetter(entry -> entry.blockState), Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(entry -> entry.probability), StructureConditionType.CONDITION_CODEC.optionalFieldOf("condition").forGetter(entry -> entry.condition)).apply(instance, BlockStateRandomizer.Entry::new));

        public BlockState blockState;

        public float probability;

        public Optional<StructureCondition> condition;

        public Entry(BlockState blockState, float probability) {
            this.blockState = blockState;
            this.probability = probability;
        }

        public Entry(BlockState blockState, float probability, Optional<StructureCondition> condition) {
            this.blockState = blockState;
            this.probability = probability;
            this.condition = condition;
        }

        public boolean passesCondition(StructureContext ctx) {
            return this.condition.isEmpty() || ((StructureCondition) this.condition.get()).passes(ctx);
        }

        public boolean equals(Object obj) {
            if (obj instanceof BlockStateRandomizer.Entry) {
                return this.blockState.equals(((BlockStateRandomizer.Entry) obj).blockState);
            } else {
                return obj instanceof BlockState ? this.blockState.equals(obj) : false;
            }
        }
    }
}