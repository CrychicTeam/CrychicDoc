package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class TooltipGetterSweepingFocus implements ITooltipGetter {

    private final IStatGetter statGetter;

    public TooltipGetterSweepingFocus(IStatGetter statGetter) {
        this.statGetter = statGetter;
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        double focus = this.statGetter.getValue(player, itemStack);
        if (focus > 1.0) {
            return I18n.get("tetra.stats.tool.sweepingFocus_width.tooltip", String.format("%.1f", focus));
        } else {
            return focus < 1.0 ? I18n.get("tetra.stats.tool.sweepingFocus_depth.tooltip", String.format("%.1f", 2.0 - focus)) : I18n.get("tetra.stats.tool.sweepingFocus.tooltip");
        }
    }
}