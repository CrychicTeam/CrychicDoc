package com.mna.blocks.artifice;

import com.mna.blocks.tileentities.WellspringPillarTile;
import com.mna.blocks.utility.BlockWithOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WellspringPillarBlock extends BlockWithOffset implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public WellspringPillarBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().strength(3.0F), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0), new BlockPos(0, 3, 0), new BlockPos(0, 4, 0), new BlockPos(0, 5, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.m_8125_().getOpposite();
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : (BlockState) state.m_61124_(FACING, face);
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return 15;
    }

    @Override
    public int getLightBlock(BlockState p_200011_1_, BlockGetter p_200011_2_, BlockPos p_200011_3_) {
        return 0;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
        return false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WellspringPillarTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_196247_1_, BlockGetter p_196247_2_, BlockPos p_196247_3_) {
        return Shapes.empty();
    }
}