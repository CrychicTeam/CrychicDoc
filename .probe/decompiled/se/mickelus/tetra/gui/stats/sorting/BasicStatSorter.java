package se.mickelus.tetra.gui.stats.sorting;

import java.util.Comparator;
import java.util.function.Function;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.StatFormat;

@ParametersAreNonnullByDefault
public class BasicStatSorter implements IStatSorter {

    private final IStatGetter getter;

    private final String name;

    private final StatFormat statFormat;

    private String suffix;

    private boolean inverted;

    public BasicStatSorter(IStatGetter getter, String name, StatFormat statFormat) {
        this.getter = getter;
        this.name = name;
        this.statFormat = statFormat;
    }

    public BasicStatSorter setInverted() {
        this.inverted = true;
        return this;
    }

    public BasicStatSorter setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    @Override
    public String getName() {
        return this.suffix != null ? I18n.get(this.name) + " " + I18n.get(this.suffix) : I18n.get(this.name);
    }

    @Override
    public <T> Comparator<T> compare(Player player, Function<? super T, ItemStack> keyExtractor) {
        return this.inverted ? Comparator.comparing(a -> this.getter.getValue(player, (ItemStack) keyExtractor.apply(a))) : Comparator.comparing(a -> -this.getter.getValue(player, (ItemStack) keyExtractor.apply(a)));
    }

    @Nullable
    @Override
    public String getValue(Player player, ItemStack itemStack) {
        return this.statFormat.get(this.getter.getValue(player, itemStack));
    }

    @Override
    public int getWeight(Player player, ItemStack itemStack) {
        return this.getter.shouldShow(player, itemStack, itemStack) ? 1 : 0;
    }
}