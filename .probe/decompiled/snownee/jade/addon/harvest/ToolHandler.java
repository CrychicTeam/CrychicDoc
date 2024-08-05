package snownee.jade.addon.harvest;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface ToolHandler {

    ItemStack test(BlockState var1, Level var2, BlockPos var3);

    List<ItemStack> getTools();

    String getName();
}