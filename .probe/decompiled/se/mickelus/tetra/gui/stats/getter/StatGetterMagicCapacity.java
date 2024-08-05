package se.mickelus.tetra.gui.stats.getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class StatGetterMagicCapacity implements IStatGetter {

    @Override
    public double getValue(Player player, ItemStack itemStack) {
        return (double) ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getMajorModules(itemStack)).map(Arrays::stream).orElse(Stream.empty())).filter(Objects::nonNull).mapToInt(module -> module.getMagicCapacity(itemStack)).sum();
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot) {
        return (double) ((Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot).getMagicCapacity(itemStack)).orElse(0)).intValue();
    }

    public boolean hasGain(ItemStack itemStack) {
        return ((Stream) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getMajorModules(itemStack)).map(Arrays::stream).orElse(Stream.empty())).filter(Objects::nonNull).mapToInt(module -> module.getMagicCapacityGain(itemStack)).anyMatch(gain -> gain > 0);
    }

    @Override
    public double getValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return 0.0;
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack) {
        return this.hasGain(currentStack) || this.hasGain(previewStack);
    }
}