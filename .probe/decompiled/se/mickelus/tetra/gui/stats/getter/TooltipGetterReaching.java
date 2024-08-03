package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.effect.ReachingEffect;

@ParametersAreNonnullByDefault
public class TooltipGetterReaching implements ITooltipGetter {

    private final IStatGetter levelGetter = new StatGetterEffectLevel(ItemEffect.reaching, 1.0);

    private final IStatGetter efficiencyGetter = new StatGetterEffectEfficiency(ItemEffect.reaching, 1.0);

    private final IStatGetter reachGetter = new StatGetterAttribute(ForgeMod.BLOCK_REACH.get(), false);

    private final IStatGetter rangeGetter = new StatGetterAttribute(ForgeMod.ENTITY_REACH.get(), false);

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.reaching.tooltip", String.format("%.0f", 100.0F * ReachingEffect.getOffset((int) this.levelGetter.getValue(player, itemStack), 3.0)), 3);
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return true;
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        int level = (int) this.levelGetter.getValue(player, itemStack);
        double rangedMultiplier = this.efficiencyGetter.getValue(player, itemStack);
        double reach = this.reachGetter.getValue(player, itemStack);
        double range = this.rangeGetter.getValue(player, itemStack);
        return I18n.get("tetra.stats.reaching.tooltip_extended", String.format("%.2f", rangedMultiplier), String.format("%.0f", 100.0F * ReachingEffect.getOffset(level, reach)), String.format("%.1f", reach), String.format("%.0f", 100.0F * ReachingEffect.getOffset(level, range)), String.format("%.1f", range));
    }
}