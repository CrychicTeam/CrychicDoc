package se.mickelus.tetra.gui.stats.sorting;

import java.util.Comparator;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IStatSorter {

    String getName();

    <T> Comparator<T> compare(Player var1, Function<? super T, ItemStack> var2);

    @Nullable
    String getValue(Player var1, ItemStack var2);

    int getWeight(Player var1, ItemStack var2);
}