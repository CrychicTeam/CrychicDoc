package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public class LoomBlock extends HorizontalDirectionalBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.loom");

    protected LoomBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player3.openMenu(blockState0.m_60750_(level1, blockPos2));
            player3.awardStat(Stats.INTERACT_WITH_LOOM);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return new SimpleMenuProvider((p_54783_, p_54784_, p_54785_) -> new LoomMenu(p_54783_, p_54784_, ContainerLevelAccess.create(level1, blockPos2)), CONTAINER_TITLE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(f_54117_, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_);
    }
}