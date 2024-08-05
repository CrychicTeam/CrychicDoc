package com.mna.blocks.sorcery;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.api.blocks.interfaces.ITranslucentBlock;
import com.mna.blocks.tileentities.TransitoryTile;
import com.mna.blocks.tileentities.init.TileEntityInit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;

public class TransitoryTileBlock extends Block implements ITranslucentBlock, IDontCreateBlockItem, EntityBlock {

    public static final IntegerProperty DURATION = IntegerProperty.create("duration", 0, 30);

    public TransitoryTileBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).strength(1.0F).noOcclusion().isViewBlocking((state, reader, pos) -> false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DURATION);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (worldIn instanceof ServerLevel) {
            this.checkNeighbors(worldIn, pos);
        }
    }

    @Override
    public void setPlacedBy(Level p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        p_180633_1_.updateNeighborsAt(p_180633_2_, this);
    }

    private void checkNeighbors(LevelReader worldIn, BlockPos pos) {
        BlockEntity te = worldIn.m_7702_(pos);
        if (te != null && te instanceof TransitoryTile) {
            for (Direction d : Direction.values()) {
                BlockPos directionOffset = pos.offset(d.getNormal());
                BlockState directionState = worldIn.m_8055_(directionOffset);
                if (directionState.m_60734_() == this) {
                    ((TransitoryTile) te).setNeighborOnSide(d);
                    BlockEntity neighbor = worldIn.m_7702_(directionOffset);
                    if (neighbor != null && neighbor instanceof TransitoryTile) {
                        ((TransitoryTile) neighbor).setNeighborOnSide(d.getOpposite());
                    }
                }
            }
        }
    }

    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        if (!world.isClientSide()) {
            BlockEntity te = world.m_7702_(pos);
            if (te != null && te instanceof TransitoryTile) {
                BlockState neighborState = world.m_8055_(neighbor);
                if (neighborState.m_60734_() == this) {
                    ((TransitoryTile) te).setNeighborOnSide(this.directionBetweenPositions(pos, neighbor));
                } else {
                    ((TransitoryTile) te).clearNeighborOnSide(this.directionBetweenPositions(pos, neighbor));
                }
            }
        }
    }

    @Nullable
    private Direction directionBetweenPositions(BlockPos a, BlockPos b) {
        if (a.above().equals(b)) {
            return Direction.UP;
        } else if (a.below().equals(b)) {
            return Direction.DOWN;
        } else if (a.east().equals(b)) {
            return Direction.EAST;
        } else if (a.west().equals(b)) {
            return Direction.WEST;
        } else if (a.north().equals(b)) {
            return Direction.NORTH;
        } else {
            return a.south().equals(b) ? Direction.SOUTH : null;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TransitoryTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.TRANSITORY_TILE.get() ? (lvl, pos, state1, be) -> TransitoryTile.Tick(lvl, pos, state1, (TransitoryTile) be) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public void setColor(Level world, BlockPos pos, int color) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te instanceof TransitoryTile tile) {
            tile.setColor(color);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState pState, Fluid pFluid) {
        return false;
    }
}