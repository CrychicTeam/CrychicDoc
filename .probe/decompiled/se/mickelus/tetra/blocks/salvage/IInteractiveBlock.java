package se.mickelus.tetra.blocks.salvage;

import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;

public interface IInteractiveBlock {

    BlockInteraction[] getPotentialInteractions(Level var1, BlockPos var2, BlockState var3, Direction var4, Collection<ToolAction> var5);
}