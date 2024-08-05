package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class TooltipGetterBashing implements ITooltipGetter {

    private static final IStatGetter levelGetter = new StatGetterEffectLevel(ItemEffect.bashing, 1.0);

    private static final IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(ItemEffect.bashing, 1.0);

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.bashing.tooltip", String.format("%d", (int) levelGetter.getValue(player, itemStack)), String.format("%.1f", efficiencyGetter.getValue(player, itemStack)));
    }
}