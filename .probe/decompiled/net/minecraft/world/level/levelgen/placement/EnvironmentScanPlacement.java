package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;

public class EnvironmentScanPlacement extends PlacementModifier {

    private final Direction directionOfSearch;

    private final BlockPredicate targetCondition;

    private final BlockPredicate allowedSearchCondition;

    private final int maxSteps;

    public static final Codec<EnvironmentScanPlacement> CODEC = RecordCodecBuilder.create(p_191650_ -> p_191650_.group(Direction.VERTICAL_CODEC.fieldOf("direction_of_search").forGetter(p_191672_ -> p_191672_.directionOfSearch), BlockPredicate.CODEC.fieldOf("target_condition").forGetter(p_191670_ -> p_191670_.targetCondition), BlockPredicate.CODEC.optionalFieldOf("allowed_search_condition", BlockPredicate.alwaysTrue()).forGetter(p_191668_ -> p_191668_.allowedSearchCondition), Codec.intRange(1, 32).fieldOf("max_steps").forGetter(p_191652_ -> p_191652_.maxSteps)).apply(p_191650_, EnvironmentScanPlacement::new));

    private EnvironmentScanPlacement(Direction direction0, BlockPredicate blockPredicate1, BlockPredicate blockPredicate2, int int3) {
        this.directionOfSearch = direction0;
        this.targetCondition = blockPredicate1;
        this.allowedSearchCondition = blockPredicate2;
        this.maxSteps = int3;
    }

    public static EnvironmentScanPlacement scanningFor(Direction direction0, BlockPredicate blockPredicate1, BlockPredicate blockPredicate2, int int3) {
        return new EnvironmentScanPlacement(direction0, blockPredicate1, blockPredicate2, int3);
    }

    public static EnvironmentScanPlacement scanningFor(Direction direction0, BlockPredicate blockPredicate1, int int2) {
        return scanningFor(direction0, blockPredicate1, BlockPredicate.alwaysTrue(), int2);
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        BlockPos.MutableBlockPos $$3 = blockPos2.mutable();
        WorldGenLevel $$4 = placementContext0.getLevel();
        if (!this.allowedSearchCondition.test($$4, $$3)) {
            return Stream.of();
        } else {
            for (int $$5 = 0; $$5 < this.maxSteps; $$5++) {
                if (this.targetCondition.test($$4, $$3)) {
                    return Stream.of($$3);
                }
                $$3.move(this.directionOfSearch);
                if ($$4.m_151562_($$3.m_123342_())) {
                    return Stream.of();
                }
                if (!this.allowedSearchCondition.test($$4, $$3)) {
                    break;
                }
            }
            return this.targetCondition.test($$4, $$3) ? Stream.of($$3) : Stream.of();
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.ENVIRONMENT_SCAN;
    }
}