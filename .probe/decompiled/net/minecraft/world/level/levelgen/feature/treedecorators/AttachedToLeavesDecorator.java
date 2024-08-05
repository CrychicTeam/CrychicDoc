package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class AttachedToLeavesDecorator extends TreeDecorator {

    public static final Codec<AttachedToLeavesDecorator> CODEC = RecordCodecBuilder.create(p_225996_ -> p_225996_.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(p_226014_ -> p_226014_.probability), Codec.intRange(0, 16).fieldOf("exclusion_radius_xz").forGetter(p_226012_ -> p_226012_.exclusionRadiusXZ), Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter(p_226010_ -> p_226010_.exclusionRadiusY), BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(p_226008_ -> p_226008_.blockProvider), Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter(p_226006_ -> p_226006_.requiredEmptyBlocks), ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(p_225998_ -> p_225998_.directions)).apply(p_225996_, AttachedToLeavesDecorator::new));

    protected final float probability;

    protected final int exclusionRadiusXZ;

    protected final int exclusionRadiusY;

    protected final BlockStateProvider blockProvider;

    protected final int requiredEmptyBlocks;

    protected final List<Direction> directions;

    public AttachedToLeavesDecorator(float float0, int int1, int int2, BlockStateProvider blockStateProvider3, int int4, List<Direction> listDirection5) {
        this.probability = float0;
        this.exclusionRadiusXZ = int1;
        this.exclusionRadiusY = int2;
        this.blockProvider = blockStateProvider3;
        this.requiredEmptyBlocks = int4;
        this.directions = listDirection5;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        Set<BlockPos> $$1 = new HashSet();
        RandomSource $$2 = treeDecoratorContext0.random();
        for (BlockPos $$3 : Util.shuffledCopy(treeDecoratorContext0.leaves(), $$2)) {
            Direction $$4 = Util.getRandom(this.directions, $$2);
            BlockPos $$5 = $$3.relative($$4);
            if (!$$1.contains($$5) && $$2.nextFloat() < this.probability && this.hasRequiredEmptyBlocks(treeDecoratorContext0, $$3, $$4)) {
                BlockPos $$6 = $$5.offset(-this.exclusionRadiusXZ, -this.exclusionRadiusY, -this.exclusionRadiusXZ);
                BlockPos $$7 = $$5.offset(this.exclusionRadiusXZ, this.exclusionRadiusY, this.exclusionRadiusXZ);
                for (BlockPos $$8 : BlockPos.betweenClosed($$6, $$7)) {
                    $$1.add($$8.immutable());
                }
                treeDecoratorContext0.setBlock($$5, this.blockProvider.getState($$2, $$5));
            }
        }
    }

    private boolean hasRequiredEmptyBlocks(TreeDecorator.Context treeDecoratorContext0, BlockPos blockPos1, Direction direction2) {
        for (int $$3 = 1; $$3 <= this.requiredEmptyBlocks; $$3++) {
            BlockPos $$4 = blockPos1.relative(direction2, $$3);
            if (!treeDecoratorContext0.isAir($$4)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.ATTACHED_TO_LEAVES;
    }
}