package com.rekindled.embers.block;

import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.util.Misc;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public abstract class ChamberBlockBase extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public SoundType topSound;

    public static final EnumProperty<ChamberBlockBase.ChamberConnection> CONNECTION = EnumProperty.create("connection", ChamberBlockBase.ChamberConnection.class);

    protected static final VoxelShape TOP_AABB = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 4.0, 13.0), Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0), Block.box(5.0, 6.0, 5.0, 11.0, 11.0, 11.0));

    public ChamberBlockBase(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(CONNECTION, ChamberBlockBase.ChamberConnection.BOTTOM)).m_61124_(BlockStateProperties.WATERLOGGED, false));
        this.topSound = topSound;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            if (state.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM) {
                BlockState above = level.getBlockState(pos.above());
                if (above.m_60734_() == this && above.m_61143_(CONNECTION) != ChamberBlockBase.ChamberConnection.BOTTOM) {
                    level.m_46961_(pos.above(), false);
                }
            } else {
                BlockState below = level.getBlockState(pos.below());
                if (below.m_60734_() == this && below.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM) {
                    level.m_46961_(pos.below(), false);
                }
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler handler = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
                if (handler != null) {
                    Misc.spawnInventoryInWorld(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, handler);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return state.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM ? super.m_49962_(state) : this.topSound;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return context.m_43725_().getBlockState(context.getClickedPos().above()).m_60629_(context) ? (BlockState) super.m_5573_(context).m_61124_(BlockStateProperties.WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER) : null;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM) {
            BlockState topState = (BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER);
            ChamberBlockBase.ChamberConnection connection = ChamberBlockBase.ChamberConnection.TOP;
            for (Direction direction : Misc.horizontals) {
                if (level.getBlockState(pos.above().relative(direction)).m_204336_(EmbersBlockTags.CHAMBER_CONNECTION)) {
                    connection = ChamberBlockBase.ChamberConnection.getConnection(direction);
                    break;
                }
            }
            level.setBlock(pos.above(), (BlockState) topState.m_61124_(CONNECTION, connection), 3);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        if (state.m_61143_(CONNECTION) != ChamberBlockBase.ChamberConnection.BOTTOM && facing.getAxis() != Direction.Axis.Y) {
            if (((ChamberBlockBase.ChamberConnection) state.m_61143_(CONNECTION)).direction == facing && !facingState.m_204336_(EmbersBlockTags.CHAMBER_CONNECTION)) {
                return (BlockState) state.m_61124_(CONNECTION, ChamberBlockBase.ChamberConnection.TOP);
            }
            if (facingState.m_204336_(EmbersBlockTags.CHAMBER_CONNECTION)) {
                return (BlockState) state.m_61124_(CONNECTION, ChamberBlockBase.ChamberConnection.getConnection(facing));
            }
        }
        return super.m_7417_(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED).add(CONNECTION);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }

    public static enum ChamberConnection implements StringRepresentable {

        BOTTOM("bottom", Direction.DOWN),
        TOP("top_none", Direction.UP),
        NORTH("top_north", Direction.NORTH),
        SOUTH("top_south", Direction.SOUTH),
        WEST("top_west", Direction.WEST),
        EAST("top_east", Direction.EAST);

        private final String name;

        public Direction direction;

        private ChamberConnection(String name, Direction direction) {
            this.name = name;
            this.direction = direction;
        }

        public static ChamberBlockBase.ChamberConnection getConnection(Direction direction) {
            return values()[direction.get3DDataValue()];
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}