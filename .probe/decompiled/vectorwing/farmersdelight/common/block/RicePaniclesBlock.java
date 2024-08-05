package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

public class RicePaniclesBlock extends CropBlock {

    public static final IntegerProperty RICE_AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0), Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0), Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0) };

    public RicePaniclesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return RICE_AGE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.getAgeProperty())];
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.RICE.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RICE_AGE);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 3;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60713_(ModBlocks.RICE_CROP.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return (level.m_45524_(pos, 0) >= 8 || level.m_45527_(pos)) && this.mayPlaceOn(level.m_8055_(pos.below()), level, pos);
    }
}