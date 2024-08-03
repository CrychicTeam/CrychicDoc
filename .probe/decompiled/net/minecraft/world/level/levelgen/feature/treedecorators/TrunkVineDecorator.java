package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;

public class TrunkVineDecorator extends TreeDecorator {

    public static final Codec<TrunkVineDecorator> CODEC = Codec.unit(() -> TrunkVineDecorator.INSTANCE);

    public static final TrunkVineDecorator INSTANCE = new TrunkVineDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.TRUNK_VINE;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        RandomSource $$1 = treeDecoratorContext0.random();
        treeDecoratorContext0.logs().forEach(p_226075_ -> {
            if ($$1.nextInt(3) > 0) {
                BlockPos $$3 = p_226075_.west();
                if (treeDecoratorContext0.isAir($$3)) {
                    treeDecoratorContext0.placeVine($$3, VineBlock.EAST);
                }
            }
            if ($$1.nextInt(3) > 0) {
                BlockPos $$4 = p_226075_.east();
                if (treeDecoratorContext0.isAir($$4)) {
                    treeDecoratorContext0.placeVine($$4, VineBlock.WEST);
                }
            }
            if ($$1.nextInt(3) > 0) {
                BlockPos $$5 = p_226075_.north();
                if (treeDecoratorContext0.isAir($$5)) {
                    treeDecoratorContext0.placeVine($$5, VineBlock.SOUTH);
                }
            }
            if ($$1.nextInt(3) > 0) {
                BlockPos $$6 = p_226075_.south();
                if (treeDecoratorContext0.isAir($$6)) {
                    treeDecoratorContext0.placeVine($$6, VineBlock.NORTH);
                }
            }
        });
    }
}