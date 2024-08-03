package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class TooltipGetterInteger implements ITooltipGetter {

    protected IStatGetter statGetter;

    protected String localizationKey;

    protected boolean absolute = false;

    public TooltipGetterInteger(String localizationKey, IStatGetter statGetter, boolean absolute) {
        this(localizationKey, statGetter);
        this.absolute = absolute;
    }

    public TooltipGetterInteger(String localizationKey, IStatGetter statGetter) {
        this.localizationKey = localizationKey;
        this.statGetter = statGetter;
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return this.absolute ? I18n.get(this.localizationKey, Math.round(Math.abs(this.statGetter.getValue(player, itemStack)))) : I18n.get(this.localizationKey, Math.round(this.statGetter.getValue(player, itemStack)));
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