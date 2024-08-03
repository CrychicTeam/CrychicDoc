package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class StatGetterCooldown implements IStatGetter {

    private double offset = 0.0;

    private double multiplier = 1.0;

    public StatGetterCooldown(double offset, double multiplier) {
        this.offset = offset;
        this.multiplier = multiplier;
    }

    public StatGetterCooldown() {
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (Double) CastOptional.cast(itemStack.getItem(), ItemModularHandheld.class).map(item -> item.getCooldownBase(itemStack)).orElse(0.0) * this.multiplier + this.offset;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return this.getValue(player, itemStack);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return this.getValue(player, itemStack);
    }
}