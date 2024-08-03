package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class CaveVinesBlock extends GrowingPlantHeadBlock implements BonemealableBlock, CaveVines {

    private static final float CHANCE_OF_BERRIES_ON_GROWTH = 0.11F;

    public CaveVinesBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, Direction.DOWN, f_152948_, false, 0.1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_53924_, 0)).m_61124_(f_152949_, false));
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource randomSource0) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState blockState0) {
        return blockState0.m_60795_();
    }

    @Override
    protected Block getBodyBlock() {
        return Blocks.CAVE_VINES_PLANT;
    }

    @Override
    protected BlockState updateBodyAfterConvertedFromHead(BlockState blockState0, BlockState blockState1) {
        return (BlockState) blockState1.m_61124_(f_152949_, (Boolean) blockState0.m_61143_(f_152949_));
    }

    @Override
    protected BlockState getGrowIntoState(BlockState blockState0, RandomSource randomSource1) {
        return (BlockState) super.getGrowIntoState(blockState0, randomSource1).m_61124_(f_152949_, randomSource1.nextFloat() < 0.11F);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(Items.GLOW_BERRIES);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        return CaveVines.use(player3, blockState0, level1, blockPos2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        super.createBlockStateDefinition(stateDefinitionBuilderBlockBlockState0);
        stateDefinitionBuilderBlockBlockState0.add(f_152949_);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return !(Boolean) blockState2.m_61143_(f_152949_);
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        serverLevel0.m_7731_(blockPos2, (BlockState) blockState3.m_61124_(f_152949_, true), 2);
    }
}