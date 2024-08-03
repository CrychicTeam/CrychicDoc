package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class LeaveVineDecorator extends TreeDecorator {

    public static final Codec<LeaveVineDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(LeaveVineDecorator::new, p_226037_ -> p_226037_.probability).codec();

    private final float probability;

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.LEAVE_VINE;
    }

    public LeaveVineDecorator(float float0) {
        this.probability = float0;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        RandomSource $$1 = treeDecoratorContext0.random();
        treeDecoratorContext0.leaves().forEach(p_226035_ -> {
            if ($$1.nextFloat() < this.probability) {
                BlockPos $$3 = p_226035_.west();
                if (treeDecoratorContext0.isAir($$3)) {
                    addHangingVine($$3, VineBlock.EAST, treeDecoratorContext0);
                }
            }
            if ($$1.nextFloat() < this.probability) {
                BlockPos $$4 = p_226035_.east();
                if (treeDecoratorContext0.isAir($$4)) {
                    addHangingVine($$4, VineBlock.WEST, treeDecoratorContext0);
                }
            }
            if ($$1.nextFloat() < this.probability) {
                BlockPos $$5 = p_226035_.north();
                if (treeDecoratorContext0.isAir($$5)) {
                    addHangingVine($$5, VineBlock.SOUTH, treeDecoratorContext0);
                }
            }
            if ($$1.nextFloat() < this.probability) {
                BlockPos $$6 = p_226035_.south();
                if (treeDecoratorContext0.isAir($$6)) {
                    addHangingVine($$6, VineBlock.NORTH, treeDecoratorContext0);
                }
            }
        });
    }

    private static void addHangingVine(BlockPos blockPos0, BooleanProperty booleanProperty1, TreeDecorator.Context treeDecoratorContext2) {
        treeDecoratorContext2.placeVine(blockPos0, booleanProperty1);
        int $$3 = 4;
        for (BlockPos var4 = blockPos0.below(); treeDecoratorContext2.isAir(var4) && $$3 > 0; $$3--) {
            treeDecoratorContext2.placeVine(var4, booleanProperty1);
            var4 = var4.below();
        }
    }
}