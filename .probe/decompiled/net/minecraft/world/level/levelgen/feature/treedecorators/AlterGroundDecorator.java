package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class AlterGroundDecorator extends TreeDecorator {

    public static final Codec<AlterGroundDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundDecorator::new, p_69327_ -> p_69327_.provider).codec();

    private final BlockStateProvider provider;

    public AlterGroundDecorator(BlockStateProvider blockStateProvider0) {
        this.provider = blockStateProvider0;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.ALTER_GROUND;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        List<BlockPos> $$1 = Lists.newArrayList();
        List<BlockPos> $$2 = treeDecoratorContext0.roots();
        List<BlockPos> $$3 = treeDecoratorContext0.logs();
        if ($$2.isEmpty()) {
            $$1.addAll($$3);
        } else if (!$$3.isEmpty() && ((BlockPos) $$2.get(0)).m_123342_() == ((BlockPos) $$3.get(0)).m_123342_()) {
            $$1.addAll($$3);
            $$1.addAll($$2);
        } else {
            $$1.addAll($$2);
        }
        if (!$$1.isEmpty()) {
            int $$4 = ((BlockPos) $$1.get(0)).m_123342_();
            $$1.stream().filter(p_69310_ -> p_69310_.m_123342_() == $$4).forEach(p_225978_ -> {
                this.placeCircle(treeDecoratorContext0, p_225978_.west().north());
                this.placeCircle(treeDecoratorContext0, p_225978_.east(2).north());
                this.placeCircle(treeDecoratorContext0, p_225978_.west().south(2));
                this.placeCircle(treeDecoratorContext0, p_225978_.east(2).south(2));
                for (int $$2x = 0; $$2x < 5; $$2x++) {
                    int $$3x = treeDecoratorContext0.random().nextInt(64);
                    int $$4x = $$3x % 8;
                    int $$5 = $$3x / 8;
                    if ($$4x == 0 || $$4x == 7 || $$5 == 0 || $$5 == 7) {
                        this.placeCircle(treeDecoratorContext0, p_225978_.offset(-3 + $$4x, 0, -3 + $$5));
                    }
                }
            });
        }
    }

    private void placeCircle(TreeDecorator.Context treeDecoratorContext0, BlockPos blockPos1) {
        for (int $$2 = -2; $$2 <= 2; $$2++) {
            for (int $$3 = -2; $$3 <= 2; $$3++) {
                if (Math.abs($$2) != 2 || Math.abs($$3) != 2) {
                    this.placeBlockAt(treeDecoratorContext0, blockPos1.offset($$2, 0, $$3));
                }
            }
        }
    }

    private void placeBlockAt(TreeDecorator.Context treeDecoratorContext0, BlockPos blockPos1) {
        for (int $$2 = 2; $$2 >= -3; $$2--) {
            BlockPos $$3 = blockPos1.above($$2);
            if (Feature.isGrassOrDirt(treeDecoratorContext0.level(), $$3)) {
                treeDecoratorContext0.setBlock($$3, this.provider.getState(treeDecoratorContext0.random(), blockPos1));
                break;
            }
            if (!treeDecoratorContext0.isAir($$3) && $$2 < 0) {
                break;
            }
        }
    }
}