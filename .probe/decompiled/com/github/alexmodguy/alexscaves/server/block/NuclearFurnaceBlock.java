package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.ACBlockEntityRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NuclearFurnaceBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public NuclearFurnaceBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 1001.0F).sound(ACSoundTypes.NUCLEAR_BOMB).noOcclusion());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NuclearFurnaceBlockEntity(pos, state);
    }

    @javax.annotation.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return m_152132_(entityType, ACBlockEntityRegistry.NUCLEAR_FURNACE.get(), NuclearFurnaceBlockEntity::tick);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return NuclearFurnaceComponentBlock.isCornerForFurnace(level, pos, false, true);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (!state.m_60710_(levelAccessor, blockPos)) {
            this.checkCriticalityExplosion(levelAccessor, blockPos);
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!player.m_6144_()) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            if (this.canSurvive(state, level, blockPos)) {
                if (level.getBlockEntity(blockPos) instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity) {
                    player.openMenu(nuclearFurnaceBlockEntity);
                    nuclearFurnaceBlockEntity.onPlayerUse(player);
                    player.awardStat(Stats.INTERACT_WITH_FURNACE);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState state, @javax.annotation.Nullable BlockEntity entity, ItemStack itemStack) {
        this.checkCriticalityExplosion(level, blockPos);
        super.m_6240_(level, player, blockPos, state, entity, itemStack);
    }

    private void checkCriticalityExplosion(LevelReader level, BlockPos pos) {
        if (level.m_7702_(pos) instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity && (float) nuclearFurnaceBlockEntity.getCriticality() >= 2.0F) {
            nuclearFurnaceBlockEntity.destroyWhileCritical(false);
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean idk) {
        if (!blockState.m_60713_(blockState1.m_60734_())) {
            BlockEntity blockentity = level.getBlockEntity(blockPos);
            if (blockentity instanceof Container) {
                Containers.dropContents(level, blockPos, (Container) blockentity);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            super.m_6810_(blockState, level, blockPos, blockState1, idk);
        }
    }
}