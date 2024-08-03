package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.be.EnderWatcherBlockEntity;
import org.violetmoon.quark.content.automation.module.EnderWatcherModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class EnderWatcherBlock extends ZetaBlock implements EntityBlock {

    public static final BooleanProperty WATCHED = BooleanProperty.create("watched");

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public EnderWatcherBlock(@Nullable ZetaModule module) {
        super("ender_watcher", module, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(3.0F, 10.0F).sound(SoundType.METAL));
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATCHED, false)).m_61124_(POWER, 0));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATCHED, POWER);
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, @NotNull BlockGetter blockAccess, @NotNull BlockPos pos, @NotNull Direction side) {
        return (Integer) blockState.m_61143_(POWER);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new EnderWatcherBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, EnderWatcherModule.blockEntityType, EnderWatcherBlockEntity::tick);
    }
}