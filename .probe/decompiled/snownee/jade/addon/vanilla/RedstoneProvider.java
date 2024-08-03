package snownee.jade.addon.vanilla;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

public enum RedstoneProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        BlockState state = accessor.getBlockState();
        Block block = state.m_60734_();
        IThemeHelper t = IThemeHelper.get();
        if (block instanceof LeverBlock) {
            Component info;
            if ((Boolean) state.m_61143_(BlockStateProperties.POWERED)) {
                info = t.success(Component.translatable("tooltip.jade.state_on"));
            } else {
                info = t.danger(Component.translatable("tooltip.jade.state_off"));
            }
            tooltip.add(Component.translatable("tooltip.jade.state", info));
        } else if (block == Blocks.REPEATER) {
            int delay = (Integer) state.m_61143_(BlockStateProperties.DELAY);
            tooltip.add(Component.translatable("tooltip.jade.delay", t.info(delay)));
        } else if (block == Blocks.COMPARATOR) {
            ComparatorMode mode = (ComparatorMode) state.m_61143_(BlockStateProperties.MODE_COMPARATOR);
            Component modeInfo = t.info(Component.translatable("tooltip.jade.mode_" + (mode == ComparatorMode.COMPARE ? "comparator" : "subtractor")));
            tooltip.add(Component.translatable("tooltip.jade.mode", modeInfo));
            if (accessor.getServerData().contains("Signal")) {
                tooltip.add(Component.translatable("tooltip.jade.power", t.info(accessor.getServerData().getInt("Signal"))));
            }
        } else {
            if (block instanceof CalibratedSculkSensorBlock && accessor.getServerData().contains("Signal")) {
                tooltip.add(Component.translatable("jade.input_signal", t.info(accessor.getServerData().getInt("Signal"))));
            }
            if (state.m_61138_(BlockStateProperties.POWER)) {
                tooltip.add(Component.translatable("tooltip.jade.power", t.info(state.m_61143_(BlockStateProperties.POWER))));
            }
            if (state.m_60734_() instanceof HopperBlock && accessor.getServerData().contains("HopperLocked")) {
                tooltip.add(t.danger(Component.translatable("jade.hopper.locked")));
            }
        }
    }

    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        BlockEntity blockEntity = accessor.getBlockEntity();
        if (blockEntity instanceof ComparatorBlockEntity comparator) {
            data.putInt("Signal", comparator.getOutputSignal());
        } else if (blockEntity instanceof HopperBlockEntity) {
            BlockState state = accessor.getBlockState();
            if (state.m_61138_(BlockStateProperties.ENABLED) && !(Boolean) state.m_61143_(BlockStateProperties.ENABLED)) {
                data.putBoolean("HopperLocked", true);
            }
        } else if (blockEntity instanceof CalibratedSculkSensorBlockEntity) {
            Direction direction = ((Direction) accessor.getBlockState().m_61143_(CalibratedSculkSensorBlock.FACING)).getOpposite();
            int signal = accessor.getLevel().m_277185_(accessor.getPosition().relative(direction), direction);
            data.putInt("Signal", signal);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return Identifiers.MC_REDSTONE;
    }
}