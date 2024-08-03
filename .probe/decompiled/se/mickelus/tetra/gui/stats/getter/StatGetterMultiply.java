package se.mickelus.tetra.gui.stats.getter;

import java.util.Arrays;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class StatGetterMultiply implements IStatGetter {

    private final IStatGetter[] statGetters;

    private double multiplier;

    public StatGetterMultiply(IStatGetter... statGetters) {
        this(1.0, statGetters);
    }

    public StatGetterMultiply(double multiplier, IStatGetter... statGetters) {
        this.multiplier = multiplier;
        this.statGetters = statGetters;
    }

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return Arrays.stream(this.statGetters).mapToDouble(getter -> getter.getValue(player, itemStack)).reduce(this.multiplier, (a, b) -> a * b);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (Double) Arrays.stream(this.statGetters).map(getter -> getter.getValue(player, itemStack, slot)).reduce(this.multiplier, (a, b) -> a * b);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (Double) Arrays.stream(this.statGetters).map(getter -> getter.getValue(player, itemStack, slot, improvement)).reduce(this.multiplier, (a, b) -> a * b);
    }
}