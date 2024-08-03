package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class StatGetterAbilityDamage implements IStatGetter {

    private final double offset;

    private final double multiplier;

    public StatGetterAbilityDamage(double offset, double multiplier) {
        this.offset = offset;
        this.multiplier = multiplier;
    }

    public StatGetterAbilityDamage() {
        this(0.0, 1.0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (Double) CastOptional.cast(itemStack.getItem(), ItemModularHandheld.class).map(item -> item.getAbilityBaseDamage(itemStack)).orElse(0.0) * this.multiplier + this.offset;
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