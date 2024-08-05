package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.effect.ChargedAbilityEffect;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class StatGetterAbilityCooldown implements IStatGetter {

    private final ChargedAbilityEffect ability;

    public StatGetterAbilityCooldown(ChargedAbilityEffect ability) {
        this.ability = ability;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (Double) CastOptional.cast(itemStack.getItem(), ItemModularHandheld.class).map(item -> this.ability.getCooldown(item, itemStack)).map(ticks -> (double) ticks.intValue() / 20.0).orElse(0.0);
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