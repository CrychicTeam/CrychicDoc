package se.mickelus.tetra.properties;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.module.data.ToolData;

public interface IToolProvider {

    Logger logger = LogManager.getLogger();

    boolean canProvideTools(ItemStack var1);

    ToolData getToolData(ItemStack var1);

    default int getToolLevel(ItemStack itemStack, ToolAction tool) {
        return !this.canProvideTools(itemStack) ? -1 : this.getToolData(itemStack).getLevel(tool);
    }

    default float getToolEfficiency(ItemStack itemStack, ToolAction tool) {
        return this.getToolLevel(itemStack, tool) <= 0 ? 0.0F : this.getToolData(itemStack).getEfficiency(tool);
    }

    default Set<ToolAction> getTools(ItemStack itemStack) {
        return !this.canProvideTools(itemStack) ? Collections.emptySet() : this.getToolData(itemStack).getValues();
    }

    default Map<ToolAction, Integer> getToolLevels(ItemStack itemStack) {
        return !this.canProvideTools(itemStack) ? Collections.emptyMap() : this.getToolData(itemStack).getLevelMap();
    }

    default ItemStack onCraftConsume(ItemStack providerStack, ItemStack targetStack, Player player, ToolAction tool, int toolLevel, boolean consumeResources) {
        return targetStack.copy();
    }

    default ItemStack onActionConsume(ItemStack providerStack, ItemStack targetStack, Player player, ToolAction tool, int toolLevel, boolean consumeResources) {
        return targetStack.copy();
    }
}