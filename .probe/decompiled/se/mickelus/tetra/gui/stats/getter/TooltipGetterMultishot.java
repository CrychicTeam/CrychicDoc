package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@ParametersAreNonnullByDefault
public class TooltipGetterMultishot implements ITooltipGetter {

    private static final IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(ItemEffect.multishot, 1.0);

    private static final IStatGetter levelGetter = new StatGetterEffectLevel(ItemEffect.multishot, 1.0);

    private static final IStatGetter enchantmentGetter = new StatGetterEnchantmentLevel(Enchantments.MULTISHOT, 3.0);

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        double enchantmentCount = enchantmentGetter.getValue(player, itemStack);
        double spread = efficiencyGetter.getValue(player, itemStack);
        if (spread == 0.0 && enchantmentCount > 0.0) {
            spread = ModularCrossbowItem.multishotDefaultSpread;
        }
        return I18n.get("tetra.stats.multishot.tooltip", String.format("%.0f", levelGetter.getValue(player, itemStack) + enchantmentCount), String.format("%.1f", spread));
    }
}