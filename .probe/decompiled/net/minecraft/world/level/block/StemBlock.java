package net.minecraft.world.level.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StemBlock extends BushBlock implements BonemealableBlock {

    public static final int MAX_AGE = 7;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    protected static final float AABB_OFFSET = 1.0F;

    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(7.0, 0.0, 7.0, 9.0, 2.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 6.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 12.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 14.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0) };

    private final StemGrownBlock fruit;

    private final Supplier<Item> seedSupplier;

    protected StemBlock(StemGrownBlock stemGrownBlock0, Supplier<Item> supplierItem1, BlockBehaviour.Properties blockBehaviourProperties2) {
        super(blockBehaviourProperties2);
        this.fruit = stemGrownBlock0;
        this.seedSupplier = supplierItem1;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_AGE[blockState0.m_61143_(AGE)];
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60713_(Blocks.FARMLAND);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_45524_(blockPos2, 0) >= 9) {
            float $$4 = CropBlock.getGrowthSpeed(this, serverLevel1, blockPos2);
            if (randomSource3.nextInt((int) (25.0F / $$4) + 1) == 0) {
                int $$5 = (Integer) blockState0.m_61143_(AGE);
                if ($$5 < 7) {
                    blockState0 = (BlockState) blockState0.m_61124_(AGE, $$5 + 1);
                    serverLevel1.m_7731_(blockPos2, blockState0, 2);
                } else {
                    Direction $$6 = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource3);
                    BlockPos $$7 = blockPos2.relative($$6);
                    BlockState $$8 = serverLevel1.m_8055_($$7.below());
                    if (serverLevel1.m_8055_($$7).m_60795_() && ($$8.m_60713_(Blocks.FARMLAND) || $$8.m_204336_(BlockTags.DIRT))) {
                        serverLevel1.m_46597_($$7, this.fruit.m_49966_());
                        serverLevel1.m_46597_(blockPos2, (BlockState) this.fruit.getAttachedStem().m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, $$6));
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack((ItemLike) this.seedSupplier.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return (Integer) blockState2.m_61143_(AGE) != 7;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        int $$4 = Math.min(7, (Integer) blockState3.m_61143_(AGE) + Mth.nextInt(serverLevel0.f_46441_, 2, 5));
        BlockState $$5 = (BlockState) blockState3.m_61124_(AGE, $$4);
        serverLevel0.m_7731_(blockPos2, $$5, 2);
        if ($$4 == 7) {
            $$5.m_222972_(serverLevel0, blockPos2, serverLevel0.f_46441_);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    public StemGrownBlock getFruit() {
        return this.fruit;
    }
}