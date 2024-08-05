package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class TooltipGetterMultiValue implements ITooltipGetter {

    protected IStatGetter[] statGetter;

    protected StatFormat[] formatters;

    protected String localizationKey;

    public TooltipGetterMultiValue(String localizationKey, IStatGetter[] statGetters, StatFormat[] formatters) {
        this.localizationKey = localizationKey;
        this.statGetter = statGetters;
        this.formatters = formatters;
        if (statGetters.length != formatters.length) {
            throw new RuntimeException(String.format("Mismatching length of stat getters and formatters for '%s', gett: %d, form: %d", localizationKey, statGetters.length, formatters.length));
        }
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        Object[] values = new String[this.statGetter.length];
        for (int i = 0; i < this.statGetter.length; i++) {
            values[i] = this.formatters[i].get(this.statGetter[i].getValue(player, itemStack));
        }
        return I18n.get(this.localizationKey, values);
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return I18n.exists(this.localizationKey + "_extended");
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return I18n.get(this.localizationKey + "_extended");
    }
}