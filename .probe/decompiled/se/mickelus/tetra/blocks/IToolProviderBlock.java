package se.mickelus.tetra.blocks;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

public interface IToolProviderBlock {

    default boolean canProvideTools(Level world, BlockPos pos, BlockPos targetPos) {
        return false;
    }

    default Collection<ToolAction> getTools(Level world, BlockPos pos, BlockState blockState) {
        return Collections.emptyList();
    }

    default int getToolLevel(Level world, BlockPos pos, BlockState blockState, ToolAction toolAction) {
        return -1;
    }

    default Map<ToolAction, Integer> getToolLevels(Level world, BlockPos pos, BlockState blockState) {
        return (Map<ToolAction, Integer>) this.getTools(world, pos, blockState).stream().collect(Collectors.toMap(Function.identity(), toolAction -> this.getToolLevel(world, pos, blockState, toolAction)));
    }

    default ItemStack onCraftConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, String slot, boolean isReplacing, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        return null;
    }

    default ItemStack onActionConsumeTool(Level world, BlockPos pos, BlockState blockState, ItemStack targetStack, Player player, ToolAction requiredTool, int requiredLevel, boolean consumeResources) {
        return null;
    }
}