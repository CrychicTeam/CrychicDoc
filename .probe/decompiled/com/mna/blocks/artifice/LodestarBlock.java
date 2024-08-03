package com.mna.blocks.artifice;

import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.LodestarTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class LodestarBlock extends WaterloggableBlock implements EntityBlock {

    public static final IntegerProperty SURFACE_TYPE = IntegerProperty.create("surface", 1, 3);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty LOW_TIER = BooleanProperty.create("low_tier");

    private final boolean low_tier;

    public LodestarBlock(boolean low_tier) {
        super(BlockBehaviour.Properties.of().noOcclusion().strength(5.0F, 1000.0F), false);
        this.low_tier = low_tier;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SURFACE_TYPE, FACING, LOW_TIER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if ((Boolean) state.m_61143_(LOW_TIER)) {
            Direction facing = (Direction) state.m_61143_(FACING);
            return facing != Direction.EAST && facing != Direction.WEST ? Block.box(1.0, 0.0, 2.0, 15.0, 14.0, 14.0) : Block.box(2.0, 0.0, 1.0, 14.0, 14.0, 15.0);
        } else {
            return Block.box(2.0, 0.0, 2.0, 14.0, 18.0, 14.0);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (this.low_tier) {
            return (BlockState) ((BlockState) ((BlockState) super.getStateForPlacement(context).m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(SURFACE_TYPE, 3)).m_61124_(LOW_TIER, this.low_tier);
        } else {
            int surfaceType;
            Direction face;
            if (context.m_43719_() == Direction.UP) {
                surfaceType = 1;
                face = context.m_8125_().getOpposite();
            } else if (context.m_43719_() == Direction.DOWN) {
                surfaceType = 2;
                face = context.m_8125_().getOpposite();
            } else {
                surfaceType = 3;
                face = context.m_43719_();
            }
            return (BlockState) ((BlockState) ((BlockState) super.getStateForPlacement(context).m_61124_(FACING, face)).m_61124_(SURFACE_TYPE, surfaceType)).m_61124_(LOW_TIER, this.low_tier);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            LodestarTile lodestar = (LodestarTile) worldIn.getBlockEntity(pos);
            if (lodestar != null) {
                NetworkHooks.openScreen((ServerPlayer) player, lodestar, lodestar);
            }
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.m_61143_(LOW_TIER) ? 0 : 10;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return this.low_tier ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LodestarTile(pos, state);
    }
}