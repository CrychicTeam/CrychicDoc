package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;

public class HeightmapPlacement extends PlacementModifier {

    public static final Codec<HeightmapPlacement> CODEC = RecordCodecBuilder.create(p_191701_ -> p_191701_.group(Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(p_191705_ -> p_191705_.heightmap)).apply(p_191701_, HeightmapPlacement::new));

    private final Heightmap.Types heightmap;

    private HeightmapPlacement(Heightmap.Types heightmapTypes0) {
        this.heightmap = heightmapTypes0;
    }

    public static HeightmapPlacement onHeightmap(Heightmap.Types heightmapTypes0) {
        return new HeightmapPlacement(heightmapTypes0);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        int $$3 = blockPos2.m_123341_();
        int $$4 = blockPos2.m_123343_();
        int $$5 = placementContext0.getHeight(this.heightmap, $$3, $$4);
        return $$5 > placementContext0.getMinBuildHeight() ? Stream.of(new BlockPos($$3, $$5, $$4)) : Stream.of();
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.HEIGHTMAP;
    }
}