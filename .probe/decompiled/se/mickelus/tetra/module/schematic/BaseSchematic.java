package se.mickelus.tetra.module.schematic;

import java.util.Map;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;

public abstract class BaseSchematic implements UpgradeSchematic {

    @Override
    public boolean canApplyUpgrade(Player player, ItemStack itemStack, ItemStack[] materials, String slot, Map<ToolAction, Integer> availableTools) {
        return this.isMaterialsValid(itemStack, slot, materials) && !this.isIntegrityViolation(player, itemStack, materials, slot) && this.checkTools(itemStack, materials, availableTools) && (player.isCreative() || player.experienceLevel >= this.getExperienceCost(itemStack, materials, slot));
    }

    @Override
    public boolean isIntegrityViolation(Player player, ItemStack itemStack, ItemStack[] materials, String slot) {
        ItemStack upgradedStack = this.applyUpgrade(itemStack, materials, false, slot, null);
        return (Boolean) CastOptional.cast(upgradedStack.getItem(), IModularItem.class).map(item -> item.getProperties(upgradedStack)).map(properties -> properties.integrity < properties.integrityUsage).orElse(true);
    }

    @Override
    public boolean checkTools(ItemStack targetStack, ItemStack[] materials, Map<ToolAction, Integer> availableTools) {
        return this.getRequiredToolLevels(targetStack, materials).entrySet().stream().allMatch(entry -> (Integer) availableTools.getOrDefault(entry.getKey(), 0) >= (Integer) entry.getValue());
    }

    @Override
    public OutcomePreview[] getPreviews(ItemStack targetStack, String slot) {
        return new OutcomePreview[0];
    }
}