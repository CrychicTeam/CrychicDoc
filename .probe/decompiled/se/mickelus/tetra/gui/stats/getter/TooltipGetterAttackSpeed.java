package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class TooltipGetterAttackSpeed implements ITooltipGetter {

    private static final String localizationKey = "tetra.stats.speed.tooltip";

    private final IStatGetter statGetter;

    public TooltipGetterAttackSpeed(IStatGetter statGetter) {
        this.statGetter = statGetter;
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        double speed = this.statGetter.getValue(player, itemStack);
        return I18n.get("tetra.stats.speed.tooltip", String.format("%.2f", 1.0 / speed), String.format("%.2f", ItemModularHandheld.getAttackSpeedHarvestModifier(speed)));
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return true;
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.speed.tooltip_extended");
    }
}