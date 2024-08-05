package com.github.alexmodguy.alexscaves.server.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AbyssmarineWallBlock extends WallBlock implements ActivatedByAltar {

    private final Map<BlockState, VoxelShape> shapeByIndex;

    private final Map<BlockState, VoxelShape> collisionShapeByIndex;

    public AbyssmarineWallBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_57949_, true)).m_61124_(f_57951_, WallSide.NONE)).m_61124_(f_57950_, WallSide.NONE)).m_61124_(f_57952_, WallSide.NONE)).m_61124_(f_57953_, WallSide.NONE)).m_61124_(f_57954_, false)).m_61124_(ACTIVE, false)).m_61124_(DISTANCE, 15));
        this.shapeByIndex = this.makeAbyssalShapes(4.0F, 3.0F, 16.0F, 0.0F, 14.0F, 16.0F);
        this.collisionShapeByIndex = this.makeAbyssalShapes(4.0F, 3.0F, 24.0F, 0.0F, 24.0F, 24.0F);
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        super.m_213897_(state, serverLevel, pos, randomSource);
        serverLevel.m_7731_(pos, this.updateDistance(state, serverLevel, pos), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState newState = super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
        int i = ActivatedByAltar.getDistanceAt(state1) + 1;
        if (i != 1 || (Integer) newState.m_61143_(DISTANCE) != i) {
            levelAccessor.scheduleTick(blockPos, this, 2);
        }
        return newState;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.updateDistance(super.getStateForPlacement(context), context.m_43725_(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, ACTIVE, f_57949_, f_57951_, f_57950_, f_57953_, f_57952_, f_57954_);
    }

    private Map<BlockState, VoxelShape> makeAbyssalShapes(float float0, float float1, float float2, float float3, float float4, float float5) {
        float f = 8.0F - float0;
        float f1 = 8.0F + float0;
        float f2 = 8.0F - float1;
        float f3 = 8.0F + float1;
        VoxelShape voxelshape = Block.box((double) f, 0.0, (double) f, (double) f1, (double) float2, (double) f1);
        VoxelShape voxelshape1 = Block.box((double) f2, (double) float3, 0.0, (double) f3, (double) float4, (double) f3);
        VoxelShape voxelshape2 = Block.box((double) f2, (double) float3, (double) f2, (double) f3, (double) float4, 16.0);
        VoxelShape voxelshape3 = Block.box(0.0, (double) float3, (double) f2, (double) f3, (double) float4, (double) f3);
        VoxelShape voxelshape4 = Block.box((double) f2, (double) float3, (double) f2, 16.0, (double) float4, (double) f3);
        VoxelShape voxelshape5 = Block.box((double) f2, (double) float3, 0.0, (double) f3, (double) float5, (double) f3);
        VoxelShape voxelshape6 = Block.box((double) f2, (double) float3, (double) f2, (double) f3, (double) float5, 16.0);
        VoxelShape voxelshape7 = Block.box(0.0, (double) float3, (double) f2, (double) f3, (double) float5, (double) f3);
        VoxelShape voxelshape8 = Block.box((double) f2, (double) float3, (double) f2, 16.0, (double) float5, (double) f3);
        Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();
        for (Boolean obool : f_57949_.getPossibleValues()) {
            for (WallSide wallside : f_57950_.getPossibleValues()) {
                for (WallSide wallside1 : f_57951_.getPossibleValues()) {
                    for (WallSide wallside2 : f_57953_.getPossibleValues()) {
                        for (WallSide wallside3 : f_57952_.getPossibleValues()) {
                            VoxelShape voxelshape9 = Shapes.empty();
                            voxelshape9 = applyWallShape(voxelshape9, wallside, voxelshape4, voxelshape8);
                            voxelshape9 = applyWallShape(voxelshape9, wallside2, voxelshape3, voxelshape7);
                            voxelshape9 = applyWallShape(voxelshape9, wallside1, voxelshape1, voxelshape5);
                            voxelshape9 = applyWallShape(voxelshape9, wallside3, voxelshape2, voxelshape6);
                            if (obool) {
                                voxelshape9 = Shapes.or(voxelshape9, voxelshape);
                            }
                            BlockState blockstate = (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_57949_, obool)).m_61124_(f_57950_, wallside)).m_61124_(f_57953_, wallside2)).m_61124_(f_57951_, wallside1)).m_61124_(f_57952_, wallside3);
                            for (int i = 1; i <= 15; i++) {
                                builder.put((BlockState) ((BlockState) ((BlockState) blockstate.m_61124_(DISTANCE, i)).m_61124_(ACTIVE, false)).m_61124_(f_57954_, false), voxelshape9);
                                builder.put((BlockState) ((BlockState) ((BlockState) blockstate.m_61124_(DISTANCE, i)).m_61124_(ACTIVE, false)).m_61124_(f_57954_, true), voxelshape9);
                                builder.put((BlockState) ((BlockState) ((BlockState) blockstate.m_61124_(DISTANCE, i)).m_61124_(ACTIVE, true)).m_61124_(f_57954_, false), voxelshape9);
                                builder.put((BlockState) ((BlockState) ((BlockState) blockstate.m_61124_(DISTANCE, i)).m_61124_(ACTIVE, true)).m_61124_(f_57954_, true), voxelshape9);
                            }
                        }
                    }
                }
            }
        }
        return builder.build();
    }

    private static VoxelShape applyWallShape(VoxelShape voxelShape0, WallSide wallSide1, VoxelShape voxelShape2, VoxelShape voxelShape3) {
        if (wallSide1 == WallSide.TALL) {
            return Shapes.or(voxelShape0, voxelShape3);
        } else {
            return wallSide1 == WallSide.LOW ? Shapes.or(voxelShape0, voxelShape2) : voxelShape0;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) this.shapeByIndex.get(blockState0);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) this.collisionShapeByIndex.get(blockState0);
    }
}