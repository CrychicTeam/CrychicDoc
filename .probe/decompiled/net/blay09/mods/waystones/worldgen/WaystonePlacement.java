package net.blay09.mods.waystones.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class WaystonePlacement extends PlacementModifier {

    public static final Codec<WaystonePlacement> CODEC = RecordCodecBuilder.create(builder -> builder.group(Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(placement -> placement.heightmap)).apply(builder, WaystonePlacement::new));

    private final Heightmap.Types heightmap;

    public WaystonePlacement() {
        this(Heightmap.Types.OCEAN_FLOOR_WG);
    }

    public WaystonePlacement(Heightmap.Types heightmap) {
        this.heightmap = heightmap;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        if (!this.isWaystoneChunk(context, pos)) {
            return Stream.empty();
        } else if (context.getLevel().m_6018_().m_46472_() != Level.NETHER) {
            int x = pos.m_123341_();
            int z = pos.m_123343_();
            int y = context.getHeight(this.heightmap, x, z);
            return y > context.getMinBuildHeight() ? Stream.of(new BlockPos(x, y, z)) : Stream.of();
        } else {
            BlockPos.MutableBlockPos mutablePos = pos.mutable();
            int topMostY = context.getHeight(this.heightmap, pos.m_123341_(), pos.m_123343_());
            mutablePos.setY(topMostY);
            BlockState stateAbove = context.getLevel().m_8055_(mutablePos);
            for (int i = mutablePos.m_123342_(); i >= 1; i--) {
                mutablePos.setY(mutablePos.m_123342_() - 1);
                BlockState state = context.getLevel().m_8055_(mutablePos);
                if (!state.m_60795_() && state.m_60819_().isEmpty() && stateAbove.m_60795_() && !state.m_60713_(Blocks.BEDROCK)) {
                    mutablePos.setY(mutablePos.m_123342_() + 1);
                    break;
                }
                stateAbove = state;
            }
            return mutablePos.m_123342_() > 0 ? Stream.of(mutablePos) : Stream.empty();
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModWorldGen.waystonePlacement.get();
    }

    private boolean isWaystoneChunk(PlacementContext world, BlockPos pos) {
        int chunkDistance = WaystonesConfig.getActive().worldGen.frequency;
        if (chunkDistance == 0) {
            return false;
        } else {
            ResourceLocation dimension = world.getLevel().m_6018_().m_46472_().location();
            List<? extends String> dimensionAllowList = WaystonesConfig.getActive().worldGen.dimensionAllowList;
            List<? extends String> dimensionDenyList = WaystonesConfig.getActive().worldGen.dimensionDenyList;
            if (!dimensionAllowList.isEmpty() && !dimensionAllowList.contains(dimension.toString())) {
                return false;
            } else if (!dimensionDenyList.isEmpty() && dimensionDenyList.contains(dimension.toString())) {
                return false;
            } else {
                int maxDeviation = (int) Math.ceil((double) ((float) chunkDistance / 2.0F));
                int chunkX = pos.m_123341_() / 16;
                int chunkZ = pos.m_123343_() / 16;
                int devGridX = pos.m_123341_() / 16 * maxDeviation;
                int devGridZ = pos.m_123343_() / 16 * maxDeviation;
                long seed = world.getLevel().getSeed();
                Random random = new Random(seed * (long) devGridX * (long) devGridZ);
                int chunkOffsetX = random.nextInt(maxDeviation);
                int chunkOffsetZ = random.nextInt(maxDeviation);
                return (chunkX + chunkOffsetX) % chunkDistance == 0 && (chunkZ + chunkOffsetZ) % chunkDistance == 0;
            }
        }
    }
}