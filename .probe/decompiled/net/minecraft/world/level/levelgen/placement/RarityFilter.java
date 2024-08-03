package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

public class RarityFilter extends PlacementFilter {

    public static final Codec<RarityFilter> CODEC = ExtraCodecs.POSITIVE_INT.fieldOf("chance").xmap(RarityFilter::new, p_191907_ -> p_191907_.chance).codec();

    private final int chance;

    private RarityFilter(int int0) {
        this.chance = int0;
    }

    public static RarityFilter onAverageOnceEvery(int int0) {
        return new RarityFilter(int0);
    }

    @Override
    protected boolean shouldPlace(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        return randomSource1.nextFloat() < 1.0F / (float) this.chance;
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.RARITY_FILTER;
    }
}