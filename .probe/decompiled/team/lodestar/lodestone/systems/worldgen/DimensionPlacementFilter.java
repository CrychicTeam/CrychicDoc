package team.lodestar.lodestone.systems.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import team.lodestar.lodestone.registry.common.LodestonePlacementFillerRegistry;

public class DimensionPlacementFilter extends PlacementFilter {

    public static final Codec<DimensionPlacementFilter> CODEC = RecordCodecBuilder.create(codec -> codec.group(ResourceLocation.CODEC.listOf().fieldOf("dimensions").forGetter(o -> (List) o.dimensions.stream().map(ResourceKey::m_135782_).collect(Collectors.toList()))).apply(codec, r -> new DimensionPlacementFilter((Set<ResourceKey<Level>>) r.stream().map(o -> ResourceKey.create(Registries.DIMENSION, o)).collect(Collectors.toSet()))));

    private final Set<ResourceKey<Level>> dimensions;

    protected DimensionPlacementFilter(Set<ResourceKey<Level>> dimensions) {
        this.dimensions = dimensions;
    }

    public static DimensionPlacementFilter of(Set<ResourceKey<Level>> dimensions) {
        return new DimensionPlacementFilter(dimensions);
    }

    @Override
    public PlacementModifierType<?> type() {
        return LodestonePlacementFillerRegistry.DIMENSION;
    }

    public static Set<ResourceKey<Level>> fromStrings(List<? extends String> dimensions) {
        return (Set<ResourceKey<Level>>) dimensions.stream().map(o -> ResourceKey.create(Registries.DIMENSION, new ResourceLocation(o))).collect(Collectors.toSet());
    }

    @Override
    protected boolean shouldPlace(PlacementContext pContext, RandomSource pRandom, BlockPos pPos) {
        ServerLevel level = pContext.getLevel().m_6018_();
        return this.dimensions.contains(level.m_46472_());
    }
}