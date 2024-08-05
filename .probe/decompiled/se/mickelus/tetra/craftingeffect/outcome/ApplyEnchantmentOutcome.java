package se.mickelus.tetra.craftingeffect.outcome;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class ApplyEnchantmentOutcome implements CraftingEffectOutcome {

    Map<Enchantment, Integer> enchantments = new HashMap();

    boolean force = false;

    ApplyEnchantmentOutcome.StackMode stacking = ApplyEnchantmentOutcome.StackMode.max;

    @Override
    public boolean apply(ResourceLocation[] unlockedEffects, ItemStack upgradedStack, String slot, boolean isReplacing, Player player, ItemStack[] preMaterials, Map<ToolAction, Integer> tools, Level world, UpgradeSchematic schematic, BlockPos pos, BlockState blockState, boolean consumeResources, ItemStack[] postMaterials) {
        if (upgradedStack.getItem() instanceof IModularItem item && item.getModuleFromSlot(upgradedStack, slot) instanceof ItemModuleMajor module) {
            AtomicBoolean success = new AtomicBoolean(false);
            Map<Enchantment, Integer> currentEnchantments = EnchantmentHelper.getEnchantments(upgradedStack);
            this.enchantments.entrySet().stream().filter(entry -> this.acceptsEnchantment(upgradedStack, module, currentEnchantments.keySet(), (Enchantment) entry.getKey(), (Integer) entry.getValue())).forEach(entry -> {
                int level = (Integer) entry.getValue();
                if (this.stacksEnchantment(upgradedStack, module, (Enchantment) entry.getKey(), level)) {
                    currentEnchantments.put((Enchantment) entry.getKey(), this.stackLevel((Integer) currentEnchantments.get(entry.getKey()), level));
                    EnchantmentHelper.setEnchantments(currentEnchantments, upgradedStack);
                } else {
                    TetraEnchantmentHelper.applyEnchantment(upgradedStack, slot, (Enchantment) entry.getKey(), level);
                }
                success.set(true);
            });
            return success.get();
        }
        return false;
    }

    protected int stackLevel(int currentLevel, int newLevel) {
        return switch(this.stacking) {
            case add ->
                currentLevel + newLevel;
            case stack ->
                newLevel == currentLevel ? currentLevel + 1 : Math.max(currentLevel, newLevel);
            case max ->
                Math.max(currentLevel, newLevel);
            case replace ->
                newLevel;
        };
    }

    protected boolean stacksEnchantment(ItemStack itemStack, ItemModuleMajor module, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> moduleEnchantments = module.getEnchantments(itemStack);
        if (!moduleEnchantments.containsKey(enchantment)) {
            return false;
        } else {
            int currentLevel = (Integer) moduleEnchantments.get(enchantment);
            return level >= currentLevel && currentLevel < enchantment.getMaxLevel();
        }
    }

    protected boolean acceptsEnchantment(ItemStack itemStack, ItemModuleMajor module, Set<Enchantment> currentEnchantments, Enchantment enchantment, int level) {
        return (module.acceptsEnchantment(itemStack, enchantment, false) || this.force) && (this.stacksEnchantment(itemStack, module, enchantment, level) || EnchantmentHelper.isEnchantmentCompatible(currentEnchantments, enchantment));
    }

    static enum StackMode {

        add, stack, max, replace
    }
}