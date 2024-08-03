package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class StatGetterToolCompoundEfficiency implements IStatGetter {

    private IStatGetter efficiencyGetter;

    private IStatGetter attackSpeedGetter;

    private IStatGetter enchantmentGetter;

    public StatGetterToolCompoundEfficiency(IStatGetter efficiencyGetter, IStatGetter attackSpeedGetter, IStatGetter enchantmentGetter) {
        this.efficiencyGetter = efficiencyGetter;
        this.attackSpeedGetter = attackSpeedGetter;
        this.enchantmentGetter = enchantmentGetter;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return this.efficiencyGetter.getValue(player, itemStack) * ItemModularHandheld.getAttackSpeedHarvestModifier(this.attackSpeedGetter.getValue(player, itemStack)) + ItemModularHandheld.getEfficiencyEnchantmentBonus((int) this.enchantmentGetter.getValue(player, itemStack));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return this.efficiencyGetter.getValue(player, itemStack, slot) * ItemModularHandheld.getAttackSpeedHarvestModifier(this.attackSpeedGetter.getValue(player, itemStack)) + ItemModularHandheld.getEfficiencyEnchantmentBonus((int) this.enchantmentGetter.getValue(player, itemStack, slot));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return this.efficiencyGetter.getValue(player, itemStack, slot, improvement) * ItemModularHandheld.getAttackSpeedHarvestModifier(this.attackSpeedGetter.getValue(player, itemStack)) + ItemModularHandheld.getEfficiencyEnchantmentBonus((int) this.enchantmentGetter.getValue(player, itemStack, slot, improvement));
    }
}