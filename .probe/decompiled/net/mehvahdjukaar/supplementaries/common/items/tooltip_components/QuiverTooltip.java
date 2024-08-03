package net.mehvahdjukaar.supplementaries.common.items.tooltip_components;

import java.util.List;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record QuiverTooltip(List<ItemStack> stacks, int selected) implements TooltipComponent {
}