package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

@Deprecated
public class CountOnEveryLayerPlacement extends PlacementModifier {

    public static final Codec<CountOnEveryLayerPlacement> CODEC = IntProvider.codec(0, 256).fieldOf("count").xmap(CountOnEveryLayerPlacement::new, p_191611_ -> p_191611_.count).codec();

    private final IntProvider count;

    private CountOnEveryLayerPlacement(IntProvider intProvider0) {
        this.count = intProvider0;
    }

    public static CountOnEveryLayerPlacement of(IntProvider intProvider0) {
        return new CountOnEveryLayerPlacement(intProvider0);
    }

    public static CountOnEveryLayerPlacement of(int int0) {
        return of(ConstantInt.of(int0));
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext0, RandomSource randomSource1, BlockPos blockPos2) {
        Builder<BlockPos> $$3 = Stream.builder();
        int $$4 = 0;
        boolean $$5;
        do {
            $$5 = false;
            for (int $$6 = 0; $$6 < this.count.sample(randomSource1); $$6++) {
                int $$7 = randomSource1.nextInt(16) + blockPos2.m_123341_();
                int $$8 = randomSource1.nextInt(16) + blockPos2.m_123343_();
                int $$9 = placementContext0.getHeight(Heightmap.Types.MOTION_BLOCKING, $$7, $$8);
                int $$10 = findOnGroundYPosition(placementContext0, $$7, $$9, $$8, $$4);
                if ($$10 != Integer.MAX_VALUE) {
                    $$3.add(new BlockPos($$7, $$10, $$8));
                    $$5 = true;
                }
            }
            $$4++;
        } while ($$5);
        return $$3.build();
    }

    @Override
    public PlacementModifierType<?> type() {
        return PlacementModifierType.COUNT_ON_EVERY_LAYER;
    }

    private static int findOnGroundYPosition(PlacementContext placementContext0, int int1, int int2, int int3, int int4) {
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos(int1, int2, int3);
        int $$6 = 0;
        BlockState $$7 = placementContext0.getBlockState($$5);
        for (int $$8 = int2; $$8 >= placementContext0.getMinBuildHeight() + 1; $$8--) {
            $$5.setY($$8 - 1);
            BlockState $$9 = placementContext0.getBlockState($$5);
            if (!isEmpty($$9) && isEmpty($$7) && !$$9.m_60713_(Blocks.BEDROCK)) {
                if ($$6 == int4) {
                    return $$5.m_123342_() + 1;
                }
                $$6++;
            }
            $$7 = $$9;
        }
        return Integer.MAX_VALUE;
    }

    private static boolean isEmpty(BlockState blockState0) {
        return blockState0.m_60795_() || blockState0.m_60713_(Blocks.WATER) || blockState0.m_60713_(Blocks.LAVA);
    }
}