package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class TooltipGetterNone implements ITooltipGetter {

    protected String localizationKey;

    public TooltipGetterNone(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return I18n.get(this.localizationKey);
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