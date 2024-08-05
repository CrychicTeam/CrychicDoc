package com.simibubi.create.content.redstone.diodes;

import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ToggleLatchBlock extends AbstractDiodeBlock {

    public static BooleanProperty POWERING = BooleanProperty.create("powering");

    public ToggleLatchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERING, false)).m_61124_(f_52496_, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52496_, POWERING, f_54117_);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.m_61143_(f_54117_) == side ? this.getOutputSignal(blockAccess, pos, blockState) : 0;
    }

    @Override
    protected int getDelay(BlockState state) {
        return 1;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.PASS;
        } else if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else {
            return AllItems.WRENCH.isIn(player.m_21120_(handIn)) ? InteractionResult.PASS : this.activated(worldIn, pos, state);
        }
    }

    @Override
    protected int getOutputSignal(BlockGetter worldIn, BlockPos pos, BlockState state) {
        return state.m_61143_(POWERING) ? 15 : 0;
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        boolean poweredPreviously = (Boolean) state.m_61143_(f_52496_);
        super.m_213897_(state, worldIn, pos, random);
        BlockState newState = worldIn.m_8055_(pos);
        if ((Boolean) newState.m_61143_(f_52496_) && !poweredPreviously) {
            worldIn.m_7731_(pos, (BlockState) newState.m_61122_(POWERING), 2);
        }
    }

    protected InteractionResult activated(Level worldIn, BlockPos pos, BlockState state) {
        if (!worldIn.isClientSide) {
            float f = !state.m_61143_(POWERING) ? 0.6F : 0.5F;
            worldIn.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
            worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERING), 2);
        }
        return InteractionResult.SUCCESS;
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return side == null ? false : side.getAxis() == ((Direction) state.m_61143_(f_54117_)).getAxis();
    }
}