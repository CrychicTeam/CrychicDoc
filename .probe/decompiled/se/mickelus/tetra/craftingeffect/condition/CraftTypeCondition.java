package se.mickelus.tetra.craftingeffect.condition;

import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class CraftTypeCondition implements CraftingEffectCondition {

    CraftTypeCondition.CraftType craft;

    @Override
    public boolean test(ResourceLocation[] unlocks, ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] materials, Map<ToolAction, Integer> tools, UpgradeSchematic schematic, Level world, BlockPos pos, BlockState blockState) {
        switch(this.craft) {
            case module:
                return isReplacing;
            case improvement:
                return !isReplacing && !this.isEnchantment(materials);
            case enchantment:
                return !isReplacing && this.isEnchantment(materials);
            case repair:
            default:
                return false;
        }
    }

    private boolean isEnchantment(ItemStack[] materials) {
        return materials.length == 1 && !materials[0].isEmpty() && Items.ENCHANTED_BOOK.equals(materials[0].getItem());
    }

    static enum CraftType {

        module, improvement, enchantment, repair
    }
}