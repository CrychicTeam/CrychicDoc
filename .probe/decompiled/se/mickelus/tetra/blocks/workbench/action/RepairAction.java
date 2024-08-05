package se.mickelus.tetra.blocks.workbench.action;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

@ParametersAreNonnullByDefault
public class RepairAction implements WorkbenchAction {

    public static final String key = "repair_action";

    @Override
    public String getKey() {
        return "repair_action";
    }

    @Override
    public boolean canPerformOn(@Nullable Player player, WorkbenchTile tile, ItemStack itemStack) {
        return player != null && itemStack.getItem() instanceof IModularItem ? Arrays.stream(SchematicRegistry.getSchematics(itemStack)).anyMatch(upgradeSchematic -> upgradeSchematic instanceof RepairSchematic) : false;
    }

    @Override
    public Collection<ToolAction> getRequiredToolActions(ItemStack itemStack) {
        return Collections.emptySet();
    }

    @Override
    public int getRequiredToolLevel(ItemStack itemStack, ToolAction toolAction) {
        return 0;
    }

    @Override
    public Map<ToolAction, Integer> getRequiredTools(ItemStack itemStack) {
        return Collections.emptyMap();
    }

    @Override
    public void perform(Player player, ItemStack itemStack, WorkbenchTile workbench) {
        Arrays.stream(SchematicRegistry.getSchematics(itemStack)).filter(upgradeSchematic -> upgradeSchematic instanceof RepairSchematic).findFirst().map(upgradeSchematic -> (RepairSchematic) upgradeSchematic).ifPresent(repairSchematic -> workbench.setCurrentSchematic(repairSchematic, repairSchematic.getSlot(itemStack)));
    }
}