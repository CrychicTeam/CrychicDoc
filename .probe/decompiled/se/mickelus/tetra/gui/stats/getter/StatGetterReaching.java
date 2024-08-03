package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.effect.ReachingEffect;

@ParametersAreNonnullByDefault
public class StatGetterReaching extends StatGetterEffectLevel {

    public StatGetterReaching() {
        super(ItemEffect.reaching, 1.0);
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return super.getValue(player, currentStack) > 0.0 || super.getValue(player, previewStack) > 0.0;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (double) (100.0F * ReachingEffect.getOffset((int) super.getValue(player, itemStack), 3.0));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (double) (100.0F * ReachingEffect.getOffset((int) super.getValue(player, itemStack, slot), 3.0));
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (double) (100.0F * ReachingEffect.getOffset((int) super.getValue(player, itemStack, slot, improvement), 3.0));
    }
}