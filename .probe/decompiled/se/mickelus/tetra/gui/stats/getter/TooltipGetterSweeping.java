package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class TooltipGetterSweeping implements ITooltipGetter {

    private final IStatGetter levelGetter;

    private final IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(ItemEffect.sweeping, 1.0, 1.0);

    public TooltipGetterSweeping(IStatGetter levelGetter) {
        this.levelGetter = levelGetter;
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.sweeping.tooltip", String.format("%.1f", this.efficiencyGetter.getValue(player, itemStack)), String.format("%.1f%%", this.levelGetter.getValue(player, itemStack)));
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return true;
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.sweeping.tooltip_extended");
    }
}