package net.minecraft.world.level.chunk;

import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class LevelChunkSection {

    public static final int SECTION_WIDTH = 16;

    public static final int SECTION_HEIGHT = 16;

    public static final int SECTION_SIZE = 4096;

    public static final int BIOME_CONTAINER_BITS = 2;

    private short nonEmptyBlockCount;

    private short tickingBlockCount;

    private short tickingFluidCount;

    private final PalettedContainer<BlockState> states;

    private PalettedContainerRO<Holder<Biome>> biomes;

    public LevelChunkSection(PalettedContainer<BlockState> palettedContainerBlockState0, PalettedContainerRO<Holder<Biome>> palettedContainerROHolderBiome1) {
        this.states = palettedContainerBlockState0;
        this.biomes = palettedContainerROHolderBiome1;
        this.recalcBlockCounts();
    }

    public LevelChunkSection(Registry<Biome> registryBiome0) {
        this.states = new PalettedContainer<>(Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), PalettedContainer.Strategy.SECTION_STATES);
        this.biomes = new PalettedContainer<>(registryBiome0.asHolderIdMap(), registryBiome0.getHolderOrThrow(Biomes.PLAINS), PalettedContainer.Strategy.SECTION_BIOMES);
    }

    public BlockState getBlockState(int int0, int int1, int int2) {
        return this.states.get(int0, int1, int2);
    }

    public FluidState getFluidState(int int0, int int1, int int2) {
        return this.states.get(int0, int1, int2).m_60819_();
    }

    public void acquire() {
        this.states.acquire();
    }

    public void release() {
        this.states.release();
    }

    public BlockState setBlockState(int int0, int int1, int int2, BlockState blockState3) {
        return this.setBlockState(int0, int1, int2, blockState3, true);
    }

    public BlockState setBlockState(int int0, int int1, int int2, BlockState blockState3, boolean boolean4) {
        BlockState $$5;
        if (boolean4) {
            $$5 = this.states.getAndSet(int0, int1, int2, blockState3);
        } else {
            $$5 = this.states.getAndSetUnchecked(int0, int1, int2, blockState3);
        }
        FluidState $$7 = $$5.m_60819_();
        FluidState $$8 = blockState3.m_60819_();
        if (!$$5.m_60795_()) {
            this.nonEmptyBlockCount--;
            if ($$5.m_60823_()) {
                this.tickingBlockCount--;
            }
        }
        if (!$$7.isEmpty()) {
            this.tickingFluidCount--;
        }
        if (!blockState3.m_60795_()) {
            this.nonEmptyBlockCount++;
            if (blockState3.m_60823_()) {
                this.tickingBlockCount++;
            }
        }
        if (!$$8.isEmpty()) {
            this.tickingFluidCount++;
        }
        return $$5;
    }

    public boolean hasOnlyAir() {
        return this.nonEmptyBlockCount == 0;
    }

    public boolean isRandomlyTicking() {
        return this.isRandomlyTickingBlocks() || this.isRandomlyTickingFluids();
    }

    public boolean isRandomlyTickingBlocks() {
        return this.tickingBlockCount > 0;
    }

    public boolean isRandomlyTickingFluids() {
        return this.tickingFluidCount > 0;
    }

    public void recalcBlockCounts() {
        class BlockCounter implements PalettedContainer.CountConsumer<BlockState> {

            public int nonEmptyBlockCount;

            public int tickingBlockCount;

            public int tickingFluidCount;

            public void accept(BlockState blockState0, int int1) {
                FluidState $$2 = blockState0.m_60819_();
                if (!blockState0.m_60795_()) {
                    this.nonEmptyBlockCount += int1;
                    if (blockState0.m_60823_()) {
                        this.tickingBlockCount += int1;
                    }
                }
                if (!$$2.isEmpty()) {
                    this.nonEmptyBlockCount += int1;
                    if ($$2.isRandomlyTicking()) {
                        this.tickingFluidCount += int1;
                    }
                }
            }
        }
        BlockCounter $$0 = new BlockCounter();
        this.states.count($$0);
        this.nonEmptyBlockCount = (short) $$0.nonEmptyBlockCount;
        this.tickingBlockCount = (short) $$0.tickingBlockCount;
        this.tickingFluidCount = (short) $$0.tickingFluidCount;
    }

    public PalettedContainer<BlockState> getStates() {
        return this.states;
    }

    public PalettedContainerRO<Holder<Biome>> getBiomes() {
        return this.biomes;
    }

    public void read(FriendlyByteBuf friendlyByteBuf0) {
        this.nonEmptyBlockCount = friendlyByteBuf0.readShort();
        this.states.read(friendlyByteBuf0);
        PalettedContainer<Holder<Biome>> $$1 = this.biomes.recreate();
        $$1.read(friendlyByteBuf0);
        this.biomes = $$1;
    }

    public void readBiomes(FriendlyByteBuf friendlyByteBuf0) {
        PalettedContainer<Holder<Biome>> $$1 = this.biomes.recreate();
        $$1.read(friendlyByteBuf0);
        this.biomes = $$1;
    }

    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeShort(this.nonEmptyBlockCount);
        this.states.write(friendlyByteBuf0);
        this.biomes.write(friendlyByteBuf0);
    }

    public int getSerializedSize() {
        return 2 + this.states.getSerializedSize() + this.biomes.getSerializedSize();
    }

    public boolean maybeHas(Predicate<BlockState> predicateBlockState0) {
        return this.states.maybeHas(predicateBlockState0);
    }

    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        return this.biomes.get(int0, int1, int2);
    }

    public void fillBiomesFromNoise(BiomeResolver biomeResolver0, Climate.Sampler climateSampler1, int int2, int int3, int int4) {
        PalettedContainer<Holder<Biome>> $$5 = this.biomes.recreate();
        int $$6 = 4;
        for (int $$7 = 0; $$7 < 4; $$7++) {
            for (int $$8 = 0; $$8 < 4; $$8++) {
                for (int $$9 = 0; $$9 < 4; $$9++) {
                    $$5.getAndSetUnchecked($$7, $$8, $$9, biomeResolver0.getNoiseBiome(int2 + $$7, int3 + $$8, int4 + $$9, climateSampler1));
                }
            }
        }
        this.biomes = $$5;
    }
}