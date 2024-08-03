package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;

public class CocoaDecorator extends TreeDecorator {

    public static final Codec<CocoaDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(CocoaDecorator::new, p_69989_ -> p_69989_.probability).codec();

    private final float probability;

    public CocoaDecorator(float float0) {
        this.probability = float0;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.COCOA;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        RandomSource $$1 = treeDecoratorContext0.random();
        if (!($$1.nextFloat() >= this.probability)) {
            List<BlockPos> $$2 = treeDecoratorContext0.logs();
            int $$3 = ((BlockPos) $$2.get(0)).m_123342_();
            $$2.stream().filter(p_69980_ -> p_69980_.m_123342_() - $$3 <= 2).forEach(p_226026_ -> {
                for (Direction $$3x : Direction.Plane.HORIZONTAL) {
                    if ($$1.nextFloat() <= 0.25F) {
                        Direction $$4 = $$3x.getOpposite();
                        BlockPos $$5 = p_226026_.offset($$4.getStepX(), 0, $$4.getStepZ());
                        if (treeDecoratorContext0.isAir($$5)) {
                            treeDecoratorContext0.setBlock($$5, (BlockState) ((BlockState) Blocks.COCOA.defaultBlockState().m_61124_(CocoaBlock.AGE, $$1.nextInt(3))).m_61124_(CocoaBlock.f_54117_, $$3x));
                        }
                    }
                }
            });
        }
    }
}