package com.mna.blocks.utility;

import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.blocks.BlockInit;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.OffsetBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public class FillerBlock extends WaterloggableBlock implements IDontCreateBlockItem, EntityBlock {

    public FillerBlock() {
        super(BlockBehaviour.Properties.of().pushReaction(PushReaction.BLOCK).noOcclusion().strength(1.5F), false);
    }

    public void onBlockExploded(BlockState state, Level world, BlockPos pos, Explosion explosion) {
        BlockPos offsetPos = this.getOffsetPos(world, pos);
        if (!world.m_46859_(offsetPos) && !offsetPos.equals(pos)) {
            world.removeBlock(offsetPos, true);
        }
        super.onBlockExploded(state, world, pos, explosion);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockPos offsetPos = this.getOffsetPos(world, pos);
        if (!world.m_46859_(offsetPos) && !offsetPos.equals(pos) && !newState.m_60734_().equals(Blocks.BARRIER)) {
            world.m_46961_(offsetPos, true);
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
        BlockPos offsetPos = this.getOffsetPos(world, pos);
        if (!world.m_46859_(offsetPos) && !offsetPos.equals(pos)) {
            BlockHitResult brtr = new BlockHitResult(rayTrace.m_82450_(), rayTrace.getDirection(), offsetPos, rayTrace.isInside());
            return world.getBlockState(offsetPos).m_60664_(world, player, hand, brtr);
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean skipRendering(BlockState p_200122_1_, BlockState p_200122_2_, Direction p_200122_3_) {
        return true;
    }

    private BlockPos getOffsetPos(BlockGetter world, BlockPos origin) {
        OffsetBlockTile tile = (OffsetBlockTile) world.getBlockEntity(origin);
        if (tile == null) {
            return origin;
        } else {
            BlockPos offset = tile.getOffset();
            return origin.offset(offset);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OffsetBlockTile(pos, state);
    }

    public static void setAtOffsetFrom(LevelAccessor world, BlockPos pos, BlockPos offset) {
        BlockPos offsetPos = pos.offset(offset);
        world.m_7731_(offsetPos, BlockInit.EMPTY_FILLER_BLOCK.get().m_49966_(), 3);
        ((OffsetBlockTile) world.m_7702_(offsetPos)).setOffset(new BlockPos(offset.m_123341_() * -1, offset.m_123342_() * -1, offset.m_123343_() * -1));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        BlockPos offsetPos = this.getOffsetPos(world, pos);
        BlockState offsetState = world.getBlockState(offsetPos);
        world.m_5898_(player, 2001, pos, m_49956_(offsetState));
    }
}