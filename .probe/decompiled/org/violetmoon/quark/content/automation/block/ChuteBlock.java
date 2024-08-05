package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.be.ChuteBlockEntity;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class ChuteBlock extends ZetaBlock implements EntityBlock {

    private static final VoxelShape INPUT_SHAPE = Block.box(0.0, 10.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape MIDDLE_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 10.0, 12.0);

    private static final VoxelShape CHUTE_TOP_SHAPE = Shapes.or(MIDDLE_SHAPE, INPUT_SHAPE);

    private static final VoxelShape DOWN_SHAPE = Shapes.or(CHUTE_TOP_SHAPE, Block.box(6.0, 0.0, 6.0, 10.0, 4.0, 10.0));

    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public ChuteBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(ENABLED, true));
        if (module != null) {
            module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT_MIPPED);
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
        }
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 20;
    }

    @Override
    public int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 5;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        boolean flag = !worldIn.m_276867_(pos);
        if (flag != (Boolean) state.m_61143_(ENABLED)) {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(ENABLED, flag), 6);
        }
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return DOWN_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ChuteBlockEntity(pos, state);
    }
}