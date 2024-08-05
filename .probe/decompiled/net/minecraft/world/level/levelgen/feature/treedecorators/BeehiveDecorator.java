package net.minecraft.world.level.levelgen.feature.treedecorators;

import com.mojang.serialization.Codec;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BeehiveDecorator extends TreeDecorator {

    public static final Codec<BeehiveDecorator> CODEC = Codec.floatRange(0.0F, 1.0F).fieldOf("probability").xmap(BeehiveDecorator::new, p_69971_ -> p_69971_.probability).codec();

    private static final Direction WORLDGEN_FACING = Direction.SOUTH;

    private static final Direction[] SPAWN_DIRECTIONS = (Direction[]) Direction.Plane.HORIZONTAL.stream().filter(p_202307_ -> p_202307_ != WORLDGEN_FACING.getOpposite()).toArray(Direction[]::new);

    private final float probability;

    public BeehiveDecorator(float float0) {
        this.probability = float0;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.BEEHIVE;
    }

    @Override
    public void place(TreeDecorator.Context treeDecoratorContext0) {
        RandomSource $$1 = treeDecoratorContext0.random();
        if (!($$1.nextFloat() >= this.probability)) {
            List<BlockPos> $$2 = treeDecoratorContext0.leaves();
            List<BlockPos> $$3 = treeDecoratorContext0.logs();
            int $$4 = !$$2.isEmpty() ? Math.max(((BlockPos) $$2.get(0)).m_123342_() - 1, ((BlockPos) $$3.get(0)).m_123342_() + 1) : Math.min(((BlockPos) $$3.get(0)).m_123342_() + 1 + $$1.nextInt(3), ((BlockPos) $$3.get($$3.size() - 1)).m_123342_());
            List<BlockPos> $$5 = (List<BlockPos>) $$3.stream().filter(p_202300_ -> p_202300_.m_123342_() == $$4).flatMap(p_202305_ -> Stream.of(SPAWN_DIRECTIONS).map(p_202305_::m_121945_)).collect(Collectors.toList());
            if (!$$5.isEmpty()) {
                Collections.shuffle($$5);
                Optional<BlockPos> $$6 = $$5.stream().filter(p_226022_ -> treeDecoratorContext0.isAir(p_226022_) && treeDecoratorContext0.isAir(p_226022_.relative(WORLDGEN_FACING))).findFirst();
                if (!$$6.isEmpty()) {
                    treeDecoratorContext0.setBlock((BlockPos) $$6.get(), (BlockState) Blocks.BEE_NEST.defaultBlockState().m_61124_(BeehiveBlock.FACING, WORLDGEN_FACING));
                    treeDecoratorContext0.level().getBlockEntity((BlockPos) $$6.get(), BlockEntityType.BEEHIVE).ifPresent(p_259007_ -> {
                        int $$2x = 2 + $$1.nextInt(2);
                        for (int $$3x = 0; $$3x < $$2x; $$3x++) {
                            CompoundTag $$4x = new CompoundTag();
                            $$4x.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.BEE).toString());
                            p_259007_.storeBee($$4x, $$1.nextInt(599), false);
                        }
                    });
                }
            }
        }
    }
}