package se.mickelus.tetra.blocks.workbench.action;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;

public interface WorkbenchAction {

    String getKey();

    boolean canPerformOn(@Nullable Player var1, WorkbenchTile var2, ItemStack var3);

    Collection<ToolAction> getRequiredToolActions(ItemStack var1);

    int getRequiredToolLevel(ItemStack var1, ToolAction var2);

    Map<ToolAction, Integer> getRequiredTools(ItemStack var1);

    void perform(Player var1, ItemStack var2, WorkbenchTile var3);

    default boolean allowInWorldInteraction() {
        return false;
    }
}